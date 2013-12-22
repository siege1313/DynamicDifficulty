package com.cjmcguire.bukkit.dynamic.monitor;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.Setting;

/**
 * The MonitorListener carries out the function of the Monitor in the 
 * Dynamic Difficulty implementation. It monitors events that occur in-game and
 * determines a player's performance level for each mob based on these events.
 * @author CJ McGuire
 */
public class MonitorListener implements Listener
{
	private DynamicDifficulty plugin;

	/**
	 * Initializes the MonitorListener.
	 * @param plugin a reference to the DynamicDifficulty plugin that uses 
	 * this MonitorListener
	 */
	public MonitorListener(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * This method triggers whenever a creature takes damage in Minecraft. This method
	 * will update a player's player data, whenever the player takes damage from a mob,
	 * or when a mob deals damage to the player. 
	 * @param event the EntityDamageEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		// get the damager and convert it to the shooter if it was originally the projectile
		Entity damager = event.getDamager();
		if(damager instanceof Projectile)
		{
			damager = ((Projectile) damager).getShooter();
		}
		
		// get the damaged
		Entity damaged = event.getEntity();
		
		// get the damage caused
		int damage = (int) event.getDamage();
		
		// if the entity being damaged was the player 
		// and the entity doing the damage was a living entity (a mob) 
		if(damaged instanceof Player && damager instanceof LivingEntity)
		{
			Player player = (Player)damaged;

			// if the player is not temporarily immune
			if(player.getNoDamageTicks() < player.getMaximumNoDamageTicks()/2.0)
			{
				// if the player is not in creative mode
				if(player.getGameMode() != GameMode.CREATIVE)
				{
					String playerName = player.getDisplayName();
					this.updateDamagePlayerReceived(playerName, (LivingEntity)damager, damage);
				}
			}
		}
		// if the entity being damaged was a living entity (a mob)
		// and the entity doing the damage was a player
		else if(damaged instanceof LivingEntity && damager instanceof Player)
		{
			LivingEntity mob = (LivingEntity)damaged;
			// if the mob is not temporarily immune
			if(mob.getNoDamageTicks() < mob.getMaximumNoDamageTicks()/2.0)
			{
				Player player = (Player)damager;
				// if the player is not in creative mode
				if(player.getGameMode() != GameMode.CREATIVE)
				{
					String playerName = player.getDisplayName();
					this.updateDamagePlayerGave(damaged, playerName, damage);
				}
			}
		}
	}

	/**
	 * Updates the value for the amount of damage that a Player has received from a 
	 * certain MobType. Note that if a player was damaged by something that was not a 
	 * standard MobType, then this method does nothing. Also, note that a player's
	 * setting must be set to Auto for this method to update the damage a player received.
	 * @param playerName the name of the player that took damage
	 * @param damager the living entity that did the damage
	 * @param damage the damage the damager caused
	 */
	public void updateDamagePlayerReceived(String playerName, Entity damager, int damage)
	{
		MobType mobType = MobType.getEntitysMobType(damager);

		if(mobType != null)
		{
			// get the PlayerInfo's MobData
			MobInfo mobInfo = plugin.getPlayersMobInfo(playerName, mobType);

			// if the player's setting is set to AUTO (OFF and MANUAL will not update anything)
			if(mobInfo.getSetting() == Setting.AUTO)
			{
				mobInfo.addIDToInteractedWithIDs(damager.getEntityId());
				mobInfo.addToDamagePlayerReceived(damage);
			}
		}
	}

	/**
	 * Updates the value for the amount of damage that a Player has given to a 
	 * certain MobType. Note that if a player damaged something that was not a 
	 * standard MobType, then this method does nothing. Also, note that a player's
	 * setting must be set to Auto for this method to update the damage a player gave.
	 * @param damaged the mob that was damaged
	 * @param playerName the name of the player that did damage
	 * @param damage the damage the player causes
	 */
	public void updateDamagePlayerGave(Entity damaged, String playerName, int damage)
	{
		MobType mobType = MobType.getEntitysMobType(damaged);
		
		if(mobType != null)
		{
			// get the PlayerInfo's MobData
			MobInfo mobInfo = plugin.getPlayersMobInfo(playerName, mobType);
			
			// if the player's setting is set to AUTO (OFF and MANUAL will not update anything)
			if(mobInfo.getSetting() == Setting.AUTO)
			{
				mobInfo.addIDToInteractedWithIDs(damaged.getEntityId());
				mobInfo.addToDamagePlayerGave(damage);
			}
		}
	}
}
