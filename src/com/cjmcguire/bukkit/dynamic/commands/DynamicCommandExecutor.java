package com.cjmcguire.bukkit.dynamic.commands;

import java.util.HashMap;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.commands.core.*;
import com.cjmcguire.bukkit.dynamic.commands.scale.*;

/**
 * This class is the command executor for the DynamicDifficulty 
 * plugin. It is used with the "/dynamic" command and all of its 
 * child commands. It is responsible for figuring out which command
 * was called.
 * @author CJ McGuire
 */
public class DynamicCommandExecutor implements CommandExecutor
{
	/**
	 * The name of the Command Executor's core command. ("dynamic")
	 */
	public static final String NAME = "dynamic";
	
	private HashMap<String, DDCommand> commands;
	
	/**
	 * Initializes the DynamicCommandExecutor.
	 * @param server - the server for this CommandExecutor
	 */
	public DynamicCommandExecutor(Server server)
	{
		commands = new HashMap<String, DDCommand>();
		
		// core
		commands.put(HelpCommand.NAME, new HelpCommand());
		commands.put(InfoCommand.NAME, new InfoCommand(server));
		commands.put(ChangeLevelCommand.NAME, new ChangeLevelCommand(server));
		commands.put(ChangeSettingCommand.NAME, new ChangeSettingCommand(server));
		commands.put(SetMaxIncrementCommand.NAME, new SetMaxIncrementCommand(server));
		
		// scale
		commands.put(ScaleAttackCommand.NAME, new ScaleAttackCommand(server));
		commands.put(ScaleDefenseCommand.NAME, new ScaleDefenseCommand(server));
		commands.put(ScaleFollowDistanceCommand.NAME, new ScaleFollowDistanceCommand(server));
		commands.put(ScaleKnockbackCommand.NAME, new ScaleKnockbackCommand(server));
		commands.put(ScaleSpeedCommand.NAME, new ScaleSpeedCommand(server));
		commands.put(ScaleXPCommand.NAME, new ScaleXPCommand(server));
		commands.put(ScaleLootCommand.NAME, new ScaleLootCommand(server));
	}
	
	/** 
	 * This method gets called in response to the "/dynamic" command.
	 * @see org.bukkit.command.CommandExecutor#onCommand(org.bukkit.command.CommandSender, 
	 * org.bukkit.command.Command, java.lang.String, java.lang.String[])
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		boolean validCommand = true;
		
		String commandName;
		
		// Player enters no args indicating he wants the "/dynamic" command.
		if(args.length == 0)	
		{
			commandName = HelpCommand.NAME;
		}
		else // The player entered some args.
		{
			commandName = args[0];
		}
		
		DDCommand ddCommand = commands.get(commandName);
		
		// If the ddCommand exists in the HashMap.
		if(ddCommand != null)
		{
			ddCommand.executeCommand(sender, args);	
		}
		else // The command is not valid.
		{
			validCommand = false;
		}
		
		return validCommand;
	}
}