package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests the MobName enum.
 * @author CJ McGuire
 */
public class TestSetting
{
	/**
	 * Tests the getSettingName() method
	 */
	@Test
	public void testGetSettingName()
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

