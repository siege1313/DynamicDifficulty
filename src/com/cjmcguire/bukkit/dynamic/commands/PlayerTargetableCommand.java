package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * This abstract command sets a outline for commands that can optionally 
 * target another player.
 * @author CJ McGuire
 */
public abstract class PlayerTargetableCommand extends AbstractDDCommand 
{
	private int selfArgsLength;
	private int otherArgsLength;

	private String selfPermission;
	private String otherPermission;
	
	private String selfDenyPermissionMessage;
	private String otherDenyPermissionMessage;
	
	private String selfDenyConsoleMessage;

	private String incorrectArgsMessage;
	
	/**
	 * Initializes this DynamicDifficulty Command.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 * @param selfArgsLength the number of arguments for when the command targets 
	 * the sender
	 * @param otherArgsLength the number of arguments for when the command targets 
	 * another player
	 * @param selfPermission the permission to target self
	 * @param otherPermission the permission to target another player
	 * @param selfDenyPermissionMessage the message to send when the sender does 
	 * not have permission to target self
	 * @param otherDenyPermissionMessage the message to send when the sender does 
	 * not have permission to target another player
	 * @param selfDenyConsoleMessage the message to send when the console tries to 
	 * target self
	 * @param incorrectArgsMessage the message to send when the arguments have an 
	 * incorrect length
	 */
	public PlayerTargetableCommand(DynamicDifficulty plugin,
			int selfArgsLength,
			int otherArgsLength,
			String selfPermission,
			String otherPermission,
			String selfDenyPermissionMessage,
			String otherDenyPermissionMessage,
			String selfDenyConsoleMessage,
			String incorrectArgsMessage)
	{
		super(plugin);
		
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
     * Executes this command. This method is part of the template method pattern.
     * It handles figuring out who sent the command, who the command is targetting,
     * and if the sender has permission or not.
	 * 
	 * 1. If the sender is not a player and it is asking about another player, then 
	 * the method will work as intended.
	 * 
	 * 2. If the sender is not a player and it is asking about itself, then 
	 * the method will work because the console is not a player.
	 * 
	 * 
	 * 3. If the sender is a player, and is asking about another player, and has 
	 * permission to do so, then the method will work as intended.
	 * 
	 * 4. If the sender is a player, and is asking about another player, and does
	 * not have permission to do so, then the method will not work as intended.
	 * 
	 * 
	 * 5. If the sender is a player, and is asking about itself, and has 
	 * permission to do so, then the method will work as intended.
	 * 
	 * 6. If the sender is a player, and is asking about itseld, and does
	 * not have permission to do so, then the method will not work as intended.
	 * 
	 * 
	 * 7. If the sender put in the incorrect number of arguments, then the 
	 * method will not work as intended.
	 * 
	 * 
	 * 8. If the sender is asking for another player who does not exist, then 
	 * this method will not work as intended.
	 * 
	 * @param sender the sender of the command
	 * @param args must contain the command name in args[0]
	 * @return true if the command worked as intended. false if it did not.
	 */
	@Override
	protected boolean executeCommand(CommandSender sender, String [] args)
	{
		boolean workedAsIntended = false;
		
		// if no player-name is given
		if(args.length == selfArgsLength)
		{
			// if the sender is a player
			if(sender instanceof Player)
			{
				// if the player has permission
				if(sender.hasPermission(selfPermission))
				{
					Player player = (Player) sender;
					String playerName = player.getDisplayName();
					workedAsIntended = this.commandAction(sender, playerName, args);
				}
				// if the player does not have permission
				else
				{
					this.safeSendMessage(sender, selfDenyPermissionMessage);
				}
			}
			// if the sender is not a player
			else
			{
				this.safeSendMessage(sender, selfDenyConsoleMessage);
			}
		}
		// if a player-name is given
		else if(args.length == otherArgsLength)
		{
			// if the sender is a player
			if(sender instanceof Player)
			{
				// if the player has permission
				if(sender.hasPermission(otherPermission))
				{
					workedAsIntended = this.otherPlayerAction(sender, args);
				}
				// if the player does not have permission
				else
				{
					this.safeSendMessage(sender, otherDenyPermissionMessage);
				}
			}
			// if the sender is not a player
			else
			{
				workedAsIntended = this.otherPlayerAction(sender, args);
			}
		}
		// if the number of arguments is wrong
		else
		{
			this.safeSendMessage(sender, incorrectArgsMessage);
		}
		
		return workedAsIntended;
	}
	
	private boolean otherPlayerAction(CommandSender sender, String [] args)
	{
		boolean workedAsIntended = false;
		
		String otherPlayerName = args[otherArgsLength-1];
		if(this.getPlugin().playerInfoExists(otherPlayerName))
		{
			workedAsIntended = this.commandAction(sender, otherPlayerName, args);
		}
		else
		{
			this.safeSendMessage(sender, ChatColor.GOLD + otherPlayerName + ChatColor.WHITE + " is not a logged in player");
		}
		
		return workedAsIntended;
		
	}
	
	/**
	 * @param sender the sender of the command
	 * @param playerName the name of a player
	 * @return true if the sender is the player. false otherwise
	 */
	protected boolean senderIsThePlayer(CommandSender sender, String playerName)
	{
		Player player = this.getPlugin().getServer().getPlayer(playerName);
		if(player == sender)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected abstract boolean commandAction(CommandSender sender, String playerName, String[] args);
}
