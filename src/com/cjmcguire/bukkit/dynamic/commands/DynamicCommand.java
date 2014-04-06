package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * This class is used with the /dynamic command. The /dynamic command 
 * prints out a message describing what the DynamicDifficulty plugin 
 * does.
 * @author CJ McGuire
 */
public class DynamicCommand extends AbstractDDCommand
{
	/**
	 * The name of this command. ("dynamic")
	 */
	public static final String NAME = "dynamic";
	
	private final static String SUMMARY_MESSAGE = ChatColor.LIGHT_PURPLE + 
			"The DynamicDifficulty plugin automatically adjusts the difficulty " +
			"of Minecraft's mobs. It measures your performance level for each mob and " +
			"scales some of their attributes to match your performance level.";
	
	private final static String DYNAMIC_MESSAGE = 
			ChatColor.GOLD + "/dynamic " + 
			ChatColor.WHITE + "Describes this plugin and its commands.";

	private final static String INFO_MESSAGE = 
			ChatColor.GOLD + "/dynamic info [player-name] " + 
			ChatColor.WHITE + "Displays the player's difficulty settings and performance levels. " +
			"If no player-name is given, it will display your information.";

	private final static String CHANGE_LEVEL_MESSAGE = 
			ChatColor.GOLD + "/dynamic changelevel <mob-name | 'all'> <50 to 200> [player-name] " + 
			ChatColor.WHITE + "Changes the player's manual performance level for the mob to the " +
			"given performance level. If no player-name is given, it will change your performance level.";
			
	private final static String CHANGE_SETTING_MESSAGE = 
			ChatColor.GOLD + "/dynamic changesetting <mob-name | 'all'> " + "<'auto' | 'manual' | 'disabled'> [player-name] " +
			ChatColor.WHITE + "Changes the player's setting for the mob to the given setting. " +
			"If no player-name is given, it will change your setting.";
			

	/**
	 * Initializes this DynamicCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public DynamicCommand(DynamicDifficulty plugin)
	{
		super(plugin);
	}

	/**
	 * Sends a summary message about the DynamicDifficulty plugin to 
	 * the sender.
	 * @param sender the sender of the command
	 * @param args not used
	 * @return true if the command worked as intended. false if it did 
	 * not work as intended.
	 */
	@Override
	protected boolean executeCommand(CommandSender sender, String[] args) 
	{
		boolean workedAsIntended = false;
		
		// if the sender has permission to use this command or is the Console
		if(sender.hasPermission("dynamic.dynamic") || !(sender instanceof Player))
		{
			this.safeSendMessage(sender, SUMMARY_MESSAGE);
			this.safeSendMessage(sender, DYNAMIC_MESSAGE);
			this.safeSendMessage(sender, INFO_MESSAGE);
			this.safeSendMessage(sender, CHANGE_LEVEL_MESSAGE);
			this.safeSendMessage(sender, CHANGE_SETTING_MESSAGE);
			
			workedAsIntended = true;
		}
		else
		{
			this.safeSendMessage(sender, "You do not have permission to use /dynamic");
		}
		
		return workedAsIntended;
	}
}