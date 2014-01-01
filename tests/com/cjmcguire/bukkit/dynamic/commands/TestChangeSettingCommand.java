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
 * Tests the ChangeSettingCommand class.
 * @author CJ McGuire
 */
public class TestChangeSettingCommand 
{
	/**
	 * Tests the commandAction() method when the mob name and setting are valid.
	 */
	@Test
	public void testCommandActionValid() 
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
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);

		String [] args = {"changesetting", "zombie", "manual"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(settingChanged);
		
		assertEquals(Setting.MANUAL, mobInfo.getSetting());
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all" and the setting is valid.
	 */
	@Test
	public void testCommandActionAll() 
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
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);

		String [] args = {"changesetting", "all", "manual"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(settingChanged);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertEquals(Setting.MANUAL, mobInfo.getSetting());
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
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);

		String [] args = {"changesetting", "zoie", "manual"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when the setting is invalid.
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
		
		plugin.addPlayerInfo(playerInfo);
		
		// perform the command
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);

		String [] args = {"changesetting", "zombie", "disled"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the commandAction() method when both the setting and the mob are invalid.
	 */
	@Test
	public void testCommandActionBothInvalid() 
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
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);

		String [] args = {"changesetting", "zoie", "disled"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
	}
}
