package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleFollowDistanceCommand class.
 * @author CJ McGuire
 */
public class TestScaleFollowDistanceCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand(Server mockServer) 
	{
		return new ScaleFollowDistanceCommand(mockServer);
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
