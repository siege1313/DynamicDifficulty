package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.command.CommandSender;

/**
 * An abstract DynamicDifficulty Command.
 * @author CJ McGuire
 */
public interface DDCommand
{
	/**
	 * Executes this command.
	 * @param sender source of the command
	 * @param args the command arguments 
	 * @return true if the command worked as intended. false if it did 
	 * not work as intended.
	 */
	public boolean executeCommand(CommandSender sender, String[] args);
}
