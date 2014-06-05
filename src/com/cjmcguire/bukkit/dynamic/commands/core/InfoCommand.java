package com.cjmcguire.bukkit.dynamic.commands.core;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.commands.AbstractPlayerTargetableCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;

/**
 * This class falls under the "/dynamic" command. The "/dynamic info"
 * command list the player's dynamic difficulty information for each
 * type of mob given that the command was entered correctly and the 
 * sender has permission to use this command.
 * @author CJ McGuire
 */
public class InfoCommand extends AbstractPlayerTargetableCommand
{
	private static final String GENERAL_INFO = "general";
	
	private static final int SELF_ARGS_LENGTH = 2;
	private static final int OTHER_ARGS_LENGTH = 3;

	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final String SELF_PERMISSION = "dynamic.info.self";
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final String OTHER_PERMISSION = "dynamic.info.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to view your own DynamicDifficulty information";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to view other players' DynamicDifficulty information";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to view your own DynamicDifficulty information";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic info <mob-name | 'general'> [player-name]";
	
	/**
	 * The name of this command. ("info")
	 */
	public static final String NAME = "info";
	
	/**
	 * Initializes this Command.
	 */
	public InfoCommand()
	{
		super(SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}
	
	/**
	 * Prints out the DynamicDifficulty info for a player.
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player to target
	 * @param args the command arguments.
	 * args[0] must contain "info".
	 * args[1] must contain a mob name or "general"
	 * args[2] can contain a player's name, optionally.
	 * @return true if the player's info was sent. false if the info
	 * could not be sent.
	 */
	@Override
	public boolean commandAction(CommandSender sender, UUID playerID, String [] args)
	{
		boolean valid = false;

		String mobName = args[1];
		MobType mobType = MobType.getMobType(mobName);
		
		if(mobName.equals(GENERAL_INFO))
		{
			this.sendGeneralInfo(sender, playerID);
			valid = true;
		}
		else if(mobType != null)
		{
			this.sendMobInfo(sender, playerID, mobType);
			valid = true;
		}
		else
		{
			sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name");
		}
		
		return valid;
	}
	
	private void sendGeneralInfo(CommandSender sender, UUID playerID)
	{
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Your DynamicDifficulty settings and performance levels:");
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "DynamicDifficulty settings and performance levels for " + playerName + ":");
		}
		
		// for each mobType
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = this.playerDataManager.getPlayersMobInfo(playerID, mobType);
			
			// get the mobName and pad it with spaces
			String mobName = mobType.getName();
			
			// get the setting name and pad it with spaces
			String settingName = mobInfo.getSetting().getName();
			
			//get the auto and manual performance levels as integers
			int autoPerformanceLevel = (int)(mobInfo.getAutoPerformanceLevel() + 0.5);
			int manualPerformanceLevel = (int)(mobInfo.getManualPerformanceLevel() + 0.5);
			
			sender.sendMessage(ChatColor.GOLD + mobName + 
					ChatColor.RESET + ChatColor.BOLD + " - Setting:" + ChatColor.RESET + ChatColor.GOLD + settingName + 
					ChatColor.RESET + ChatColor.BOLD +  " - Auto:" + ChatColor.RESET + ChatColor.GOLD + autoPerformanceLevel +
					ChatColor.RESET + ChatColor.BOLD + " - Manual:" + ChatColor.RESET + ChatColor.GOLD +  manualPerformanceLevel);
		}
	}

	private void sendMobInfo(CommandSender sender, UUID playerID, MobType mobType)
	{
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "Your DynamicDifficulty information for " + mobType.getName() + ":");
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			sender.sendMessage(ChatColor.LIGHT_PURPLE + playerName + "'s DynamicDifficulty information for " + mobType.getName() + ":");
		}

		MobInfo mobInfo = this.playerDataManager.getPlayersMobInfo(playerID, mobType);

		// get the setting name
		String settingName = mobInfo.getSetting().getName();

		//get the auto and manual performance levels as integers
		int autoPerformanceLevel = (int)(mobInfo.getAutoPerformanceLevel() + 0.5);
		int manualPerformanceLevel = (int)(mobInfo.getManualPerformanceLevel() + 0.5);

		int maxIncrement = mobInfo.getMaxIncrement();

		boolean scaleAttack = mobInfo.shouldScaleAttack();
		boolean scaleDefense = mobInfo.shouldScaleDefense();
		boolean scaleSpeed = mobInfo.shouldScaleSpeed();
		boolean scaleKnockback = mobInfo.shouldScaleKnockbackResistance();
		boolean scaleFollowDistance = mobInfo.shouldScaleMaxFollowDistance();
		boolean scaleXP = mobInfo.shouldScaleXP();
		boolean scaleLoot = mobInfo.shouldScaleLoot();

		/*
		 * Your DynamicDifficulty information for zombie:
		 * Performance Levels: 
		 *   Auto: 100 - Manual: 100
		 * Setting: auto
		 * Max Increment: 10
		 * Scale:
		 *   Attack: true - Defense: true
		 *   Speed: true - Knockback: true
		 *   Max Follow Distance: true
		 *   XP: true - Loot: true
		 */

		sender.sendMessage("" + 
				ChatColor.RESET + ChatColor.BOLD + "Performance Levels:");
		sender.sendMessage("" + 
				ChatColor.RESET + "  Auto: " +
				ChatColor.RESET + ChatColor.GOLD + autoPerformanceLevel + 
				ChatColor.RESET + " - Manual: " + 
				ChatColor.RESET + ChatColor.GOLD + manualPerformanceLevel);
		sender.sendMessage("" + 
				ChatColor.RESET + ChatColor.BOLD + "Setting: " + 
				ChatColor.RESET + ChatColor.GOLD + settingName);
		sender.sendMessage("" + 
				ChatColor.RESET + ChatColor.BOLD + "Max Increment: " + 
				ChatColor.RESET + ChatColor.GOLD + maxIncrement);
		sender.sendMessage("" +  
				ChatColor.RESET + ChatColor.BOLD + "Scale:");
		sender.sendMessage("" + 
				ChatColor.RESET + "  Attack: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleAttack +
				ChatColor.RESET + " - Defense: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleDefense);
		sender.sendMessage("" +  
				ChatColor.RESET + "  Speed: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleSpeed +
				ChatColor.RESET + " - Knockback: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleKnockback);
		sender.sendMessage("" +  
				ChatColor.RESET + "  Max Follow Distance: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleFollowDistance);
		sender.sendMessage("" +  
				ChatColor.RESET + "  XP: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleXP + 
				ChatColor.RESET + " - Loot: " + 
				ChatColor.RESET + ChatColor.GOLD + scaleLoot);
	}
}