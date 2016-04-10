package com.cjmcguire.bukkit.dynamic.filehandlers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * The PlayerFileHandler is responsible for handling all of the i/o 
 * that goes on with the player yml files. It handles the conversion 
 * from a "player name" and UUID to a "player file" to a "player 
 * config" to "player data".
 * @author CJ McGuire
 */
public class PlayerFileHandler extends FileHandler implements Listener
{
	private final static String DEFAULT_PLAYER_FILE_NAME = "default_player.yml";
	private final static String PLAYERS_FOLDER = "players";
	
	private PlayerDataManager playerDataManager;
	
	/**
	 * Initializes the PlayerFileHandler.
	 * @param plugin a reference to the plugin that uses this 
	 * PlayerFileHandler
	 */
	public PlayerFileHandler(Plugin plugin)
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
			Collection<? extends Player> players = plugin.getServer().getOnlinePlayers();
			
			for(Player player: players)
			{
				UUID playerID = player.getUniqueId();
				
				// Load the playerData contained in the 
				// FileConfiguration into the plugin.
				this.loadPlayerData(playerID);
			}
		}
	}
	
	/**
	 * This method triggers whenever a player logs onto a Minecraft 
	 * Bukkit Server. If no player data exists for the player that 
	 * just logged in, this method initializes player data for that 
	 * player. If player data does exist, this method will load in 
	 * the player data from the player's config.yml file.
	 * @param event the PlayerLoginEvent that just occurred.
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		UUID playerID = player.getUniqueId();
		
		this.loadPlayerData(playerID);
	}
	
	/**
	 * Loads the info from the <player name>.yml file to the plugin's 
	 * player data.
	 * @param playerID the UUID of the player whose player info you 
	 * want to load into the plugin's memory
	 */
	public void loadPlayerData(UUID playerID)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.getPlayerConfig(playerID);
		
		// create the PlayerInfo
		PlayerInfo playerInfo = new PlayerInfo(playerID);
		
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
	 * @param playerID the UUID of the player whose yml file you want.
	 * @return a FileConfiguration based of the playerName.yml file.
	 */
	protected FileConfiguration getPlayerConfig(UUID playerID)
	{
		File playerFile;
		FileConfiguration playerConfig = null;
		
		if(this.isRunningWithHead())
		{
			playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator  + playerID + ".yml");
		}
		else
		{
			playerFile = new File(PLAYERS_FOLDER + File.separator  + playerID + ".yml");
		}
		
		if(this.isRunningWithHead() && !playerFile.exists())
		{
			playerFile.getParentFile().mkdirs();
			
			try
			{
				playerConfig = YamlConfiguration.loadConfiguration(configFile);
				playerConfig.save(playerFile);
	        } 
			catch (IOException e) 
			{
				plugin.getLogger().info("Could not save to " + playerID + ".yml");
	        }
		}
		else
		{
			playerConfig = YamlConfiguration.loadConfiguration(playerFile);
		}
		
		// We set the defaults to the defaultPlayerConfig so that if
		// the playerConfig contains garbage data, we can still load 
		// the MobInfo.
		playerConfig.setDefaults(config);
		if(this.isRunningWithHead())
		{
			playerConfig.set("lastKnownName", this.plugin.getServer().getPlayer(playerID).getName());
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

		return playerConfig;
	}
	
	private void loadMobInfoFromConfig(MobInfo mobInfo, FileConfiguration playerConfig)
	{
		String mobName = mobInfo.getMobType().getName();
		
		// Set the setting in mobInfo.
		String settingName = playerConfig.getString(mobName + ".setting", MobInfo.DEFAULT_SETTING);
		Setting setting = Setting.getSetting(settingName);
		mobInfo.setSetting(setting);
		
		// Set the manualPerformanceLevel in the MobInfo.
		int manualPerformanceLevel = playerConfig.getInt(mobName + ".manualPerformanceLevel", MobInfo.DEFAULT_PERFORMANCE_LEVEL);
		mobInfo.setManualPerformanceLevel(manualPerformanceLevel);
	
		// Set the currentPerformanceLevel in the MobInfo.
		int autoPerformanceLevel = playerConfig.getInt(mobName + ".autoPerformanceLevel", MobInfo.DEFAULT_PERFORMANCE_LEVEL);
		mobInfo.setAutoPerformanceLevel(autoPerformanceLevel);
		
		// The estimated performance level should start off equal to 
		// the current performance level.
		mobInfo.setEstimatedPerformanceLevel(autoPerformanceLevel);
		
		int maxIncrement = playerConfig.getInt(mobName + ".maxIncrement", MobInfo.DEFAULT_MAX_INCREMENT);
		mobInfo.setMaxIncrement(maxIncrement);
		
		boolean scaleAttack = playerConfig.getBoolean(mobName + ".scaleAttributes.attack", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleAttack(scaleAttack);
		
		boolean scaleDefense = playerConfig.getBoolean(mobName + ".scaleAttributes.defense", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleDefense(scaleDefense);
		
		boolean scaleSpeed = playerConfig.getBoolean(mobName + ".scaleAttributes.speed", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleSpeed(scaleSpeed);
		
		boolean scaleKnockBackResistance = playerConfig.getBoolean(mobName + ".scaleAttributes.knockback", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleKnockbackResistance(scaleKnockBackResistance);
		
		boolean scaleMaxFollowDistance = playerConfig.getBoolean(mobName + ".scaleAttributes.followDistance", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleMaxFollowDistance(scaleMaxFollowDistance);
		
		boolean scaleXP = playerConfig.getBoolean(mobName + ".scaleAttributes.xp", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleXP(scaleXP);
		
		boolean scaleLoot = playerConfig.getBoolean(mobName + ".scaleAttributes.loot", MobInfo.DEFAULT_SCALE);
		mobInfo.setScaleLoot(scaleLoot);
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
		Player player = event.getPlayer();
		
		UUID playerID = player.getUniqueId();
		
		// Save key variables from PlayerInfo to the player.yml file.
		this.savePlayerData(playerID);

		// Remove the PlayerInfo from the plugin.
		playerDataManager.removePlayerInfo(playerID);
	}
	
	/**
	 * Saves the info from the plugin's PlayerData to the player yml
	 * file. It does not delete it from the plugin's PlayerData.
	 * @param playerID the UUID of the player whose player data you 
	 * want to save to disk
	 */
	public void savePlayerData(UUID playerID)
	{
		// create the FileConfiguration object based on the playerFile.yml
		FileConfiguration playerConfig = this.getPlayerConfig(playerID);
		
		// get the PlayerInfo
		PlayerInfo playerInfo = playerDataManager.getPlayerInfo(playerID);
		
		// loop through each MobType
		MobType [] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			MobInfo mobInfo = playerInfo.getMobInfo(mobType);
			this.saveMobInfoToConfig(mobInfo, playerConfig);
		}
		
		File playerFile;
		if(this.isRunningWithHead())
		{
			playerFile = new File(plugin.getDataFolder(), PLAYERS_FOLDER + File.separator + playerID + ".yml");
		}
		else
		{
			playerFile = new File(PLAYERS_FOLDER + File.separator + playerID + ".yml");
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
		String mobName = mobInfo.getMobType().getName();
		
		// save the setting for the MobType in the player.yml file
		Setting setting = mobInfo.getSetting();
		String settingName = setting.getName();
		playerConfig.set(mobName + ".setting", settingName);
		
		// save the manualPerformanceLevel for the MobType in the player.yml file
		int manualPerformanceLevel = (int) (mobInfo.getManualPerformanceLevel());
		playerConfig.set(mobName + ".manualPerformanceLevel", manualPerformanceLevel);

		// save the autoPerformanceLevel for the MobType in the player.yml file
		int autoPerformanceLevel = (int) (mobInfo.getAutoPerformanceLevel()+.5);
		playerConfig.set(mobName + ".autoPerformanceLevel", autoPerformanceLevel);
		
		int maxIncrement = mobInfo.getMaxIncrement();
		playerConfig.set(mobName + ".maxIncrement", maxIncrement);
		
		boolean scaleAttack = mobInfo.shouldScaleAttack();
		playerConfig.set(mobName + ".scaleAttributes.attack", scaleAttack);
		
		boolean scaleDefense = mobInfo.shouldScaleDefense();
		playerConfig.set(mobName + ".scaleAttributes.defense", scaleDefense);
		
		boolean scaleSpeed = mobInfo.shouldScaleSpeed();
		playerConfig.set(mobName + ".scaleAttributes.speed", scaleSpeed);
		
		boolean scaleKnockBackResistance = mobInfo.shouldScaleKnockbackResistance();
		playerConfig.set(mobName + ".scaleAttributes.knockback", scaleKnockBackResistance);
		
		boolean scaleMaxFollowDistance = mobInfo.shouldScaleMaxFollowDistance();
		playerConfig.set(mobName + ".scaleAttributes.followDistance", scaleMaxFollowDistance);
		
		boolean scaleXP = mobInfo.shouldScaleXP();
		playerConfig.set(mobName + ".scaleAttributes.xp", scaleXP);
		
		boolean scaleLoot = mobInfo.shouldScaleLoot();
		playerConfig.set(mobName + ".scaleAttributes.loot", scaleLoot);
	}
}
