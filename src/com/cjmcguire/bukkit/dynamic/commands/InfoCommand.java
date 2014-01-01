package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;

/*
 * THE COMMENTED OUT CODE IN THIS CLASS WAS PART OF AN ATTEMPT TO
 * LINE UP THE PRINTED OUT STATEMENTS INTO COLUMNS (SO ALL OF THE
 * SETTTINGS WOULD BE IN ONE COLUMN, ETC.). UNFORTUNATELY, IT ONLY
 * WORKED UNDER THE DEFAULT MINECRAFT RESOURCE PACK. OTHER RESOURCE
 * PACKS HAVE DIFFERENT FONTS, THUS DIFFERENT FONT SIZES, SO THE
 * CALCULATIONS FOR LINEING UP THE PRINT OUTS INTO COLUMNS NO LONGER
 * WORKED. I LEFT IT HERE BECAUSE IT WAS CLEVER CODE AND I DON'T
 * WANT TO DELETE IT PERMINENTLY.
 */

/**
 * This class is used with the /dynamic info command. The 
 * /dynamic info command prints out a message listing a
 * player's setting, auto performance level, and manual performance level
 * for each mob.
 * @author CJ McGuire
 */
public class InfoCommand extends PlayerTargetableCommand
{
	
	private static final int SELF_ARGS_LENGTH = 1;
	private static final int OTHER_ARGS_LENGTH = 2;

	private static final String SELF_PERMISSION = "dynamic.info.self";
	private static final String OTHER_PERMISSION = "dynamic.info.other";
	
	private static final String SELF_DENY_PERMISSION_MESSAGE = "You do not have permission to view your own DynamicDifficulty info";
	private static final String OTHER_DENY_PERMISSION_MESSAGE = "You do not have permission to view other players' DynamicDifficulty info";
	
	private static final String SELF_DENY_CONSOLE_MESSAGE = "You must be a player to view your own DynamicDifficulty info";
	
	private static final String INCORRECT_ARGS_MESSAGE = "Incorrect number of arguments. Use the command like this: " +
			ChatColor.GOLD + "/dynamic info [player-name]";
	
	
	//private HashMap<Character, Integer> fontPixelTable;
	
	//private int WITHER_SKELETON_PIXEL_LENGTH;

	//private int DISABLED_PIXEL_LENGTH;
	
	/**
	 * The name of this command. ("info")
	 */
	public static final String NAME = "info";
	
	/**
	 * Initializes this InfoCommand.
	 * @param plugin a reference to the DynamicDifficulty plugin
	 */
	public InfoCommand(DynamicDifficulty plugin)
	{
		super(plugin,
				SELF_ARGS_LENGTH, OTHER_ARGS_LENGTH,
				SELF_PERMISSION, OTHER_PERMISSION,
				SELF_DENY_PERMISSION_MESSAGE, OTHER_DENY_PERMISSION_MESSAGE,
				SELF_DENY_CONSOLE_MESSAGE, INCORRECT_ARGS_MESSAGE);
		//this.initFontPixelTable();
		//WITHER_SKELETON_PIXEL_LENGTH = this.getTotalPixelLength(MobType.WITHER_SKELETON.getName());
		//DISABLED_PIXEL_LENGTH = this.getTotalPixelLength(Setting.DISABLED.getName());
		
	}
	
	/*private void initFontPixelTable()
	{
		fontPixelTable = new HashMap<Character, Integer>();
		fontPixelTable.put('a', 5);
		fontPixelTable.put('b', 5);
		fontPixelTable.put('c', 5);
		fontPixelTable.put('d', 5);
		fontPixelTable.put('e', 5);
		fontPixelTable.put('f', 4);
		fontPixelTable.put('g', 5);
		fontPixelTable.put('h', 5);
		fontPixelTable.put('i', 1);
		fontPixelTable.put('j', 5);
		fontPixelTable.put('k', 4);
		fontPixelTable.put('l', 2);
		fontPixelTable.put('m', 5);
		fontPixelTable.put('n', 5);
		fontPixelTable.put('o', 5);
		fontPixelTable.put('p', 5);
		fontPixelTable.put('q', 5);
		fontPixelTable.put('r', 5);
		fontPixelTable.put('s', 5);
		fontPixelTable.put('t', 3);
		fontPixelTable.put('u', 5);
		fontPixelTable.put('v', 5);
		fontPixelTable.put('w', 5);
		fontPixelTable.put('x', 5);
		fontPixelTable.put('y', 5);
		fontPixelTable.put('z', 5);
	}*/
	
	/*private int getTotalPixelLength(String string)
	{
		int totalPixelLength = 0;
		
		int size = string.length();
		for(int looper = 0; looper < size; looper++)
		{
			char c = string.charAt(looper);
			int pixelLength = fontPixelTable.get(c) + 1;
			totalPixelLength += pixelLength;
		}
		
		return totalPixelLength;
	}*/
	
	/*private String getPadding(String string, int maxPixelLength)
	{
		int totalPixelLength = this.getTotalPixelLength(string);
		
		int numberOfSpaces = (int) ((maxPixelLength - totalPixelLength)/4.0 + .5);
		
		String padding = "";
		for(int looper = 0; looper < numberOfSpaces; looper++)
		{
			padding += " "; // space is 4 pixels wide
		}
		
		return padding;
	}*/
	
	/**
	 * Prints out the DynamicDifficulty settings and performance levels for a player.
	 * @param sender the sender of the command
	 * @param args must contain "info" in args[0]. Optionally args[1] can contain a 
	 * player's name
	 * @return true because this method will always print out the info as intended
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String [] args)
	{
		if(!this.getPlugin().isRunningWithHead() || this.senderIsThePlayer(sender, playerName))
		{
			this.safeSendMessage(sender, ChatColor.LIGHT_PURPLE + "Your DynamicDifficulty Settings and Performance Levels:");
		}
		else
		{
			this.safeSendMessage(sender, ChatColor.LIGHT_PURPLE + "DynamicDifficulty Settings and Performance Levels for " + playerName + ":");
		}
		
		// for each mobType
		for(MobType mobType: MobType.values())
		{
			MobInfo mobInfo = this.getPlugin().getPlayersMobInfo(playerName, mobType);
			
			// get the mobName and pad it with spaces
			String mobName = mobType.getName();
			//String paddedMobName = this.getPadding(mobName,WITHER_SKELETON_PIXEL_LENGTH) + mobName;
			
			// get the setting name and pad it with spaces
			String settingName = mobInfo.getSetting().getName();
			//String paddedSettingName = this.getPadding(settingName,DISABLED_PIXEL_LENGTH) + settingName;
			
			//get the auto and manual performance levels as integers
			int autoPerformanceLevel = (int)(mobInfo.getAutoPerformanceLevel() + 0.5);
			int manualPerformanceLevel = (int)(mobInfo.getManualPerformanceLevel() + 0.5);
			
			// figure out if auto should be padded
			/*String autoPadding = "";
			if(autoPerformanceLevel < 100)
			{
				autoPadding = " ";
			}*/

			// figure out if auto should be padded
			/*String manualPadding = "";
			if(manualPerformanceLevel < 100)
			{
				manualPadding = " ";
			}*/
			
			// Print out a message like this example:
			// "zombie - Setting: auto - Auto Performance Level: 120 - Manual Performance Level: 100"
			/*this.safeSendMessage(sender, ChatColor.GOLD + paddedMobName + 
					ChatColor.WHITE + " - Setting: " + ChatColor.GOLD + paddedSettingName + 
					ChatColor.WHITE + " - Auto: " + ChatColor.GOLD + autoPadding + autoPerformanceLevel +
					ChatColor.WHITE + " - Manual: " + ChatColor.GOLD + manualPadding + manualPerformanceLevel);*/
			
			this.safeSendMessage(sender, ChatColor.GOLD + mobName + 
					ChatColor.WHITE + ChatColor.BOLD + " - Setting:" + ChatColor.RESET + ChatColor.GOLD + settingName + 
					ChatColor.WHITE + ChatColor.BOLD +  " - Auto:" + ChatColor.RESET + ChatColor.GOLD + autoPerformanceLevel +
					ChatColor.WHITE + ChatColor.BOLD + " - Manual:" + ChatColor.RESET + ChatColor.GOLD +  manualPerformanceLevel);
		}
		
		return true;
	}
}