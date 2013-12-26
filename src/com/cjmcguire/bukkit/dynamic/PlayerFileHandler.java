package com.cjmcguire.bukkit.dynamic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * The PlayerFileHandler is responsible for handling all of the i/o 
 * that goes on with the <player name>.yml files. It handles the conversion
 * from a "player name" to "player file" to "player config" to "player data".
 * @author CJ McGuire
 */
public class PlayerFileHandler implements Listener
{
	private final static String DEFAULT_PLAYER_FILE_NAME = "default_player.yml";
	private final static String PLAYERS_FOLDER = "players";
	
	private final DynamicDifficulty plugin;
	
	/**
	 * Initializes the PlayerFileHandler.
	 * @param plugin a reference to the DynamicDifficulty plugin that uses 
	 * this PlayerFileHandler
	 */
	protected PlayerFileHandler(DynamicDifficulty plugin)
	{
		this.plugin = plugin;
	}
	
	/**
	 * Creates the default_player.yml file in the plugin data folder if it does not exist.
	 */
	protected void createDefaultPlayerFile()
	{
		File dataDefaultFile = new File(plugin.getDataFolder(), DEFAULT_PLAYER_FILE_NAME);
		
		if(!dataDefaultFile.exists())
		{
			InputStream jarDefaultFile = plugin.getResource(DEFAULT_PLAYER_FILE_NAME);
			FileConfiguration defaultPlayerConfig = YamlConfiguration.loadConfiguration(jarDefaultFile);
			
			try 
			{
				defaultPlayerConfig.save(dataDefaultFile);
			} 
			catch (IOException e) 
			{
				plugin.safeLogInfo("Could not create default_player.yml");
			}
		}
	}
	
	/**
	 * Creates the players folder if it does not exist.
	 */
	protected void createPlayersFolder()
	{
		File playersFolder = new File(plugin.getDataFolder(), PLAYERS_FOLDER);
		if(!playersFolder.exists())
		{
			plugin.safeLogInfo("players FOLDER DOES NOT EXIST");
			playersFolder.mkdirs();
		}
	}
	
	/**
	 * This method triggers whenever a player logs onto a Minecraft Bukkit Server.
	 * If no player data exists for the player that just logged in, this method
	 * initializes player data for that player. If player data does exist, this method
	 * will load in the player data from the player's config.yml file
	 * @param event the PlayerLoginEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent event)
	{
		// get the player
		Player player = event.getPlayer();
		// get the player's name
		String playerName = player.getDisplayName();

		plugin.safeLogInfo(playerName + " LOGGED IN");
		// load the playerData contained in the FileConfiguration into the plugin
		this.loadPlayerDataFromFile(playerName);
	}

	/**
	 * This method triggers whenever a player logs out of a Minecraft Bukkit Server.
	 * It will clear temporary variables from the player's player data and will save
	 * permanent variables.
	 * @param event the PlayerQuitEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogout(PlayerQuitEvent event)
	{
		// get the player
		Player player = event.getPlayer();
		// get the player's name
		String playerName = player.getDisplayName();

		plugin.safeLogInfo(playerName + " LOGGED OUT");
		
		// save key variables from PlayerInfo to the player.yml file
		this.savePlayerDataToFile(playerName);

		// remove the PlayerInfo from the plugin
		plugin.removePlayerInfo(playerName);
	}
	
	/**
	 * If the plugin is not running with its head, it will return the 
	 * "default_player.yml" file from the src folder. 
	 * 
	 * If the plugin is running with its head and the "default_player.yml" 
	 * file exists in the plugin's data folder, then it will load that file. 
	 * 
	 * If the plugin is running with its head and the "default_player.yml" 
	 * does not exist in the plugin's data folder, then it will load the 
	 * default file from the plugin jar and create the file in the data
	 * folder.
	 * 
	 * @return the default config for a <player name>.yml file
	 */
	protected FileConfiguration getDefaultPlayerConfig()
	{	
		FileConfiguration defaultPlayerConfig = null;
		
		if(plugin.isRunningWithHead())
		{
			File dataDefaultFile = new File(plugin.getDataFolder(), DEFAULT_PLAYER_FILE_NAME);
			
			if(dataDefaultFile.exists())
			{
				defaultPlayerConfig = YamlConfiguration.loadConfiguration(dataDefaultFile);
				
				// fix dataDefaultFile if it has garbage data.
				InputStream jarDefaultFile = plugin.getResource(DEFAULT_PLAYER_FILE_NAME);
				FileConfiguration tempConfig = YamlConfiguration.loadConfiguration(jarDefaultFile);
				defaultPlayerConfig.setDefaults(tempConfig);
			}
			else
			{
				InputStream jarDefaultFile = plugin.getResource(DEFAULT_PLAYER_FILE_NAME);
				defaultPlayerConfig = YamlConfiguration.loadConfiguration(jarDefaultFile);
				
				try 
				{
					defaultPlayerConfig.save(dataDefaultFile);
				} 
				catch (IOException e) 
				{
					plugin.safeLogInfo("Could not create default_player.yml");
				}
			}
		}
		else
		{
			File srcDefaultFile = new File(DEFAULT_PLAYER_FILE_NAME);
			defaultPlayerConfig = YamlConfiguration.loadConfiguration(srcDefaultFile);
		}
		
		return defaultPlayerConfig;
	}
	
	/**
	 * Loads the info from the <player name>.yml file to the plugin's player data.
	 * @param playerName the name of the player whose player info you want to load into
	 * the plugin's memory
	 */
	protected void loadPlayerDataFromFile(String playerName)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.loadPlayerConfig(playerName);
		
		// we set the defaults to the defaultPlayerConfig so that if the 
		// playerConfig contains garbage data, we can still load the MobInfo
		playerConfig.setDefaults(this.getDefaultPlayerConfig());
				
		// create the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(playerName);
		
		// loop through each MobType
		for(MobType mobType: MobType.values())
		{
			// get the MobType
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.loadMobInfoFromConfig(mobInfo, playerConfig);
		}
		
		// add the playerInfo to the plugin
		plugin.addPlayerInfo(playerInfo);
	}
	
	private void loadMobInfoFromConfig(MobInfo mobInfo, FileConfiguration playerConfig)
	{
		// set the setting in mobInfo
		String settingName = playerConfig.getString(mobInfo.getMobType().getName() + ".setting");
		Setting setting = Setting.getSetting(settingName);
		mobInfo.setSetting(setting);
		
		// set the manualPerformanceLevel in the MobInfo
		int manualPerformanceLevel = playerConfig.getInt(mobInfo.getMobType().getName() + ".manualPerformanceLevel");
		mobInfo.setManualPerformanceLevel(manualPerformanceLevel);
	
		// set the currentPerformanceLevel in the MobInfo
		int autoPerformanceLevel = playerConfig.getInt(mobInfo.getMobType().getName() + ".autoPerformanceLevel");
		mobInfo.setAutoPerformanceLevel(autoPerformanceLevel);
		// the estimated performance level should start off equal to the current performance level
		mobInfo.setEstimatedPerformanceLevel(autoPerformanceLevel);
	}
	
	/**
	 * Save the info from the plugin's PlayerData to the <player name>.yml file 
	 * file. It does not delete it from the plugins' PlayerData.
	 * @param playerName the name of the player whose player data you want 
	 * to save to disk
	 */
	protected void savePlayerDataToFile(String playerName)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.loadPlayerConfig(playerName);
		
		// get the PlayerInfo
		PlayerInfo playerInfo = plugin.getPlayerInfo(playerName);
		
		// loop through each MobType
		for(MobType mobType: MobType.values())
		{
			// get the MobInfo
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.saveMobInfoToConfig(mobInfo, playerConfig);
		}
		
		File playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator + playerName + ".yml");
			
		try 
		{
			playerConfig.save(playerFile);
		}
		catch (IOException e)
		{
			plugin.safeLogInfo("Could not save player data to " + PLAYERS_FOLDER + playerFile);
			e.printStackTrace();
		}
		
	}
	
	private void saveMobInfoToConfig(MobInfo mobInfo, FileConfiguration playerConfig)
	{
		// save the setting for the MobType in the player.yml file
		Setting setting = mobInfo.getSetting();
		String settingName = setting.getName();
		playerConfig.set(mobInfo.getMobType().getName() + ".setting", settingName);
		
		// save the manualPerformanceLevel for the MobType in the player.yml file
		int manualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
		playerConfig.set(mobInfo.getMobType().getName() + ".manualPerformanceLevel", manualPerformanceLevel);

		// save the autoPerformanceLevel for the MobType in the player.yml file
		int autoPerformanceLevel = (int) (mobInfo.getAutoPerformanceLevel()+.5);
		playerConfig.set(mobInfo.getMobType().getName() + ".autoPerformanceLevel", autoPerformanceLevel);
	}
	
	/**
	 * Saves all of the PlayerData contained in the DynamicDifficulty plugin to the
	 * <player name>.yml files in the players/ folder. This method is safe to use even 
	 * if the plugin is running headless in which case it will save them to the files
	 * in the src/ folder. It does not delete any PlayerInfo from the DynamicDifficulty
	 * plugin.
	 */
	protected void saveAllPlayerData()
	{
		// translate playerData into a Collection because you can't loop through a HashMap
		Collection<PlayerInfo> playerCollection = plugin.getPlayerData();
		
		// for each playerInfo
		for(PlayerInfo playerInfo: playerCollection)
		{
			String playerName = playerInfo.getPlayerName();
			
			// save key variables from PlayerInfo to the player.yml file
			this.savePlayerDataToFile(playerName);
		}
	}
	
	/**
	 * If the playerName.yml file exists, then this method will load it from disk
	 * and return a FileConfiguration based on it. If the player name.yml file 
	 * does not exist, then this method will create a default playerName.yml, save
	 * it to disk, and return a FileConfiguration based on it.
	 * @param playerName the name of the player whose playerName.yml file you want
	 * @return a FileConfiguration based of the playerName.yml file
	 */
	protected FileConfiguration loadPlayerConfig(String playerName)
	{
		FileConfiguration playerConfig = null;
		
		// get the playerName.yml file from disk
		File playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator  + playerName + ".yml");
		
		// if the File exists
		if(playerFile.exists())
		{
			// load the YAML config based on it
			playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		}
		else
		{
			// load the YAML config based on the default player.yml File
			playerConfig = this.getDefaultPlayerConfig(); //YamlConfiguration.loadConfiguration(plugin.getResource(DEFAULT_PLAYER_FILE_NAME));

			try
			{
				playerConfig.save(playerFile);
			}
			catch (IOException e)
			{
				plugin.safeLogInfo("Could not save config to " + PLAYERS_FOLDER + playerFile);
			}
		}

		return playerConfig;
	}
	
	/**
	 * Reloads all of the PlayerInfo for players that are still logged in.
	 */
	protected void reloadInfoForLoggedInPlayers()
	{
		Player [] players = plugin.getServer().getOnlinePlayers();
		
		for(Player player: players)
		{
			String playerName = player.getDisplayName();

			// load the playerData contained in the FileConfiguration into the plugin
			this.loadPlayerDataFromFile(playerName);
		}
	}
}
