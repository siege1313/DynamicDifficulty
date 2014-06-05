package com.cjmcguire.bukkit.dynamic.playerdata;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.cjmcguire.bukkit.dynamic.filehandlers.PlayerFileHandler;

/**
 * A singleton class that manages the dynamic difficulty player data.
 * @author CJ McGuire
 */
public class PlayerDataManager 
{
	private static PlayerDataManager singleton = new PlayerDataManager();
	
	private ConcurrentHashMap<UUID, PlayerInfo> playerData;
	
	private PlayerFileHandler playerFileHandler;
	
	private PlayerDataManager()
	{
		playerData = new ConcurrentHashMap<UUID, PlayerInfo>();
		playerFileHandler = null;
	}
	
	/**
	 * @return the singleton instance of this PlayerDataManager.
	 */
	public static PlayerDataManager getInstance()
	{
		return singleton;
	}
	
	/**
	 * Sets the PlayerFileHandler equal to the given PlayerFileHandler.
	 * @param playerFileHandler the PlayerFileHandler to set
	 */
	public void setPlayerFileHandler(PlayerFileHandler playerFileHandler)
	{
		this.playerFileHandler = playerFileHandler;
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
	 * Adds the given PlayerInfo to this plugin.
	 * @param playerInfo the PlayerInfo you want to add to this 
	 * PlayerDataManager's player data
	 * @return the previous value associated with playerInfo's 
	 * playerID, or null if there was no mapping for the playerID. 
	 * (A null return can also indicate that the map previously 
	 * associated null with the playerID.)
	 */
	public PlayerInfo addPlayerInfo(PlayerInfo playerInfo) 
	{
		return playerData.put(playerInfo.getPlayerID(), playerInfo);
	}
	
	/**
	 * Removes a player's PlayerInfo.
	 * @param playerID the UUID of the player whose PlayerInfo you 
	 * want to remove
	 * @return the previous value associated with playerID, or null 
	 * if there was no mapping for the playerID. (A null return can 
	 * also indicate that the map previously associated null with 
	 * the playerID.)
	 */
	public PlayerInfo removePlayerInfo(UUID playerID) 
	{
		return playerData.remove(playerID);
	}
	
	/**
	 * Gets a player's PlayerInfo. If no PlayerInfo currently exists 
	 * for the player when this method is called, then it will 
	 * generate new PlayerInfo for the player before returning it.
	 * @param playerID the UUID of the player whose PlayerInfo you 
	 * want to get
	 * @return the PlayerInfo of the player with the given UUID.
	 */
	public PlayerInfo getPlayerInfo(UUID playerID) 
	{
		if(!playerData.containsKey(playerID) && playerFileHandler != null)
		{
			playerFileHandler.loadPlayerData(playerID);
		}
		
		return playerData.get(playerID);
	}
	
	/**
	 * Removes all of the player data.
	 */
	public void clearPlayerData()
	{
		playerData.clear();
	}
	
	/**
	 * @param playerID the UUID of the player whose PlayerInfo you 
	 * want to know exists or not
	 * @return true if PlayerInfo for the given UUID exists in the 
	 * player data
	 */
	public boolean playerInfoExists(UUID playerID)
	{
		return playerData.containsKey(playerID);
	}

	/**
	 * Gets a player's MobInfo for a particular player and mob.
	 * @param playerID the UUID of the player whose MobInfo you want 
	 * to get
	 * @param mobType the MobType of the MobInfo that you want the get
	 * @return the MobInfo for the given MobType and playerID
	 */
	public MobInfo getPlayersMobInfo(UUID playerID, MobType mobType)
	{
		PlayerInfo playerInfo = this.getPlayerInfo(playerID);
		
		MobInfo mobInfo = playerInfo.getMobInfo(mobType);
		
		return mobInfo;
	}
	
	/**
	 * Saves all of the PlayerData contained in the DynamicDifficulty 
	 * plugin to the <player name>.yml files in the players/ folder. 
	 * This method is safe to use even if the plugin is running 
	 * headless in which case it will save them to the files in the 
	 * src/ folder. It does not delete any PlayerInfo from the 
	 * DynamicDifficulty plugin.
	 */
	public void saveAllPlayerData()
	{
		if(playerFileHandler != null)
		{
			// translate playerData into a Collection because you can't loop through a HashMap
			Collection<PlayerInfo> playerCollection = playerData.values();
			
			// For each playerInfo
			for(PlayerInfo playerInfo: playerCollection)
			{
				UUID playerID = playerInfo.getPlayerID();
				
				// Save key variables from PlayerInfo to the player yml file
				playerFileHandler.savePlayerData(playerID);
			}
		}
	}
}
