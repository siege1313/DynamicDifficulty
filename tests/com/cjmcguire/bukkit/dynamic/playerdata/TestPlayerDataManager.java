package com.cjmcguire.bukkit.dynamic.playerdata;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.filehandlers.PlayerFileHandler;

/**
 * Tests the PlayerDataManager class.
 * @author CJ McGuire
 */
public class TestPlayerDataManager 
{
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
		
		PlayerInfo playerInfo = new PlayerInfo("player");
		
		PlayerInfo nullInfo = playerDataManager.addPlayerInfo(playerInfo);
		assertNull(nullInfo);
		
		assertSame(playerInfo, playerDataManager.getPlayerInfo("player"));
	}
	
	/**
	 * Tests what happens when a player with the same name gets added.
	 */
	@Test
	public void testDuplicateAddOfPlayerData()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo1 = new PlayerInfo("player1");
		playerDataManager.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo2 = new PlayerInfo("player1");
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
		
		PlayerInfo playerInfo1 = new PlayerInfo("player1");
		playerDataManager.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo1Copy = playerDataManager.removePlayerInfo("player1");
		
		assertSame(playerInfo1, playerInfo1Copy);
	}
	
	/**
	 * Tests the getPlayersMobInfo() method.
	 */
	@Test
	public void testGetPlayersMobInfo()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		// make the playerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		MobInfo testInfo = playerDataManager.getPlayersMobInfo(playerName, MobType.BLAZE);
		
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
		
		String playerName = "player";
		
		assertFalse(playerDataManager.playerInfoExists("player"));
		
		
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		playerDataManager.addPlayerInfo(playerInfo);

		assertTrue(playerDataManager.playerInfoExists("player"));
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
		
		String playerName = "testPlayer1";

		assertFalse(playerDataManager.playerInfoExists("player"));
		
		playerDataManager.getPlayerInfo(playerName);

		assertTrue(playerDataManager.playerInfoExists(playerName));
	}
	
	/**
	 * Tests the saveAllPlayerData() method.
	 */
	@Test
	public void testSaveAllPlayerData()
	{
		// make the playerInfo
		String playerName = "testPlayer2";
		PlayerInfo playerInfo = new PlayerInfo(playerName);

		String playerName3 = "testPlayer3";
		PlayerInfo playerInfo3 = new PlayerInfo(playerName3);
		
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setSetting(Setting.MANUAL);
		
		MobInfo caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		caveSpiderInfo.setManualPerformanceLevel(130);

		MobInfo creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		creeperInfo.setAutoPerformanceLevel(70);
		
		// add the playerInfo to the plugin
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		playerDataManager.addPlayerInfo(playerInfo);
		playerDataManager.addPlayerInfo(playerInfo3);
		
		playerDataManager.saveAllPlayerData();
		playerDataManager.clearPlayerData();
		
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		playerDataManager.setPlayerFileHandler(fileHandler);
			
		// test that the values were saved
		fileHandler.loadPlayerData(playerName);
		fileHandler.loadPlayerData(playerName3);
		
		
		playerInfo = playerDataManager.getPlayerInfo(playerName);
		playerInfo3 = playerDataManager.getPlayerInfo(playerName3);
		
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .0001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .0001);
		
		
		MobInfo zombieInfo = playerInfo3.getMobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getAutoPerformanceLevel(), .0001);
	}
}
