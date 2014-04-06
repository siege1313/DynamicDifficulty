package com.cjmcguire.bukkit.dynamic.analyzer;

import java.util.Collection;

import org.bukkit.scheduler.BukkitRunnable;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * The AnalyzerTask carries out the function of the Analyzer in the 
 * Dynamic Difficulty implementation. It is scheduled to run repeatedly
 * on a timer. The AnalyzerTask takes the player data obtained by the
 * Monitor section and analyzes it to determine a player's performance 
 * level.
 * @author CJ McGuire
 */
public class AnalyzerTask extends BukkitRunnable
{
	
	private final PlayerDataManager playerDataManager;
	
	/**
	 * Initializes the AnalyzerTask.
	 */
	public AnalyzerTask()
	{
		this.playerDataManager = PlayerDataManager.getInstance();
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
	 * Updates the current performance level and estimated performance 
	 * level of all mob types for all players as long as the player's 
	 * performance level is set to AUTO.
	 */
	protected void updatePlayerData()
	{
		Collection<PlayerInfo> playerCollection = playerDataManager.getPlayerData();
		
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
