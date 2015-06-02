package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import java.util.UUID;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.MockServer;
import com.cjmcguire.bukkit.dynamic.commands.core.InfoCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * Tests the PlayerTargetableCommand class.
 * @author CJ McGuire
 */
public class TestAbstractPlayerTargetableCommand 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final String PLAYER_1_NAME = "Player1";
	
	private static final UUID PLAYER_2_ID = UUID.fromString("12345678-1234-1234-1234-123456789002");
	private static final String PLAYER_2_NAME = "Player2";
	
	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 */
	@Test
	public void testWhenConsoleAsksForOther()
	{
		// Create the mock player
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayerExact(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_1_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);

		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is the Console. 
	 *   2. The sender is asking for itself.
	 */
	@Test
	public void testWhenConsoleAsksForSelf()
	{
		// Create the mock sender.
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		String [] args = {"info", "general"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for another player.
	 *   3. The sender has permission.
	 */
	@Test
	public void testWhenPlayerAsksForOtherAndHasPermission()
	{
		// Create the mock player
		Player mockPlayer2 = EasyMock.createMock(MockPlayer.class);
		EasyMock.expect(mockPlayer2.getUniqueId()).andReturn(PLAYER_2_ID);
		EasyMock.expect(mockPlayer2.getName()).andReturn(PLAYER_2_NAME);
		EasyMock.replay(mockPlayer2);
		
		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayerExact(PLAYER_2_NAME)).andReturn(mockPlayer2);
		EasyMock.expect(mockServer.getPlayer(PLAYER_2_ID)).andReturn(mockPlayer2);
		EasyMock.expect(mockServer.getPlayer(PLAYER_2_ID)).andReturn(mockPlayer2);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.OTHER_PERMISSION)).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockSender);

		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_2_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_2_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer2);
	}
	
	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender is not specifying its name.
	 *   4. The sender has permission.
	 */
/*	@Test
	public void testPlayerAsksForSelfAndHasPermissionAndSpellsNameWrong()
	{	
		// Create the mock sender.
		Player mockSender = EasyMock.createMock(MockPlayer.class);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.OTHER_PERMISSION)).andReturn(true);
//		EasyMock.expect(mockSender.getUniqueId()).andReturn(PLAYER_1_ID);
		EasyMock.replay(mockSender);

		// Create the mock server.
		Server mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer("Pl")).andReturn(mockSender);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		
		String [] args = {"info", "general", "Pl"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
*/
	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for another player
	 *   3. The sender does not have permission.
	 */
	@Test
	public void testWhenPlayerAsksForOtherAndHasNoPermission()
	{
		// Create the mock player
		Player mockPlayer2 = EasyMock.createMock(MockPlayer.class);
		EasyMock.replay(mockPlayer2);
		
		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.OTHER_PERMISSION)).andReturn(false);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockSender);

		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_2_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_2_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer2);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender is not specifying its name.
	 *   4. The sender has permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfAndHasPermission()
	{	
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.SELF_PERMISSION)).andReturn(true);
		EasyMock.expect(mockSender.getUniqueId()).andReturn(PLAYER_1_ID);
		EasyMock.replay(mockSender);

		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockSender);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender is not specifying its name.
	 *   4. The sender does not have permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfAndHasNoPermission()
	{
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.SELF_PERMISSION)).andReturn(false);
		EasyMock.replay(mockSender);

		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender is specifying its name.
	 *   4. The sender does have permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfUsingOtherCommand()
	{
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.SELF_PERMISSION)).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.expect(mockSender.getUniqueId()).andReturn(PLAYER_1_ID);
		EasyMock.replay(mockSender);

		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockSender);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_1_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}
	
	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender is specifying its name.
	 *   4. The sender does not have permission.
	 */
	@Test
	public void testWhenPlayerAsksForSelfUsingOtherCommandNoPermission()
	{
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.SELF_PERMISSION)).andReturn(false);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockSender);

		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_1_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for itself.
	 *   3. The sender does have permission.
	 *   4. The sender entered too many args
	 */
	@Test
	public void testWhenThereAreTooManyArgs()
	{
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockSender);

		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		// Set up the PlayerInfo.
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", "asdf", "as"};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockSender);
	}

	/**
	 * Tests the executeCommand() method under the following 
	 * conditions:
	 *   1. The sender is a player.
	 *   2. The sender is asking for another player.
	 *   3. The sender has permission.
	 *   4. The other player is not logged in.
	 */
	@Test
	public void testWhenOtherDoesNotExist()
	{
		// Create the mock server.
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayerExact(PLAYER_2_NAME)).andReturn(null);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		Player mockSender = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockSender.hasPermission(InfoCommand.OTHER_PERMISSION)).andReturn(true);
		EasyMock.expect(mockSender.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockSender);

		// Set up the PlayerInfo.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		// Perform the command.
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		String [] args = {"info", "general", PLAYER_2_NAME};
		boolean valid = infoCommand.executeCommand(mockSender, args);
		
		assertFalse(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
	}
}