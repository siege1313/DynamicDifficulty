package com.cjmcguire.bukkit.dynamic.analyzer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.Setting;

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
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addIDToInteractedWithIDs(2);
		blazeInfo.addIDToInteractedWithIDs(3);
		
		blazeInfo.addToDamagePlayerGave(100);
		
		plugin.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask(plugin);

		analyzer.run();
		
		assertEquals(200, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100 + MobInfo.MAX_INCREMENT, blazeInfo.getCurrentPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updatePlayerData() method.
	 */
	@Test
	public void testUpdatePlayerData()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addIDToInteractedWithIDs(2);
		blazeInfo.addIDToInteractedWithIDs(3);
		blazeInfo.addToDamagePlayerGave(100);
		
		plugin.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask(plugin);

		analyzer.updatePlayerData();
		
		assertEquals(200, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100 + MobInfo.MAX_INCREMENT, blazeInfo.getCurrentPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updatePlayerData() method when the player's setting is manual.
	 */
	@Test
	public void testUpdatePlayerDataWhenManual()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addToDamagePlayerGave(100);
		blazeInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask(plugin);

		analyzer.updatePlayerData();
		
		assertEquals(100, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100, blazeInfo.getCurrentPerformanceLevel(), .0001);
	}
}
