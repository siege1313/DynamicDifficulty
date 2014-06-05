package com.cjmcguire.bukkit.dynamic.commands.scale;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleXPCommand class.
 * @author CJ McGuire
 */
public class TestScaleXPCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand() 
	{
		return new ScaleXPCommand();
	}

	@Override
	protected String getCommandName()
	{
		return ScaleXPCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleXP();
	}
}
