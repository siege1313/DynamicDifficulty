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
public class ChangeSettingCommand extends PlayerTargetableCommand
{
	/**
	 * The name of this command. ("changesetting")
	 */
	public static final String NAME = "changesetting";
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	private static final String SELF_PERMISSION = "dynamic.changesetting.self";
	private static final String OTHER_PERMISSION = "dynamic.changesetting.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to change your own DynamicDifficulty settings";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to change other players' DynamicDifficulty settings";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to change your own DynamicDifficulty settings";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic changesetting <mob-name | 'all'> <'auto' | 'manual' | 'disabled'> [player-name]";
	
	/**
	 * Initializes this DynamicDifficulty Command.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public ChangeSettingCommand(DynamicDifficulty plugin)
	{
		super(plugin,
				SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
				SELF_PERMISSION, OTHER_PERMISSION,
				SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
				SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}
	
	/**
	 * Changes the setting for the player with the given playerName 
	 * given that the parameters contain valid info. Several things 
	 * could go wrong when trying to change a player's setting. First, 
	 * the setting is not a valid setting. Second, the mob name is not 
	 * a valid mob name.
	 * @param sender the sender of the command
	 * @param args must contain "changesetting" in args[0], args[1]
	 * must contain the mob name and args[2] must contain the setting 
	 * to change to. Optionally, args[3] can contain a player's name
	 * @return true if the player's setting was changed. false if the 
	 * setting change could not be completed
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String [] args)
	{
		boolean valid = false;

		String mobName = args[1];
		String settingName = args[2];
			
		MobType mobType = MobType.getMobType(mobName);
		Setting setting = Setting.getSetting(settingName);
			
		if(setting != null && mobName.equals("all"))	// if the mobName was "all"
		{
			this.changeAllSettings(sender, playerName, setting);
			valid = true;
		}
		else if(setting != null && mobType != null)		// if setting was valid and mobType was valid
		{
			this.changeMobSetting(sender, playerName, setting, mobType);
			valid = true;
		}
		else if(setting == null && mobType == null)		// if setting was invalid and mobType was invalid
		{
			this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name, and " + 
					ChatColor.GOLD + settingName + ChatColor.WHITE + " is not a valid setting");
		}
		else if(setting == null && mobType != null)		// if only the setting was invalid 
		{
			this.safeSendMessage(sender, ChatColor.GOLD + settingName + ChatColor.WHITE + " is not a valid setting");
		}
		else if(setting != null && mobType == null)		// if only the mobType was invalid
		{
			this.safeSendMessage(sender, ChatColor.GOLD + mobName + ChatColor.WHITE + " is not a valid mob name");
		}

		return valid;
	}
	
	private void changeAllSettings(CommandSender sender, String playerName, Setting setting)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerName);
		
		for(MobType mob: MobType.values())
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mob);
			mobInfo.setSetting(setting);
		}
		
		if(!this.isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.GOLD + "Your " + 
					ChatColor.WHITE + "settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + setting.getName());
		}
		else
		{
			Player player = this.plugin.getServer().getPlayer(playerName);
			this.safeSendMessage(player, ChatColor.GOLD + "Your " + 
					ChatColor.WHITE + "settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + setting.getName());
			
			this.safeSendMessage(sender, ChatColor.GOLD + playerName + 
					ChatColor.WHITE + "'s settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.WHITE + " were changed to " + ChatColor.GOLD + setting.getName());
		}
	}
	
	private void changeMobSetting(CommandSender sender, String playerName, Setting setting, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerName);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		mobInfo.setSetting(setting);
		
		if(!this.isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.GOLD + "Your " +
					ChatColor.WHITE + "setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + setting.getName());
			
		}
		else
		{
			Player player = this.plugin.getServer().getPlayer(playerName);
			this.safeSendMessage(player, ChatColor.GOLD + "Your " + 
					ChatColor.WHITE + "setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + setting.getName());
			
			this.safeSendMessage(sender, ChatColor.GOLD + playerName + 
					ChatColor.WHITE + "'s setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.WHITE + " was changed to " + ChatColor.GOLD + setting.getName());
		}
	}
}