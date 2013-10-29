package com.cjmcguire.bukkit.dynamic;

import java.util.HashMap;

/**
 * The Player Info class holds the name of a player and all of the player's 
 * MobInfo for each type of hostile mob in the game.
 * @author CJ McGuire
 */
public class PlayerInfo 
{
	private String playerName;
	
	private HashMap<MobType, MobInfo> mobData;
	
	/**
	 * Initializes this PlayerInfo
	 * @param playerName the name of the player associated with this PlayerInfo
	 */
	public PlayerInfo(String playerName)
	{
		this.playerName = playerName;
		
		mobData = new HashMap<MobType, MobInfo>();
		
		for(MobType mobType: MobType.values())
		{
			mobData.put(mobType, new MobInfo(mobType));
		}
	}

	/**
	 * @return the player's name
	 */
	public String getPlayerName() 
	{
		return playerName;
	}
	
	/**
	 * @param mobType the MobType that you want to get
	 * @return the player's MobInfo for the given MobType
	 */
	public MobInfo getMobInfo(MobType mobType)
	{
		return mobData.get(mobType);
	}
}
