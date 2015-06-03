package com.cjmcguire.bukkit.dynamic.commands.core;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * This class falls under the "/dynamic" command. The "/dynamic 
 * changelevel" command will change the manual performance level for
 * a given player, given that the command was entered correctly and
 * the sender has permission to use this command.
 * @author CJ McGuire
 */
public class ChangeLevelCommand extends AbstractChangeCommand
{
	/**
	 * The name of this command. ("changelevel")
	 */
	public static final String NAME = "changelevel";
	
	private static final String ALL_MOBS = "all";
	
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final String SELF_PERMISSION = "dynamic.changelevel.self";
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final String OTHER_PERMISSION = "dynamic.changelevel.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to change your own DynamicDifficulty levels";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to change other players' DynamicDifficulty levels";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to change your own DynamicDifficulty levels";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic changelevel <mob-name | 'all'> <50 to 200> [player-name]";

	/**
	 * Initializes this Command.
	 * @param server - the server to get players from
	 */
	public ChangeLevelCommand(Server server)
	{
		super(server, 
			  SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}

	/**
	 * Changes the level for the player with the given playerName 
	 * given that the parameters contain valid info. Several things 
	 * could go wrong when trying to change a player's performance 
	 * level. 
	 * 
	 * First, the performance level is not a number. 
	 * Second, the mob name is not a valid mob name. 
	 * 
	 * If any of these conditions occur, the method will not change 
	 * the performance level and will inform the sender of the error.
	 * 
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player to target
	 * @param args the command arguments. 
	 * args[0] must contain "changelevel". 
	 * args[1] must contain the mob name 
	 * args[2] must contain the level to change to. 
	 * args[3] can contain a player's name, optionally.
	 * @return true if the player's performance level was changed. 
	 * false if the level change could not be completed
	 */
	@Override
	public boolean commandAction(CommandSender sender, UUID playerID, String[] args)
	{
		boolean levelChanged = false;

		try
		{
			String mobName = args[1];
			MobType mobType = MobType.getMobType(mobName);

			int performanceLevel = Integer.parseInt(args[2]);
			
			if(mobName.equals(ALL_MOBS)) // if the player want to change all performance levels
			{
				this.changeAllPerformanceLevels(sender, playerID, performanceLevel);
				levelChanged = true;
			}
			else if(mobType != null) // if the player wants to change their performance level for only one mob
			{
				this.changeMobPerformanceLevel(sender, playerID, performanceLevel, mobType);
				levelChanged = true;
			}
			else	// if the player entered an invalid mob name
			{
				sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name");
			}
		}
		catch(NumberFormatException e)
		{
			// Catch the error if the user enters a performance level 
			// that is not a number.
			sender.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RESET + " is not an integer number");
		}

		return levelChanged;
	}

	private void changeAllPerformanceLevels(CommandSender sender, UUID playerID, int performanceLevel)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);

		boolean oneWasAutoOrDisabled = false;
		
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);

			mobInfo.setManualPerformanceLevel(performanceLevel);

			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				oneWasAutoOrDisabled = true;
			}
		}

		int actualPerformanceLevel = (int) (playerInfo.getMobInfo(MobType.ZOMBIE).getManualPerformanceLevel());

		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(oneWasAutoOrDisabled)
			{
				sender.sendMessage("At least one of your settings is not set to " +
						ChatColor.GOLD + "manual" + ChatColor.RESET + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.RESET + "to change your setting to " + ChatColor.GOLD + "manual");
			}
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			player.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			sender.sendMessage(ChatColor.GOLD + playerName +
					ChatColor.RESET + "'s manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(oneWasAutoOrDisabled)
			{
				sender.sendMessage("At least one of " + 
						ChatColor.GOLD + playerName + ChatColor.RESET + "'s settings is not set to " +
						ChatColor.GOLD + "manual" + ChatColor.RESET + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.RESET + "to change " + ChatColor.GOLD + playerName +
						ChatColor.RESET + "'s settings to " + ChatColor.GOLD + "manual");
			}
		}
	}

	private void changeMobPerformanceLevel(CommandSender sender, UUID playerID, double performanceLevel, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);

		MobInfo mobInfo = playerInfo.getMobInfo(mobType);

		mobInfo.setManualPerformanceLevel(performanceLevel);

		int actualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				sender.sendMessage(ChatColor.GOLD + "Your " +
						ChatColor.RESET + "setting for " + ChatColor.GOLD + mobType.getName() + 
						ChatColor.RESET + " is not set to " + ChatColor.GOLD + "manual" + 
						ChatColor.RESET + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.RESET + "to change your setting to " + ChatColor.GOLD + "manual");
			}
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			sender.sendMessage(ChatColor.GOLD + playerName +
					ChatColor.RESET + "'s manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				sender.sendMessage(ChatColor.GOLD + playerName +
						ChatColor.RESET + "'s setting for " + ChatColor.GOLD + mobType.getName() + 
						ChatColor.RESET + " is not set to " + ChatColor.GOLD + "manual" + 
						ChatColor.RESET + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.RESET + "to change " + ChatColor.GOLD + playerName +
						ChatColor.RESET + "'s setting to " + ChatColor.GOLD + "manual");
			}
		}
	}
}