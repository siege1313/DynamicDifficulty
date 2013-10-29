package com.cjmcguire.bukkit.dynamic;

import static org.junit.Assert.*;

import org.bukkit.craftbukkit.v1_6_R2.entity.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.easymock.EasyMock;
import org.junit.Test;

/**
 * Tests the MobName enum.
 * @author CJ McGuire
 */
public class TestMobType
{
	/**
	 * Tests the getNameAsString() method
	 */
	@Test
	public void testGetName()
	{
		assertEquals("blaze", MobType.BLAZE.getName());
		assertEquals("cavespider", MobType.CAVE_SPIDER.getName());
		assertEquals("creeper", MobType.CREEPER.getName());
		assertEquals("enderman", MobType.ENDERMAN.getName());
		assertEquals("ghast", MobType.GHAST.getName());
		assertEquals("magmacube", MobType.MAGMACUBE.getName());
		assertEquals("pigzombie", MobType.ZOMBIE_PIGMAN.getName());
		assertEquals("silverfish", MobType.SILVERFISH.getName());
		assertEquals("skeleton", MobType.SKELETON.getName());
		assertEquals("slime", MobType.SLIME.getName());
		assertEquals("spider", MobType.SPIDER.getName());
		assertEquals("witch", MobType.WITCH.getName());
		assertEquals("witherskeleton", MobType.WITHER_SKELETON.getName());
		assertEquals("zombie", MobType.ZOMBIE.getName());
	}
	
	/**
	 * Tests the getMaxHealth() method
	 */
	@Test
	public void testGetMaxHealth()
	{
		assertEquals(20, MobType.BLAZE.getMaxHealth());
		assertEquals(12, MobType.CAVE_SPIDER.getMaxHealth());
		assertEquals(20, MobType.CREEPER.getMaxHealth());
		assertEquals(40, MobType.ENDERMAN.getMaxHealth());
		assertEquals(10, MobType.GHAST.getMaxHealth());
		assertEquals(4, MobType.MAGMACUBE.getMaxHealth());
		assertEquals(8, MobType.SILVERFISH.getMaxHealth());
		assertEquals(20, MobType.SKELETON.getMaxHealth());
		assertEquals(4, MobType.SLIME.getMaxHealth());
		assertEquals(16, MobType.SPIDER.getMaxHealth());
		assertEquals(26, MobType.WITCH.getMaxHealth());
		assertEquals(20, MobType.WITHER_SKELETON.getMaxHealth());
		assertEquals(20, MobType.ZOMBIE.getMaxHealth());
		assertEquals(20, MobType.ZOMBIE_PIGMAN.getMaxHealth());
	}
	
	/**
	 * Tests the getDefaultFollowDistance() method
	 */
	@Test
	public void testGetDefaultFollowDistance()
	{
		assertEquals(16, MobType.BLAZE.getDefaultFollowDistance());
		assertEquals(16, MobType.CAVE_SPIDER.getDefaultFollowDistance());
		assertEquals(16, MobType.CREEPER.getDefaultFollowDistance());
		assertEquals(16, MobType.ENDERMAN.getDefaultFollowDistance());
		assertEquals(100, MobType.GHAST.getDefaultFollowDistance());
		assertEquals(16, MobType.MAGMACUBE.getDefaultFollowDistance());
		assertEquals(16, MobType.SILVERFISH.getDefaultFollowDistance());
		assertEquals(16, MobType.SKELETON.getDefaultFollowDistance());
		assertEquals(16, MobType.SLIME.getDefaultFollowDistance());
		assertEquals(16, MobType.SPIDER.getDefaultFollowDistance());
		assertEquals(16, MobType.WITCH.getDefaultFollowDistance());
		assertEquals(16, MobType.WITHER_SKELETON.getDefaultFollowDistance());
		assertEquals(40, MobType.ZOMBIE.getDefaultFollowDistance());
		assertEquals(40, MobType.ZOMBIE_PIGMAN.getDefaultFollowDistance());
	}
	
	/**
	 * Tests the getMobType() method
	 */
	@Test
	public void testGetMobType()
	{
		assertEquals(MobType.BLAZE, MobType.getMobType("blaze"));
		assertEquals(MobType.CAVE_SPIDER, MobType.getMobType("cavespider"));
		assertEquals(MobType.CREEPER, MobType.getMobType("creeper"));
		assertEquals(MobType.ENDERMAN, MobType.getMobType("enderman"));
		assertEquals(MobType.GHAST, MobType.getMobType("ghast"));
		assertEquals(MobType.MAGMACUBE, MobType.getMobType("magmacube"));
		assertEquals(MobType.SILVERFISH, MobType.getMobType("silverfish"));
		assertEquals(MobType.SKELETON, MobType.getMobType("skeleton"));
		assertEquals(MobType.SLIME, MobType.getMobType("slime"));
		assertEquals(MobType.SPIDER, MobType.getMobType("spider"));
		assertEquals(MobType.WITCH, MobType.getMobType("witch"));
		assertEquals(MobType.WITHER_SKELETON, MobType.getMobType("witherskeleton"));
		assertEquals(MobType.ZOMBIE, MobType.getMobType("zombie"));
		assertEquals(MobType.ZOMBIE_PIGMAN, MobType.getMobType("pigzombie"));
	}
	
	/**
	 * Tests the getEntitysMobType() method
	 */
	@Test
	public void testGetEntitysMobType()
	{
		CraftSkeleton mockSkeleton = EasyMock.createMock(CraftSkeleton.class);
        EasyMock.expect(mockSkeleton.getType()).andReturn(EntityType.SKELETON);
        EasyMock.expect(mockSkeleton.getSkeletonType()).andReturn(SkeletonType.NORMAL);
        EasyMock.replay(mockSkeleton);
		assertEquals(MobType.SKELETON, MobType.getEntitysMobType(mockSkeleton));
		EasyMock.verify(mockSkeleton);
		
		
		CraftSkeleton mockWitherSkeleton = EasyMock.createMock(CraftSkeleton.class);
        EasyMock.expect(mockWitherSkeleton.getType()).andReturn(EntityType.SKELETON);
        EasyMock.expect(mockWitherSkeleton.getSkeletonType()).andReturn(SkeletonType.WITHER);
        EasyMock.replay(mockWitherSkeleton);
		assertEquals(MobType.WITHER_SKELETON, MobType.getEntitysMobType(mockWitherSkeleton));
		EasyMock.verify(mockWitherSkeleton);
		
		
		CraftZombie mockZombie = EasyMock.createMock(CraftZombie.class);
        EasyMock.expect(mockZombie.getType()).andReturn(EntityType.ZOMBIE);
        EasyMock.replay(mockZombie);
		assertEquals(MobType.ZOMBIE, MobType.getEntitysMobType(mockZombie));
		EasyMock.verify(mockZombie);
	}
}