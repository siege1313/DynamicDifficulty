package com.cjmcguire.bukkit.dynamic.commands.scale;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the BooleanSetting enum.
 * @author CJ McGuire
 */
public class TestBooleanSetting 
{
	/**
	 * This test is here mostly to boost code coverage.
	 */
	@Test
	public void boostCoverage()
	{
		BooleanSetting[] booleans = BooleanSetting.values();
		for(int looper = 0; looper < booleans.length; looper++)
		{
			assertEquals(looper, booleans[looper].ordinal());
		}

		assertEquals(BooleanSetting.TRUE, BooleanSetting.valueOf("TRUE"));
	}

	/**
	 * Tests the getName() method
	 */
	@Test
	public void testGetName()
	{
		assertEquals("true", BooleanSetting.TRUE.getName());
		assertEquals("false", BooleanSetting.FALSE.getName());
	}

	/**
	 * Tests the getValue() method
	 */
	@Test
	public void testGetValue()
	{
		assertTrue(BooleanSetting.TRUE.getValue());
		assertFalse(BooleanSetting.FALSE.getValue());
	}
	
	/**
	 * Tests the getBooelanSetting() method
	 */
	@Test
	public void testGetMobType()
	{
		assertEquals(BooleanSetting.TRUE, BooleanSetting.getBooleanSetting("true"));
		assertEquals(BooleanSetting.FALSE, BooleanSetting.getBooleanSetting("false"));
		assertNull(BooleanSetting.getBooleanSetting("hi"));
	}
}