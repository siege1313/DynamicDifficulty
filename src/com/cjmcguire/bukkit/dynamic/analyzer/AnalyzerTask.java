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
 * dynamic difficulty implementation. It is scheduled to run 
 * repeatedly on a timer. The AnalyzerTask takes the data obtained by 
 * the Monitor and analyzes it to determine all of the players' 
 * performance levels.
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
	 * This method updates the values for the players' estimated 
	 * performance levels and current performance levels for each 
	 * type of mob. 
	 */
	@Override
	public void run()
	{
		this.updatePlayerData();
	}
	
	/**
	 * Updates the players' estimated performance level and current 
	 * performance levels for each type of mob as long as the player's 
	 * setting is set to AUTO.
	 */
	protected void updatePlayerData()
	{
		Collection<PlayerInfo> playerCollection = playerDataManager.getPlayerData();
		
		MobType[] mobTypes = MobType.values();
		
		// Loop through all PlayerInfos.
		for(PlayerInfo playerInfo: playerCollection)
		{
			// Loop through each MobType in the PlayerInfo.
			for(MobType mobType: mobTypes)
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
