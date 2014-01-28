package com.cjmcguire.bukkit.dynamic.analyzer;

import java.util.Collection;

import org.bukkit.scheduler.BukkitRunnable;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.Setting;

/**
 * The AnalyzerTask carries out the function of the Analyzer in the 
 * Dynamic Difficulty implementation. It is scheduled to run repeatedly
 * on a timer. The AnalyzerTask takes the player data obtained by the
 * Monitor section and analyzes it to determine a player's performance level.
 * @author CJ McGuire
 */
public class AnalyzerTask extends BukkitRunnable
{
	
	private final DynamicDifficulty plugin;
	
	/**
	 * Initializes the AnalyzerTask.
	 * @param plugin a reference to the DynamicDifficulty plugin that uses 
	 * this AnalyzerTask
	 */
	public AnalyzerTask(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * This method updates the values for current performance level and 
	 * estimated performance level for each mob for all players. 
	 */
	@Override
	public void run()
	{
		this.updatePlayerData();
	}
	
	/**
	 * Updates the current performance level and estimated performance level 
	 * of all mob types for all players as long as the player's performance 
	 * level is set to auto.
	 */
	protected void updatePlayerData()
	{
		Collection<PlayerInfo> playerCollection = plugin.getPlayerData();
		
		// loop through all PlayerInfos
		for(PlayerInfo playerInfo: playerCollection)
		{
			// loop through each MobType in each PlayerInfo
			for(MobType mobType: MobType.values())
			{
				MobInfo mobInfo = playerInfo.getMobInfo(mobType);
				if(mobInfo.getSetting() == Setting.AUTO)
				{
					mobInfo.updateEstimatedPerformanceLevel();
					mobInfo.updateAutoPerformanceLevel();
				}		
			}
		}
	}
}
