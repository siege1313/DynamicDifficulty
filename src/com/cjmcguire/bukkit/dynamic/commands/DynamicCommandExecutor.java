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
	
	private DynamicCommand dynamicCommand;
	private InfoCommand infoCommand;
	private ChangeLevelCommand changeLevelCommand;
	private ChangeSettingCommand changeSettingCommand;
	
	/**
	 * Initializes this DynamicCommand.
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
		boolean validCommand = false;
		
		// /dynmaic command
		if(args.length == 0)
		{
			validCommand = dynamicCommand.executeCommand(sender, args);
		}
		else if(args.length == 1)
		{
			// /dynamic info
			if(args[0].equalsIgnoreCase(InfoCommand.NAME))
			{
				validCommand = infoCommand.executeCommand(sender, args);
			}
		}
		else if(args.length == 4)
		{
			// /dynamic settings
			String commandName = args[0] + " " + args[1];
			if(commandName.equalsIgnoreCase(ChangeSettingCommand.NAME))
			{
				validCommand = changeSettingCommand.executeCommand(sender, args);
			}
			// /dynamic levels
			else if(commandName.equalsIgnoreCase(ChangeLevelCommand.NAME))
			{
				validCommand = changeLevelCommand.executeCommand(sender, args);
			}
		}
		
		return validCommand;
	}
}