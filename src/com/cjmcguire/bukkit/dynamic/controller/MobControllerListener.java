package com.cjmcguire.bukkit.dynamic.controller;

import java.util.UUID;

import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.AttributeModifier;
import net.minecraft.server.v1_8_R3.GenericAttributes;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.cjmcguire.bukkit.dynamic.AbstractEntityDamageListener;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * The MobControllerListener carries out the function of the 
 * Controller in the dynamic difficulty implementation. It listens to 
 * events that relate to making mobs dynamic and controls the outcome 
 * of the events based on the data gathered by the Analyzer.
 * @author CJ McGuire
 */
public class MobControllerListener extends AbstractEntityDamageListener
{
	private final PlayerDataManager playerDataManager;

	//	private ConfigFileHandler configFileHandler;

	private static final UUID movementSpeedUID = UUID.fromString("206a89dc-be78-4c4d-b42c-3b31db3f5a7c");

	/**
	 * Initializes the MobControllerListener.
	 */
	public MobControllerListener()
	{
		this.playerDataManager = PlayerDataManager.getInstance();

//		this.configFileHandler = plugin.getConfigFileHandler();
	}

	/**
	 * This method triggers whenever a creature takes damage. This 
	 * method scales the damage that the player received from the 
	 * mob based on the player's performance level for that mob.
	 * @param event the EntityDamageEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		this.onEntityDamageByEntityAction(event);
	}
	
	@Override
	protected int playerDamagedAction(Player player, LivingEntity mob, int damage) 
	{
		int alteredDamage = damage;
		
		// Get the mobType of the mob.
		MobType mobType = MobType.getEntitysMobType(mob);

		// If the mob had a valid mobType
		if(mobType != null)
		{
			// If the player is not temporarily immune
			if(this.notInvincible(player))
			{
				UUID playerID = player.getUniqueId();

				// Manipulate the damage they received based on 
				// their dynamic difficulty.
				alteredDamage = this.manipulateDamagePlayerReceived(playerID, mobType, damage);
			}
		}
		
		return alteredDamage;
	}

	@Override
	protected int livingEntityDamagedAction(LivingEntity mob, Player player, int damage) 
	{
		int alteredDamage = damage;
		
		// Get the mobType of the mob
		MobType mobType = MobType.getEntitysMobType(mob);

		// If the mob had a valid mobType
		if(mobType != null)
		{
			// If the player is not temporarily immune
			if(this.notInvincible(mob))
			{
				UUID playerID = player.getUniqueId();
				
				// Manipulate the damage they received based on 
				// their dynamic difficulty.
				alteredDamage = this.manipulateDamageMobReceived(playerID, mobType, damage);
			}
		}
		
		return alteredDamage;
	}

	/**
	 * Manipulates the damage that a Player received based on their 
	 * dynamic difficulty settings.
	 * @param playerID the UUID of the player that was damaged
	 * @param mobType the mobType that damaged the player
	 * @param baseDamage the base damage that was done to the player
	 * @return the new damage done by the mob
	 */
	protected int manipulateDamagePlayerReceived(UUID playerID, MobType mobType, int baseDamage)
	{
		// get the PlayerInfo's MobData
		MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);

		double performanceLevel = mobInfo.getPerformanceLevelInUse();

		// figure out the altered damage
		int alteredDamage;
		if(mobInfo.shouldScaleAttack())
		{
			alteredDamage = (int)(baseDamage * performanceLevel/100.0 + 0.5);
		}
		else
		{
			alteredDamage = baseDamage;
		}

		return alteredDamage;
	}
	
	/**
	 * Manipulates the damage that a Mob received based on the 
	 * player's dynamic difficulty settings.
	 * @param playerID the UUID of the player that damaged the mob
	 * @param mobType the mobType that was damaged
	 * @param baseDamage the base damage that was done to the mob
	 * @return the new damage done to the mob
	 */
	protected int manipulateDamageMobReceived(UUID playerID, MobType mobType, int baseDamage)
	{
		// get the PlayerInfo's MobData
		MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);

		double performanceLevel = mobInfo.getPerformanceLevelInUse();

		// figure out the altered damage
		int alteredDamage;
		if(mobInfo.shouldScaleDefense())
		{
			alteredDamage = (int) (baseDamage * 100.0/performanceLevel + 0.5);
		}
		else
		{
			alteredDamage = baseDamage;
		}

		return alteredDamage;
	}

	/**
	 * This method triggers whenever a mob targets a Player. This 
	 * method changes the movement speed, knockback resistance, and 
	 * max follow distance of a mob based on its target's performance 
	 * level for that mob.
	 * @param event the EntityTargetLivingEntityEvent that just 
	 * occurred.
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityTargetEntityEvent(EntityTargetEvent event)
	{
		// Get the targeter.
		Entity targeter = event.getEntity();
		// Get the target.
		Entity target = event.getTarget();

		// If targeter was a LivingEntity (Ideally, the targeter should 
		// be a mob but there is no way to know that at this point)
		if(targeter instanceof LivingEntity)
		{
			if(target instanceof Player)
			{
				this.makeTargeterDynamic((LivingEntity)targeter, (Player)target);
			}
			else if(event.getReason() == TargetReason.FORGOT_TARGET)
			{
				this.resetTargeterToDefault((LivingEntity)targeter);
			}
		}
	}

	/**
	 * @param targeter the targeter whose attributes to make dynamic. 
	 * It should be a hostile mob that has a type in the MobType enum.
	 * @param player the player whose mob performance level to made 
	 * the targeter dynamic from
	 */
	private void makeTargeterDynamic(LivingEntity targeter, Player player)
	{
		// get the mob's MobType
		MobType mobType = MobType.getEntitysMobType(targeter);

		// if the mob had a valid MobType
		if(mobType != null)
		{
			// get the player name
			UUID playerID = player.getUniqueId();

			// get the Player's MobInfo for the mob
			MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);

			double performanceLevel = mobInfo.getPerformanceLevelInUse();

			CraftLivingEntity livingEntity = (CraftLivingEntity) targeter;
			EntityInsentient insEntity = (EntityInsentient) livingEntity.getHandle();

			if(mobInfo.shouldScaleSpeed())
			{
				this.makeSpeedDynamic(insEntity, performanceLevel);
			}
			
			if(mobInfo.shouldScaleKnockbackResistance())
			{
				this.makeKnockbackDynamic(insEntity, performanceLevel);
			}
			
			if(mobInfo.shouldScaleMaxFollowDistance())
			{
				this.makeFollowDistanceDynamic(insEntity, mobInfo, performanceLevel);
			}
		}
	}

	/**
	 * Changes the mob speed of the given EntityInsentient based on 
	 * the given MobInfo. Will change the mob speed by adding 
	 * (performanceLevel/100.0-1)/2 to the mobs base speed.
	 * @param insEntity the EntityInsentient whose speed you want to change
	 * @param performanceLevel the performance level of the player
	 */
	protected void makeSpeedDynamic(EntityInsentient insEntity, double performanceLevel)
	{
		// get the mob speed attribute
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);

		//performance level of 200 scales to -> 150% speed 
		//performance level of 50 scales to -> 75% speed
		double modifier = (performanceLevel/100.0-1)/2.0;
		AttributeModifier attributeModifier = new AttributeModifier(movementSpeedUID, "DynamicDifficulty movement speed modifier", modifier, 1);

		// c() removes the modifier if one was on it
		attribute.c(attributeModifier);
		// b() adds the modifier to the attribute
		attribute.b(attributeModifier);
	}
	
	/**
	 * Changes the mob knockback resistance of the given 
	 * EntityInsentient based on the given MobInfo. Will change the 
	 * mob knockback resistance by adding (performancePercent/100-1)/2 
	 * to the mob's base knockback resistance. Note that Knockback 
	 * resistance cannot go below 0, so if a player's performance 
	 * percent is below 100, knockback resistance cannot be reduced. 
	 * @param insEntity the EntityInsentient whose speed you want to 
	 * change
	 * @param performanceLevel the performance level of the player
	 */
	protected void makeKnockbackDynamic(EntityInsentient insEntity, double performanceLevel)
	{
		// The player's performance level must be greater than 100 because if it is less 
		// than 100, we are trying to reduce knockback resistance but Bukkit does not allow
		// knockback resistance to be negative.
		if(performanceLevel > 100)
		{
			// performance level of 50 to 100 scales to -> 0%
			// performance level of 100 to 200 scales to -> 0% to 50%
			double changeAmount = (performanceLevel/100.0-1.0)/2.0;

			// get the mob knockback resistance attribute
			AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.c);
			//set it to the change amount
			attribute.setValue(changeAmount);
		}
	}
	
	/**
	 * Changes the mob follow distance of the given EntityInsentient 
	 * based on the given MobInfo. Will change the mob follow distance 
	 * by setting it to "normal follow distance" performanceLevel/100.0. 
	 * Note that this method will only change the follow distance if 
	 * the performance level is greater than 100.
	 * @param insEntity the EntityInsentient whose follow distance you 
	 * want to change.
	 * @param mobInfo the MobInfo which contains the necessary 
	 * information for how much to change the follow distance.
	 * @param performanceLevel the performance level of the player.
	 */
	protected void makeFollowDistanceDynamic(EntityInsentient insEntity, MobInfo mobInfo, double performanceLevel)
	{
		// The player's performance level must be greater than 100 because if it is less 
		// than 100 and we try to decrease the mob's view distance, the mob will just 
		// stand there stupidly looking at the player due to its follow distance constantly 
		// decreasing and increasing. Basically, this causes the mob to go into an endless
		// loop of targeting and untargeting the player. 
		if(performanceLevel > 100)
		{
			AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
			double value = mobInfo.getMobType().getDefaultFollowDistance() * performanceLevel/100.0;
			attribute.setValue(value);
		}
	}

	/**
	 * @param targeter the targeter to reset. It should be a hostile 
	 * mob that has a type in the MobType enum.
	 */
	private void resetTargeterToDefault(LivingEntity targeter)
	{
		MobType mobType = MobType.getEntitysMobType(targeter);

		if(mobType != null)
		{
			CraftLivingEntity livingEntity = (CraftLivingEntity) targeter;
			EntityInsentient insEntity = (EntityInsentient) livingEntity.getHandle();

			this.resetSpeed(insEntity);
			this.resetKnockback(insEntity);
			this.resetFollowDistance(insEntity, mobType);
		}
	}

	/**
	 * Resets the speed of the given EntityInsentient back to its 
	 * default move speed.
	 * @param insEntity the EntityInsentient whose speed you want to 
	 * reset.
	 */
	protected void resetSpeed(EntityInsentient insEntity)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "DynamicDifficulty movement speed reset", 0, 1);
		// c() removes the modifier if one was on it
		attribute.c(modifier);
		// b() adds the modifier to the attribute
		attribute.b(modifier);
	}

	/**
	 * Resets the knockback resistance of the given EntityInsentient 
	 * back to its default move knockback resistance.
	 * @param insEntity the EntityInsentient whose knockback 
	 * resistance you want to reset.
	 */
	protected void resetKnockback(EntityInsentient insEntity)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.c);
		attribute.setValue(0);
	}

	/**
	 * Resets the follow distance of the given EntityInsentient back 
	 * to its default follow distance.
	 * @param insEntity the EntityInsentient whose follow distance you 
	 * want to reset.
	 * @param mobType the mob type of the entity insentient
	 */
	protected void resetFollowDistance(EntityInsentient insEntity, MobType mobType)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
		attribute.setValue(mobType.getDefaultFollowDistance());
	}
}