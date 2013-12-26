package com.cjmcguire.bukkit.dynamic.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.MobInfo;
import com.cjmcguire.bukkit.dynamic.MobType;
import com.cjmcguire.bukkit.dynamic.PlayerInfo;

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
public class InfoCommand extends AbstractDDCommand
{
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
		super(plugin);
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
	 * Sends the settings, auto performance levels, and manual performance levels for 
	 * each mob to the player with the given name. This command will only 
	 * send this info if the player has permission to use this command.
	 * @param sender the sender of the command
	 * @param playerName the name of the player
	 * @param args not used
	 * @return true if the player had permission to use this command, 
	 * false the sender did not have permission.
	 */
	@Override
	protected boolean commandAction(CommandSender sender, String playerName, String [] args)
	{
		boolean validCommand = false;

		// if the sender has permission to use this command
		if(sender.hasPermission("dynamic.info"))
		{
			validCommand = true;
			
			this.safeSendMessage(sender, ChatColor.LIGHT_PURPLE + "Your Dynamic Difficulty Settings and Performance Levels:");
			
			PlayerInfo playerInfo = this.getPlugin().getPlayerInfo(playerName);
			
			// for each mobType
			for(MobType mobType: MobType.values())
			{
				this.printMobInfo(sender, playerInfo, mobType);
			}
		}
		else
		{
			this.safeSendMessage(sender, "You do not have permission to use this command");
		}
		
		return validCommand;
	}
	
	private void printMobInfo(CommandSender sender, PlayerInfo playerInfo, MobType mobType)
	{
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
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
}