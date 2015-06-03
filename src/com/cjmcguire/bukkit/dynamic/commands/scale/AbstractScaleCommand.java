package com.cjmcguire.bukkit.dynamic.commands.scale;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.commands.PlayerTargetableCommand;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * This abstract command lays out the basic setup for player 
 * targetable commands that are used to toggle a setting between 
 * true and false.
 * @author CJ McGuire
 */
public abstract class AbstractScaleCommand extends PlayerTargetableCommand
{
	private String attributeName;
	
	/**
	 * Initializes this Command.
	 * @param server - the server to get players from
	 * @param selfArgsLength the number of arguments for when the 
	 * command targets the sender.
	 * @param otherArgsLength the number of arguments for when the 
	 * command targets another player.
	 * @param selfPermission the permission to target self
	 * @param otherPermission the permission to target another player
	 * @param selfDenyPermissionMessage the message to send when the 
	 * sender does not have permission to target self.
	 * @param otherDenyPermissionMessage the message to send when the 
	 * sender does not have permission to target another player.
	 * @param selfDenyConsoleMessage the message to send when the 
	 * console tries to target self.
	 * @param incorrectArgsMessage the message to send when the 
	 * arguments have an incorrect length.
	 * @param attributeName the name of the atrribute to scale
	 */
	public AbstractScaleCommand(Server server,  
			int selfArgsLength, int otherArgsLength, 
			String selfPermission, String otherPermission,
			String selfDenyPermissionMessage, String otherDenyPermissionMessage, 
			String selfDenyConsoleMessage, String incorrectArgsMessage, 
			String attributeName) 
	{
		super(server, 
			  selfArgsLength, otherArgsLength, 
			  selfPermission, otherPermission,
			  selfDenyPermissionMessage, otherDenyPermissionMessage,
			  selfDenyConsoleMessage, incorrectArgsMessage);
		
		this.attributeName = attributeName;
	}
	
	/**
	 * Changes whether or not one of the mob's attribute should be 
	 * scaled or not for the player with the given playerName given 
	 * that the parameters contain valid info. Several things could 
	 * go wrong when trying to change a player's setting. 
	 * 
	 * First, the boolean value is not valid. 
	 * Second, the mob name is not a valid mob name.
	 * 
	 * @param sender the sender of the command
	 * @param args the command arguments. 
	 * args[0] must contain the command name.
	 * args[1] must contain the mob name.
	 * args[2] must contain "true" or "false". 
	 * args[3] can contain a player's name, optionally.
	 * @return true if the scale value was changed. false if the 
	 * value change could not be completed.
	 */
	@Override
	public boolean commandAction(CommandSender sender, UUID playerID, String [] args)
	{
		boolean valid = false;

		String mobName = args[1];
		String settingName = args[2];
			
		MobType mobType = MobType.getMobType(mobName);
		BooleanSetting setting = BooleanSetting.getBooleanSetting(settingName);
		
		if(setting != null && mobName.equals("all"))	// if the mobName was "all"
		{
			this.changeAllBooleanSettings(sender, playerID, setting);
			valid = true;
		}
		else if(setting != null && mobType != null)		// if setting was valid and mobType was valid
		{
			this.changeMobBooleanSetting(sender, playerID, setting, mobType);
			valid = true;
		}
		else if(setting == null && mobType == null)		// if setting was invalid and mobType was invalid
		{
			sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name, and " + 
					ChatColor.GOLD + settingName + ChatColor.RESET + " is not a valid setting");
		}
		else if(setting == null && mobType != null)		// if only the setting was invalid 
		{
			sender.sendMessage(ChatColor.GOLD + settingName + ChatColor.RESET + " is not a valid setting");
		}
		else if(setting != null && mobType == null)		// if only the mobType was invalid
		{
			sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name");
		}

		return valid;
	}
	
	/**
	 * Changes the setting for a particular attribute for all mob 
	 * types. This method uses the Template Method pattern.
	 * @param sender the sender of the command.
	 * @param playerID the UUID of the player to target
	 * @param setting the BooleanSetting to change to
	 */
	private void changeAllBooleanSettings(CommandSender sender, UUID playerID, BooleanSetting setting)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);
		
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.setScaleAttribute(mobInfo, setting.getValue());
		}
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "scale " + attributeName + " settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "scale " + attributeName + " settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
			
			sender.sendMessage(ChatColor.GOLD + playerName + 
					ChatColor.RESET + "'s scale attack settings for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + setting.getName());
		}
	}
	
	/**
	 * Changes the setting for a particular attribute for the given 
	 * mob type. This method uses the Template Method pattern.
	 * @param sender the sender of the command.
	 * @param playerID the UUID of the player to target
	 * @param setting the BooleanSetting to change to
	 * @param mobType the type of mob to change the setting for
	 */
	private void changeMobBooleanSetting(CommandSender sender, UUID playerID, BooleanSetting setting, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);

		this.setScaleAttribute(mobInfo, setting.getValue());
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "scale " + attributeName + " setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " + 
					ChatColor.RESET + "scale " + attributeName + " setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
			
			sender.sendMessage(ChatColor.GOLD + playerName + 
					ChatColor.RESET + "'s scale " + attributeName + " setting for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + setting.getName());
		}
	}
	
	protected abstract void setScaleAttribute(MobInfo mobInfo, boolean value);
}
