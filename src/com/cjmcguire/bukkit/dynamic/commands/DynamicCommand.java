package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * This class is used with the /dynamic command. The /dynamic command 
 * prints out a message describing what the DynamicDifficulty plugin does.
 * @author CJ McGuire
 */
public class DynamicCommand extends AbstractDDCommand
{
	/**
	 * The name of this command. ("dynamic")
	 */
	public static final String NAME = "dynamic";
	
	private final static String summaryMessage = ChatColor.LIGHT_PURPLE + 
			"The DynamicDifficulty plugin automatically adjusts the difficulty " +
			"of Minecraft's mobs. It measures your performance level for each mob and " +
			"scales some of their attributes to match your performance level.";
	
	
	/**
	 * Initializes this DynamicCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public DynamicCommand(DynamicDifficulty plugin)
	{
		super(plugin);
	}

	/**
	 * Executes this command. 
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true beecause the command is always valid
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String[] args) 
	{
		this.commandAction(sender, null, null);
		return true;
	}

	/**
	 * Sends a summary message about the DynamicDifficulty plugin to the sender.
	 * @param sender the sender of the command
	 * @param playerName not used
	 * @param args not used
	 * @return true if the sender had permission to use this command or if the 
	 * sender was the console (in which case, it would have permission), false 
	 * if the sender did not have permission
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String[] args) 
	{
		boolean hasPermission = false;

		// if the sender has permission to use this command or is the Console
		if(sender.hasPermission("dynamic.dynamic") || sender instanceof ConsoleCommandSender)
		{
			this.safeSendMessage(sender, summaryMessage);
			this.safeSendMessage(sender, ChatColor.GOLD + "/dynamic " + ChatColor.WHITE + "describes this plugin and its commands");
			this.safeSendMessage(sender, ChatColor.GOLD + "/dynamic info " + ChatColor.WHITE + "displays your difficulty settings and performance levels");
			this.safeSendMessage(sender, ChatColor.GOLD + "/dynamic change setting [mob name | all] [auto | manual | disabled] " + ChatColor.WHITE +
					"changes your setting for the mob to the given setting");
			this.safeSendMessage(sender, ChatColor.GOLD + "/dynamic change level [mob name | all] [50 to 200] " + ChatColor.WHITE + 
					"changes your manual performance level for the mob to the given performance level");
			
			hasPermission = true;
		}
		else
		{
			this.safeSendMessage(sender, "You do not have permission to use this command");
		}
		
		return hasPermission;
	}
}