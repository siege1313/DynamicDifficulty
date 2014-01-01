package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

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
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		playerInfo.getMobInfo(MobType.BLAZE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.BLAZE).setAutoPerformanceLevel(50);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info"};
		boolean valid = infoCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}
}
