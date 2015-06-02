package com.cjmcguire.bukkit.dynamic.commands.core;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.MockServer;
import com.cjmcguire.bukkit.dynamic.commands.core.ChangeLevelCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;

/**
 * Tests the ChangeLevelCommand class.
 * @author CJ McGuire
 */
public class TestChangeLevelCommand extends TestAbstractChangeCommand
{
	
	@Override
	protected AbstractChangeCommand getCommand() 
	{
		return new ChangeLevelCommand();
	}
	
	@Override
	protected void assertTest(MobType mobType)
	{
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is not manual.
	 */
	@Test
	public void testCommandActionValidAndSettingNotManual() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoNoneManaul();
		
		// Perform the command.
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is not manual.
	 */
	@Test
	public void testCommandActionValidAndSettingNotManualPlayerSender() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoNoneManaul();
		
		// Perform the command.
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);

		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is manual.
	 */
	@Test
	public void testCommandActionValidAndSettingManual() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoZombieManual();
		
		// Perform the command.
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);

		this.verifyMocksWhenSenderIsConsole();
	}

	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 *   4. The setting is manual.
	 */
	@Test
	public void testCommandActionValidAndSettingManualPlayerSender() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoZombieManual();
		
		// Perform the command.
		String [] args = {"changelevel", "zombie", "150"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);

		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(150, mobInfo.getManualPerformanceLevel(), .0001);

		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. No settings are manual.
	 */
	@Test
	public void testCommandActionAllAndNoSettingManual() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoNoneManaul();
		
		// Perform the command.
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		this.assertAllMobInfos();

		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. No settings are manual.
	 */
	@Test
	public void testCommandActionAllAndNoSettingManualPlayerSender() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoNoneManaul();
		
		// Perform the command.
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);
		
		this.assertAllMobInfos();

		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. At least on setting is manual.
	 */
	@Test
	public void testCommandActionAllAndSettingManual() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoZombieManual();
		
		// Perform the command.
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		this.assertAllMobInfos();

		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is "all".
	 *   4. At least on setting is manual.
	 */
	@Test
	public void testCommandActionAllAndSettingManualPlayerSender() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoZombieManual();
		
		// Perform the command.
		String [] args = {"changelevel", "all", "150"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);
		
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

		// Perform the command.
		String [] args = {"changelevel", "zoie", "150"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertFalse(levelChanged);

		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The performance level is not a number.
	 */
	@Test
	public void testCommandActionNotANumber() 
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
		
		// Perform the command.
		String [] args = {"changelevel", "zombie", "not number"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertFalse(levelChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(100, mobInfo.getManualPerformanceLevel(), .0001);
		
		this.verifyMocksWhenSenderIsConsole();
	}
}
