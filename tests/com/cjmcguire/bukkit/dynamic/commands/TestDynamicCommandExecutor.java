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
		CommandSender mockSender = EasyMock.createNiceMock(CommandSender.class);
		EasyMock.expect(mockSender.hasPermission("dynamic.dynamic")).andReturn(true);
		EasyMock.replay(mockSender);

		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		boolean hasPermission = executor.onCommand(mockSender, null, "dynamic", new String[0]);
		
		assertTrue(hasPermission);
		
		EasyMock.verify(mockSender);
		
		/*DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		boolean sentToConsole = executor.onCommand(null, null, "dynamic", new String[0]);
		
		assertFalse(sentToConsole);*/
	}
	
	/**
	 * Tests the onCommand() method when the command is /dynamic info.
	 */
	@Test
	public void testOnCommandInfo() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"info"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}

	/**
	 * Tests the onCommand() method when the command has invalid args.
	 */
	@Test
	public void testOnCommandInvalidArgs() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"lvls"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}
	
	/**
	 * Tests the onCommand() method when the command is /dynamic change setting.
	 */
	@Test
	public void testOnCommandChangeSetting() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"change", "setting", "zombie", "disabled"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}
	
	
	/**
	 * Tests the onCommand() method when the command is /dynamic change level.
	 */
	@Test
	public void testOnCommandChangeLevel() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"change", "level", "zombie", "150"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}
	
	/**
	 * Tests the onCommand() method when their are an incorrect number of parameters.
	 */
	@Test
	public void testOnCommandInvalidParameterNumber() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"change", "level", "zombie", "150", "extra"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}
	
	
	/**
	 * Tests the onCommand() method when the command is /dynamic change lvl.
	 */
	@Test
	public void testOnCommandChangeLvl() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		DynamicCommandExecutor executor = new DynamicCommandExecutor(plugin);
		
		String[] args = {"change", "lvl", "zombie", "150"};
		boolean validCommand = executor.onCommand(null, null, "dynamic", args);
		
		assertFalse(validCommand);
	}
}
