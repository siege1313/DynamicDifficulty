package com.cjmcguire.bukkit.dynamic.analyzer;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

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
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	
	/**
	 * Tests the updatePlayerData() method when the player's setting is AUTO.
	 */
	@Test
	public void testUpdatePlayerDataWhenAuto()
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.BLAZE);
		mobInfo.addIDToInteractedWithIDs(1);
		mobInfo.addIDToInteractedWithIDs(2);
		mobInfo.addIDToInteractedWithIDs(3);
		mobInfo.addToDamagePlayerGave(100);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask();

		analyzer.run();
		
		assertEquals(200, mobInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100 + mobInfo.getMaxIncrement(), mobInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updatePlayerData() method when the player's setting is MANUAL.
	 */
	@Test
	public void testUpdatePlayerDataWhenManual()
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.addIDToInteractedWithIDs(1);
		blazeInfo.addToDamagePlayerGave(100);
		blazeInfo.setSetting(Setting.MANUAL);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		AnalyzerTask analyzer = new AnalyzerTask();

		analyzer.run();
		
		assertEquals(100, blazeInfo.getEstimatedPerformanceLevel(), .0001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
}
