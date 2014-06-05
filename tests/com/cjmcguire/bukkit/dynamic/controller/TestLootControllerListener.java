package com.cjmcguire.bukkit.dynamic.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockLivingEntity;
import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;

/**
 * Tests the LootControllerListener class.
 * @author CJ McGuire
 */
public class TestLootControllerListener
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");

	/**
	 * Tests that the manipulateEXP() method returns the correct exp 
	 * when the player's performance level is above 100.
	 */
	@Test
	public void testManipulateEXPPerformanceAbove100() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
        int alteredEXP = controller.manipulateEXP(PLAYER_1_ID, MobType.BLAZE, 20);
        
        assertEquals(40, alteredEXP);
	}

	/**
	 * Tests that the manipulateEXP() method returns the correct exp 
	 * when the player's performance level is below 100.
	 */
	@Test
	public void testManipulateEXPPerformanceBelow100() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(50);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
        int alteredEXP = controller.manipulateEXP(PLAYER_1_ID, MobType.BLAZE, 20);
        
        assertEquals(20, alteredEXP);
	}

	/**
	 * Tests that the manipulateEXP() method returns the correct exp 
	 * when the player's scaleXp value is set to false.
	 */
	@Test
	public void testDontManipulateEXP() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		blazeInfo.setScaleXP(false);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
        int alteredEXP = controller.manipulateEXP(PLAYER_1_ID, MobType.BLAZE, 20);
        
        assertEquals(20, alteredEXP);
	}
	
	/**
	 * Tests that the manipulateItemAmounts() method sets the items 
	 * to the correct amounts when the player's performance level is 
	 * above 100.
	 */
	@Test
	public void testManipulateItemAmountsAbove100() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		
		ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH, 2);
		
		itemStacks.add(itemStack);
		
        controller.manipulateItemAmounts(PLAYER_1_ID, MobType.BLAZE, itemStacks);
        
        assertEquals(4, itemStack.getAmount());
	}

	/**
	 * Tests that the manipulateItemAmounts() method sets the items 
	 * to the correct amounts when the player's performance level is 
	 * below 100.
	 */
	@Test
	public void testManipulateItemAmountsBelow100() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(50);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		
		ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH, 2);
		
		itemStacks.add(itemStack);
		
        controller.manipulateItemAmounts(PLAYER_1_ID, MobType.BLAZE, itemStacks);
        
        assertEquals(2, itemStack.getAmount());
	}
	
	/**
	 * Tests that the manipulateItemAmounts() method does not change
	 * the items amounts when the player's scaleLoot value is set to
	 * false.
	 */
	@Test
	public void testDontManipulateItemAmounts() 
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		MobInfo blazeInfo = playerInfo.getMobInfo(MobType.BLAZE);
		blazeInfo.setAutoPerformanceLevel(200);
		blazeInfo.setScaleLoot(false);
		
		playerDataManager.addPlayerInfo(playerInfo);
		
		LootControllerListener controller = new LootControllerListener();
		
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		
		ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH, 2);
		
		itemStacks.add(itemStack);
		
        controller.manipulateItemAmounts(PLAYER_1_ID, MobType.BLAZE, itemStacks);
        
        assertEquals(2, itemStack.getAmount());
	}
	
	/**
	 * Tests the onEntityDeathEvent() method when the player's game 
	 * mode is SURVIVAL. It should scale the drops and xp.
	 */
	@Test
	public void testOnEntityDeathEventSurvival()
	{
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.SURVIVAL);
        EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.replay(mockPlayer);
        
        LivingEntity mockKilledEntity = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockKilledEntity.getType()).andReturn(EntityType.ZOMBIE);
		EasyMock.expect(mockKilledEntity.getKiller()).andReturn(mockPlayer);
		EasyMock.replay(mockKilledEntity);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		zombieInfo.setAutoPerformanceLevel(200);
		playerDataManager.addPlayerInfo(playerInfo);
		
		
		LootControllerListener lootControllerListener = new LootControllerListener();
        
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH, 2);
		itemStacks.add(itemStack);
		
		EntityDeathEvent event = new EntityDeathEvent(mockKilledEntity, itemStacks, 20);
		lootControllerListener.onEntityDeathEvent(event);
		
        assertEquals(40, event.getDroppedExp());
        assertEquals(4, itemStack.getAmount());
		
		EasyMock.verify(mockKilledEntity);
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the onEntityDeathEvent() method when the player's game 
	 * mode is CREATIVE. It should not scale the drops and xp.
	 */
	@Test
	public void testOnEntityDeathEventCreative()
	{
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.CREATIVE);
        EasyMock.replay(mockPlayer);
        
        LivingEntity mockKilledEntity = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockKilledEntity.getType()).andReturn(EntityType.ZOMBIE);
		EasyMock.expect(mockKilledEntity.getKiller()).andReturn(mockPlayer);
		EasyMock.replay(mockKilledEntity);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		zombieInfo.setAutoPerformanceLevel(200);
		playerDataManager.addPlayerInfo(playerInfo);
		
		
		LootControllerListener lootControllerListener = new LootControllerListener();
        
		List<ItemStack> itemStacks = new ArrayList<ItemStack>();
		ItemStack itemStack = new ItemStack(Material.ROTTEN_FLESH, 2);
		itemStacks.add(itemStack);
		
		EntityDeathEvent event = new EntityDeathEvent(mockKilledEntity, itemStacks, 20);
		lootControllerListener.onEntityDeathEvent(event);
		
        assertEquals(20, event.getDroppedExp());
        assertEquals(2, itemStack.getAmount());
		
		EasyMock.verify(mockKilledEntity);
		EasyMock.verify(mockPlayer);
	}
}