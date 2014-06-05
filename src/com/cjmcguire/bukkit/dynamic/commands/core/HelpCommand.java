package com.cjmcguire.bukkit.dynamic.commands.core;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.commands.AbstractDDCommand;

/**
 * This class falls under the "/dynamic" command. The "/dynamic help" 
 * command prints out messages describing what the DynamicDifficulty 
 * plugin and its commands do.
 * @author CJ McGuire
 */
public class HelpCommand extends AbstractDDCommand
{
	/**
	 * The name of this command. ("help")
	 */
	public static final String NAME = "help";
	
	/**
	 * The Permission node for this command.
	 */
	public static final String PERMISSION = "dynamic.help";
	
	/*
	 * Page 1.
	 */
	private final static String SUMMARY_MESSAGE = ChatColor.LIGHT_PURPLE + 
			"DynamicDifficulty gives you more control over the difficulty of " +
			"Minecraft's mobs. You can have the plugin automatically " +
			"adjust the difficulty to match your performance level or you " +
			"can manually set the difficulty yourself.";
	
	private final static String PLAYER_TARGET_MESSAGE = "" +
			ChatColor.BOLD + "Note: " + ChatColor.RESET +
			"For commands that request a player name, if no name is " +
			"given, your name will be used.";
	
	private final static String HELP_MESSAGE = 
			ChatColor.GOLD + "/dynamic help [page number] " + 
			ChatColor.RESET + "Lists descriptions of the " +
			"plugin and its commands.";

	/*
	 * Page 2.
	 */
	private final static String INFO_MESSAGE = 
			ChatColor.GOLD + "/dynamic info <mob-name | 'general'> [player-name] " + 
			ChatColor.RESET + "Displays the player's dynamic difficulty info.";

	private final static String CHANGE_LEVEL_MESSAGE = 
			ChatColor.GOLD + "/dynamic changelevel <mob-name | 'all'> <50 to 200> [player-name] " + 
			ChatColor.RESET + "Changes the player's manual performance " +
			"level for the mob to the given performance level.";
			
	private final static String CHANGE_SETTING_MESSAGE = 
			ChatColor.GOLD + "/dynamic changesetting <mob-name | 'all'> " + "<'auto' | 'manual' | 'disabled'> [player-name] " +
			ChatColor.RESET + "Changes the player's setting for " +
			"the mob to the given setting.";
	
	/*
	 * Page 3.
	 */
	private final static String SET_MAX_INCREMENT_MESSAGE = 
			ChatColor.GOLD + "/dynamic setmaxincrement <mob-name | 'all'> " + "<positive number> [player-name] " +
			ChatColor.RESET + "Changes the max amount that the player's " +
			"auto performance level can change by in a single update.";
	
	private final static String SCALE_ATTACK_MESSAGE = 
			ChatColor.GOLD + "/dynamic scaleattack <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not a mob's attack damage " +
			"should be scaled for the player.";

	private final static String SCALE_DEFENSE_MESSAGE = 
			ChatColor.GOLD + "/dynamic scaledefense <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not a mob's defense should " +
			"be scaled for the player.";

	/*
	 * Page 4
	 */
	private final static String SCALE_SPEED_MESSAGE = 
			ChatColor.GOLD + "/dynamic scalespeed <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not a mob's speed should be " +
			"scaled for the player.";

	private final static String SCALE_KNOCKBACK_MESSAGE = 
			ChatColor.GOLD + "/dynamic scaleknockback <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not a mob's knockback " +
			"resistance should be scaled for the player.";

	private final static String SCALE_FOLLOW_DISTANCE_MESSAGE = 
			ChatColor.GOLD + "/dynamic scalefollowdistance <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not a mob's max " +
			"follow distance should be scaled for the player.";

	/*
	 * Page 5.
	 */
	private final static String SCALE_XP_MESSAGE = 
			ChatColor.GOLD + "/dynamic scalexp <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not the xp a mob drops " +
			"should be scaled for the player.";
	
	private final static String SCALE_LOOT_MESSAGE = 
			ChatColor.GOLD + "/dynamic scaleloot <mob-name | 'all'> <'true' | 'false'> [player-name] " +
			ChatColor.RESET + "Sets whether or not the loot a mob drops " +
					"should be scaled for the player.";
	
	
	/**
	 * Initializes this Command.
	 */
	public HelpCommand()
	{
		super();
	}

	/**
	 * Sends a help message about DynamicDifficulty and its commands 
	 * to the sender.
	 * @param sender the sender of the command
	 * @param args the command arguments
	 * args[0] must contain "help"
	 * args[1] optionally can contain a positive number indicating 
	 * the page number
	 * @return true if the sender had permission to use this command
	 * false if the sender did not.
	 */
	@Override
	protected boolean executeCommand(CommandSender sender, String[] args) 
	{
		boolean workedAsIntended = true;
		
		// if the sender has permission to use this command or is the Console
		if(sender.hasPermission(PERMISSION) || !(sender instanceof Player))
		{
			if(args.length != 2)
			{
				this.printPage1(sender);
			}
			else 
			{
				if(args[1].equals("1"))
				{
					this.printPage1(sender);
				}
				else if(args[1].equals("2"))
				{
					this.printPage2(sender);
				}
				else if(args[1].equals("3"))
				{
					this.printPage3(sender);
				}
				else if(args[1].equals("4"))
				{
					this.printPage4(sender);
				}
				else if(args[1].equals("5"))
				{
					this.printPage5(sender);
				}
				else
				{
					sender.sendMessage("" + ChatColor.GOLD + args[1] + 
									   ChatColor.RESET + " is not valid for a page number");
					workedAsIntended = false;
				}
			}
		}
		else
		{
			sender.sendMessage("You do not have permission to use the help command");
			workedAsIntended = false;
		}
		
		return workedAsIntended;
	}
	
	private void printPage1(CommandSender sender)
	{
		sender.sendMessage("----- Page 1 -----");
		sender.sendMessage(SUMMARY_MESSAGE);
		sender.sendMessage(PLAYER_TARGET_MESSAGE);
		sender.sendMessage(HELP_MESSAGE);	
	}
	
	private void printPage2(CommandSender sender)
	{
		sender.sendMessage("----- Page 2 -----");
		sender.sendMessage(INFO_MESSAGE);
		sender.sendMessage(CHANGE_LEVEL_MESSAGE);
		sender.sendMessage(CHANGE_SETTING_MESSAGE);
	}
	
	private void printPage3(CommandSender sender)
	{
		sender.sendMessage("----- Page 3 -----");
		sender.sendMessage(SET_MAX_INCREMENT_MESSAGE);
		sender.sendMessage(SCALE_ATTACK_MESSAGE);
		sender.sendMessage(SCALE_DEFENSE_MESSAGE);
	}
	
	private void printPage4(CommandSender sender)
	{
		sender.sendMessage("----- Page 4 -----");
		sender.sendMessage(SCALE_SPEED_MESSAGE);
		sender.sendMessage(SCALE_KNOCKBACK_MESSAGE);
		sender.sendMessage(SCALE_FOLLOW_DISTANCE_MESSAGE);
	}
	
	private void printPage5(CommandSender sender)
	{
		sender.sendMessage("----- Page 5 -----");
		sender.sendMessage(SCALE_XP_MESSAGE);
		sender.sendMessage(SCALE_LOOT_MESSAGE);	
	}
}