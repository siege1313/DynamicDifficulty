package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.ChatColor;
import org.bukkit.Server;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * This class falls under the "/dynamic" command. The "/dynamic 
 * scaleknockback" command will set whether or not a mob's knockback  
 * should be scaled or not, given that the command was entered 
 * correctly and the sender has permission to use this command.
 * @author CJ McGuire
 */
public class ScaleKnockbackCommand extends AbstractScaleCommand
{
	/**
	 * The name of this command. ("scaleknockback")
	 */
	public static final String NAME = "scaleknockback";
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final String SELF_PERMISSION = "dynamic.scaleknockback.self";
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final String OTHER_PERMISSION = "dynamic.scaleknockback.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to turn mob knockback scaling on or off for yourself";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to turn mob knockback scaling on or off for others";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to turn mob knockback scaling on or off for yourself";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic scaleknockback <mob-name | 'all'> <'true' | 'false'> [player-name]";
	
	private static final String ATTRIBUTE_NAME = "knockback";
	
	/**
	 * Initializes this Command.
	 * @param server - the server to get players from
	 */
	public ScaleKnockbackCommand(Server server)
	{
		super(server,
			  SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE,
			  ATTRIBUTE_NAME);
	}

	@Override
	protected void setScaleAttribute(MobInfo mobInfo, boolean value)
	{
		mobInfo.setScaleKnockbackResistance(value);
	}
}