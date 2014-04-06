package com.cjmcguire.bukkit.dynamic.controller;

import static org.junit.Assert.*;

import net.minecraft.server.v1_7_R2.AttributeInstance;
import net.minecraft.server.v1_7_R2.EntityInsentient;
import net.minecraft.server.v1_7_R2.GenericAttributes;

import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the ControllerListener class.
 * @author CJ McGuire
 */
public class TestControllerListener 
{

	/**
	 * Tests the manipulateDamagePlayerReceived() method for when
	 * the player's setting is on AUTO.
	 */
	@Test
	public void testManipulateDamageAuto() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		ControllerListener controller = new ControllerListener();
		
        int damage = controller.manipulateDamagePlayerReceived(playerName, MobType.BLAZE, 2);
        assertEquals(4, damage);
	}
	
	/**
	 * Tests the manipulateDamagePlayerReceived() method for when
	 * the player's setting is on MANUAL.
	 */
	@Test
	public void testManipulateDamageManual() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setManualPerformanceLevel(200);
		blazeInfo.setSetting(Setting.MANUAL);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		ControllerListener controller = new ControllerListener();
		
        int damage = controller.manipulateDamagePlayerReceived(playerName, MobType.BLAZE, 2);
        assertEquals(4, damage);
	}
	
	/**
	 * Tests the manipulateDamagePlayerReceived() method for when
	 * the player's setting is to DISABLED.
	 */
	@Test
	public void testManipulateDamageDisabled() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		String playerName = "testPlayer";
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(2);
		blazeInfo.setManualPerformanceLevel(2);
		blazeInfo.setSetting(Setting.DISABLED);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		ControllerListener controller = new ControllerListener();
		
        int damage = controller.manipulateDamagePlayerReceived(playerName, MobType.BLAZE, 2);
        assertEquals(2, damage);
	}
	
	/**
	 * Tests the makeMoveSpeedDynamic() method.
	 */
	@Test
	public void testMakeMoveSpeedDynamic()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(.32+.32/2);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		controller.makeMobSpeedDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.d);
		
		assertEquals(.32+.32/2, attributes.getValue(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetMoveSpeed() method.
	 */
	@Test
	public void testResetMoveSpeed()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(.4);
		EasyMock.expect(mockAttributes.getValue()).andReturn(.32);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(.4, mockAttributes.getValue(),.0001);
				
		controller.resetMobSpeed(mockIns);
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.d);
		
		assertEquals(.32, attributes.getValue(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the makeKnockbackDynamic() method.
	 */
	@Test
	public void testMakeKnockbackDynamic()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(.5);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.c)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.c)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(200);
		
		controller.makeMobKnockbackDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.c);
		
		assertEquals(.5, attributes.getValue(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the makeKnockbackDynamic() method when the player's performance level is less than 100%.
	 */
	@Test
	public void testMakeKnockbackDynamicWhenPerformanceLow()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(0.0);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.c)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(60);

		controller.makeMobKnockbackDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.c);
		
		assertEquals(0.0, attributes.getValue(), .001);
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetMobKnockback() method.
	 */
	@Test
	public void testResetMobKnockback()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(1.0);
		EasyMock.expect(mockAttributes.getValue()).andReturn(0.0);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.c)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.c)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(1, mockAttributes.getValue(),.0001);
				
		controller.resetMobKnockback(mockIns);
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.c);
		
		assertEquals(0, attributes.getValue(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	
	/**
	 * Tests the makeFollowDistanceDynamic() method.
	 */
	@Test
	public void testMakeFollowDistanceDynamic()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.b()).andReturn(32.0);
		EasyMock.replay(mockAttributes);
		
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.b)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.b)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(200);
		
		controller.makeMobFollowDistanceDynamic(mockIns, mobInfo, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.b);
		
		assertEquals(32.0, attributes.b(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the makeFollowDistanceDynamic() method when the Player's performance level
	 * is lower than 100%.
	 */
	@Test
	public void testMakeFollowDistanceDynamicWhenPerformanceLow()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.b()).andReturn(16.0);
		EasyMock.replay(mockAttributes);

		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.b)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(.75);

		controller.makeMobFollowDistanceDynamic(mockIns, mobInfo, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.b);
		
		assertEquals(16.0, attributes.b(), .0001);
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetMobFollwDistance() method.
	 */
	@Test
	public void testResetMobFollowDistance()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.b()).andReturn(32.0);
		EasyMock.expect(mockAttributes.b()).andReturn(16.0);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.b)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.b)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		ControllerListener controller = new ControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(32, mockAttributes.b(),.0001);
				
		controller.resetMobFollowDistance(mockIns, mobInfo.getMobType());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.b);
		
		assertEquals(16, attributes.b(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
}
