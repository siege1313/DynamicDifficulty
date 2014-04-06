package com.cjmcguire.bukkit.dynamic.filehandlers;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * The PlayerFileHandler is responsible for handling all of the i/o 
 * that goes on with the <player name>.yml files. It handles the 
 * conversion from a "player name" to "player file" to "player config" 
 * to "player data".
 * @author CJ McGuire
 */
public class PlayerFileHandler extends FileHandler implements Listener
{
	private final static String DEFAULT_PLAYER_FILE_NAME = "default_player.yml";
	private final static String PLAYERS_FOLDER = "players";
	
	private PlayerDataManager playerDataManager;
	
	/**
	 * Initializes the PlayerFileHandler.
	 * @param plugin a reference to the DynamicDifficulty plugin that 
	 * uses this PlayerFileHandler
	 */
	public PlayerFileHandler(DynamicDifficulty plugin)
	{
		super(plugin, DEFAULT_PLAYER_FILE_NAME);
		
		this.playerDataManager = PlayerDataManager.getInstance();
		
		this.createPlayersFolder();
		this.reloadInfoForLoggedInPlayers();
	}
	
	/**
	 * Creates the players folder if it does not exist.
	 */
	private void createPlayersFolder()
	{
		if(this.isRunningWithHead())
		{
			File playersFolder = new File(plugin.getDataFolder(), PLAYERS_FOLDER);
			
			if(!playersFolder.exists())
			{
				playersFolder.mkdirs();
			}
		}
	}
	
	/**
	 * Reloads all of the PlayerInfo for players that are still 
	 * logged in.
	 */
	private void reloadInfoForLoggedInPlayers()
	{
		if(this.isRunningWithHead())
		{
			Player [] players = plugin.getServer().getOnlinePlayers();
			
			for(Player player: players)
			{
				String playerName = player.getName();
	
				// load the playerData contained in the 
				// FileConfiguration into the plugin
				this.loadPlayerData(playerName);
			}
		}
	}
	
	/**
	 * This method triggers whenever a player logs onto a Minecraft 
	 * Bukkit Server. If no player data exists for the player that 
	 * just logged in, this method initializes player data for that 
	 * player. If player data does exist, this method will load in the 
	 * player data from the player's config.yml file.
	 * @param event the PlayerLoginEvent that just occurred.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		// get the player
		Player player = event.getPlayer();
		// get the player's name
		String playerName = player.getName();
		// load the playerData contained in the FileConfiguration 
		// into the plugin
		this.loadPlayerData(playerName);
	}
	
	/**
	 * Loads the info from the <player name>.yml file to the plugin's 
	 * player data.
	 * @param playerName the name of the player whose player info you 
	 * want to load into the plugin's memory
	 */
	public void loadPlayerData(String playerName)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.getPlayerConfig(playerName);
		
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
		playerDataManager.addPlayerInfo(playerInfo);
	}
	
	/**
	 * If the playerName.yml file exists, then this method will load 
	 * it from disk and return a FileConfiguration based on it. If the 
	 * player name.yml file does not exist, then this method will 
	 * create a default playerName.yml, save it to disk, and return a 
	 * FileConfiguration based on it.
	 * @param playerName the name of the player whose playerName.yml 
	 * file you want.
	 * @return a FileConfiguration based of the playerName.yml file.
	 */
	protected FileConfiguration getPlayerConfig(String playerName)
	{
		File playerFile;
		FileConfiguration playerConfig = null;
		
		if(this.isRunningWithHead())
		{
			playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator  + playerName + ".yml");
		}
		else
		{
			playerFile = new File(PLAYERS_FOLDER + File.separator  + playerName + ".yml");
		}
		
		if(this.isRunningWithHead()&& !playerFile.exists())
		{
			playerFile.getParentFile().mkdirs();
			try
			{
				playerConfig = YamlConfiguration.loadConfiguration(configFile);
				playerConfig.save(playerFile);
	        } 
			catch (IOException e) 
			{
				plugin.getLogger().info("Could not save to " + playerName + ".yml");
	        }
		}
		else
		{
			playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		}
		
		// we set the defaults to the defaultPlayerConfig so that if
		// the playerConfig contains garbage data, we can still load 
		// the MobInfo
		playerConfig.setDefaults(config);

		return playerConfig;
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
		// the estimated performance level should start off equal to 
		// the current performance level
		mobInfo.setEstimatedPerformanceLevel(autoPerformanceLevel);
	}
	
	/**
	 * This method triggers whenever a player logs out of a Minecraft 
	 * Bukkit Server. It will clear temporary variables from the 
	 * player's player data and will save permanent variables.
	 * @param event the PlayerQuitEvent that just occurred
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogout(PlayerQuitEvent event)
	{
		// get the player
		Player player = event.getPlayer();
		// get the player's name
		String playerName = player.getName();
		
		// save key variables from PlayerInfo to the player.yml file
		this.savePlayerData(playerName);

		// remove the PlayerInfo from the plugin
		playerDataManager.removePlayerInfo(playerName);
	}
	
	/**
	 * Saves the info from the plugin's PlayerData to the 
	 * <player name>.yml file. It does not delete it from the plugin's 
	 * PlayerData.
	 * @param playerName the name of the player whose player data you 
	 * want to save to disk
	 */
	public void savePlayerData(String playerName)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.getPlayerConfig(playerName);
		
		// get the PlayerInfo
		PlayerInfo playerInfo = playerDataManager.getPlayerInfo(playerName);
		
		// loop through each MobType
		for(MobType mobType: MobType.values())
		{
			// get the MobInfo
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.saveMobInfoToConfig(mobInfo, playerConfig);
		}
		
		File playerFile;
		if(this.isRunningWithHead())
		{
			playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator + playerName + ".yml");
		}
		else
		{
			playerFile = new File(PLAYERS_FOLDER + File.separator + playerName + ".yml");
		}
			
		try 
		{
			playerConfig.save(playerFile);
		}
		catch (IOException e)
		{
			plugin.getLogger().info("Could not save player data to " + PLAYERS_FOLDER + playerFile);
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
}
