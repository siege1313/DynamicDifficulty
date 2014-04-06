package com.cjmcguire.bukkit.dynamic.analyzer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the AnalyzerTask class.
 * @author CJ McGuire
 */
public class TestAnalyzerTask 
{
	/**
	 * Tests the updatePlayerData() method.
	 */
	@Test
	public void testRun()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addIDToInteractedWithIDs(2);
		blazeInfo.addIDToInteractedWithIDs(3);
		
		blazeInfo.addToDamagePlayerGave(100);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask();

		analyzer.run();
		
		assertEquals(200, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100 + MobInfo.MAX_INCREMENT, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updatePlayerData() method.
	 */
	@Test
	public void testUpdatePlayerData()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addIDToInteractedWithIDs(2);
		blazeInfo.addIDToInteractedWithIDs(3);
		blazeInfo.addToDamagePlayerGave(100);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask();

		analyzer.updatePlayerData();
		
		assertEquals(200, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100 + MobInfo.MAX_INCREMENT, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updatePlayerData() method when the player's setting is manual.
	 */
	@Test
	public void testUpdatePlayerDataWhenManual()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addToDamagePlayerGave(100);
		blazeInfo.setSetting(Setting.MANUAL);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask();

		analyzer.updatePlayerData();
		
		assertEquals(100, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
}
