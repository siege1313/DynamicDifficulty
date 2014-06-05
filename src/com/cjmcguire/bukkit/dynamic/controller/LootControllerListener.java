package com.cjmcguire.bukkit.dynamic.controller;

import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * The LootControllerListener carries out the function of the 
 * Controller in the dynamic difficulty implementation. It listens to 
 * events that relate to mob loot drops and controls the outcome of 
 * the events based on the data gathered by the Analyzer.
 * @author CJ McGuire
 */
public class LootControllerListener implements Listener
{
	private final PlayerDataManager playerDataManager;

	/**
	 * Initializes the LootControllerListener.
	 */
	public LootControllerListener()
	{
		this.playerDataManager = PlayerDataManager.getInstance();
	}
	
	/**
	 * This method triggers whenever a creature dies. This method 
	 * scales the amount of exp and items dropped that the mob
	 * drops based on the player's performance level for that mob.
	 * @param event the EntityDeathEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void onEntityDeathEvent(EntityDeathEvent event)
	{
		// Get the killed LivingEntity.
		LivingEntity killedEntity = event.getEntity();
		
		// Get the MobType of the living entity.
		MobType mobType = MobType.getEntitysMobType(killedEntity);
		
		Player player = killedEntity.getKiller();
		
		// If the player exists and the mob had a valid MobType
		if(player != null && mobType != null && player.getGameMode() != GameMode.CREATIVE)
		{
			UUID playerID = player.getUniqueId();
			
			int alteredEXP = this.manipulateEXP(playerID, mobType, (int) event.getDroppedExp());
			event.setDroppedExp(alteredEXP);
			
			this.manipulateItemAmounts(playerID, mobType, event.getDrops());
		}
	}
	
	protected int manipulateEXP(UUID playerID, MobType mobType, int baseEXP)
	{
		// get the PlayerInfo's MobData
		MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);
		
		int alteredEXP = baseEXP;
		
		if(mobInfo.shouldScaleXP())
		{
			double performanceLevel = mobInfo.getPerformanceLevelInUse();
		
			// If performance level is greater than 100, we reward 
			// the player with more EXP. If the player has a lower 
			// performance level, we do not penalize them; we give 
			// them the normal EXP.
			if(performanceLevel > 100)
			{
				alteredEXP = (int) (baseEXP * performanceLevel/100.0 + 0.5); // add .5 here so that it rounds correctly
			}
		}
		
		return alteredEXP;
	}
	
	protected void manipulateItemAmounts(UUID playerID, MobType mobType, List<ItemStack> itemStacks)
	{
		// get the PlayerInfo's MobData
		MobInfo mobInfo = playerDataManager.getPlayersMobInfo(playerID, mobType);
		
		if(mobInfo.shouldScaleLoot())
		{
			double performanceLevel = mobInfo.getPerformanceLevelInUse(); 
		
			// If performance level is greater than 100, we reward the player
			// with more items.
			if(performanceLevel > 100)
			{
				for(ItemStack stack: itemStacks)
				{
					int amount = (int)(stack.getAmount() * performanceLevel/100.0 + 0.5);
					stack.setAmount(amount);
				}
			}
		}
	}
}
