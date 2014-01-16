package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;

/**
 * Tests the PlayerTargetableCommand class.
 * @author CJ McGuire
 */
public class TestPlayerTargetableCommand 
{

	/**
	 * Tests the executeCommand() method when the sender is not a player
	 * and the sender is asking for another player.
	 */
	@Test
	public void testWhenConsoleAsksForOther()
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", playerName};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is not a player
	 * and the sender is asking for itself.
	 */
	@Test
	public void testWhenConsoleAsksForSelf()
	{
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player
	 * and the sender is asking for another player and the sender has
	 * permission.
	 */
	@Test
	public void testWhenPlayerAsksForOtherAndHasPermission()
	{
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.other")).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn("testPlayerSender");
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", playerName};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player
	 * and the sender is asking for another player and the sender does
	 * not have permission.
	 */
	@Test
	public void testWhenPlayerAsksForOtherAndHasNoPermission()
	{
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.other")).andReturn(false);
		EasyMock.expect(mockSender.getName()).andReturn("testPlayerSender");
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", playerName};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player
	 * and the sender is asking for itself and the sender has permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfAndHasPermission()
	{
		String playerName = "testPlayer";
		
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.self")).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn(playerName);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player and 
	 * the sender is asking for itself and the sender does not have permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfAndHasNoPermission()
	{
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.self")).andReturn(false);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);
	}
	

	/**
	 * Tests the executeCommand() method when the sender is a player
	 * and the sender is asking for itself using the /dynamic info "player"
	 * command. Even if the player does not have the "dynmaic.info.other"
	 * permission, the player should be able to use the command.
	 */
	@Test
	public void testWhenPlayerAsksForSelfUsingOtherCommand()
	{
		String playerName = "testPlayer";
		
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.self")).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn(playerName);
		EasyMock.expect(mockSender.getName()).andReturn(playerName);
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", playerName};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player and 
	 * the sender is asking for itself and the sender does have permission
	 * but entered the wrong number of arguments.
	 */
	@Test
	public void testWhenThereAreTooManyArgs()
	{
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", "asdf", "aa"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method when the sender is a player
	 * and the sender is asking for another player and the sender has permission
	 * but the other player does not exist.
	 */
	@Test
	public void testWhenOtherDoesNotExist()
	{
		//create the mock sender
		Player mockSender = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockSender.hasPermission("dynamic.info.other")).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn("testPlayer");
		EasyMock.replay(mockSender);

		// set up the plugin
		DynamicDifficulty plugin = new DynamicDifficulty();
		plugin.setRunningWithHead(false);

		// set up the PlayerInfo
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		plugin.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand(plugin);
		String [] args = {"info", "otherplayer"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		EasyMock.verify(mockSender);
	}
}