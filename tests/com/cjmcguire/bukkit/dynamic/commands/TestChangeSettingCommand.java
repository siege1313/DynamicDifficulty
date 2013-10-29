package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
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
	 * Tests the executeCommand() method when the sender is null.
	 */
	@Test
	public void testOnCommandNull() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		ChangeSettingCommand changeSettingsCommand = new ChangeSettingCommand(plugin);
		
		boolean validCommand = changeSettingsCommand.executeCommand(null, null);
		
		assertFalse(validCommand);
	}

	/**
	 * Tests the changeSetting() method when the args are valid.
	 */
	@Test
	public void testChangeSettingValid() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		plugin.addPlayerInfo(playerInfo);
		
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		String [] args = {"change", "setting", "zombie", "manual"};
		boolean valid = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.MANUAL, mobInfo.getSetting());
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeSetting() method when the setting is for "all".
	 */
	@Test
	public void testChangeSettingAll() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		plugin.addPlayerInfo(playerInfo);
		
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		String [] args = {"change", "setting", "all", "manual"};
		boolean valid = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertEquals(Setting.MANUAL, mobInfo.getSetting());
		}
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the changeSetting() method when the mob is invalid.
	 */
	@Test
	public void testChangeSettingMobInvalid() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		plugin.addPlayerInfo(playerInfo);
		
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		String [] args = {"change", "setting", "zoie", "manual"};
		boolean valid = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(valid);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.AUTO, mobInfo.getSetting());
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeSetting() method when the setting is invalid.
	 */
	@Test
	public void testChangeSettingSettingInvalid() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		plugin.addPlayerInfo(playerInfo);
		
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		String [] args = {"change", "setting", "zombie", "disled"};
		boolean valid = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(valid);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.AUTO, mobInfo.getSetting());
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeSetting() method when both the setting and
	 * the mob are invalid.
	 */
	@Test
	public void testChangeSettingBothInvalid() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		plugin.addPlayerInfo(playerInfo);
		
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		String [] args = {"change", "setting", "zome", "disled"};
		boolean valid = changeSettingCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(valid);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.AUTO, mobInfo.getSetting());
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender does not have permission
	 * to use the command.
	 */
	@Test
	public void testExecuteCommandWithOutPermission() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changesetting")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		ChangeSettingCommand changeSettingCommand = new ChangeSettingCommand(plugin);
		
		boolean hasPermission = changeSettingCommand.commandAction(mockSender, null, null);
		
		assertFalse(hasPermission);
		
		EasyMock.verify(mockSender);	
	}
}
