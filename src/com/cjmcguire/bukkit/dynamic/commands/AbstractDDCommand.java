package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * An abstract DynamicDifficulty Command. 
 * @author CJ McGuire
 */
public abstract class AbstractDDCommand
{
	private final DynamicDifficulty plugin;
	
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
	 * Executes this DynamicDifficulty command.
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true if the command worked as intended. false if it did not.
	 */
	protected abstract boolean executeCommand(CommandSender sender, String[] args);
	
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
}
