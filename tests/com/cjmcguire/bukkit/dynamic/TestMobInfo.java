package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the MobInfo class.
 * @author CJ McGuire
 */
public class TestMobInfo
{
	
	/**
	 * Tests the getMobType() method.
	 */
	@Test
	public void testGetMobType() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(MobType.ZOMBIE, zombieInfo.getMobType());
	}
	
	/**
	 * Tests the getMobSetting() and setSetting() methods.
	 */
	@Test
	public void testSetting() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(Setting.AUTO, zombieInfo.getSetting());
		
		zombieInfo.setSetting(Setting.DISABLED);
		assertEquals(Setting.DISABLED, zombieInfo.getSetting());
	}
	
	/**
	 * Tests the methods tied to the estimatedPerformanceLevel variable.
	 */
	@Test
	public void testEstimatedPerformanceLevel() 
	{
		// default
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// setting middle
		zombieInfo.setEstimatedPerformanceLevel(110);
		assertEquals(110, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// setting low
		zombieInfo.setEstimatedPerformanceLevel(30);
		assertEquals(50, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// setting high
		zombieInfo.setEstimatedPerformanceLevel(250);
		assertEquals(200, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// updating no change
		zombieInfo.updateEstimatedPerformanceLevel();
		assertEquals(200, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		

		zombieInfo.addIDToInteractedWithIDs(0);
		zombieInfo.addIDToInteractedWithIDs(1);
		zombieInfo.addIDToInteractedWithIDs(2);
		zombieInfo.addToDamagePlayerGave(10);
		zombieInfo.addToDamagePlayerGave(10);
		zombieInfo.addToDamagePlayerGave(10);
		zombieInfo.addToDamagePlayerReceived(10);
		zombieInfo.addToDamagePlayerReceived(5);
		zombieInfo.addToDamagePlayerReceived(5);
		
		zombieInfo.updateEstimatedPerformanceLevel();
		double expected = 100 + (30.0/20.0-20.0/20.0)/3.0*100;
		assertEquals(expected, zombieInfo.getEstimatedPerformanceLevel(), .0001);
	}

	/**
	 * Tests the updateEstimatedPerformanceLevel() method when the mob's health is less than 20.
	 */
	@Test
	public void testUpdateEstimatedPerformanceLevelWhenMobHealthLessThan20() 
	{
		MobInfo silverfishInfo = new MobInfo(MobType.SILVERFISH);
		assertEquals(100, silverfishInfo.getEstimatedPerformanceLevel(), .0001);
		
		silverfishInfo.addIDToInteractedWithIDs(0);
		silverfishInfo.addIDToInteractedWithIDs(1);
		silverfishInfo.addIDToInteractedWithIDs(2);
		silverfishInfo.addIDToInteractedWithIDs(3);
		silverfishInfo.addIDToInteractedWithIDs(4);
		silverfishInfo.addToDamagePlayerGave(10*5);
		silverfishInfo.addToDamagePlayerReceived(10);
		
		silverfishInfo.updateEstimatedPerformanceLevel();
		
		double expected = 100 + (50.0/10.0-10.0/20.0)/5.0*100;
		assertEquals(expected, silverfishInfo.getEstimatedPerformanceLevel(), .0001);
	}

	/**
	 * Tests the updateEstimatedPerformanceLevel() method when the mob's health is greater than 20.
	 */
	@Test
	public void testUpdateEstimatedPerformanceLevelWhenMobHealthGreaterThan20() 
	{
		MobInfo enderManInfo = new MobInfo(MobType.ENDERMAN);
		assertEquals(100, enderManInfo.getEstimatedPerformanceLevel(), .0001);
		
		enderManInfo.addIDToInteractedWithIDs(0);
		enderManInfo.addIDToInteractedWithIDs(1);
		enderManInfo.addIDToInteractedWithIDs(2);
		enderManInfo.addIDToInteractedWithIDs(3);
		enderManInfo.addIDToInteractedWithIDs(4);
		enderManInfo.addToDamagePlayerGave(40*5);
		enderManInfo.addToDamagePlayerReceived(40);
		
		enderManInfo.updateEstimatedPerformanceLevel();
		
		double expected = 100 + (200.0/40.0-40.0/20.0)/5.0*100;
		assertEquals(expected, enderManInfo.getEstimatedPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the setCurrentPerformanceLevel() method.
	 */
	@Test
	public void testSetCurrentPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.setCurrentPerformanceLevel(110);
		assertEquals(110, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.setCurrentPerformanceLevel(30);
		assertEquals(50, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.setCurrentPerformanceLevel(250);
		assertEquals(200, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.updateCurrentPerformanceLevel();
		assertEquals(200-MobInfo.MAX_INCREMENT, zombieInfo.getCurrentPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updateCurrentPerformanceLevel() method.
	 */
	@Test
	public void testUpdateCurrentPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.setEstimatedPerformanceLevel(200);
		zombieInfo.updateCurrentPerformanceLevel();
		assertEquals(100+MobInfo.MAX_INCREMENT, zombieInfo.getCurrentPerformanceLevel(), .0001);
		
		zombieInfo.setCurrentPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(105);
		zombieInfo.updateCurrentPerformanceLevel();
		assertEquals(zombieInfo.getEstimatedPerformanceLevel(), zombieInfo.getCurrentPerformanceLevel(), .0001);

		zombieInfo.setCurrentPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(95);
		zombieInfo.updateCurrentPerformanceLevel();
		assertEquals(zombieInfo.getEstimatedPerformanceLevel(), zombieInfo.getCurrentPerformanceLevel(), .0001);

		zombieInfo.setCurrentPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(50);
		zombieInfo.updateCurrentPerformanceLevel();
		assertEquals(100-MobInfo.MAX_INCREMENT, zombieInfo.getCurrentPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the methods tied to the manualPerformanceLevel variable.
	 */
	@Test
	public void testManualPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getManualPerformanceLevel(), .0001);
		
		zombieInfo.setManualPerformanceLevel(110);
		assertEquals(110, zombieInfo.getManualPerformanceLevel(), .0001);
		
		zombieInfo.setManualPerformanceLevel(30);
		assertEquals(50, zombieInfo.getManualPerformanceLevel(), .0001);
		
		zombieInfo.setManualPerformanceLevel(250);
		assertEquals(200, zombieInfo.getManualPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the methods tied to the damagePlayerGave variable.
	 */
	@Test
	public void testDamagePlayerGave() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getDamagePlayerGave());
		
		zombieInfo.addIDToInteractedWithIDs(1);
		zombieInfo.addToDamagePlayerGave(2);
		assertEquals(2, zombieInfo.getDamagePlayerGave());
		
		zombieInfo.addToDamagePlayerGave(100);
		assertEquals(20, zombieInfo.getDamagePlayerGave());
	}
	
	/**
	 * Tests the methods tied to the damagePlayerReceived variable.
	 */
	@Test
	public void testDamagePlayerReceived() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getDamagePlayerReceived());
		
		zombieInfo.addToDamagePlayerReceived(2);
		assertEquals(2, zombieInfo.getDamagePlayerReceived());
	}
	
	/**
	 * Tests the methods dealing with the number of enemies the player
	 * has interacted with.
	 */
	@Test
	public void testInteractedWith() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getNumberInteractedWith());
		
		assertTrue(zombieInfo.addIDToInteractedWithIDs(0));
		assertEquals(1, zombieInfo.getNumberInteractedWith());

		assertFalse(zombieInfo.addIDToInteractedWithIDs(0));
		assertEquals(1, zombieInfo.getNumberInteractedWith());
	}
	
	/**
	 * Tests the getPerformanceLevelInUse() method.
	 * has interacted with.
	 */
	@Test
	public void testGetPerformanceLevelInUse() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		zombieInfo.setCurrentPerformanceLevel(150);
		zombieInfo.setManualPerformanceLevel(200);
		
		zombieInfo.setSetting(Setting.AUTO);
		assertEquals(150, zombieInfo.getPerformanceLevelInUse(), .0001);

		zombieInfo.setSetting(Setting.MANUAL);
		assertEquals(200, zombieInfo.getPerformanceLevelInUse(), .0001);

		zombieInfo.setSetting(Setting.DISABLED);
		assertEquals(100, zombieInfo.getPerformanceLevelInUse(), .0001);
	}
}