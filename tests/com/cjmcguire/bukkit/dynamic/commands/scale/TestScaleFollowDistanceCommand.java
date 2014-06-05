package com.cjmcguire.bukkit.dynamic.commands.scale;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleFollowDistanceCommand class.
 * @author CJ McGuire
 */
public class TestScaleFollowDistanceCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand() 
	{
		return new ScaleFollowDistanceCommand();
	}

	@Override
	protected String getCommandName()
	{
		return ScaleFollowDistanceCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleMaxFollowDistance();
	}
}
