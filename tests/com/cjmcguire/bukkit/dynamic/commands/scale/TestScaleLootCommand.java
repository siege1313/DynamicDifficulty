package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * Tests the ScaleLootCommand class.
 * @author CJ McGuire
 */
public class TestScaleLootCommand extends TestAbstractScaleCommand
{
	@Override
	protected AbstractScaleCommand getScaleCommand(Server mockServer) 
	{
		return new ScaleLootCommand(mockServer);
	}

	@Override
	protected String getCommandName()
	{
		return ScaleLootCommand.NAME;
	}

	@Override
	protected boolean shouldScaleAttribute(MobInfo mobInfo) 
	{
		return mobInfo.shouldScaleLoot();
	}
}
