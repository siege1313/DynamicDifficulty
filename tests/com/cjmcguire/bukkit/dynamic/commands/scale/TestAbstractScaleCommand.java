package com.cjmcguire.bukkit.dynamic.commands.scale;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.MockServer;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * An abstract class for testing the scale commands.
 * @author CJ McGuire
 */
public abstract class TestAbstractScaleCommand 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	private static final String PLAYER_1_NAME = "Player1";
	
	/**
	 * Tests the commandAction() method when the mob name and setting 
	 * are valid.
	 */
	@Test
	public void testCommandActionValid() 
	{
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
//		EasyMock.expect(mockServer.getPlayer(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);

		String [] args = {this.getCommandName(), "zombie", "false"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertTrue(settingChanged);
		
		assertFalse(this.shouldScaleAttribute(mobInfo));

		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when the mob name and setting 
	 * are valid.
	 */
	@Test
	public void testCommandActionValidPlayerSender() 
	{
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
//		EasyMock.expect(mockServer.getPlayer(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);

		String [] args = {this.getCommandName(), "zombie", "false"};
		boolean settingChanged = changeSettingCommand.commandAction(mockPlayer, PLAYER_1_ID, args);
		
		assertTrue(settingChanged);
		
		assertFalse(this.shouldScaleAttribute(mobInfo));

		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all" and 
	 * the setting is valid.
	 */
	@Test
	public void testCommandActionAll() 
	{
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getName()).andReturn(PLAYER_1_NAME);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
//		EasyMock.expect(mockServer.getPlayer(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);

		String [] args = {this.getCommandName(), "all", "false"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertTrue(settingChanged);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertFalse(this.shouldScaleAttribute(mobInfo));
		}
		
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all" and 
	 * the setting is valid.
	 */
	@Test
	public void testCommandActionAllPlayerSender() 
	{
		Player mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.expect(mockServer.getPlayer(PLAYER_1_ID)).andReturn(mockPlayer);
	//	EasyMock.expect(mockServer.getPlayer(PLAYER_1_NAME)).andReturn(mockPlayer);
		EasyMock.replay(mockServer);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);
	
		String [] args = {this.getCommandName(), "all", "false"};
		boolean settingChanged = changeSettingCommand.commandAction(mockPlayer, PLAYER_1_ID, args);
		
		assertTrue(settingChanged);
		
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			assertFalse(this.shouldScaleAttribute(mobInfo));
		}
		
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	
	/**
	 * Tests the commandAction() method when the mob name is invalid.
	 */
	@Test
	public void testCommandActionInvalidMobName() 
	{
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);

		String [] args = {this.getCommandName(), "zole", "false"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when the setting is invalid.
	 */
	@Test
	public void testCommandActionNotANumber() 
	{
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		AbstractScaleCommand changeSettingCommand = this.getScaleCommand(mockServer);

		String [] args = {this.getCommandName(), "zombie", "asdfasdf"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the commandAction() method when both the setting and the 
	 * mob are invalid.
	 */
	@Test
	public void testCommandActionBothInvalid() 
	{
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);
		
		MockServer mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);
		
		//create the mock sender
		ConsoleCommandSender mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);
		
		// set up the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
	
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		// perform the command
		ScaleAttackCommand changeSettingCommand = new ScaleAttackCommand(mockServer);

		String [] args = {this.getCommandName(), "zole", "asdfasdf"};
		boolean settingChanged = changeSettingCommand.commandAction(mockSender, PLAYER_1_ID, args);
		
		assertFalse(settingChanged);
		
		EasyMock.verify(mockSender);
		EasyMock.verify(mockServer);
		EasyMock.verify(mockPlayer);
	}
	
	protected abstract AbstractScaleCommand getScaleCommand(Server server);
	
	protected abstract String getCommandName();
	
	protected abstract boolean shouldScaleAttribute(MobInfo mobInfo);
}
