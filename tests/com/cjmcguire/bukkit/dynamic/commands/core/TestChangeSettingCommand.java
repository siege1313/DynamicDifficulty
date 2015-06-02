package com.cjmcguire.bukkit.dynamic.commands.core;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.MockServer;
import com.cjmcguire.bukkit.dynamic.commands.core.ChangeSettingCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the ChangeSettingCommand class.
 * @author CJ McGuire
 */
public class TestChangeSettingCommand extends TestAbstractChangeCommand 
{

	@Override
	protected AbstractChangeCommand getCommand()
	{
		return new ChangeSettingCommand();
	}
	
	@Override
	protected void assertTest(MobType mobType)
	{
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		assertEquals(Setting.MANUAL, mobInfo.getSetting());
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is valid.
	 */
	@Test
	public void testCommandActionValid() 
	{
		this.createMocksWhenSenderIsConsole();
		
		// set up the PlayerInfo
		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "zombie", "manual"};
		boolean settingChanged = this.performCommand(mockSender, args);
		
		assertTrue(settingChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.MANUAL, mobInfo.getSetting());
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is valid.
	 */
	@Test
	public void testCommandActionValidSenderPlayer() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		// set up the PlayerInfo
		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "zombie", "manual"};
		boolean settingChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(settingChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(Setting.MANUAL, mobInfo.getSetting());
		
		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. The setting is valid.
	 */
	@Test
	public void testCommandActionAll() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "all", "manual"};
		boolean settingChanged = this.performCommand(mockSender, args);
		
		assertTrue(settingChanged);
		
		this.assertAllMobInfos();
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. The setting is valid.
	 */
	@Test
	public void testCommandActionAllSenderPlayer() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "all", "manual"};
		boolean settingChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(settingChanged);
		
		this.assertAllMobInfos();
		
		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is invalid.
	 */
	@Test
	public void testCommandActionInvalidMobName() 
	{
		// Create the mock player
		mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);

		// Create the mock server.
		mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);

		// Create the mock sender.
		mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "zoie", "manual"};
		boolean settingChanged = this.performCommand(mockSender, args);
		
		assertFalse(settingChanged);
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method when the setting is invalid.
	 */
	@Test
	public void testCommandActionSettingInvalid() 
	{
		// Create the mock player
		mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);

		// Create the mock server.
		mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);

		// Create the mock sender.
		mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		this.createPlayerInfoNoneManaul();
		
		// perform the command

		String [] args = {"changesetting", "zombie", "disled"};
		boolean settingChanged = this.performCommand(mockSender, args);
		
		assertFalse(settingChanged);
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method when both the setting and the 
	 * mob are invalid.
	 */
	@Test
	public void testCommandActionBothInvalid() 
	{
		// Create the mock player
		mockPlayer = EasyMock.createNiceMock(MockPlayer.class);
		EasyMock.replay(mockPlayer);

		// Create the mock server.
		mockServer = EasyMock.createMock(MockServer.class);
		EasyMock.replay(mockServer);

		// Create the mock sender.
		mockSender = EasyMock.createNiceMock(ConsoleCommandSender.class);
		EasyMock.replay(mockSender);

		this.createPlayerInfoNoneManaul();
		
		// perform the command
		String [] args = {"changesetting", "zoie", "disled"};
		boolean settingChanged = this.performCommand(mockSender, args);
		
		assertFalse(settingChanged);
		
		this.verifyMocksWhenSenderIsConsole();
	}
}
