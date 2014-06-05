package com.cjmcguire.bukkit.dynamic.commands.core;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.commands.core.HelpCommand;

/**
 * Tests the HelpCommand class.
 * @author CJ McGuire
 */
public class TestHelpCommand 
{
	
	/**
	 * Tests the executeCommand() method when the sender has permission 
	 * to use the command.
	 */
	@Test
	public void testExecuteCommandWithPermission() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
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
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(false);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
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
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(false);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();

		String [] args = {"help"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}	

	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for page 1.
	 */
	@Test
	public void testExecuteCommandPage1() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "1"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}
	
	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for page 2.
	 */
	@Test
	public void testExecuteCommandPage2() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "2"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}
	
	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for page 3.
	 */
	@Test
	public void testExecuteCommandPage3() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "3"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}
	
	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for page 4.
	 */
	@Test
	public void testExecuteCommandPage4() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "4"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}
	
	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for page 5.
	 */
	@Test
	public void testExecuteCommandPage5() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "5"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);	
	}

	/**
	 * Tests the executeCommand() method when the sender is asking 
	 * for an invalid page.
	 */
	@Test
	public void testExecuteCommandInvalidPage() 
	{
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(HelpCommand.PERMISSION)).andReturn(true);
		EasyMock.replay(mockSender);

		HelpCommand helpCommand = new HelpCommand();
		
		String [] args = {"help", "asdf"};
		
		boolean valid = helpCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);	
	}
}
