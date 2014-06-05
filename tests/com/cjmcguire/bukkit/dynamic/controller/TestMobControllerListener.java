package com.cjmcguire.bukkit.dynamic.controller;

import static org.junit.Assert.*;

import java.util.UUID;

import net.minecraft.server.v1_7_R3.AttributeInstance;
import net.minecraft.server.v1_7_R3.EntityInsentient;
import net.minecraft.server.v1_7_R3.GenericAttributes;

import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the MobControllerListener class.
 * @author CJ McGuire
 */
public class TestMobControllerListener 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");

	/**
	 * Tests the manipulateDamagePlayerReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on AUTO.
	 *   2. The attack should be scaled.
	 */
	@Test
	public void testManipulateDamageAuto() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamagePlayerReceived(PLAYER_1_ID, MobType.BLAZE, 2);
        assertEquals(4, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamagePlayerReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on AUTO.
	 *   2. The attack should not be scaled.
	 */
	@Test
	public void testManipulateDamageAutoNotScaled() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		blazeInfo.setScaleAttack(false);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamagePlayerReceived(PLAYER_1_ID, MobType.BLAZE, 2);
        assertEquals(2, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamagePlayerReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on MANUAL.
	 *   2. The attack should be scaled.
	 */
	@Test
	public void testManipulateDamageManual() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setManualPerformanceLevel(200);
		blazeInfo.setSetting(Setting.MANUAL);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamagePlayerReceived(PLAYER_1_ID, MobType.BLAZE, 2);
        assertEquals(4, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamagePlayerReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on DISABLED.
	 *   2. The attack should be scaled.
	 */
	@Test
	public void testManipulateDamageDisabled() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(2);
		blazeInfo.setManualPerformanceLevel(2);
		blazeInfo.setSetting(Setting.DISABLED);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
		double damage = controller.manipulateDamagePlayerReceived(PLAYER_1_ID, MobType.BLAZE, 2);
        assertEquals(2, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamageMobReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on AUTO.
	 *   2. The defense should be scaled.
	 */
	@Test
	public void testManipulateMobDamageAuto() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamageMobReceived(PLAYER_1_ID, MobType.BLAZE, 4);
        assertEquals(2, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamageMobReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on AUTO.
	 *   2. The defense should not be scaled.
	 */
	@Test
	public void testManipulateMobDamageAutoNotScaled() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		blazeInfo.setScaleDefense(false);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamageMobReceived(PLAYER_1_ID, MobType.BLAZE, 4);
        assertEquals(4, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamageMobReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on MANUAL.
	 *   2. The defense should be scaled.
	 */
	@Test
	public void testManipulateMobDamageManual() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setManualPerformanceLevel(200);
		blazeInfo.setSetting(Setting.MANUAL);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
        double damage = controller.manipulateDamageMobReceived(PLAYER_1_ID, MobType.BLAZE, 4);
        assertEquals(2, damage, .0001);
	}
	
	/**
	 * Tests the manipulateDamageMobReceived() method under the 
	 * following conditions:
	 *   1. The player's setting is on DISABLED.
	 *   2. The attack should be scaled.
	 */
	@Test
	public void testManipulateMobDamageDisabled() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		blazeInfo.setManualPerformanceLevel(200);
		blazeInfo.setSetting(Setting.DISABLED);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		playerDataManager.addPlayerInfo(playerInfo);
		
		MobControllerListener controller = new MobControllerListener();
		
		double damage = controller.manipulateDamagePlayerReceived(PLAYER_1_ID, MobType.BLAZE, 4);
        assertEquals(4, damage, .0001);
	}
	
	/**
	 * Tests the makeSpeedDynamic() method.
	 */
	@Test
	public void testMakeSpeedDynamic()
	{
		AttributeInstance mockAttributes = EasyMock.createNiceMock(AttributeInstance.class);
		EasyMock.expect(mockAttributes.getValue()).andReturn(.32+.32/2);
		EasyMock.replay(mockAttributes);
		
		EntityInsentient mockIns = EasyMock.createNiceMock(EntityInsentient.class);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.expect(mockIns.getAttributeInstance(GenericAttributes.d)).andReturn(mockAttributes);
		EasyMock.replay(mockIns);

		// Run the actual test.
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		controller.makeSpeedDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.d);
		
		assertEquals(.32+.32/2, attributes.getValue(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetSpeed() method.
	 */
	@Test
	public void testResetSpeed()
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(.4, mockAttributes.getValue(),.0001);
				
		controller.resetSpeed(mockIns);
		
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(200);
		
		controller.makeKnockbackDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(60);

		controller.makeKnockbackDynamic(mockIns, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.c);
		
		assertEquals(0.0, attributes.getValue(), .001);
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetKnockback() method.
	 */
	@Test
	public void testResetKnockback()
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(1, mockAttributes.getValue(),.0001);
				
		controller.resetKnockback(mockIns);
		
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(200);
		
		controller.makeFollowDistanceDynamic(mockIns, mobInfo, mobInfo.getAutoPerformanceLevel());
		
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(.75);

		controller.makeFollowDistanceDynamic(mockIns, mobInfo, mobInfo.getAutoPerformanceLevel());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.b);
		
		assertEquals(16.0, attributes.b(), .0001);
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
	
	/**
	 * Tests the resetFollwDistance() method.
	 */
	@Test
	public void testResetFollowDistance()
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
		MobControllerListener controller = new MobControllerListener();

		MobInfo mobInfo = new MobInfo(MobType.ZOMBIE);
		mobInfo.setAutoPerformanceLevel(2.0);
		
		assertEquals(32, mockAttributes.b(),.0001);
				
		controller.resetFollowDistance(mockIns, mobInfo.getMobType());
		
		AttributeInstance attributes = mockIns.getAttributeInstance(GenericAttributes.b);
		
		assertEquals(16, attributes.b(), .0001);
		
		
		EasyMock.verify(mockIns);
		EasyMock.verify(mockAttributes);
	}
}
