package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
	 * Tests the loadPlayerInfo() method.
	 */
	@Test
	public void testLoadPlayerInfo()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer";
		
		File playerYmlFile = new File(playerName + ".yml");
		
		FileConfiguration playerYmlFileConfig = YamlConfiguration.loadConfiguration(playerYmlFile);
		
		plugin.loadPlayerInfo(playerYmlFileConfig, playerName);
		
		PlayerInfo playerInfo = plugin.getPlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.AUTO, blazeInfo.getSetting());
		assertEquals(100, blazeInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .001);
		
		MobInfo caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(Setting.DISABLED, caveSpiderInfo.getSetting());
		assertEquals(100, caveSpiderInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, caveSpiderInfo.getAutoPerformanceLevel(), .001);

		MobInfo creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(Setting.MANUAL, creeperInfo.getSetting());
		assertEquals(200, creeperInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, creeperInfo.getAutoPerformanceLevel(), .001);

		MobInfo enderManInfo = playerInfo.getMobInfo(MobType.ENDERMAN);
		assertEquals(Setting.AUTO, enderManInfo.getSetting());
		assertEquals(100, enderManInfo.getManualPerformanceLevel(), .001);
		assertEquals(145, enderManInfo.getAutoPerformanceLevel(), .001);

		MobInfo ghastInfo = playerInfo.getMobInfo(MobType.GHAST);
		assertEquals(Setting.AUTO, ghastInfo.getSetting());
		assertEquals(90, ghastInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, ghastInfo.getAutoPerformanceLevel(), .001);
	}
	
	/**
	 * Tests the loadPlayerInfo() method when the player's player.yml
	 * file has bad data in it.
	 */
	@Test
	public void testLoadBadPlayerYml()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer4";
		
		File playerYmlFile = new File(playerName + ".yml");
		
		FileConfiguration playerYmlFileConfig = YamlConfiguration.loadConfiguration(playerYmlFile);
		
		plugin.loadPlayerInfo(playerYmlFileConfig, playerName);
		
		PlayerInfo playerInfo = plugin.getPlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.AUTO, blazeInfo.getSetting());
		assertEquals(100, blazeInfo.getManualPerformanceLevel(), .0001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
	
	
	/**
	 * Tests the savePlayerInfo() method.
	 */
	@Test
	public void testSavePlayerInfo()
	{
		// make the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		// make the playerInfo
		String playerName = "testPlayer2";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setSetting(Setting.MANUAL);
		
		MobInfo caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		caveSpiderInfo.setManualPerformanceLevel(130);

		MobInfo creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		creeperInfo.setAutoPerformanceLevel(70);
		
		// add the playerInfo to the plugin
		plugin.addPlayerInfo(playerInfo);
		
		// save the values
		File playerFile = new File(playerName + ".yml");
		FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		plugin.savePlayerInfo(playerConfig, playerFile, playerName);
		
		// test that the values were saved
		plugin.loadPlayerInfo(playerConfig, playerName);
		
		playerInfo = plugin.getPlayerInfo(playerName);
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .0001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * 
	 */
	@Test
	public void testOnDisableWithPlayersStillLoggedIn()
	{
		// make the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		// make the playerInfo
		String playerName = "testPlayer3";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		// change the 3 mob infos
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setSetting(Setting.MANUAL);
		
		MobInfo caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		caveSpiderInfo.setManualPerformanceLevel(130);

		MobInfo creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		creeperInfo.setAutoPerformanceLevel(70);
		
		// add the playerInfo to the plugin
		plugin.addPlayerInfo(playerInfo);
		
		// save the values
		//File playerFile = new File(playerName + ".yml");
		//FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		//plugin.savePlayerInfo(playerConfig, playerFile, playerName);
		plugin.onDisable();
		
		File playerFile = new File(playerName + ".yml");
		FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		
		// test that the values were saved
		plugin.loadPlayerInfo(playerConfig, playerName);
		
		playerInfo = plugin.getPlayerInfo(playerName);
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .001);
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
	
	/**
	 * Tests the getDefaultPlayerConfig() method.
	 */
	@Test
	public void testGetDefaultPlayerConfig()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		FileConfiguration fileConfig = plugin.getDefaultPlayerConfig();
		
		assertEquals("auto", fileConfig.getString("blaze.setting"));
		assertEquals(100, fileConfig.getInt("blaze.manualPerformanceLevel"));
		assertEquals(100, fileConfig.getInt("blaze.autoPerformanceLevel"));

		assertEquals("auto", fileConfig.getString("cavespider.setting"));
		assertEquals(100, fileConfig.getInt("cavespider.manualPerformanceLevel"));
		assertEquals(100, fileConfig.getInt("cavespider.autoPerformanceLevel"));
		
		assertEquals("auto", fileConfig.getString("zombie.setting"));
		assertEquals(100, fileConfig.getInt("zombie.manualPerformanceLevel"));
		assertEquals(100, fileConfig.getInt("zombie.autoPerformanceLevel"));
	}
	
	/**
	 * Tests the loadPlayerYmlFileConfig() method.
	 */
	@Test
	public void testLoadPlayerYmlFileConfig()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();

		String playerName = "testPlayer";

		File playerYmlFile = new File(playerName + ".yml");

		FileConfiguration playerYmlFileConfig = plugin.loadPlayerConfig(playerYmlFile, playerName);

		assertEquals("auto", playerYmlFileConfig.getString("blaze.setting"));
		assertEquals(100, playerYmlFileConfig.getInt("blaze.manualPerformanceLevel"));
		assertEquals(100, playerYmlFileConfig.getInt("blaze.autoPerformanceLevel"));

		assertEquals("disabled", playerYmlFileConfig.getString("cavespider.setting"));
		assertEquals(100, playerYmlFileConfig.getInt("cavespider.manualPerformanceLevel"));
		assertEquals(100, playerYmlFileConfig.getInt("cavespider.autoPerformanceLevel"));

		assertEquals("manual", playerYmlFileConfig.getString("creeper.setting"));
		assertEquals(200, playerYmlFileConfig.getInt("creeper.manualPerformanceLevel"));
		assertEquals(100, playerYmlFileConfig.getInt("creeper.autoPerformanceLevel"));

		assertEquals("auto", playerYmlFileConfig.getString("enderman.setting"));
		assertEquals(100, playerYmlFileConfig.getInt("enderman.manualPerformanceLevel"));
		assertEquals(145, playerYmlFileConfig.getInt("enderman.autoPerformanceLevel"));

		assertEquals("auto", playerYmlFileConfig.getString("ghast.setting"));
		assertEquals(90, playerYmlFileConfig.getInt("ghast.manualPerformanceLevel"));
		assertEquals(100, playerYmlFileConfig.getInt("ghast.autoPerformanceLevel"));
	}
}
