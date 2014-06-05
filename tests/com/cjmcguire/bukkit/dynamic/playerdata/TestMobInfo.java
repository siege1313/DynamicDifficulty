package com.cjmcguire.bukkit.dynamic.playerdata;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

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
	 * Tests that the setting variable can be set correctly.
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
	 * Tests that the maxIntrement variable can be set correctly.
	 */
	@Test
	public void testMaxIncrement()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertEquals(10, blazeInfo.getMaxIncrement());
		
		blazeInfo.setMaxIncrement(-10);
		assertEquals(1, blazeInfo.getMaxIncrement());
		
		blazeInfo.setMaxIncrement(9);
		assertEquals(9, blazeInfo.getMaxIncrement());
	}
	
	/**
	 * Tests that the scaleAttack variable gets set correctly.
	 */
	@Test
	public void testScaleAttack()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleAttack());
		
		blazeInfo.setScaleAttack(false);
		
		assertFalse(blazeInfo.shouldScaleAttack());
	}
	
	/**
	 * Tests that the scaleDefense variable gets set correctly.
	 */
	@Test
	public void testScaleDefense()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleDefense());
		
		blazeInfo.setScaleDefense(false);
		
		assertFalse(blazeInfo.shouldScaleDefense());
	}
	
	/**
	 * Tests that the scaleSpeed variable gets set correctly.
	 */
	@Test
	public void testScaleSpeed()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleSpeed());
		
		blazeInfo.setScaleSpeed(false);
		
		assertFalse(blazeInfo.shouldScaleSpeed());
	}
	
	/**
	 * Tests that the scaleKnockBackResistance variable gets set 
	 * correctly.
	 */
	@Test
	public void testScaleKnockBackResistance()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleKnockbackResistance());
		
		blazeInfo.setScaleKnockbackResistance(false);
		
		assertFalse(blazeInfo.shouldScaleKnockbackResistance());
	}
	
	/**
	 * Tests that the scaleMaxFollowDistance variable gets set 
	 * correctly.
	 */
	@Test
	public void testScaleMaxFollowDistance()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleMaxFollowDistance());
		
		blazeInfo.setScaleMaxFollowDistance(false);
		
		assertFalse(blazeInfo.shouldScaleMaxFollowDistance());
	}
	
	/**
	 * Tests that the scaleXP variable gets set correctly.
	 */
	@Test
	public void testScaleXP()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleXP());
		
		blazeInfo.setScaleXP(false);
		
		assertFalse(blazeInfo.shouldScaleXP());
	}
	
	/**
	 * Tests that the scaleLoot variable gets set correctly.
	 */
	@Test
	public void testScaleLoot()
	{
		MobInfo blazeInfo = new MobInfo(MobType.BLAZE);
		
		assertTrue(blazeInfo.shouldScaleLoot());
		
		blazeInfo.setScaleLoot(false);
		
		assertFalse(blazeInfo.shouldScaleLoot());
	}
	
	/**
	 * Tests that the estimatedPerformanceLevel starts at 100.
	 */
	@Test
	public void testInitialEstimatedPerformanceLevel()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getEstimatedPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the setEstimatedPerformanceLevel() method.
	 */
	@Test
	public void testSetEstimatedPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		// setEstimatedPerformanceLevel() in middle
		zombieInfo.setEstimatedPerformanceLevel(110);
		assertEquals(110, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// setEstimatedPerformanceLevel() too low
		zombieInfo.setEstimatedPerformanceLevel(30);
		assertEquals(50, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// setEstimatedPerformanceLevel() too high
		zombieInfo.setEstimatedPerformanceLevel(250);
		assertEquals(200, zombieInfo.getEstimatedPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updateEstimatedPerformanceLevel() method.
	 */
	@Test
	public void testUpdateEstimatedPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
	
		// update with no change
		zombieInfo.updateEstimatedPerformanceLevel();
		assertEquals(100, zombieInfo.getEstimatedPerformanceLevel(), .0001);
		
		// update with change
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
	 * Tests the updateEstimatedPerformanceLevel() method when the 
	 * mob's health is less than 20.
	 */
	@Test
	public void testUpdateEstimatedPerformanceLevelWhenMobHealthLessThan20() 
	{
		MobInfo silverfishInfo = new MobInfo(MobType.SILVERFISH);
		
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
	 * Tests the updateEstimatedPerformanceLevel() method when the 
	 * mob's health is greater than 20.
	 */
	@Test
	public void testUpdateEstimatedPerformanceLevelWhenMobHealthGreaterThan20() 
	{
		MobInfo enderManInfo = new MobInfo(MobType.ENDERMAN);
		
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
	 * Tests that the autoPerformanceLevel starts at 100.
	 */
	@Test
	public void testInitialAutoPerformanceLevel()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the setAutoPerformanceLevel() method.
	 */
	@Test
	public void testSetAutoPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		// setAutoPerformanceLevel() in middle
		zombieInfo.setAutoPerformanceLevel(110);
		assertEquals(110, zombieInfo.getAutoPerformanceLevel(), .0001);
		
		// setAutoPerformanceLevel() too low
		zombieInfo.setAutoPerformanceLevel(30);
		assertEquals(50, zombieInfo.getAutoPerformanceLevel(), .0001);
		
		// setAutoPerformanceLevel() too high
		zombieInfo.setAutoPerformanceLevel(250);
		assertEquals(200, zombieInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the updateAutoPerformanceLevel() method.
	 */
	@Test
	public void testUpdateAutoPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		// update by max amount upward
		zombieInfo.setEstimatedPerformanceLevel(200);
		zombieInfo.updateAutoPerformanceLevel();
		assertEquals(100+zombieInfo.getMaxIncrement(), zombieInfo.getAutoPerformanceLevel(), .0001);
		
		// update by less than max amount upward
		zombieInfo.setAutoPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(105);
		zombieInfo.updateAutoPerformanceLevel();
		assertEquals(zombieInfo.getEstimatedPerformanceLevel(), zombieInfo.getAutoPerformanceLevel(), .0001);

		// update by less than max amount downward
		zombieInfo.setAutoPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(95);
		zombieInfo.updateAutoPerformanceLevel();
		assertEquals(zombieInfo.getEstimatedPerformanceLevel(), zombieInfo.getAutoPerformanceLevel(), .0001);

		//update by max amount downward
		zombieInfo.setAutoPerformanceLevel(100);
		zombieInfo.setEstimatedPerformanceLevel(50);
		zombieInfo.updateAutoPerformanceLevel();
		assertEquals(100-zombieInfo.getMaxIncrement(), zombieInfo.getAutoPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests that the manualPerformanceLevel starts at 100.
	 */
	@Test
	public void testInitialManualPerformanceLevel()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(100, zombieInfo.getManualPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the setManualPerformanceLevel() method.
	 */
	@Test
	public void testSetManualPerformanceLevel() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		// setManualPerformanceLevel() in middle
		zombieInfo.setManualPerformanceLevel(110);
		assertEquals(110, zombieInfo.getManualPerformanceLevel(), .0001);
		
		// setManualPerformanceLevel() too low
		zombieInfo.setManualPerformanceLevel(30);
		assertEquals(50, zombieInfo.getManualPerformanceLevel(), .0001);
		
		// setManualPerformanceLevel() too high
		zombieInfo.setManualPerformanceLevel(250);
		assertEquals(200, zombieInfo.getManualPerformanceLevel(), .0001);
	}
	
	/**
	 * Tests the getPerformanceLevelInUse() method.
	 */
	@Test
	public void testGetPerformanceLevelInUse() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		zombieInfo.setAutoPerformanceLevel(150);
		zombieInfo.setManualPerformanceLevel(200);
		
		zombieInfo.setSetting(Setting.AUTO);
		assertEquals(150, zombieInfo.getPerformanceLevelInUse(), .0001);

		zombieInfo.setSetting(Setting.MANUAL);
		assertEquals(200, zombieInfo.getPerformanceLevelInUse(), .0001);

		zombieInfo.setSetting(Setting.DISABLED);
		assertEquals(100, zombieInfo.getPerformanceLevelInUse(), .0001);
	}
	
	/**
	 * Tests that the damagePlayerGave starts at 0.
	 */
	@Test
	public void testInitialDamagePlayerGave()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getDamagePlayerGave());
	}
	
	/**
	 * Tests the addToDamagePlayerGave() method.
	 */
	@Test
	public void testAddToDamagePlayerGave()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		zombieInfo.addIDToInteractedWithIDs(1);
		zombieInfo.addToDamagePlayerGave(2);
		assertEquals(2, zombieInfo.getDamagePlayerGave());
		
		zombieInfo.addToDamagePlayerGave(100);
		assertEquals(20, zombieInfo.getDamagePlayerGave());
	}
	
	/**
	 * Tests that the damagePlayerReceived starts at 0.
	 */
	@Test
	public void testInitialDamagePlayerReceived()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getDamagePlayerReceived());
	}
	
	/**
	 * Tests the addToDamagePlayerReceived() method.
	 */
	@Test
	public void testAddToDamagePlayerReceived()
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);

		zombieInfo.addToDamagePlayerReceived(2);
		assertEquals(2, zombieInfo.getDamagePlayerReceived());
	}
	
	/**
	 * Tests that the number of mobs interacted with starts at 0.
	 */
	@Test
	public void testInitialGetNumberInteractedWith() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		assertEquals(0, zombieInfo.getNumberInteractedWith());
	}
	
	/**
	 * Tests the addIDToInteractedWithIDs() method.
	 */
	@Test
	public void testAddIDToInteractedWithIDs() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		assertTrue(zombieInfo.addIDToInteractedWithIDs(0));
		assertEquals(1, zombieInfo.getNumberInteractedWith());
	}
	
	/**
	 * Tests the addIDToInteractedWithIDs() method when the same ID 
	 * is added twice.
	 */
	@Test
	public void testAddSameIDToInteractedWithIDs() 
	{
		MobInfo zombieInfo = new MobInfo(MobType.ZOMBIE);
		
		assertTrue(zombieInfo.addIDToInteractedWithIDs(0));
		assertEquals(1, zombieInfo.getNumberInteractedWith());

		assertFalse(zombieInfo.addIDToInteractedWithIDs(0));
		assertEquals(1, zombieInfo.getNumberInteractedWith());
	}
}