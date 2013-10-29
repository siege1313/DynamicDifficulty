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
 * Tests the ChangeLevelCommand class.
 * @author CJ McGuire
 */
public class TestChangeLevelCommand 
{
	
	/**
	 * Tests the executeCommand() method when the sender is null.
	 */
	@Test
	public void testOnCommandNull() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		boolean validCommand = changeLevelCommand.executeCommand(null, null);
		
		assertFalse(validCommand);
	}

	/**
	 * Tests the changeLevel() method when the mob name is valid and the performance
	 * level is in the valid range.
	 */
	@Test
	public void testChangeLevelValid() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zombie", "150"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the mob name is "all" and the performance
	 * level is in the valid range.
	 */
	@Test
	public void testChangeLevelAll() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "all", "150"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfoTest = playerInfo.getMobInfo(mobType);
			assertEquals(150, mobInfoTest.getManualPerformanceLevel(), .0001);
		}
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the mob name is valid and the performance
	 * level above 200.
	 */
	@Test
	public void testChangeLevelPerformanceLevelHigh() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zombie", "300"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		assertEquals(200, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the mob name is valid and the performance
	 * level is below 50.
	 */
	@Test
	public void testChangeLevelPerformanceLevelLow() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zombie", "10"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		assertEquals(50, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the mob name is invalid.
	 */
	@Test
	public void testChangeLevelInvalidMobName() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zoie", "150"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(valid);
		
		assertEquals(100, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the performance level arg is not
	 * a number.
	 */
	@Test
	public void testChangeLevelNotANumber() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zombie", "not number"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertFalse(valid);
		
		assertEquals(100, mobInfo.getManualPerformanceLevel(), .0001);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the changeLevel() method when the setting is not manual.
	 */
	@Test
	public void testChangeLevelNotManual() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		
		plugin.addPlayerInfo(playerInfo);
		
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		String [] args = {"change", "level", "zombie", "150"};
		boolean valid = changeLevelCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		
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
		EasyMock.expect(mockSender.hasPermission("dynamic.changelevel")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		ChangeLevelCommand changeLevelCommand = new ChangeLevelCommand(plugin);
		
		boolean hasPermission = changeLevelCommand.commandAction(mockSender, null, null);
		
		assertFalse(hasPermission);
		
		EasyMock.verify(mockSender);	
	}
}
