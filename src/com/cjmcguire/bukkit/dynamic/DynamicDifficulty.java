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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
	
	private FileConfiguration defaultPlayerConfig = null;
	
	/**
	 * Starts the DynamicDifficulty plugin.
	 */
	@Override
	public void onEnable()
	{
		if(this.isRunningWithHead())
		{
			// set up the monitor
			this.getServer().getPluginManager().registerEvents(new MonitorListener(this), this);
			
			// set up the controller
			this.getServer().getPluginManager().registerEvents(new ControllerListener(this), this);
			
			// set up the analyzer
			AnalyzerTask analyzer = new AnalyzerTask(this);
			analyzer.runTaskTimer(this, 0, HALF_MINUTE);
			
			// register the commands
			this.getCommand(DynamicCommandExecutor.NAME).setExecutor(new DynamicCommandExecutor(this));
			
			// load the default player config
			InputStream defaultPlayerYmlStream = this.getResource("player.yml");
			defaultPlayerConfig = YamlConfiguration.loadConfiguration(defaultPlayerYmlStream);
			
			// if a reload occurs, load the PlayerInfo for any players that
			// are currently logged in
			this.loadDataForLoggedInPlayers();
		}
		else
		{
			File playerFile = new File("player.yml");
			defaultPlayerConfig = YamlConfiguration.loadConfiguration(playerFile);
		}
	}
	
	private void loadDataForLoggedInPlayers()
	{
		Player [] players = this.getServer().getOnlinePlayers();
		
		for(Player player: players)
		{
			String playerName = player.getDisplayName();
			
			// get the playerName.yml file from disk
			File playerFile = new File(this.getDataFolder(), playerName + ".yml");
			// create the FileConfiguration object based on the playerFile.yml
			FileConfiguration playerConfig = this.loadPlayerConfig(playerFile, playerName);

			// load the playerData contained in the FileConfiguration into the plugin
			this.loadPlayerInfo(playerConfig, playerName);
		}
	}
	
	/**
	 * Closes the DynamicDifficulty plugin.
	 */
	@Override
	public void onDisable()
	{
		this.saveAllPlayerInfo();
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
	 * @return this plugin's default player.yml file config.
	 */
	public FileConfiguration getDefaultPlayerConfig()
	{
		return defaultPlayerConfig;
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
	 * Saves all of the playerData contained in this plugin to the
	 * player files in the folder specified
	 * @param dataFolder the folder containing all the player.yml files
	 */
	private void saveAllPlayerInfo()
	{
		// translate playerData into a Collection because you can't loop through a HashMap
		Collection<PlayerInfo> playerCollection = playerData.values();
		
		// for each playerInfo
		for(PlayerInfo playerInfo: playerCollection)
		{
			String playerName = playerInfo.getPlayerName();
			
			// get the playerName.yml file from disk
			File playerFile;
			if(isRunningWithHead())
			{
				playerFile = new File(this.getDataFolder(), playerName + ".yml");
			}
			else
			{
				playerFile = new File(playerName + ".yml");
			}
			
			// create the FileConfiguration object based on the playerFile.yml
			FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(playerFile);
			
			// save key variables from PlayerInfo to the player.yml file
			this.savePlayerInfo(playerConfig, playerFile, playerName);
		}
	}
	
	/**
	 * If the playerName.yml file exists, then this method will load it from disk
	 * and return a FileConfiguration based on it. If the player name.yml file 
	 * does not exist, then this method will create a default playerName.yml, save
	 * it to disk, and return a FileConfiguration based on it.
	 * @param playerFile the file that is the playerName.yml file on disk
	 * @param playerName the name of the player whose playerName.yml file you want
	 * @return a FileConfiguration based of the playerName.yml file
	 */
	public FileConfiguration loadPlayerConfig(File playerFile, String playerName)
	{
		FileConfiguration playerConfig = null;
		
		// if the File exists
		if(playerFile.exists())
		{
			this.safeLogInfo(playerName + ".yml exists. Loaded it");
			// load the YAML config based on it
			playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		}
		else
		{
			this.safeLogInfo(playerName + ".yml does not exist. Creating it");
			// load the YAML config based on the default player.yml File
			playerConfig = YamlConfiguration.loadConfiguration(this.getResource("player.yml"));

			try
			{
				// save a blank file so that is exists
				playerConfig.save(playerFile);
				// save the default values to the FileConfiguration
				playerConfig.setDefaults(this.getDefaultPlayerConfig());
				// overwrite the blank file with the file containing the defaults
				playerConfig.save(playerFile);
			}
			catch (IOException e)
			{
				this.safeLogInfo("Could not save config to " + playerFile);
			}
		}

		return playerConfig;
	}
	
	/**
	 * Save the player data from the plugin to disk in the playerName.yml 
	 * file and removes it from the plugin.
	 * @param playerConfig the FileConfiguration containing the player data
	 * @param playerFile the File that is the playerName.yml
	 * @param playerName the name of the player whose player data you want 
	 * to save to disk
	 */
	public void savePlayerInfo(FileConfiguration playerConfig, File playerFile, String playerName)
	{
		this.safeLogInfo("Saving " + playerName + "'s player data to " + playerName + ".yml");
		
		// get the PlayerInfo
		PlayerInfo playerInfo = this.getPlayerInfo(playerName);
		
		// loop through each MobType
		for(MobType mobType: MobType.values())
		{
			// get the MobInfo
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			
			// save the setting for the MobType in the player.yml file
			Setting setting = mobInfo.getSetting();
			String settingName = setting.getName();
			playerConfig.set(mobType.getName() + ".setting", settingName);
			
			// save the manualPerformanceLevel for the MobType in the player.yml file
			int manualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
			playerConfig.set(mobType.getName() + ".manualPerformanceLevel", manualPerformanceLevel);

			// save the autoPerformanceLevel for the MobType in the player.yml file
			int autoPerformanceLevel = (int) (mobInfo.getCurrentPerformanceLevel()+.5);
			playerConfig.set(mobType.getName() + ".autoPerformanceLevel", autoPerformanceLevel);
		}
		
		try 
		{
			playerConfig.save(playerFile);
		}
		catch (IOException e)
		{
			this.safeLogInfo("Could not save player data to " + playerFile);
			e.printStackTrace();
		}
		
		// remove the PlayerInfo from the plugin
		this.removePlayerInfo(playerName);
	}
	
	/**
	 * Loads the player data from the playerYmlFileConfig to the plugin's player data
	 * so that it is now loaded in memory.
	 * @param playerConfig the FileConfiguration containing the player data
	 * @param playerName the name of the player whose player data you want to load into
	 * the plugin's memory
	 */
	public void loadPlayerInfo(FileConfiguration playerConfig, String playerName)
	{
		// create the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		// loop through each MobType
		for(MobType mobType: MobType.values())
		{
			// get the MobType
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.loadMobInfo(mobInfo, playerConfig);
		}
		
		// add the playerInfo to the plugin
		this.addPlayerInfo(playerInfo);
	}
	
	private void loadMobInfo(MobInfo mobInfo, FileConfiguration playerConfig)
	{
		// we set the defaults to the defaultPlayerConfig so that if the 
		// playerConfig contains garbage data, we can still load the MobInfo
		playerConfig.setDefaults(defaultPlayerConfig);
		
		// set the setting in mobInfo
		String settingName = playerConfig.getString(mobInfo.getMobType().getName() + ".setting");
		Setting setting = Setting.getSetting(settingName);
		mobInfo.setSetting(setting);
		
		// set the manualPerformanceLevel in the MobInfo
		int manualPerformanceLevel = playerConfig.getInt(mobInfo.getMobType().getName() + ".manualPerformanceLevel");
		mobInfo.setManualPerformanceLevel(manualPerformanceLevel);
	
		// set the currentPerformanceLevel in the MobInfo
		int autoPerformanceLevel = playerConfig.getInt(mobInfo.getMobType().getName() + ".autoPerformanceLevel");
		mobInfo.setCurrentPerformanceLevel(autoPerformanceLevel);
		// the estimated performance level should start off equal to the current performance level
		mobInfo.setEstimatedPerformanceLevel(autoPerformanceLevel);
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
