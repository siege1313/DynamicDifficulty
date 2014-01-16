package com.cjmcguire.bukkit.dynamic.controller;

import java.util.UUID;

import net.minecraft.server.v1_7_R1.EntityInsentient;
import net.minecraft.server.v1_7_R1.AttributeInstance;
import net.minecraft.server.v1_7_R1.AttributeModifier;
import net.minecraft.server.v1_7_R1.GenericAttributes;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;

/**
 * The ControllerListener carries out the function of the Controller in the 
 * Dynamic Difficulty implementation. It listens to events that occur in-game and
 * manipulates (controls) the outcome of the events based on the dynamic difficulty
 * settings determined by the Analyzer.
 * @author CJ McGuire
 */
public class ControllerListener implements Listener
{
	private final DynamicDifficulty plugin;

	private static final UUID movementSpeedUID = UUID.fromString("206a89dc-be78-4c4d-b42c-3b31db3f5a7c");
	
	/**
	 * Initializes the ControllerListener.
	 * @param plugin a reference to the DynamicDifficulty plugin that uses 
	 * this ControllerListener
	 */
	public ControllerListener(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * This method triggers whenever a creature takes damage in Minecraft. 
	 * This method changes the damage that the player received from a mob
	 * based on the player's performance level for that mob.
	 * @param event the EntityDamageEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		// get the damager and convert it to the shooter if it was originally the projectile
		Entity damager = event.getDamager();
		if(damager instanceof Projectile)
		{
			damager = ((Projectile) damager).getShooter();
		}

		// get the entity that was damaged
		Entity damaged = event.getEntity();

		// if the entity that got damaged was the player
		// and the entity doing the damage was a living entity (a mob)
		if(damaged instanceof Player && damager instanceof LivingEntity)
		{
			// get the mobType of the mob
			MobType mobType = MobType.getEntitysMobType((LivingEntity)damager);

			// if the mob had a valid mobType
			if(mobType != null)
			{
				// get the player
				Player player = (Player)damaged;

				// if the player is not temporarily immune
				if(player.getNoDamageTicks() < player.getMaximumNoDamageTicks()/2f)
				{
					// get the player's name
					String playerName = player.getName();

					// manipulate the damage they received based on their dynamic difficulty
					int alteredDamage = manipulateDamagePlayerReceived(playerName, mobType, (int) event.getDamage());
					event.setDamage((double)alteredDamage);
				}
			}
		}
	}	

	/**
	 * Manipulates the damage that a Player received based on their dynamic 
	 * difficulty settings.
	 * @param playerName the name of the player that was damaged
	 * @param mobType the mobType that damaged the player
	 * @param baseDamage the base damage that was done to the player
	 * @return the new damage done by the mob
	 */
	protected int manipulateDamagePlayerReceived(String playerName, MobType mobType, int baseDamage)
	{
		// get the PlayerInfo's MobData
		MobInfo mobInfo = plugin.getPlayersMobInfo(playerName, mobType);

		double performanceLevel = mobInfo.getPerformanceLevelInUse();
		
		// figure out the altered damage
		int alteredDamage = (int) (baseDamage * performanceLevel/100.0 + 0.5); // add .5 here so that it rounds correctly
		
		return alteredDamage;
	}

	/**
	 * This method triggers whenever a mob targets a Player. This method
	 * changes the movement speed of a mob based on its target's performance level
	 * for that mob.
	 * @param event the EntityTargetLivingEntityEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityTargetEntityEvent(EntityTargetEvent event)
	{
		// get the targeter
		Entity targeter = event.getEntity();
		// get the target
		Entity target = event.getTarget();
		
		// if the mob was not untargeting
		// and the target was a Player
		// and the targeter was a LivingEntity (a mob)
		if(target != null && target instanceof Player && targeter instanceof LivingEntity)
		{
			this.makeTargeterDynamic(targeter, target);
		}
		else if(event.getReason() == TargetReason.FORGOT_TARGET)
		{
			this.resetTargeterToDefault(targeter);
		}
	}
	
	private void makeTargeterDynamic(Entity targeter, Entity target)
	{
		// cast the target to a Player
		Player player = (Player) target;
		// cast the targeter to a LivingEntity
		LivingEntity mob = (LivingEntity)targeter;

		// get the player name
		String playerName = player.getName();
		// get the mob's MobType
		MobType mobType = MobType.getEntitysMobType(mob);

		// if the mob had a valid MobType
		if(mobType != null)
		{
			// get the Player's MobInfo for the mob
			MobInfo mobInfo = plugin.getPlayersMobInfo(playerName, mobType);
			
			double performanceLevel = mobInfo.getPerformanceLevelInUse();
			
			CraftLivingEntity livingEntity = (CraftLivingEntity) mob;
			EntityInsentient insEntity = (EntityInsentient) livingEntity.getHandle();

			this.makeMobSpeedDynamic(insEntity, performanceLevel);
			this.makeMobKnockbackDynamic(insEntity, performanceLevel);
			this.makeMobFollowDistanceDynamic(insEntity, mobInfo, performanceLevel);
		}
	}
	
	/**
	 * Changes the mob speed of the given EntityInsentient based on the given MobInfo
	 * Will change the mob speed by adding (performanceLevel/100.0-1)/2 to the mobs base speed.
	 * @param insEntity the EntityInsentient whose speed you want to change
	 * @param performanceLevel the performance level of the player
	 */
	protected void makeMobSpeedDynamic(EntityInsentient insEntity, double performanceLevel)
	{
		// get the mob speed attribute
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.d);
		
		//performance level of 200 scales to -> 150% speed 
		//performance level of 50 scales to -> 75% speed
		double modifier = (performanceLevel/100.0-1)/2.0;
		
		AttributeModifier attributeModifier = new AttributeModifier(movementSpeedUID, "DynamicDifficulty movement speed modifier", modifier, 1);
		
		// remove the modifier if one was on it
		attribute.b(attributeModifier);
		// add the modifier we created
		attribute.a(attributeModifier);
		
	}
	
	/**
	 * Changes the mob follow distance of the given EntityInsentient based on the given MobInfo
	 * Will change the mob follow distance by setting it to "normal follow distance" *
	 * performanceLevel/100.0. Note that this method will only change the follow distance if the 
	 * performance level is greater than 100.
	 * @param insEntity the EntityInsentient whose follow distance you want to change
	 * @param mobInfo the MobInfo which contains the necessary information for how much
	 * to change the follow distance
	 * @param performanceLevel the performance level of the player
	 */
	protected void makeMobFollowDistanceDynamic(EntityInsentient insEntity, MobInfo mobInfo, double performanceLevel)
	{
		// The player's performance level must be greater than 100 because if it is less 
		// than 100 and we try to decrease the mob's view distance, the mob will just 
		// stand there stupidly looking at the player due to its follow distance constantly 
		// decreasing and increasing. Basically, this causes the mob to go into an endless
		// loop of targeting and untargeting the player. 
		if(performanceLevel > 100)
		{
			AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.b);
			double value = mobInfo.getMobType().getDefaultFollowDistance() * performanceLevel/100.0;
			attribute.setValue(value);
		}
	}
	
	/**
	 * Changes the mob knockback resistance of the given EntityInsentient based on the 
	 * given MobInfo. Will change the mob knockback resistance by adding 
	 * (performancePercent/100-1)/2 to the mob's base knockback resistance. Note that 
	 * Knockback resistance cannot go below 0, so if a player's performance percent is 
	 * below 100, knockback resistance cannot be reduced.
	 * @param insEntity the EntityInsentient whose speed you want to change
	 * @param performanceLevel the performance level of the player
	 */
	protected void makeMobKnockbackDynamic(EntityInsentient insEntity, double performanceLevel)
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
	
	private void resetTargeterToDefault(Entity targeter)
	{
		LivingEntity mob = (LivingEntity)targeter;
		CraftLivingEntity livingEntity = (CraftLivingEntity) mob;
		EntityInsentient insEntity = (EntityInsentient) livingEntity.getHandle();
		
		MobType mobType = MobType.getEntitysMobType(targeter);
		
		this.resetMobSpeed(insEntity);
		this.resetMobKnockback(insEntity);
		this.resetMobFollowDistance(insEntity, mobType);
	}
	
	/**
	 * Resets the speed of the given EntityInsentient back to its default move speed.
	 * @param insEntity the EntityInsentient whose speed you want to reset
	 */
	protected void resetMobSpeed(EntityInsentient insEntity)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.d);
		AttributeModifier modifier = new AttributeModifier(movementSpeedUID, "DynamicDifficulty movement speed reset", 0, 1);
		attribute.b(modifier);
		attribute.a(modifier);
	}
	
	/**
	 * Resets the follow distance of the given EntityInsentient back to its default follow distance.
	 * @param insEntity the EntityInsentient whose follow distance you want to reset
	 * @param mobType the mob type of the entity insentient
	 */
	protected void resetMobFollowDistance(EntityInsentient insEntity, MobType mobType)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.b);
		attribute.setValue(mobType.getDefaultFollowDistance());
	}
	
	/**
	 * Resets the knockback resistance of the given EntityInsentient back to its 
	 * default move knockback resistance.
	 * @param insEntity the EntityInsentient whose knockback resistance you want to reset
	 */
	protected void resetMobKnockback(EntityInsentient insEntity)
	{
		AttributeInstance attribute = insEntity.getAttributeInstance(GenericAttributes.c);
		attribute.setValue(0);
	}
}
