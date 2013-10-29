package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * An abstract DynamicDifficulty Command. 
 * @author CJ McGuire
 */
public abstract class AbstractDDCommand
{
	private DynamicDifficulty plugin;
	
	/**
	 * Initializes the DynamicDifficulty Command.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public AbstractDDCommand(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
	}
	
	protected DynamicDifficulty getPlugin()
	{
		return plugin;
	}
	
	/**
	 * Sends the given message to the sender. If the plugin is running with its
	 * head, then the message gets sent to the sender's screen. If the plugin is
	 * running without its head, then this method calls System.out.println(message)
	 * This method is safe to use regardless of whether the plugin is running with 
	 * its head or without its head.
	 * @param sender who to send the message to
	 * @param message the message to send
	 * @return true if the message was sent to the sender's screen. false if the 
	 * message was sent through System.out.println().
	 */
	public boolean safeSendMessage(CommandSender sender, String message)
	{
		if(plugin.isRunningWithHead())
		{
			//send message to sender
			sender.sendMessage(message);
		}
		else
		{
			// print out the summary message
			System.out.println(message);
		}
		
		return plugin.isRunningWithHead();
	}
	
	/**
	 * Executes this DynamicDifficulty command. The sender must be a player
	 * for this command to execute as intended. If the sender is not a player,
	 * then this method will send a message to the sender telling it that
	 * it must be a player. 
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true if the command was valid (the sender was a player and everything
	 * else worked), false if not valid (the sender was not a player or something
	 * else went wrong)
	 */
	public boolean executeCommand(CommandSender sender, String[] args) 
	{
		boolean validCommand = false;
		
		// the sender must be a player
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			String playerName = player.getDisplayName();
			
			this.commandAction(sender, playerName, args);
			
			validCommand = true;
		}
		else
		{
			this.safeSendMessage(sender, "You must be a player to use this command!");
		}
		
		return validCommand;
	}
	
	/**
	 * Performs the command's action. This method is part of the template 
	 * method pattern and is used in the executeCommand() method.
	 * @param sender the sender of the command
	 * @param playerName the name of the player
	 * @param args the command arguments
	 * @return true if the command action worked as intended. false if the 
	 * command action did not work as intended
	 */
	protected abstract boolean commandAction(CommandSender sender, String playerName, String [] args);
}
