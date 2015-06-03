package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleKnockbackCommand class.
 * @author CJ McGuire
 */
public class TestScaleKnockbackCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand(Server mockServer) 
	{
		return new ScaleKnockbackCommand(mockServer);
	}

	@Override
	protected String getCommandName()
	{
		return ScaleKnockbackCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleKnockbackResistance();
	}
}
