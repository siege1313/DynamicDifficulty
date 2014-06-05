package com.cjmcguire.bukkit.dynamic.playerdata;

import java.util.HashMap;
import java.util.UUID;

/**
 * The PlayerInfo class holds the name of a player and the player's 
 * MobInfo for each type of hostile mob in the game.
 * @author CJ McGuire
 */
public class PlayerInfo 
{
	private final UUID playerID;
	
	private final HashMap<MobType, MobInfo> mobData;
	
	/**
	 * Initializes this PlayerInfo
	 * @param playerID the UUID of the player associated with this 
	 * PlayerInfo
	 */
	public PlayerInfo(UUID playerID)
	{
		this.playerID = playerID;
		
		mobData = new HashMap<MobType, MobInfo>();
		
		MobType[] mobTypes = MobType.values();
		for(MobType mobType: mobTypes)
		{
			mobData.put(mobType, new MobInfo(mobType));
		}
	}

	/**
	 * @return the player's UUID
	 */
	public UUID getPlayerID() 
	{
		return playerID;
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