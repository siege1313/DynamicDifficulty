package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockServer;

/**
 * Tests the DynamicCommandExecutor class.
 * @author CJ McGuire
 */
public class TestDynamicCommandExecutor 
{

	/**
	 * Tests the onCommand() method when the command is /dynamic.
	 */
	@Test
	public void testOnCommandDynamic() 
	{	
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);
		
		MockServer mockServer = EasyMock.createNiceMock(MockServer.class);
		EasyMock.replay(mockServer);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(mockServer);
		
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", new String[0]);
		
		assertTrue(validCommand);

		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the onCommand() method when the command is /dynamic info.
	 */
	@Test
	public void testOnCommandInfo() 
	{	
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		MockServer mockServer = EasyMock.createNiceMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(mockServer);
		
		String[] args = {"info"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);
		
		assertTrue(validCommand);

		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the onCommand() method when the command is /dynamic 
	 * changesetting.
	 */
	@Test
	public void testOnCommandChangeSetting() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		MockServer mockServer = EasyMock.createNiceMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(mockServer);
		
		String[] args = {"changesetting", "zombie", "disabled"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);

		assertTrue(validCommand);

		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the onCommand() method when the command is /dynamic 
	 * changelevel.
	 */
	@Test
	public void testOnCommandChangeLevel() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		MockServer mockServer = EasyMock.createNiceMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(mockServer);
		
		String[] args = {"changelevel", "zombie", "150"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);

		assertTrue(validCommand);

		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the onCommand() method when the command has an invalid 
	 * command.
	 */
	@Test
	public void testOnCommandInvalidCommand() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		MockServer mockServer = EasyMock.createNiceMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(mockServer);
		
		String[] args = {"lvls"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);
		
		assertFalse(validCommand);

		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
}
