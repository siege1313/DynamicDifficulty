package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleDefenseCommand class.
 * @author CJ McGuire
 */
public class TestScaleDefenseCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand(Server mockServer) 
	{
		return new ScaleDefenseCommand(mockServer);
	}

	@Override
	protected String getCommandName()
	{
		return ScaleDefenseCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleDefense();
	}
}
