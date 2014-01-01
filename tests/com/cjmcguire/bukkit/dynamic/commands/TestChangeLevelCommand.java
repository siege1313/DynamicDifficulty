package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.Setting;

/**
 * Tests the ChangeLevelCommand class.
 * @author CJ McGuire
 */
public class TestChangeLevelCommand 
{
	/**
	 * Tests the commandAction() method when the mob name is valid and the setting is not manual.
	 */
	@Test
	public void testCommandActionValidAndSettingNotManual() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(levelChanged);
		
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is valid and the setting is manual.
	 */
	@Test
	public void testCommandActionValidAndSettingManual() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(levelChanged);
		
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all" and no setting is manual.
	 */
	@Test
	public void testCommandActionAllAndNoSettingManual() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(levelChanged);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		}
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all" and at least one setting
	 * is manual.
	 */
	@Test
	public void testCommandActionAllAndSettingManual() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		zombieInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(levelChanged);
		

		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		}
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is invalid.
	 */
	@Test
	public void testCommandActionInvalidMobName() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "zoie", "150"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(levelChanged);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the performance level arg is not a number.
	 */
	@Test
	public void testCommandActionNotANumber() 
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"changelevel", "zombie", "not number"};
		boolean levelChanged = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(levelChanged);
		
		assertEquals(100, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
}
