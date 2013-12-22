/*
 * "DynamicDifficulty" is a plugin that implements dynamic difficulty 
 * on a Minecraft Bukkit Server.
 * Copyright (C) 2013  C.J. McGuire
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.cjmcguire.bukkit.dynamic;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;

import com.cjmcguire.bukkit.dynamic.analyzer.AnalyzerTask;
import com.cjmcguire.bukkit.dynamic.commands.DynamicCommandExecutor;
import com.cjmcguire.bukkit.dynamic.controller.ControllerListener;
import com.cjmcguire.bukkit.dynamic.monitor.MonitorListener;

/**
 * DynamicDifficulty core Bukkit plugin class. This plugin implements 
 * dynamic difficulty on a Minecraft Bukkit Server.
 * @author CJ McGuire
 */
public final class DynamicDifficulty extends JavaPlugin
{
	private final static long TICKS_PER_MINUTE = 1200;
	private final static long HALF_MINUTE = TICKS_PER_MINUTE/2;
	
	// This line of code is here for when testing
	// private final static String TEXT_LABEL = ChatColor.GREEN + "[DynamicDifficulty] ";
	
	private boolean runningWithHead = true;
	
	private HashMap<String, PlayerInfo> playerData = new HashMap<String, PlayerInfo>();
	
	private PlayerFileHandler fileHandler;
	
	/**
	 * Starts the DynamicDifficulty plugin.
	 */
	@Override
	public void onEnable()
	{
		fileHandler = new PlayerFileHandler(this);
		
		if(this.isRunningWithHead())
		{
			this.getServer().getPluginManager().registerEvents(fileHandler, this);
			
			// set up the monitor
			this.getServer().getPluginManager().registerEvents(new MonitorListener(this), this);
			
			// set up the controller
			this.getServer().getPluginManager().registerEvents(new ControllerListener(this), this);
			
			// set up the analyzer
			AnalyzerTask analyzer = new AnalyzerTask(this);
			analyzer.runTaskTimer(this, 0, HALF_MINUTE);
			
			// register the commands
			this.getCommand(DynamicCommandExecutor.NAME).setExecutor(new DynamicCommandExecutor(this));
				
			// if a reload occurs, load the PlayerInfo for any players that
			// are currently logged in
			fileHandler.reloadInfoForLoggedInPlayers();
		}
	}
	
	/**
	 * Closes the DynamicDifficulty plugin.
	 */
	@Override
	public void onDisable()
	{
		fileHandler.saveAllPlayerData();
		playerData.clear();
	}
	
	/**
	 * @return true if this pluggin is running with its head, false if
	 * it running headless.
	 */
	public boolean isRunningWithHead()
	{
		return runningWithHead;
	}
	
	/**
	 * Sets this plugin to run with its head or run headless.
	 * @param runningWithHead true if you want the plugin to run with its 
	 * head, false if you want it to run without its head
	 */
	public void setRunningWithHead(boolean runningWithHead)
	{
		this.runningWithHead = runningWithHead;
	}
	
	/**
	 * @return a Collection containing the PlayerInfo for all players
	 * currently logged in.
	 */
	public Collection<PlayerInfo> getPlayerData()
	{
		return playerData.values();
	}
	
	/**
	 * Adds the given player info to this plugin.
	 * @param playerInfo the PlayerInfo you want to add to this plugin's player data
	 * @return the previous value associated with playerName, or null if there was 
	 * no mapping for key. (A null return can also indicate that the map previously 
	 * associated null with key.)
	 */
	public PlayerInfo addPlayerInfo(PlayerInfo playerInfo) 
	{
		return playerData.put(playerInfo.getPlayerName(), playerInfo);
	}
	
	/**
	 * Removes a player's player info.
	 * @param playerName the name of the player whose PlayerInfo
	 * you want to remove
	 * @return the previous value associated with playerName, or null if 
	 * there was no mapping for key. (A null return can also indicate that 
	 * the map previously associated null with key.)
	 */
	public PlayerInfo removePlayerInfo(String playerName) 
	{
		return playerData.remove(playerName);
	}
	
	/**
	 * Gets a player's player info.
	 * @param playerName the name of the player whose PlayerInfo
	 * you want to get
	 * @return the PlayerInfo of the player with the given player name or 
	 * null if this plugin contains no PlayerInfo for the given player name
	 */
	public PlayerInfo getPlayerInfo(String playerName) 
	{
		return playerData.get(playerName);
	}

	/**
	 * Gets a player's MobInfo for a particular player and mob
	 * @param playerName the name of the player whose MobInfo you want the get
	 * @param mobType the MobType of the MobInfo that you want the get
	 * @return the MobInfo for the given MobType and playerName
	 */
	public MobInfo getPlayersMobInfo(String playerName, MobType mobType)
	{
		// get the player's PlayerInfo
		PlayerInfo playerInfo = this.getPlayerInfo(playerName);
		// get the PlayerInfo's MobData
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		return mobInfo;
	}

	/**
	 * @return this plugin's PlayerFileHandler
	 */
	protected PlayerFileHandler getPlayerFileHandler()
	{
		return fileHandler;
	}
	
	/**
	 * If this plugin is running with its head, then this method will send the message to
	 * this plugin's Logger and log the message. If this plugin is running without its head,
	 * then this method will simply call System.out.println(message). This method is the
	 * advised way to log a message to the Logger.
	 * @param message the message you want to log (or println if this plugin is running headless)
	 * @return true if this plugin printed the message on the Logger, false if it printed to
	 * System.out.println().
	 */
	public boolean safeLogInfo(String message)
	{
		if(this.isRunningWithHead())
		{
			// This line of code is here for when testing
			//Bukkit.getConsoleSender().sendMessage(TEXT_LABEL + message);
			
			this.getLogger().info(message);
		}
		else
		{
			System.out.println(message);
		}
		
		return this.isRunningWithHead();
	}
}
