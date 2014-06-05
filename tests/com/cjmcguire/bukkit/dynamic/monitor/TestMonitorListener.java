package com.cjmcguire.bukkit.dynamic.monitor;

import static org.junit.Assert.*;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.easymock.EasyMock;
import org.junit.Test;

import com.cjmcguire.bukkit.dynamic.MockLivingEntity;
import com.cjmcguire.bukkit.dynamic.MockPlayer;
import com.cjmcguire.bukkit.dynamic.playerdata.MobInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.MobType;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerInfo;
import com.cjmcguire.bukkit.dynamic.playerdata.Setting;

/**
 * Tests the MonitorListener class.
 * @author CJ McGuire
 */
public class TestMonitorListener 
{
	private static final UUID PLAYER_1_ID = UUID.fromString("12345678-1234-1234-1234-123456789001");
	
	private static final double ACCURACY = .0001;
	
	/**
	 * Tests the updatePlayerDamageReceived() method.
	 */
	@Test
	public void testUpdatePlayerDamageReceived()
	{
		// Set up the Mock stuff.
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getEntityId()).andReturn(1);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.ZOMBIE);
		
		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerReceived(playerInfo.getPlayerID(), mockEntity, 2);

		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerReceived(), ACCURACY);
		
		// Verify the Mock stuff.
		EasyMock.verify(mockEntity);
	}
	
	/**
	 * Tests the updatePlayerDamageReceived() method when the player's
	 * setting is manual.
	 */
	@Test
	public void testUpdatePlayerDamageReceivedWhenManual()
	{
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.ZOMBIE);
		
		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerReceived(playerInfo.getPlayerID(), mockEntity, 2);

		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(0, zombieInfo.getDamagePlayerReceived(), ACCURACY);
		EasyMock.verify(mockEntity);
	}
	
	/**
	 * Tests the updatePlayerDamageReceived() method when the Entity 
	 * is not a valid MobType.
	 */
	@Test
	public void testUpdatePlayerDamageReceivedWhenInvaldMobType()
	{
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.EGG);
		
		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerReceived(playerInfo.getPlayerID(), mockEntity, 2);

		for(MobType mobType: MobType.values())
		{
			assertEquals(0, playerInfo.getMobInfo(mobType).getDamagePlayerReceived(), ACCURACY);
		}
		
		EasyMock.verify(mockEntity);
	}
	
	/**
	 * Tests the updatePlayerDamageGave() method.
	 */
	@Test
	public void testUpdatePlayerDamageGave()
	{
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getEntityId()).andReturn(1);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.ZOMBIE);

		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerGave(mockEntity, playerInfo.getPlayerID(), 2);

		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerGave(), ACCURACY);
		EasyMock.verify(mockEntity);
	}

	/**
	 * Tests the updatePlayerDamageGave() method when the player's 
	 * setting is manual.
	 */
	@Test
	public void testUpdatePlayerDamageGaveWhenManual()
	{
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.ZOMBIE);
		
		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		MobInfo mobInfo = playerInfo.getMobInfo(MobType.ZOMBIE);
		mobInfo.setSetting(Setting.MANUAL);
		
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerGave(mockEntity, playerInfo.getPlayerID(), 2);

		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(0, zombieInfo.getDamagePlayerGave(), ACCURACY);
		EasyMock.verify(mockEntity);
	}
	
	/**
	 * Tests the updatePlayerDamageGave() method when the Entity is 
	 * not a valid MobType.
	 */
	@Test
	public void testUpdatePlayerDamageGaveWhenInvaldMobType()
	{
		Entity mockEntity = EasyMock.createMock(Entity.class);
		EasyMock.expect(mockEntity.getType()).andReturn(EntityType.EGG);
		
		EasyMock.replay(mockEntity);

		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();

		MonitorListener monitor = new MonitorListener();

		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		
		playerDataManager.addPlayerInfo(playerInfo);

		monitor.updateDamagePlayerGave(mockEntity, playerInfo.getPlayerID(), 2);

		for(MobType mobType: MobType.values())
		{
			assertEquals(0, playerInfo.getMobInfo(mobType).getDamagePlayerGave(), ACCURACY);
		}
		
		EasyMock.verify(mockEntity);
	}
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a mob, not a projectile.
	 * 2. the player is who gets damaged.
	 * 3. the player is does not have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome1()
	{
		LivingEntity mockDamager = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockDamager.getEntityId()).andReturn(1);
		EasyMock.expect(mockDamager.getType()).andReturn(EntityType.ZOMBIE);
		EasyMock.replay(mockDamager);
		
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.SURVIVAL);
        EasyMock.expect(mockPlayer.getNoDamageTicks()).andReturn(1);
        EasyMock.expect(mockPlayer.getMaximumNoDamageTicks()).andReturn(5);
        
        EasyMock.replay(mockPlayer);
       
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockDamager, mockPlayer, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerReceived(), ACCURACY);
		
		EasyMock.verify(mockDamager);
		
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a mob, not a projectile.
	 * 2. the player is who gets damaged.
	 * 3. the player is does have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome2()
	{
		LivingEntity mockDamager = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.replay(mockDamager);
		
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
        EasyMock.expect(mockPlayer.getNoDamageTicks()).andReturn(5);
        EasyMock.expect(mockPlayer.getMaximumNoDamageTicks()).andReturn(5);
        
        EasyMock.replay(mockPlayer);
       
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockDamager, mockPlayer, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(0, zombieInfo.getDamagePlayerReceived(), ACCURACY);
		
		EasyMock.verify(mockDamager);
		
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a projectile, fired by a zombie.
	 * 2. the player is who gets damaged.
	 * 3. the player is does not have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome3()
	{
		LivingEntity mockDamager = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockDamager.getEntityId()).andReturn(1);
		EasyMock.expect(mockDamager.getType()).andReturn(EntityType.ZOMBIE);
		EasyMock.replay(mockDamager);
		
		Arrow mockArrow = EasyMock.createMock(Arrow.class);
		EasyMock.expect(mockArrow.getShooter()).andReturn(mockDamager);
		EasyMock.replay(mockArrow);
		
		Player mockPlayer = EasyMock.createMock(MockPlayer.class);
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.SURVIVAL);
        EasyMock.expect(mockPlayer.getNoDamageTicks()).andReturn(1);
        EasyMock.expect(mockPlayer.getMaximumNoDamageTicks()).andReturn(5);
        
        EasyMock.replay(mockPlayer);
       
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockArrow, mockPlayer, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerReceived(), ACCURACY);
		
		EasyMock.verify(mockArrow);
		
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a player, not a projectile.
	 * 2. the mob is who gets damaged.
	 * 3. the mob is does not have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome4()
	{
		LivingEntity mockDamaged = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockDamaged.getEntityId()).andReturn(1);
		EasyMock.expect(mockDamaged.getType()).andReturn(EntityType.ZOMBIE);
        EasyMock.expect(mockDamaged.getNoDamageTicks()).andReturn(1);
        EasyMock.expect(mockDamaged.getMaximumNoDamageTicks()).andReturn(5);
		EasyMock.replay(mockDamaged);
		
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.SURVIVAL);
        
        EasyMock.replay(mockPlayer);
       
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockPlayer, mockDamaged, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerGave(), ACCURACY);
		
		EasyMock.verify(mockDamaged);
		
		EasyMock.verify(mockPlayer);
	}
	
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a player, not a projectile.
	 * 2. the mob is who gets damaged.
	 * 3. the mob is does have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome5()
	{
		LivingEntity mockDamaged = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
        EasyMock.expect(mockDamaged.getNoDamageTicks()).andReturn(5);
        EasyMock.expect(mockDamaged.getMaximumNoDamageTicks()).andReturn(5);
		EasyMock.replay(mockDamaged);
		
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
        
        EasyMock.replay(mockPlayer);
       
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockPlayer, mockDamaged, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(0, zombieInfo.getDamagePlayerGave(), ACCURACY);
		
		EasyMock.verify(mockDamaged);
		
		EasyMock.verify(mockPlayer);
	}
	
	/**
	 * Tests the onEntityDamageByEntityEvent() where:
	 * 1. the damager is a projectile, fired by a player.
	 * 2. the mob is who gets damaged.
	 * 3. the player is does not have invincibility frames
	 */
	@Test
	public void testOnEntityDamageByEntityEventOutcome6()
	{
		Player mockPlayer = EasyMock.createMockBuilder(MockPlayer.class).createMock();
		EasyMock.expect(mockPlayer.getUniqueId()).andReturn(PLAYER_1_ID);
        EasyMock.expect(mockPlayer.getGameMode()).andReturn(GameMode.SURVIVAL);
        EasyMock.replay(mockPlayer);
        
        Arrow mockArrow = EasyMock.createMock(Arrow.class);
		EasyMock.expect(mockArrow.getShooter()).andReturn(mockPlayer);
		EasyMock.replay(mockArrow);
		
        LivingEntity mockDamaged = EasyMock.createMockBuilder(MockLivingEntity.class).createMock();
		EasyMock.expect(mockDamaged.getEntityId()).andReturn(1);
		EasyMock.expect(mockDamaged.getType()).andReturn(EntityType.ZOMBIE);
		EasyMock.expect(mockDamaged.getNoDamageTicks()).andReturn(1);
        EasyMock.expect(mockDamaged.getMaximumNoDamageTicks()).andReturn(5);
        EasyMock.replay(mockDamaged);
	
		// Run the actual test.
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.clearPlayerData();
		
		PlayerInfo playerInfo = new PlayerInfo(PLAYER_1_ID);
		playerDataManager.addPlayerInfo(playerInfo);

		MonitorListener monitor = new MonitorListener();
		
		EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(mockArrow, mockDamaged, null, 2.0);
		
		monitor.onEntityDamageByEntityEvent(event);
		
		MobInfo zombieInfo = playerInfo.getMobInfo(MobType.ZOMBIE);

		assertEquals(2, zombieInfo.getDamagePlayerGave(), ACCURACY);
		
		EasyMock.verify(mockArrow);
		
		EasyMock.verify(mockPlayer);
	}
}