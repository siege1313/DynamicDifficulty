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
	 * Tests running headless.
	 */
	@Test
	public void testHeadless()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		assertTrue(plugin.isRunningWithHead());
		
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
		
		PlayerInfo playerInfo1 = new PlayerInfo("player1");
		
		PlayerInfo nullInfo = plugin.addPlayerInfo(playerInfo1);
		assertNull(nullInfo);
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
		PlayerInfo copy = plugin.addPlayerInfo(playerInfo2);
		
		assertSame(playerInfo1, copy);
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

		PlayerInfo copy = plugin.removePlayerInfo("player1");
		
		assertSame(playerInfo1, copy);
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
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		
		plugin.addPlayerInfo(playerInfo);
		
		MobInfo testInfo = plugin.getPlayersMobInfo(playerName, MobType.BLAZE);
		
		assertSame(blazeInfo, testInfo);
		
	}
}
