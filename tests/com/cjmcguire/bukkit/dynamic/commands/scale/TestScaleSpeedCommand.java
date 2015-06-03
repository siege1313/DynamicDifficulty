package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleSpeedCommand class.
 * @author CJ McGuire
 */
public class TestScaleSpeedCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand(Server mockServer) 
	{
		return new ScaleSpeedCommand(mockServer);
	}

	@Override
	protected String getCommandName()
	{
		return ScaleSpeedCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleSpeed();
	}
}
