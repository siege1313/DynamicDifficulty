package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.Test;

/**
 * Tests the PlayerFileHandler class.
 * @author CJ McGuire
 */
public class TestPlayerFileHandler 
{
	
	/**
	 * Tests the getDefaultPlayerConfig() method.
	 */
	@Test
	public void testGetDefaultPlayerConfig()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		FileConfiguration fileConfig = fileHandler.getDefaultPlayerConfig();
		
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
	 * Tests the loadPlayerInfoFromFile() method.
	 */
	@Test
	public void testLoadPlayerInfoFromFile()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		
		String playerName = "testPlayer";
		
		fileHandler.loadPlayerDataFromFile(playerName);
		
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
	 * Tests the loadPlayerInfoFromFile() method when the player's player.yml
	 * file has bad data in it.
	 */
	@Test
	public void testLoadPlayerInfoFromFileBadPlayerYml()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		String playerName = "testPlayer4";
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		fileHandler.loadPlayerDataFromFile(playerName);
		
		PlayerInfo playerInfo = plugin.getPlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.AUTO, blazeInfo.getSetting());
		assertEquals(100, blazeInfo.getManualPerformanceLevel(), .0001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .0001);
	}
	
	
	/**
	 * Tests the savePlayerDataToFile() method.
	 */
	@Test
	public void testSavePlayerDataToFile()
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
		
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		fileHandler.savePlayerDataToFile(playerName);
		plugin.removePlayerInfo(playerName);
		
		
		fileHandler.loadPlayerDataFromFile(playerName);
		playerInfo = plugin.getPlayerInfo(playerName);
		
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .0001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the saveAllPlayerData() method.
	 */
	@Test
	public void testSaveAllPlayerData()
	{
		// make the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();
		
		
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
		plugin.addPlayerInfo(playerInfo);
		plugin.addPlayerInfo(playerInfo3);
		
		plugin.onDisable();
		
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
			
		// test that the values were saved
		fileHandler.loadPlayerDataFromFile(playerName);
		fileHandler.loadPlayerDataFromFile(playerName3);
		
		
		playerInfo = plugin.getPlayerInfo(playerName);
		playerInfo3 = plugin.getPlayerInfo(playerName3);
		
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .0001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .0001);
		
		
		MobInfo zombieInfo = playerInfo3.getMobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getAutoPerformanceLevel(), .0001);
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
	
		
		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		// test that the values were saved
		fileHandler.loadPlayerDataFromFile(playerName);
		
		playerInfo = plugin.getPlayerInfo(playerName);
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .001);
	}

	/**
	 * Tests the loadPlayerConfig() method.
	 */
	@Test
	public void testLoadPlayerConfig()
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		plugin.onEnable();

		String playerName = "testPlayer";

		PlayerFileHandler fileHandler = plugin.getPlayerFileHandler();
		
		FileConfiguration playerYmlFileConfig = fileHandler.loadPlayerConfig(playerName);

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
