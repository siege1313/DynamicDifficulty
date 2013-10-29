package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * Tests the AbstractDDCommand class.
 */
public class TestAbstractDDCommand 
{

	/**
	 * Tests the getPlugin() method.
	 */
	@Test
	public void testGetPlugin() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		AbstractDDCommand command = new DynamicCommand(plugin);
		
		assertSame(plugin, command.getPlugin());
	}
	
	/**
	 * Tests the safeSendMessage() method.
	 */
	@Test
	public void testSafeSendMessage() 
	{
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);
		
		AbstractDDCommand command = new DynamicCommand(plugin);
		
		assertFalse(command.safeSendMessage(null, "test send message"));
	}

}
