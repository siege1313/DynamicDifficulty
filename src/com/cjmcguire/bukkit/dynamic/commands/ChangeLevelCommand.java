package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * This class is used with the /dynamic command. The /dynamic command 
 * prints out a message describing what the DynamicDifficulty plugin 
 * does.
 * @author CJ McGuire
 */
public class ChangeLevelCommand extends PlayerTargetableCommand
{
	/**
	 * The name of this command. ("changelevel")
	 */
	public static final String NAME = "changelevel";
	
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	private static final String SELF_PERMISSION = "dynamic.changelevel.self";
	private static final String OTHER_PERMISSION = "dynamic.changelevel.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to change your own DynamicDifficulty levels";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to change other players' DynamicDifficulty levels";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to change your own DynamicDifficulty levels";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic changelevel <mob-name | 'all'> <50 to 200> [player-name]";

	/**
	 * Initializes this DynamicDifficulty Command.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public ChangeLevelCommand(DynamicDifficulty plugin)
	{
		super(plugin,
				SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
				SELF_PERMISSION, OTHER_PERMISSION,
				SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
				SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}

	/**
	 * Changes the level for the player with the given playerName 
	 * given that the parameters contain valid info. Several things 
	 * could go wrong when trying to change a player's performance 
	 * level. First, the performance level is not a number. Second, 
	 * the mob name is not a valid mob name. If any of these 
	 * conditions occur, the method will not change the performance 
	 * level and will inform the sender of the error.
	 * @param sender the sender of the command
	 * @param playerName the name of the player
	 * @param args must contain "changelevel" in args[0], args[1] must 
	 * contain the mob name and args[2] must contain the level to 
	 * change to. Optionally, args[3] can contain a player's name
	 * @return true if the player's performance level was changed. 
	 * false if the level change could not be completed
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String[] args)
	{
		boolean levelChanged = false;

		try
		{
			String mobName = args[1];
			MobType mobType = MobType.getMobType(mobName);

			double performanceLevel = Integer.parseInt(args[2]);
			
			if(mobName.equals("all"))	// if the player want to change all performance levels
			{
				this.changeAllPerformanceLevels(sender, playerName, performanceLevel);
				levelChanged = true;
			}
			else if(mobType != null)	// if the player wants to change their performance level for only one mob
			{
				this.changeMobPerformanceLevel(sender, playerName, performanceLevel, mobType);
				levelChanged = true;
			}
			else	// if the player entered an invalid mob name
			{
				this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name");
			}
		}
		catch(NumberFormatException e)
		{
			// catch the error if the user enters a performance level 
			// that is not a number
			this.safeSendMessage(sender, ChatColor.GOLD + args[2] + ChatColor.WHITE + " is not an integer number");
		}

		return levelChanged;
	}

	private void changeAllPerformanceLevels(CommandSender sender, String playerName, double performanceLevel)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerName);

		boolean oneWasAutoOrDisabled = false;
		for(MobType mob: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mob);

			mobInfo.setManualPerformanceLevel(performanceLevel);

			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				oneWasAutoOrDisabled = true;
			}
		}

		int actualPerformanceLevel = (int) (playerInfo.getMobInfo(MobType.ZOMBIE).getManualPerformanceLevel());

		
		if(!this.isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.GOLD + "Your " +
					ChatColor.WHITE + "manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(oneWasAutoOrDisabled)
			{
				this.safeSendMessage(sender, "At least one of your settings is not set to " +
						ChatColor.GOLD + "manual" + ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.WHITE + "to change your setting to " + ChatColor.GOLD + "manual");
			}
		}
		else
		{
			Player player = this.plugin.getServer().getPlayer(playerName);
			this.safeSendMessage(player, ChatColor.GOLD + "Your " +
					ChatColor.WHITE + "manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			this.safeSendMessage(sender, ChatColor.GOLD + playerName +
					ChatColor.WHITE + "'s manual performance levels for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(oneWasAutoOrDisabled)
			{
				this.safeSendMessage(sender, "At least one of " + 
						ChatColor.GOLD + playerName + ChatColor.WHITE + "'s settings is not set to " +
						ChatColor.GOLD + "manual" + ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.WHITE + "to change " + ChatColor.GOLD + playerName +
						ChatColor.WHITE + "'s settings to " + ChatColor.GOLD + "manual");
			}
		}
	}

	private void changeMobPerformanceLevel(CommandSender sender, String playerName, double performanceLevel, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerName);

		MobInfo mobInfo = playerInfo.getMobInfo(mobType);

		mobInfo.setManualPerformanceLevel(performanceLevel);

		int actualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
		
		if(!this.isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.GOLD + "Your " +
					ChatColor.WHITE + "manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + "Your " +
						ChatColor.WHITE + "setting for " + ChatColor.GOLD + mobType.getName() + 
						ChatColor.WHITE + " is not set to " + ChatColor.GOLD + "manual" + 
						ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.WHITE + "to change your setting to " + ChatColor.GOLD + "manual");
			}
		}
		else
		{
			Player player = this.plugin.getServer().getPlayer(playerName);
			this.safeSendMessage(player, ChatColor.GOLD + "Your " +
					ChatColor.WHITE + "manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			this.safeSendMessage(sender, ChatColor.GOLD + playerName +
					ChatColor.WHITE + "'s manual performance level for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
			
			if(mobInfo.getSetting() != Setting.MANUAL)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + playerName +
						ChatColor.WHITE + "'s setting for " + ChatColor.GOLD + mobType.getName() + 
						ChatColor.WHITE + " is not set to " + ChatColor.GOLD + "manual" + 
						ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic changesetting " + 
						ChatColor.WHITE + "to change " + ChatColor.GOLD + playerName +
						ChatColor.WHITE + "'s setting to " + ChatColor.GOLD + "manual");
			}
		}
	}
}