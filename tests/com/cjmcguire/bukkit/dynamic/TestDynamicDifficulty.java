package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the DynamicDifficulty class.
 * @author CJ McGuire
 */
public class TestDynamicDifficulty 
{
	/**
	 * Tests that DynamicDifficulty runs with its head by default.
	 */
	@Test
	public void testRunWithHeadByDefault()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		assertTrue(plugin.isRunningWithHead());
	}
	
	/**
	 * Tests that DynamicDifficulty can run headless.
	 */
	@Test
	public void testHeadless()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();		
		plugin.setRunningWithHead(false);
		
		plugin.onEnable();
		plugin.onDisable();
		
		assertFalse(plugin.isRunningWithHead());
	}
	
	/**
	 * Tests the initial adding of one PlayerData.
	 */
	@Test
	public void testFirstAddOfPlayerData()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("player");
		
		PlayerInfo nullInfo = plugin.addPlayerInfo(playerInfo);
		assertNull(nullInfo);
		
		assertSame(playerInfo, plugin.getPlayerInfo("player"));
	}
	
	/**
	 * Tests what happens when a player with the same name gets added.
	 */
	@Test
	public void testDuplicateAddOfPlayerData()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo1 = new PlayerInfo("player1");
		plugin.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo2 = new PlayerInfo("player1");
		PlayerInfo playerInfo1Copy = plugin.addPlayerInfo(playerInfo2);
		
		assertSame(playerInfo1, playerInfo1Copy);
	}
	
	/**
	 * Tests removing a PlayerInfo.
	 */
	@Test
	public void testRemovePlayerData()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo1 = new PlayerInfo("player1");
		plugin.addPlayerInfo(playerInfo1);

		PlayerInfo playerInfo1Copy = plugin.removePlayerInfo("player1");
		
		assertSame(playerInfo1, playerInfo1Copy);
	}
	
	/**
	 * Tests the safeLogInfo() method.
	 */
	@Test
	public void testSafeLogInfo()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		assertFalse(plugin.safeLogInfo("test safeLogInfo()"));
	}
	
	/**
	 * Tests the getPlayersMobInfo() method.
	 */
	@Test
	public void testGetPlayersMobInfo()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// make the playerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		MobInfo testInfo = plugin.getPlayersMobInfo(playerName, MobType.BLAZE);
		
		assertSame(blazeInfo, testInfo);
	}
	
	/**
	 * Tests the playerInfoExists() method.
	 */
	@Test
	public void testPlayerInfoExists()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		String playerName = "player";
		
		assertFalse(plugin.playerInfoExists("player"));
		
		
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		plugin.addPlayerInfo(playerInfo);

		assertTrue(plugin.playerInfoExists("player"));
	}
	
	/**
	 * Tests the getPlayerInfo() method when the player's PalyerInfo
	 * does not initially exist.
	 */
	@Test
	public void testGetPlayerInfoWhenNotExist()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer1";

		assertFalse(plugin.playerInfoExists("player"));
		
		plugin.getPlayerInfo(playerName);

		assertTrue(plugin.playerInfoExists(playerName));
	}
}