package com.cjmcguire.bukkit.dynamic.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * An abstract DynamicDifficulty Command.
 * @author CJ McGuire
 */
public abstract class AbstractDDCommand
{
	protected final PlayerDataManager playerDataManager;
	
	protected Server server;
	
	/**
	 * Initializes the AbstractDDCommand.
	 */
	public AbstractDDCommand()
	{	
		this.playerDataManager = PlayerDataManager.getInstance();

		this.server = Bukkit.getServer();
	}
	
	/**
	 * Sets this command's server equal to the given server.
	 * @param server the server this command is to use
	 */
	public void setServer(Server server)
	{
		this.server = server;
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
	 * Executes this command.
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true if the command worked as intended. false if it did 
	 * not work as intended.
	 */
	protected abstract boolean executeCommand(CommandSender sender, String[] args);
}
