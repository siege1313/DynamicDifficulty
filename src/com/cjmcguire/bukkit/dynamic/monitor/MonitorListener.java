package com.cjmcguire.bukkit.dynamic.monitor;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.cjmcguire.bukkit.dynamic.AbstractEntityDamageListener;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * The MonitorListener carries out the function of the Monitor in the 
 * Dynamic Difficulty implementation. It monitors events that occur in 
 * game and determines a player's performance level for each mob based 
 * on these events.
 * @author CJ McGuire
 */
public class MonitorListener extends AbstractEntityDamageListener
{
	private final PlayerDataManager playerDataManager;

	/**
	 * Initializes the MonitorListener.
	 */
	public MonitorListener()
	{
		this.playerDataManager = PlayerDataManager.getInstance();
	}
	
	/**
	 * This method triggers whenever a creature takes damage in 
	 * Minecraft. This method will update a player's player data, 
	 * whenever the player takes damage from a mob, or when a mob 
	 * deals damage to the player. 
	 * @param event the EntityDamageEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event)
	{
		this.onEntityDamageByEntityAction(event);
	}
	
	@Override
	protected int playerDamagedAction(Player player, LivingEntity mob, int damage) 
	{
		// If the player is not temporarily immune
		if(this.notInvincible(player))
		{
			// If the player is not in creative mode
			if(player.getGameMode() != GameMode.CREATIVE)
			{
				UUID playerID = player.getUniqueId();
				this.updateDamagePlayerReceived(playerID, mob, damage);
			}
		}
		
		return damage;
	}

	@Override
	protected int livingEntityDamagedAction(LivingEntity mob, Player player, int damage) 
	{
		// if the mob is not temporarily immune
		if(this.notInvincible(mob))
		{
			// if the player is not in creative mode
			if(player.getGameMode() != GameMode.CREATIVE)
			{
				UUID playerID = player.getUniqueId();
				this.updateDamagePlayerGave(mob, playerID, damage);
			}
		}
		
		return damage;
	}
	
	/**
	 * Updates the value for the amount of damage that a Player has 
	 * received from a certain MobType. Note that if a player was 
	 * damaged by something that was not a standard MobType, then this 
	 * method does nothing. Also, note that a player's setting must be 
	 * set to Auto for this method to update the damage a player 
	 * received.
	 * @param playerID the UUID of the player that took damage
	 * @param damager the living entity that did the damage
	 * @param damage the damage the damager caused
	 */
	protected void updateDamagePlayerReceived(UUID playerID, Entity damager, int damage)
	{
		MobType mobType = MobType.getEntitysMobType(damager);

		if(mobType != null)
		{
			// get the PlayerInfo's MobData
			MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);

			// if the player's setting is set to AUTO (OFF and MANUAL will not update anything)
			if(mobInfo.getSetting() == Setting.AUTO)
			{
				mobInfo.addIDToInteractedWithIDs(damager.getEntityId());
				mobInfo.addToDamagePlayerReceived(damage);
			}
		}
	}

	/**
	 * Updates the value for the amount of damage that a Player has 
	 * given to a certain MobType. Note that if a player damaged 
	 * something that was not a standard MobType, then this method 
	 * does nothing. Also, note that a player's setting must be set to 
	 * Auto for this method to update the damage a player gave.
	 * @param damaged the mob that was damaged
	 * @param playerID the UUID of the player that did damage
	 * @param damage the damage the player causes
	 */
	protected void updateDamagePlayerGave(Entity damaged, UUID playerID, int damage)
	{
		MobType mobType = MobType.getEntitysMobType(damaged);
		
		if(mobType != null)
		{
			// get the PlayerInfo's MobData
			MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);
			
			// if the player's setting is set to AUTO (OFF and MANUAL will not update anything)
			if(mobInfo.getSetting() == Setting.AUTO)
			{
				mobInfo.addIDToInteractedWithIDs(damaged.getEntityId());
				mobInfo.addToDamagePlayerGave(damage);
			}
		}
	}
}
