package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * An abstract DynamicDifficulty Command. 
 * @author CJ McGuire
 */
public abstract class AbstractDDCommand
{
	protected final DynamicDifficulty plugin;
	
	protected final PlayerDataManager playerDataManager;
	
	private boolean runningWithHead;
	
	/**
	 * Initializes the DynamicDifficulty Command.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public AbstractDDCommand(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
		
		this.playerDataManager = PlayerDataManager.getInstance();
		
		if(this.plugin != null)
		{
			runningWithHead = true;
		}
		else
		{
			runningWithHead = false;
		}
	}
	
	protected boolean isRunningWithHead()
	{
		return runningWithHead;
	}

	/**
	 * Executes this DynamicDifficulty command.
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true if the command worked as intended. false if it did 
	 * not work as intended.
	 */
	protected abstract boolean executeCommand(CommandSender sender, String[] args);
	
	/**
	 * Sends the given message to the sender. If the plugin is running 
	 * with its head, then the message gets sent to the sender's screen.
	 * If the plugin is running without its head, then this method 
	 * calls System.out.println(message). This method is safe to use 
	 * regardless of whether the plugin is running with its head or 
	 * without its head.
	 * @param sender who to send the message to
	 * @param message the message to send
	 * @return true if the message was sent to the sender's screen. 
	 * false if the message was sent through System.out.println().
	 */
	protected boolean safeSendMessage(CommandSender sender, String message)
	{
		if(runningWithHead)
		{
			sender.sendMessage(message);
		}
		else
		{
			System.out.println(message);
		}
		
		return runningWithHead;
	}
}
