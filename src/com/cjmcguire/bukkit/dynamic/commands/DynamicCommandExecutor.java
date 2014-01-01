package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * This class is the command executor for the DynamicDifficulty plugin.
 * It is used with the /dynamic command and all of its child commands.
 * It is responsible for figuring out which DynamicDifficulty command
 * was called.
 * @author CJ McGuire
 */
public class DynamicCommandExecutor implements CommandExecutor
{
	/**
	 * The name of the core command. ("dynamic")
	 */
	public static final String NAME = "dynamic";
	
	private final DynamicCommand dynamicCommand;
	private final InfoCommand infoCommand;
	private final ChangeLevelCommand changeLevelCommand;
	private final ChangeSettingCommand changeSettingCommand;
	
	/**
	 * Initializes this DynamicCommandExecutor.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public DynamicCommandExecutor(DynamicDifficulty plugin)
	{
		dynamicCommand = new DynamicCommand(plugin);
		infoCommand = new InfoCommand(plugin);
		changeLevelCommand = new ChangeLevelCommand(plugin);
		changeSettingCommand = new ChangeSettingCommand(plugin);
	}
	
	/** 
	 * This method gets called in response to the /dynamic command.
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		boolean validCommand = true;
		
		// /dynmaic command
		if(args.length == 0)
		{
			dynamicCommand.executeCommand(sender, args);
		}
		// /dynamic info
		else if(args[0].equalsIgnoreCase(InfoCommand.NAME))
		{
			infoCommand.executeCommand(sender, args);
		}
		// /dynamic changelevel
		else if(args[0].equalsIgnoreCase(ChangeLevelCommand.NAME))
		{
			changeLevelCommand.executeCommand(sender, args);
		}
		// /dynamic changesetting
		else if(args[0].equalsIgnoreCase(ChangeSettingCommand.NAME))
		{
			changeSettingCommand.executeCommand(sender, args);
		}
		else
		{
			validCommand = false;
		}
		
		return validCommand;
	}
}