package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;

/**
 * This class is used with the /dynamic info command. The /dynamic 
 * info command prints out a message listing a player's setting, auto
 * performance level, and manual performance level for each mob.
 * @author CJ McGuire
 */
public class InfoCommand extends PlayerTargetableCommand
{
	
	private static final int SELF_ARGS_LENGTH = 1;
	private static final int OTHER_ARGS_LENGTH = 2;

	private static final String SELF_PERMISSION = "dynamic.info.self";
	private static final String OTHER_PERMISSION = "dynamic.info.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to view your own DynamicDifficulty info";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to view other players' DynamicDifficulty info";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to view your own DynamicDifficulty info";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic info [player-name]";
	
	/**
	 * The name of this command. ("info")
	 */
	public static final String NAME = "info";
	
	/**
	 * Initializes this InfoCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public InfoCommand(DynamicDifficulty plugin)
	{
		super(plugin,
				SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
				SELF_PERMISSION, OTHER_PERMISSION,
				SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
				SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}
	
	/**
	 * Prints out the DynamicDifficulty settings and performance 
	 * levels for a player.
	 * @param sender the sender of the command
	 * @param args must contain "info" in args[0]. Optionally args[1] 
	 * can contain a player's name
	 * @return true because this method will always print out the info 
	 * as intended.
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String [] args)
	{
		if(!this.isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.LIGHT_PURPLE + "Your DynamicDifficulty Settings and Performance Levels:");
		}
		else
		{
			this.safeSendMessage(sender, ChatColor.LIGHT_PURPLE + "DynamicDifficulty Settings and Performance Levels for " + playerName + ":");
		}
		
		// for each mobType
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = this.playerDataManager.getPlayersMobInfo(playerName, mobType);
			
			// get the mobName and pad it with spaces
			String mobName = mobType.getName();
			
			// get the setting name and pad it with spaces
			String settingName = mobInfo.getSetting().getName();
			
			//get the auto and manual performance levels as integers
			int autoPerformanceLevel = (int)(mobInfo.getAutoPerformanceLevel() + 0.5);
			int manualPerformanceLevel = (int)(mobInfo.getManualPerformanceLevel() + 0.5);
			
			this.safeSendMessage(sender, ChatColor.GOLD + mobName + 
					ChatColor.WHITE + ChatColor.BOLD + " - Setting:" + ChatColor.RESET + ChatColor.GOLD + settingName + 
					ChatColor.WHITE + ChatColor.BOLD +  " - Auto:" + ChatColor.RESET + ChatColor.GOLD + autoPerformanceLevel +
					ChatColor.WHITE + ChatColor.BOLD + " - Manual:" + ChatColor.RESET + ChatColor.GOLD +  manualPerformanceLevel);
		}
		
		return true;
	}
}