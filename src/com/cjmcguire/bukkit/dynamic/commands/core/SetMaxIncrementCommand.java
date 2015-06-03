package com.cjmcguire.bukkit.dynamic.commands.core;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * This class falls under the "/dynamic" command. The "/dynamic 
 * setmaxincrement" command will change the maximum amount that a 
 * player's auto performance level can increment by in a single 
 * update, given that the command was entered correctly and the 
 * sender has permission to use this command.
 * @author CJ McGuire
 */
public class SetMaxIncrementCommand extends AbstractChangeCommand
{
	/**
	 * The name of this command. ("setmaxincrement")
	 */
	public static final String NAME = "setmaxincrement";
	
	private static final String ALL_MOBS = "all";
	
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final String SELF_PERMISSION = "dynamic.setmaxincrement.self";
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final String OTHER_PERMISSION = "dynamic.setmaxincrement.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to change your own max increment amounts";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to change other players' max increment amounts";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to change your own max increment amounts";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic setmaxincrement <mob-name | 'all'> <positive integer number> [player-name]";

	/**
	 * Initializes this Command.
	 * @param server - the server to get players from
	 */
	public SetMaxIncrementCommand(Server server)
	{
		super(server, 
			  SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
	}

	/**
	 * Changes the maximum amount that a player's auto performance 
	 * level can increment by in a single update for the given 
	 * playerName given that the parameters contain valid info. 
	 * Several things could go wrong when trying to change a player's 
	 * max increment. 
	 * 
	 * First, the max increment is not a number. 
	 * Second, the mob name is not a valid mob name. 
	 * 
	 * If any of these conditions occur, the method will not change 
	 * the max increment and will inform the sender of the error.
	 * 
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player to target
	 * @param args the command arguments. 
	 * args[0] must contain "setmaxincrement". 
	 * args[1] must contain the mob name 
	 * args[2] must contain the max increment number to change to. 
	 * args[3] can contain a player's name, optionally.
	 * @return true if the player's max increment level was changed. 
	 * false if the max increment change could not be completed
	 */
	@Override
	public boolean commandAction(CommandSender sender, UUID playerID, String[] args)
	{
		boolean incrementChanged = false;

		try
		{
			String mobName = args[1];
			MobType mobType = MobType.getMobType(mobName);

			int maxIncrement = Integer.parseInt(args[2]);
			
			if(mobName.equals(ALL_MOBS))	// if the player want to change all performance levels
			{
				this.changeAllMaxIncrements(sender, playerID, maxIncrement);
				incrementChanged = true;
			}
			else if(mobType != null)	// if the player wants to change their performance level for only one mob
			{
				this.changeMobMaxIncrement(sender, playerID, maxIncrement, mobType);
				incrementChanged = true;
			}
			else	// if the player entered an invalid mob name
			{
				sender.sendMessage(ChatColor.GOLD + mobName + ChatColor.RESET + " is not a valid mob name");
			}
		}
		catch(NumberFormatException e)
		{
			// catch the error if the user enters a performance level 
			// that is not a number
			sender.sendMessage(ChatColor.GOLD + args[2] + ChatColor.RESET + " is not an integer number");
		}

		return incrementChanged;
	}

	private void changeAllMaxIncrements(CommandSender sender, UUID playerID, int maxIncrement)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);

		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);

			mobInfo.setMaxIncrement(maxIncrement);
		}

		int actualMaxIncrement = playerInfo.getMobInfo(MobType.ZOMBIE).getMaxIncrement();

		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "max increments for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualMaxIncrement);
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "max increment for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualMaxIncrement);
			
			sender.sendMessage(ChatColor.GOLD + playerName +
					ChatColor.RESET + "'s max increments for " + ChatColor.GOLD + "all mobs" + 
					ChatColor.RESET + " were changed to " + ChatColor.GOLD + actualMaxIncrement);
		}
	}

	private void changeMobMaxIncrement(CommandSender sender, UUID playerID, int maxIncrement, MobType mobType)
	{
		PlayerInfo playerInfo = this.playerDataManager.getPlayerInfo(playerID);

		MobInfo mobInfo = playerInfo.getMobInfo(mobType);

		mobInfo.setMaxIncrement(maxIncrement);

		int actualMaxIncrement = mobInfo.getMaxIncrement();
		
		if(this.senderIsThePlayer(sender, playerID))
		{
			sender.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "max increment for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualMaxIncrement);
		}
		else
		{
			Player player = this.server.getPlayer(playerID);
			String playerName = player.getName();
			
			player.sendMessage(ChatColor.GOLD + "Your " +
					ChatColor.RESET + "max increment for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualMaxIncrement);
			
			sender.sendMessage(ChatColor.GOLD + playerName +
					ChatColor.RESET + "'s max increment for " + ChatColor.GOLD + mobType.getName() + 
					ChatColor.RESET + " was changed to " + ChatColor.GOLD + actualMaxIncrement);
		}
	}
}