package com.cjmcguire.bukkit.dynamic.commands.core;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * This class falls under the "/dynamic" command. The "/dynamic 
 * changesetting" command will change the setting for a given player, 
 * given that the command was entered correctly and the sender has 
 * permission to use this command.
 * @author CJ McGuire
 */
public class ChangeSettingCommand extends AbstractChangeCommand
{
	/**
	 * The name of this command. ("changesetting")
	 */
	public static final String NAME = "changesetting";
	
	private static final String ALL_MOBS = "all";
	
	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final int SELF_ARGS_LENGTH = 3;
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final int OTHER_ARGS_LENGTH = 4;

	private static final String SELF_PERMISSION = "dynamic.changesetting.self";
	private static final String OTHER_PERMISSION = "dynamic.changesetting.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to change your own DynamicDifficulty settings";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to change other players' DynamicDifficulty settings";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to change your own DynamicDifficulty settings";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic changesetting <mob-name | 'all'> <'auto' | 'manual' | 'disabled'> [player-name]";
	
	/**
	 * Initializes this Command.
	 */
	public ChangeSettingCommand()
	{
		super(SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}
	
	/**
	 * Changes the setting for the player with the given playerName 
	 * given that the parameters contain valid info. Several things 
	 * could go wrong when trying to change a player's setting. 
	 * 
	 * First, the setting is not a valid setting. 
	 * Second, the mob name is not a valid mob name.
	 * 
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player to target
	 * @param args the command arguments. 
	 * args[0] must contain "changesetting".
	 * args[1] must contain the mob name.
	 * args[2] must contain the setting. 
	 * args[3] can contain a player's name, optionally.
	 * @return true if the player's setting was changed. false if the 
	 * setting change could not be completed
	 */
	@Override
	public boolean commandAction(CommandSender sender, UUID playerID, String [] args)
	{
		boolean valid = false;

		String mobName = args[1];
		String settingName = args[2];
			
		MobType mobType = MobType.getMobType(mobName);
		Setting setting = Setting.getSetting(settingName);
			
		if(setting != null && mobName.equals(ALL_MOBS))	// if the mobName was "all"
		{
			this.changeAllSettings(sender, playerID, setting);
			valid = true;
		}
		else if(setting != null && mobType != null)		// if setting was valid and mobType was valid
		{
			this.changeMobSetting(sender, playerID, setting, mobType);
			valid = true;
		}
		else if(setting == null && this.invalidMob(mobType, mobName))		// if setting was invalid and mobType was invalid
		{
			sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name, and " + 
					ChatColor.GOLD + settingName + ChatColor.RESET + " is not a valid setting");
		}
		else if(setting == null && !this.invalidMob(mobType, mobName))		// if only the setting was invalid 
		{
			sender.sendMessage(ChatColor.GOLD + settingName + ChatColor.RESET + " is not a valid setting");
		}
		else if(setting != null && this.invalidMob(mobType, mobName))		// if only the mobType was invalid
		{
			sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name");
		}

		return valid;
	}
	
	private boolean invalidMob(MobType mobType, String mobName)
	{
		if(mobName.equals(ALL_MOBS))
		{
			return false;
		}
		else if(mobType != null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private void changeAllSettings(CommandSender sender, UUID playerID, Setting setting)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);
		
		
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			mobInfo.setSetting(setting);
		}
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
			
			sender.sendMessage(ChatColor.GOLD + playerName + 
					ChatColor.RESET + "'s settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
		}
	}
	
	private void changeMobSetting(CommandSender sender, UUID playerID, Setting setting, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		mobInfo.setSetting(setting);
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
			
			sender.sendMessage(ChatColor.GOLD + playerName + 
					ChatColor.RESET + "'s setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
		}
	}
}