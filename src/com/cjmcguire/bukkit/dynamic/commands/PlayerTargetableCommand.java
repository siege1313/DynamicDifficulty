package com.cjmcguire.bukkit.dynamic.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * This abstract command lays out the basic setup for commands that 
 * can optionally target another player.
 * @author CJ McGuire
 */
public abstract class PlayerTargetableCommand implements DDCommand 
{
	protected Server server;
	
	protected final PlayerDataManager playerDataManager;
	
	private int selfArgsLength;
	private int otherArgsLength;

	private String selfPermission;
	private String otherPermission;
	
	private String selfDenyPermissionMessage;
	private String otherDenyPermissionMessage;
	
	private String selfDenyConsoleMessage;

	private String incorrectArgsMessage;
	
	/**
	 * Initializes this Command.
	 * @param server - the server to get players from
	 * @param selfArgsLength - the number of arguments for when the 
	 * command targets the sender.
	 * @param otherArgsLength - the number of arguments for when the 
	 * command targets another player.
	 * @param selfPermission - the permission to target self
	 * @param otherPermission - the permission to target another player
	 * @param selfDenyPermissionMessage - the message to send when the 
	 * sender does not have permission to target self.
	 * @param otherDenyPermissionMessage - the message to send when the 
	 * sender does not have permission to target another player.
	 * @param selfDenyConsoleMessage - the message to send when the 
	 * console tries to target self.
	 * @param incorrectArgsMessage - the message to send when the 
	 * arguments have an incorrect length.
	 */
	public PlayerTargetableCommand(Server server,
			int selfArgsLength,
			int otherArgsLength,
			String selfPermission,
			String otherPermission,
			String selfDenyPermissionMessage,
			String otherDenyPermissionMessage,
			String selfDenyConsoleMessage,
			String incorrectArgsMessage)
	{
		super();

		this.server = server;
		
		this.playerDataManager = PlayerDataManager.getInstance();
		
		this.selfArgsLength = selfArgsLength;
		this.otherArgsLength = otherArgsLength;
		
		this.selfPermission = selfPermission;
		this.otherPermission = otherPermission;
		
		this.selfDenyPermissionMessage = selfDenyPermissionMessage;
		this.otherDenyPermissionMessage = otherDenyPermissionMessage;
		
		this.selfDenyConsoleMessage = selfDenyConsoleMessage;
		
		this.incorrectArgsMessage = incorrectArgsMessage;
	}
	
	/**
	 * @param playerName the name of the player whose UUID you want
	 * to get
	 * @return the UUID of the Player with the given player name or 
	 * null if no player with that name is logged in on the server
	 */
	protected UUID getUUIDFromPlayerName(String playerName)
	{
		Player player = server.getPlayerExact(playerName);
		
//		console.sendMessage(player.toString());
		UUID playerID = null;
		
		if(player != null)
		{
			playerID = player.getUniqueId();
//			console.sendMessage(""+playerID);
		}
		
		return playerID;
	}
	
	/**
     * Executes this command. This method is part of the template 
     * method pattern. It handles figuring out who sent the command, 
     * who the command is targetting, and if the sender has permission 
     * or not.
	 * 
	 * 1. If the sender is not a player and it is asking about another 
	 * player, then the method will work as intended.
	 * 
	 * 2. If the sender is not a player and it is asking about itself, 
	 * then the method will work because the console is not a player.
	 * 
	 * 3. If the sender is a player, and is asking about another 
	 * player, and has permission to do so, then the method will work 
	 * as intended.
	 * 
	 * 4. If the sender is a player, and is asking about another 
	 * player, and does not have permission to do so, then the method 
	 * will not work as intended.
	 * 
	 * 5. If the sender is a player, and is asking about itself, and 
	 * has permission to do so, then the method will work as intended.
	 * 
	 * 6. If the sender is a player, and is asking about itseld, and 
	 * does not have permission to do so, then the method will not 
	 * work as intended.
	 * 
	 * 7. If the sender put in the incorrect number of arguments, then 
	 * the method will not work as intended.
	 * 
	 * 8. If the sender is asking for another player who does not 
	 * exist, then this method will not work as intended.
	 * 
	 * @param sender the sender of the command
	 * @param args must contain the command name in args[0]
	 * @return true if the command worked as intended. false if it did
	 * not.
	 */
	@Override
	public boolean executeCommand(CommandSender sender, String [] args)
	{
		boolean workedAsIntended = false;
		
		// If no player-name is given
		if(args.length == selfArgsLength)
		{
			// If the sender is a player
			if(sender instanceof Player)
			{
				workedAsIntended = this.playerAskForSelf((Player)sender, args);
			}
			else // the sender is not a player
			{
				sender.sendMessage(selfDenyConsoleMessage);
			}
		}
		// if a player-name is given
		else if(args.length == otherArgsLength)
		{
			// if the sender is a player
			if(sender instanceof Player)
			{
				if(this.isAskingForSelf((Player)sender, args))
				{
					workedAsIntended = this.playerAskForSelf((Player)sender, args);
				}
				// if the player has permission
				else if(sender.hasPermission(otherPermission))
				{
					workedAsIntended = this.askForOther(sender, args);
				}
				else // the player does not have permission
				{
					sender.sendMessage(otherDenyPermissionMessage);
				}
			}
			else // the sender is not a player
			{
				workedAsIntended = this.askForOther(sender, args);
			}
		}
		else // the number of arguments is wrong
		{
			sender.sendMessage(incorrectArgsMessage);
		}
		
		return workedAsIntended;
	}
	
	private boolean playerAskForSelf(Player player, String [] args)
	{
		boolean workedAsIntended = false;
		
		// if the player has permission
		if(player.hasPermission(selfPermission))
		{
			UUID playerID = player.getUniqueId();
			workedAsIntended = this.commandAction(player, playerID, args);
		}
		else // if the player does not have permission
		{
			player.sendMessage(selfDenyPermissionMessage);
		}
		
		return workedAsIntended;
	}
	
	private boolean askForOther(CommandSender sender, String [] args)
	{
		boolean workedAsIntended = false;
		
		String otherPlayerName = args[otherArgsLength-1];
		
		UUID playerID = this.getUUIDFromPlayerName(otherPlayerName);
		
		if(playerID != null && this.playerDataManager.playerInfoExists(playerID))
		{
			workedAsIntended = this.commandAction(sender, playerID, args);
		}
		else
		{
			sender.sendMessage(ChatColor.GOLD + otherPlayerName + ChatColor.RESET + " is not a logged in player");
		}
		
		return workedAsIntended;
	}
	
	private boolean isAskingForSelf(Player player, String [] args)
	{
		String otherPlayerName = args[otherArgsLength-1];
		String playerName = player.getName();
		if(playerName.equals(otherPlayerName))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * This checks whether the Player with the given playerName is
	 * the CommandSender. If this command is running headless, then 
	 * the player is assumed to be the sender.
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player
	 * @return true if the sender is the player or if the command is
	 * running headless. false otherwise
	 */
	protected boolean senderIsThePlayer(CommandSender sender, UUID playerID)
	{
		boolean senderIsThePlayer = false;
		
		Player player = this.server.getPlayer(playerID);
		if(player == sender)
		{
			senderIsThePlayer = true;
		}
		
		return senderIsThePlayer;
	}
	
	/**
	 * 
	 * @param sender the sender of the command
	 * @param playerID the UUID of the player to target
	 * @param args the command arguments
	 * @return true if the command worked as intended. false if it
	 * did not.
	 */
	public abstract boolean commandAction(CommandSender sender, UUID playerID, String[] args);
}
