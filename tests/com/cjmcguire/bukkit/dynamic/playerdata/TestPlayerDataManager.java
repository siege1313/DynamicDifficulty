package com.cjmcguire.bukkit.dynamic.playerdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.filehandlers.PlayerFileHandler;

/**
 * Tests the PlayerDataManager class.
 * @author CJ McGuire
 */
public class TestPlayerDataManager 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final UUID PLAYER_2_ID = UUID.fromString("12345678-1234-1234-1234-123456789002");
	private static final UUID PLAYER_3_ID = UUID.fromString("12345678-1234-1234-1234-123456789003");

	/**
	 * Tests the PlayerDataManager is a singleton.
	 */
	@Test
	public void testSingleton()
	{
		PlayerDataManager playerDataManager1 = PlayerDataManager.getInstance();
		PlayerDataManager playerDataManager2 = PlayerDataManager.getInstance();
		
		assertSame(playerDataManager1, playerDataManager2);
	}
	
	/**
	 * Tests the initial adding of one PlayerData.
	 */
	@Test
	public void testFirstAddOfPlayerData()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		PlayerInfo nullInfo = playerDataManager.addPlayerInfo(playerInfo);
		assertNull(nullInfo);
		
		assertSame(playerInfo, playerDataManager.getPlayerInfo(PLAYER_1_ID));
	}
	
	/**
	 * Tests what happens when a player with the same name gets added.
	 */
	@Test
	public void testDuplicateAddOfPlayerData()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo1 = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo2 = new PlayerInfo(PLAYER_1_ID);
		PlayerInfo playerInfo1Copy = playerDataManager.addPlayerInfo(playerInfo2);
		
		assertSame(playerInfo1, playerInfo1Copy);
	}
	
	/**
	 * Tests removing a PlayerInfo.
	 */
	@Test
	public void testRemovePlayerData()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo1 = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo1Copy = playerDataManager.removePlayerInfo(PLAYER_1_ID);
		
		assertSame(playerInfo1, playerInfo1Copy);
	}
	
	/**
	 * Tests the getPlayersMobInfo() method.
	 */
	@Test
	public void testGetPlayersMobInfo()
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		MobInfo testInfo = playerDataManager.getPlayersMobInfo(PLAYER_1_ID, MobType.BLAZE);
		
		assertSame(blazeInfo, testInfo);
	}
	
	/**
	 * Tests the playerInfoExists() method.
	 */
	@Test
	public void testPlayerInfoExists()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		assertFalse(playerDataManager.playerInfoExists(PLAYER_1_ID));
		
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		assertTrue(playerDataManager.playerInfoExists(PLAYER_1_ID));
	}
	
	/**
	 * Tests the getPlayerInfo() method when the player's PalyerInfo
	 * does not initially exist.
	 */
	@Test
	public void testGetPlayerInfoWhenNotExist()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.setPlayerFileHandler(new PlayerFileHandler(null));

		assertFalse(playerDataManager.playerInfoExists(PLAYER_1_ID));
		
		playerDataManager.getPlayerInfo(PLAYER_1_ID);

		assertTrue(playerDataManager.playerInfoExists(PLAYER_1_ID));
	}
	
	/**
	 * Tests the saveAllPlayerData() method.
	 */
	@Test
	public void testSaveAllPlayerData()
	{
		// make the playerInfo
		PlayerInfo playerInfo2 = new PlayerInfo(PLAYER_2_ID);

		PlayerInfo playerInfo3 = new PlayerInfo(PLAYER_3_ID);
		
		
		MobInfo blazeInfo = playerInfo2.getMobInfo(MobType.BLAZE);
		blazeInfo.setSetting(Setting.MANUAL);
		
		MobInfo caveSpiderInfo = playerInfo2.getMobInfo(MobType.CAVE_SPIDER);
		caveSpiderInfo.setManualPerformanceLevel(130);

		MobInfo creeperInfo = playerInfo2.getMobInfo(MobType.CREEPER);
		creeperInfo.setAutoPerformanceLevel(70);
		
		// add the playerInfo to the plugin
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		playerDataManager.addPlayerInfo(playerInfo2);
		playerDataManager.addPlayerInfo(playerInfo3);
		
		
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		playerDataManager.setPlayerFileHandler(fileHandler);
		
		playerDataManager.saveAllPlayerData();
		playerDataManager.clearPlayerData();
			
		// test that the values were saved
		fileHandler.loadPlayerData(PLAYER_2_ID);
		fileHandler.loadPlayerData(PLAYER_3_ID);
		
		
		playerInfo2 = playerDataManager.getPlayerInfo(PLAYER_2_ID);
		playerInfo3 = playerDataManager.getPlayerInfo(PLAYER_3_ID);
		
		
		blazeInfo = playerInfo2.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo2.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .0001);

		creeperInfo = playerInfo2.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .0001);
		
		
		MobInfo zombieInfo = playerInfo3.getMobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getAutoPerformanceLevel(), .0001);
	}
}
