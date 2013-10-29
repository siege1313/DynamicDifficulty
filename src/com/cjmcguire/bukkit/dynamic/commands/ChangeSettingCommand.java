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
public class ChangeSettingCommand extends AbstractDDCommand
{
	/**
	 * The name of this command. ("change setting")
	 */
	public static final String NAME = "change setting";
	
	/**
	 * Initializes this DynamicCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public ChangeSettingCommand(DynamicDifficulty plugin)
	{
		super(plugin);
	}
	
	/**
	 * Changes the setting for the player with the given playerName given that
	 * the parameters contain valid info. Several things could go wrong when 
	 * trying to change a player's setting. First, the setting is not a valid
	 * setting. Second, the mob name is not a valid mob name. Third, the player
	 * may not have permission to use the command.
	 * @param sender the sender of the command
	 * @param playerName the name of the player
	 * @param args args[2] must contain the mob name and args[3] must contain
	 * the setting to change to
	 * @return true if the setting change was valid. false if the setting change
	 * could not be completed
	 */
	protected boolean commandAction(CommandSender sender, String playerName, String[] args)
	{
		boolean valid = false;

		// if the sender has permission to use this command
		if(sender.hasPermission("dynamic.changesetting"))
		{
			String mobName = args[2];
			String settingName = args[3];
			
			MobType mobType = MobType.getMobType(mobName);
			Setting setting = Setting.getSetting(settingName);
			
			// if the mobName was "all"
			if(setting != null && mobName.equals("all"))
			{
				this.changeAllSettings(sender, playerName, setting);
				valid = true;
			}
			// if setting was valid and mobType was valid
			else if(setting != null && mobType != null)
			{
				this.changeMobSetting(sender, playerName, setting, mobType);
				valid = true;
			}		
			//if setting was invalid and mobType was invalid
			else if(setting == null && mobType == null)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name, and " + 
						ChatColor.GOLD + settingName + ChatColor.WHITE + " is not a valid setting");
			}
			// if only the setting was invalid 
			else if(setting == null && mobType != null)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + settingName + ChatColor.WHITE + " is not a valid setting");
			}
			// if only the mobType was invalid
			else if(setting != null && mobType == null)
			{
				this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name");
			}
		}
		else
		{
			this.safeSendMessage(sender, "You do not have permission to use this command");
		}
		
		return valid;
	}
	
	private void changeAllSettings(CommandSender sender, String playerName, Setting setting)
	{
		PlayerInfo playerInfo = this.getPlugin().getPlayerInfo(playerName);
		
		for(MobType mob: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mob);
			mobInfo.setSetting(setting);
		}
		
		this.safeSendMessage(sender, "Your setting for " + ChatColor.GOLD + "every mob" + 
				ChatColor.WHITE + " was changed to " + ChatColor.GOLD + setting.getName());
		
	}
	
	private void changeMobSetting(CommandSender sender, String playerName, Setting setting, MobType mobType)
	{
		PlayerInfo playerInfo = this.getPlugin().getPlayerInfo(playerName);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		mobInfo.setSetting(setting);
		
		this.safeSendMessage(sender, "Your setting for " + ChatColor.GOLD + mobType.getName() + 
				ChatColor.WHITE + " was changed to " + ChatColor.GOLD + setting.getName());
		
	}
}