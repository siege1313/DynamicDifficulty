package com.cjmcguire.bukkit.dynamic.commands.core;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * 
 * @author CJ McGuire
 */
public abstract class TestAbstractChangeCommand 
{
	protected static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	protected static final String PLAYER_1_NAME = "Player1";
	
	protected Player mockPlayer;
	protected Server mockServer;
	protected ConsoleCommandSender mockSender;
	protected PlayerInfo playerInfo;
	
	protected void createMocksWhenSenderIsConsole()
	{
		// Create the mock player
		mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// Create the mock sender.
		mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
	}
	
	protected void createMocksWhenSenderIsPlayer()
	{
		// Create the mock player
		mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		// Create the mock server.
		mockServer = EasyMock.createMock(Server.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
	}
	
	protected void verifyMocksWhenSenderIsConsole()
	{
		// Verify mocks.
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	protected void verifyMocksWhenSenderIsPlayer()
	{
		// Verify mocks.
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	protected void createPlayerInfoNoneManaul()
	{
		playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
	}
	
	protected void createPlayerInfoZombieManual()
	{
		playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
	}
	
	protected boolean performCommand(CommandSender sender, String [] args)
	{
		AbstractChangeCommand abstractChangeCommand = this.getCommand();
		abstractChangeCommand.setServer(mockServer);
		
		return abstractChangeCommand.commandAction(sender, PLAYER_1_ID, args);
	}
	
	protected abstract AbstractChangeCommand getCommand();
	
	protected void assertAllMobInfos()
	{
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			this.assertTest(mobType);
		}
	}
	
	protected abstract void assertTest(MobType mobType);
}