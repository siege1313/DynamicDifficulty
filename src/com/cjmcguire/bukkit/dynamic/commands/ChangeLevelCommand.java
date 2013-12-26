package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.Setting;

/**
 * This class is used with the /dynamic command. The /dynamic command 
 * prints out a message describing what the DynamicDifficulty plugin does.
 * @author CJ McGuire
 */
public class ChangeLevelCommand extends AbstractDDCommand
{
	/**
	 * The name of this command. ("change level")
	 */
	public static final String NAME = "change level";

	/**
	 * Initializes this DynamicCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public ChangeLevelCommand(DynamicDifficulty plugin)
	{
		super(plugin);
	}

	/**
	 * Changes the level for the player with the given playerName given that
	 * the parameters contain valid info. Several things could go wrong when 
	 * trying to change a player's performance level. First, the performance level
	 * is not a number. Second, the mob name is not a valid mob name. Third,
	 * the player's setting is not set to Manual. Fourth, the player may not
	 * have permission to use the command. If any of these conditions
	 * occur, the method will not change the performance level and will inform
	 * the sender of the error.
	 * @param sender the sender of the command
	 * @param playerName the name of the player
	 * @param args args[2] must contain the mob name and args[3] must contain
	 * the new performance level to change to
	 * @return true if the player's performance level was changed. false if the 
	 * level change could not be completed
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String[] args)
	{
		boolean levelChanged = false;
		
		// if the sender has permission to use this command
		if(sender.hasPermission("dynamic.changelevel"))
		{
			try
			{
				double performanceLevel = Integer.parseInt(args[3]);
				
				String mobName = args[2];
				
				MobType mobType = MobType.getMobType(mobName);
				
				// if the player want to change all performance levels
				if(mobName.equals("all"))
				{
					this.changeAllPerformanceLevels(sender, playerName, performanceLevel);
					levelChanged = true;
				}
				// if the player wants to change their performance level for only one mob
				else if(mobType != null)
				{
					
					this.changeMobPerformanceLevel(sender, playerName, performanceLevel, mobType);
					levelChanged = true;
				}
				// if the player entered an invalid mob name
				else
				{
					this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name");
				}
			}
			// catch the error if the user enters a performance level that is not a number
			catch(NumberFormatException e)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + args[3] + ChatColor.WHITE + " is not an integer number");
			}
		}
		else
		{
			this.safeSendMessage(sender, "You do not have permission to use this command");
		}
		
		
		return levelChanged;
	}
	
	private void changeAllPerformanceLevels(CommandSender sender, String playerName, double performanceLevel)
	{
		PlayerInfo playerInfo = this.getPlugin().getPlayerInfo(playerName);
		
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
			
		this.safeSendMessage(sender, "Your manual performance level for " + ChatColor.GOLD + "every mob" + 
				ChatColor.WHITE + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
		
		if(oneWasAutoOrDisabled)
		{
			this.safeSendMessage(sender, "At least one of your settings is not set to " +
					ChatColor.GOLD + "manual" + ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic change setting " + 
					ChatColor.WHITE + "to change your setting to " + ChatColor.GOLD + "manual");
		}
	}
	
	private void changeMobPerformanceLevel(CommandSender sender, String playerName, double performanceLevel, MobType mobType)
	{
		PlayerInfo playerInfo = this.getPlugin().getPlayerInfo(playerName);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		mobInfo.setManualPerformanceLevel(performanceLevel);
		
		int actualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
			
		this.safeSendMessage(sender, "Your manual performance level for " + ChatColor.GOLD + mobType.getName() + 
				ChatColor.WHITE + " was changed to " + ChatColor.GOLD + actualPerformanceLevel);
		
		if(mobInfo.getSetting() != Setting.MANUAL)
		{
			this.safeSendMessage(sender, "Your setting for " + ChatColor.GOLD + mobType.getName() + ChatColor.WHITE + " is not set to " +
					ChatColor.GOLD + "manual" + ChatColor.WHITE + ". Use " + ChatColor.GOLD + "/dynamic change setting " + 
					ChatColor.WHITE + "to change your setting to " + ChatColor.GOLD + "manual");
		}
	}
}