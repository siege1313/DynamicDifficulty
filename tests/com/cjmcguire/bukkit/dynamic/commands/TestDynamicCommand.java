package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;

/**
 * Tests the DynamicCommand class.
 * @author CJ McGuire
 */
public class TestDynamicCommand 
{
	
	/**
	 * Tests the executeCommand() method when the sender has permission 
	 * to use the command.
	 */
	@Test
	public void testExecuteCommandWithPermission() 
	{
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicCommand dynamicCommand = new DynamicCommand(null);
		
		boolean valid = dynamicCommand.executeCommand(mockSender, null);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}

	/**
	 * Tests the executeCommand() method when the sender does not have 
	 * permission to use the command.
	 */
	@Test
	public void testExecuteCommandWithOutPermission() 
	{
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(false);
		EasyMock.replay(mockSender);

		DynamicCommand dynamicCommand = new DynamicCommand(null);
		
		boolean valid = dynamicCommand.executeCommand(mockSender, null);
		
		assertFalse(valid);
		
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

		DynamicCommand dynamicCommand = new DynamicCommand(null);
		
		boolean valid = dynamicCommand.executeCommand(mockSender, null);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}
}
