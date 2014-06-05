package com.cjmcguire.bukkit.dynamic.filehandlers;

import static org.junit.Assert.*;

import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.filehandlers.PlayerFileHandler;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the PlayerFileHandler class.
 * @author CJ McGuire
 */
public class TestPlayerFileHandler 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final UUID PLAYER_2_ID = UUID.fromString("12345678-1234-1234-1234-123456789002");
	private static final UUID PLAYER_4_ID = UUID.fromString("12345678-1234-1234-1234-123456789004");

	/**
	 * Tests that the defaultPlayerConfiguration gets loaded in correctly.
	 */
	@Test
	public void testGetDefaultPlayerConfig()
	{		
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		
		FileConfiguration fileConfig = fileHandler.getFileConfig();
		
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
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		playerDataManager.setPlayerFileHandler(fileHandler);
		
		fileHandler.loadPlayerData(PLAYER_1_ID);
		
		PlayerInfo playerInfo = playerDataManager.getPlayerInfo(PLAYER_1_ID);
		
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.AUTO, blazeInfo.getSetting());
		assertEquals(100, blazeInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, blazeInfo.getAutoPerformanceLevel(), .001);
		assertEquals(10, blazeInfo.getMaxIncrement());
		assertTrue(blazeInfo.shouldScaleAttack());
		assertTrue(blazeInfo.shouldScaleDefense());
		assertTrue(blazeInfo.shouldScaleSpeed());
		assertTrue(blazeInfo.shouldScaleKnockbackResistance());
		assertTrue(blazeInfo.shouldScaleMaxFollowDistance());
		assertTrue(blazeInfo.shouldScaleXP());
		assertTrue(blazeInfo.shouldScaleLoot());
		
		MobInfo caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(Setting.DISABLED, caveSpiderInfo.getSetting());
		assertEquals(100, caveSpiderInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, caveSpiderInfo.getAutoPerformanceLevel(), .001);
		assertEquals(20, caveSpiderInfo.getMaxIncrement());
		assertFalse(caveSpiderInfo.shouldScaleAttack());
		assertTrue(caveSpiderInfo.shouldScaleDefense());
		assertTrue(caveSpiderInfo.shouldScaleSpeed());
		assertTrue(caveSpiderInfo.shouldScaleKnockbackResistance());
		assertTrue(caveSpiderInfo.shouldScaleMaxFollowDistance());
		assertTrue(caveSpiderInfo.shouldScaleXP());
		assertTrue(caveSpiderInfo.shouldScaleLoot());

		MobInfo creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(Setting.MANUAL, creeperInfo.getSetting());
		assertEquals(200, creeperInfo.getManualPerformanceLevel(), .001);
		assertEquals(100, creeperInfo.getAutoPerformanceLevel(), .001);
		assertEquals(30, creeperInfo.getMaxIncrement());
		assertTrue(creeperInfo.shouldScaleAttack());
		assertFalse(creeperInfo.shouldScaleDefense());
		assertTrue(creeperInfo.shouldScaleSpeed());
		assertTrue(creeperInfo.shouldScaleKnockbackResistance());
		assertTrue(creeperInfo.shouldScaleMaxFollowDistance());
		assertTrue(creeperInfo.shouldScaleXP());
		assertTrue(creeperInfo.shouldScaleLoot());

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
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		
		fileHandler.loadPlayerData(PLAYER_4_ID);

		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.setPlayerFileHandler(fileHandler);
		
		PlayerInfo playerInfo = playerDataManager.getPlayerInfo(PLAYER_4_ID);
		
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
		// make the playerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_2_ID);
		
		
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
		
		
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		playerDataManager.setPlayerFileHandler(fileHandler);
		
		fileHandler.savePlayerData(PLAYER_2_ID);
		playerDataManager.removePlayerInfo(PLAYER_2_ID);
		
		
		fileHandler.loadPlayerData(PLAYER_2_ID);
		playerInfo = playerDataManager.getPlayerInfo(PLAYER_2_ID);
		
		
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
/*	@Test
	public void testOnDisableWithPlayersStillLoggedIn()
	{
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
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		// save the values
		//File playerFile = new File(playerName + ".yml");
		//FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		//plugin.savePlayerInfo(playerConfig, playerFile, playerName);
	

		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		
		// test that the values were saved
		fileHandler.loadPlayerData(playerName);
		
		playerInfo = playerDataManager.getPlayerInfo(playerName);
		
		blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		assertEquals(Setting.MANUAL, blazeInfo.getSetting());
		
		caveSpiderInfo = playerInfo.getMobInfo(MobType.CAVE_SPIDER);
		assertEquals(130, caveSpiderInfo.getManualPerformanceLevel(), .001);

		creeperInfo = playerInfo.getMobInfo(MobType.CREEPER);
		assertEquals(70, creeperInfo.getAutoPerformanceLevel(), .001);
	}*/

	/**
	 * Tests the loadPlayerConfig() method.
	 */
	@Test
	public void testLoadPlayerConfig()
	{
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		
		FileConfiguration playerYmlFileConfig = fileHandler.getPlayerConfig(PLAYER_1_ID);

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
	
	/**
	 * Tests the onPlayerLogin() method.
	 */
	@Test
	public void testOnPlayerLogin()
	{
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.replay(mockPlayer);
        
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
        
		PlayerFileHandler fileHandler = new PlayerFileHandler(null);
		
		PlayerJoinEvent event = new PlayerJoinEvent(mockPlayer, "");
		fileHandler.onPlayerJoin(event);
		
		assertEquals(PLAYER_1_ID, playerDataManager.getPlayerInfo(PLAYER_1_ID).getPlayerID());
		EasyMock.verify(mockPlayer);
	}
}
