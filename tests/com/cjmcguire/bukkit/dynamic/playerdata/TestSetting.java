package com.cjmcguire.bukkit.dynamic.playerdata;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the MobName enum.
 * @author CJ McGuire
 */
public class TestSetting
{

	/**
	 * This test is here mostly to boost code coverage.
	 */
	@Test
	public void boostCoverage()
	{
		Setting[] settings = Setting.values();
		for(int looper = 0; looper < settings.length; looper++)
		{
			assertEquals(looper, settings[looper].ordinal());
		}
		
		assertEquals(Setting.AUTO, Setting.valueOf("AUTO"));
	}
	
	/**
	 * Tests the getName() method
	 */
	@Test
	public void testGetName()
	{
		assertEquals("auto", Setting.AUTO.getName());
		assertEquals("manual", Setting.MANUAL.getName());
		assertEquals("disabled", Setting.DISABLED.getName());
	}
	
	/**
	 * Tests the getSetting() method
	 */
	@Test
	public void testGetSetting()
	{
		assertEquals(Setting.AUTO, Setting.getSetting("auto"));
		assertEquals(Setting.MANUAL, Setting.getSetting("manual"));
		assertEquals(Setting.DISABLED, Setting.getSetting("disabled"));
		assertNull(Setting.getSetting("not setting"));
	}
}