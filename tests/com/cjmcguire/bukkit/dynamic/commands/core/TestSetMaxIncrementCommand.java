package com.cjmcguire.bukkit.dynamic.commands.core;

import static org.junit.Assert.*;

import org.bukkit.command.ConsoleCommandSender;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.MockServer;
import com.cjmcguire.bukkit.dynamic.commands.core.SetMaxIncrementCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;

/**
 * Tests the SetMaxIncrementCommand class.
 * @author CJ McGuire
 */
public class TestSetMaxIncrementCommand  extends TestAbstractChangeCommand
{
	
	@Override
	protected AbstractChangeCommand getCommand() 
	{
		return new SetMaxIncrementCommand();
	}
	
	@Override
	protected void assertTest(MobType mobType)
	{
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		assertEquals(5, mobInfo.getMaxIncrement());
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Console.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 */
	@Test
	public void testCommandActionMobNameValid() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoNoneManaul();
		
		String [] args = {"setmaxincrement", "zombie", "5"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		this.assertTest(MobType.ZOMBIE);
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method under the following 
	 * conditions:
	 *   1. The sender is the Player.
	 *   2. The sender is asking for a player.
	 *   3. The mob name is valid.
	 */
	@Test
	public void testCommandActionMobNameValidSenderPlayer() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoNoneManaul();
		
		String [] args = {"setmaxincrement", "zombie", "5"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);
		
		this.assertTest(MobType.ZOMBIE);
		
		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all".
	 */
	@Test
	public void testCommandActionAll() 
	{
		this.createMocksWhenSenderIsConsole();
		
		this.createPlayerInfoNoneManaul();
		
		String [] args = {"setmaxincrement", "all", "5"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertTrue(levelChanged);
		
		this.assertAllMobInfos();
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method when the mob name is "all".
	 */
	@Test
	public void testCommandActionAllSenderPlayer() 
	{
		this.createMocksWhenSenderIsPlayer();
		
		this.createPlayerInfoNoneManaul();
		
		String [] args = {"setmaxincrement", "all", "5"};
		boolean levelChanged = this.performCommand(mockPlayer, args);
		
		assertTrue(levelChanged);
		
		this.assertAllMobInfos();
		
		this.verifyMocksWhenSenderIsPlayer();
	}
	
	/**
	 * Tests the commandAction() method when the mob name is invalid.
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
		
		String [] args = {"setmaxincrement", "zoie", "5"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertFalse(levelChanged);
		
		this.verifyMocksWhenSenderIsConsole();
	}
	
	/**
	 * Tests the commandAction() method when the performance level arg 
	 * is not a number.
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
		
		// perform the command
		String [] args = {"setmaxincrement", "zombie", "not number"};
		boolean levelChanged = this.performCommand(mockSender, args);
		
		assertFalse(levelChanged);
		
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		assertEquals(10, mobInfo.getMaxIncrement());
		
		this.verifyMocksWhenSenderIsConsole();
	}
}
