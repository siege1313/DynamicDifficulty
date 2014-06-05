package com.cjmcguire.bukkit.dynamic.commands.core;

import com.cjmcguire.bukkit.dynamic.commands.AbstractPlayerTargetableCommand;

/**
 * An abstract class for a command that is used to change the value
 * of an attribute.
 * @author CJ McGuire
 */
public abstract class AbstractChangeCommand  extends AbstractPlayerTargetableCommand
{
	
	/**
	 * Initializes this Command.
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
	 */
	public AbstractChangeCommand( 
			int selfArgsLength, int otherArgsLength, 
			String selfPermission, String otherPermission,
			String selfDenyPermissionMessage, String otherDenyPermissionMessage, 
			String selfDenyConsoleMessage, String incorrectArgsMessage) 
	{
		super(selfArgsLength, otherArgsLength, 
			  selfPermission, otherPermission,
			  selfDenyPermissionMessage, otherDenyPermissionMessage,
			  selfDenyConsoleMessage, incorrectArgsMessage);
	}
}
