package com.cjmcguire.bukkit.dynamic.commands;

import static org.junit.Assert.*;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.commands.core.HelpCommand;

/**
 * Tests the AbstractDDCommand class.
 */
public class TestAbstractDDCommand 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final String PLAYER_1_NAME = "Player1";
	
	/**
	 * Tests the getUUIDFromPlayerName() method.
	 */
	@Test
	public void testGetUUIDFromPlayerName()
	{		
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
		EasyMock.replay(mockPlayer);
		
		Server mockServer = EasyMock.createNiceMock(Server.class);
		EasyMock.expect(mockServer.getPlayerExact(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		AbstractDDCommand command = new HelpCommand();
		command.setServer(mockServer);
		
		UUID uuid = command.getUUIDFromPlayerName(PLAYER_1_NAME);
		
		assertEquals(PLAYER_1_ID, uuid);
		
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
}