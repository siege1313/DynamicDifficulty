package com.cjmcguire.bukkit.dynamic.commands.scale;

import org.bukkit.ChatColor;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;

/**
 * This class falls under the "/dynamic" command. The "/dynamic 
 * scaledefense" command will set whether or not a mob's defense 
 * should be scaled or not, given that the command was entered 
 * correctly and the sender has permission to use this command.
 * @author CJ McGuire
 */
public class ScaleDefenseCommand extends AbstractScaleCommand
{
	/**
	 * The name of this command. ("scaledefense)
	 */
	public static final String NAME = "scaledefense";
	
	private static final int SELF_ARGS_LENGTH = 3;
	private static final int OTHER_ARGS_LENGTH = 4;

	/**
	 * The permission node to allow access to target yourself with 
	 * the command.
	 */
	public static final String SELF_PERMISSION = "dynamic.scaledefense.self";
	
	/**
	 * The permission node to allow access to target others with the 
	 * command.
	 */
	public static final String OTHER_PERMISSION = "dynamic.scaledefense.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to turn mob defense scaling on or off for yourself";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to turn mob defense scaling on or off for others";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to turn mob defense scaling on or off for yourself";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic scaledefense <mob-name | 'all'> <'true' | 'false'> [player-name]";

	private static final String ATTRIBUTE_NAME = "defense";
	
	/**
	 * Initializes this Command.
	 */
	public ScaleDefenseCommand()
	{
		super(SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
			  SELF_PERMISSION, OTHER_PERMISSION,
			  SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
			  SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE,
			  ATTRIBUTE_NAME);
	}

	@Override
	protected void setScaleAttribute(MobInfo mobInfo, boolean value) 
	{
		mobInfo.setScaleDefense(value);		
	}
}