package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * Tests the DynamicCommand class.
 * @author CJ McGuire
 */
public class TestDynamicCommand 
{
	
	/**
	 * Tests the executeCommand() method when the sender has permission to use
	 * the command.
	 */
	@Test
	public void testExecuteCommandWithPermission() 
	{
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		DynamicCommand dynamicCommand = new DynamicCommand(plugin);
		
		boolean hasPermission = dynamicCommand.executeCommand(mockSender, null);
		
		assertTrue(hasPermission);
		
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
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		DynamicCommand dynamicCommand = new DynamicCommand(plugin);
		
		boolean valid = dynamicCommand.executeCommand(mockSender, null);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}

	/**
	 * Tests the executeCommand() method when the sender is the Console.
	 */
	@Test
	public void testExecuteCommandConsole() 
	{
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		DynamicCommand dynamicCommand = new DynamicCommand(plugin);
		
		boolean hasPermission = dynamicCommand.executeCommand(mockSender, null);
		
		assertTrue(hasPermission);
		
		EasyMock.verify(mockSender);	
	}
}
