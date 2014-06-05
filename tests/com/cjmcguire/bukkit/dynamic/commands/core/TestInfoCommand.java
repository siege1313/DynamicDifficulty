package com.cjmcguire.bukkit.dynamic.commands.core;

import static org.junit.Assert.*;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.commands.core.InfoCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * Tests the InfoCommand class.
 * @author CJ McGuire
 */
public class TestInfoCommand 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final String PLAYER_1_NAME = "Player1";
	
	/**
	 * Tests the commandAction() method when the request is for 
	 * "general".
	 */
	@Test
	public void testCommandActionGeneral() 
	{
		// Create the mock player
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		Server mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerInfo.getMobInfo(MobType.BLAZE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.BLAZE).setAutoPerformanceLevel(50);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		
		String [] args = {"info", "general"};
		boolean valid = infoCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}

	/**
	 * Tests the commandAction() method when the request is for 
	 * a single, valid mob.
	 */
	@Test
	public void testCommandActionValidMob() 
	{
		// Create the mock player
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		Server mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerInfo.getMobInfo(MobType.ZOMBIE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.ZOMBIE).setAutoPerformanceLevel(50);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		
		String [] args = {"info", "zombie"};
		boolean valid = infoCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when the request is for 
	 * a single, valid mob.
	 */
	@Test
	public void testCommandActionValidMobSenderPlayer() 
	{
		// Create the mock player
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		Server mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerInfo.getMobInfo(MobType.ZOMBIE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.ZOMBIE).setAutoPerformanceLevel(50);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		
		String [] args = {"info", "zombie"};
		boolean valid = infoCommand.commandAction(mockPlayer, PLAYER_1_ID, args);
		
		assertTrue(valid);
		
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}

	/**
	 * Tests the commandAction() method when the request is for 
	 * an invalid mob name.
	 */
	@Test
	public void testCommandActionInvalidMob() 
	{
		// Create the mock player
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		Server mockServer = EasyMock.createMock(Server.class);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerInfo.getMobInfo(MobType.ZOMBIE).setManualPerformanceLevel(50);
		playerInfo.getMobInfo(MobType.ZOMBIE).setAutoPerformanceLevel(50);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);

		// perform the command
		InfoCommand infoCommand = new InfoCommand();
		infoCommand.setServer(mockServer);
		
		String [] args = {"info", "zoie"};
		boolean valid = infoCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertFalse(valid);

		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
}
