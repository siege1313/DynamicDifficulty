package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;

/**
 * Tests the InfoCommand class.
 * @author CJ McGuire
 */
public class TestInfoCommand 
{
	
	/**
	 * Tests the commandAction() method.
	 */
	@Test
	public void testCommandAction() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.info")).andReturn(true);
		EasyMock.replay(mockSender);
		
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		playerInfo.getMobInfo(MobType.BLAZE).setManualPerformanceLevel(.5);
		playerInfo.getMobInfo(MobType.BLAZE).setAutoPerformanceLevel(.5);
		
		plugin.addPlayerInfo(playerInfo);
		
		InfoCommand infoCommand = new InfoCommand(plugin);
		
		boolean valid = infoCommand.commandAction(mockSender, "testPlayer", null);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the executeCommand() method when the sender is null.
	 */
	@Test
	public void testExecuteCommandNull() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		InfoCommand infoCommand = new InfoCommand(plugin);
		
		boolean valid = infoCommand.executeCommand(null, null);
		
		assertFalse(valid);
	}

	/**
	 * Tests the executeCommand() method when the sender does not have permission
	 * to use the command.
	 */
	@Test
	public void testExecuteCommandWithOutPermission() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.info")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		InfoCommand infoCommand = new InfoCommand(plugin);
		
		boolean hasPermission = infoCommand.commandAction(mockSender, null, null);
		
		assertFalse(hasPermission);
		
		EasyMock.verify(mockSender);	
	}
}
