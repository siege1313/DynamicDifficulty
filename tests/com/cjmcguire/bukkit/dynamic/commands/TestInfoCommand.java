package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

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

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo("testPlayer");
		playerInfo.getMobInfo(MobType.BLAZE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.BLAZE).setAutoPerformanceLevel(50);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(null);
		String [] args = {"info"};
		boolean valid = infoCommand.commandAction(mockSender, "testPlayer", args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}
}
