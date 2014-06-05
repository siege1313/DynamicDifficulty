package com.cjmcguire.bukkit.dynamic.playerdata;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * Tests the PlayerInfo class.
 * @author CJ McGuire
 */
public class TestPlayerInfo 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");

	/**
	 * Tests the getPlayerName() method.
	 */
	@Test
	public void testGetPlayerName() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		assertEquals(PLAYER_1_ID, playerInfo.getPlayerID());
	}
	
	/**
	 * Tests the getMobInfo() method.
	 */
	@Test
	public void testGetMobInfo() 
	{
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		assertEquals(MobType.BLAZE, playerInfo.getMobInfo(MobType.BLAZE).getMobType());
		assertEquals(MobType.CAVE_SPIDER, playerInfo.getMobInfo(MobType.CAVE_SPIDER).getMobType());
		assertEquals(MobType.CREEPER, playerInfo.getMobInfo(MobType.CREEPER).getMobType());
		assertEquals(MobType.ENDERMAN, playerInfo.getMobInfo(MobType.ENDERMAN).getMobType());
		assertEquals(MobType.GHAST, playerInfo.getMobInfo(MobType.GHAST).getMobType());
		assertEquals(MobType.MAGMA_CUBE, playerInfo.getMobInfo(MobType.MAGMA_CUBE).getMobType());
		assertEquals(MobType.SILVERFISH, playerInfo.getMobInfo(MobType.SILVERFISH).getMobType());
		assertEquals(MobType.SKELETON, playerInfo.getMobInfo(MobType.SKELETON).getMobType());
		assertEquals(MobType.SLIME, playerInfo.getMobInfo(MobType.SLIME).getMobType());
		assertEquals(MobType.SPIDER, playerInfo.getMobInfo(MobType.SPIDER).getMobType());
		assertEquals(MobType.WITCH, playerInfo.getMobInfo(MobType.WITCH).getMobType());
		assertEquals(MobType.WITHER_SKELETON, playerInfo.getMobInfo(MobType.WITHER_SKELETON).getMobType());
		assertEquals(MobType.ZOMBIE, playerInfo.getMobInfo(MobType.ZOMBIE).getMobType());
		assertEquals(MobType.PIG_ZOMBIE, playerInfo.getMobInfo(MobType.PIG_ZOMBIE).getMobType());
	}
}