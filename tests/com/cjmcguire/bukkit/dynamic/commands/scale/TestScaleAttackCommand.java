package com.cjmcguire.bukkit.dynamic.commands.scale;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleAttackCommand class.
 * @author CJ McGuire
 */
public class TestScaleAttackCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand() 
	{
		return new ScaleAttackCommand();
	}

	@Override
	protected String getCommandName()
	{
		return ScaleAttackCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleAttack();
	}
}
