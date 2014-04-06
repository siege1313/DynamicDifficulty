package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the AbstractDDCommand class.
 */
public class TestAbstractDDCommand 
{	
	/**
	 * Tests the safeSendMessage() method.
	 */
	@Test
	public void testSafeSendMessage() 
	{
		AbstractDDCommand command = new DynamicCommand(null);
		
		assertFalse(command.safeSendMessage(null, "test send message"));
	}

}
