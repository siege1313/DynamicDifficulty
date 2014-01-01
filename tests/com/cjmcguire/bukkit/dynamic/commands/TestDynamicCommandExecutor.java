package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.CommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

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

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", new String[0]);
		
		assertTrue(validCommand);
		
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

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"info"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);
		
		assertTrue(validCommand);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the onCommand() method when the command is /dynamic changesetting.
	 */
	@Test
	public void testOnCommandChangeSetting() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"changesetting", "zombie", "disabled"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);

		assertTrue(validCommand);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the onCommand() method when the command is /dynamic changelevel.
	 */
	@Test
	public void testOnCommandChangeLevel() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"changelevel", "zombie", "150"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);

		assertTrue(validCommand);
		
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the onCommand() method when the command has an invalid command.
	 */
	@Test
	public void testOnCommandInvalidCommand() 
	{
		//create the mock sender
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// perform the command
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"lvls"};
		boolean validCommand = executor.onCommand(mockSender, null, "dynamic", args);
		
		assertFalse(validCommand);
		
		EasyMock.verify(mockSender);
	}
}
