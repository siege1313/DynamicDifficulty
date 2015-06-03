package com.cjmcguire.bukkit.dynamic.playerdata;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Skeleton.SkeletonType;

/**
 * The MobName Enum is used to represent the various hostile mobs in
 * Minecraft. It provides a mob name, max health, and the mob's 
 * default follow distance.
 * @author CJ McGuire
 */
public enum MobType 
{
	/**
	 * Blaze Mob. Name: "blaze". Health: 20. Default Follow Distance: 16
	 */
	BLAZE(EntityType.BLAZE.getEntityClass().getSimpleName(), 20, 16),
	
	/**
	 * Cave Spider Mob. Name: "cavespider". Health: 12. Default Follow Distance: 16
	 */
	CAVE_SPIDER(EntityType.CAVE_SPIDER.getEntityClass().getSimpleName(), 12, 16),
	
	/**
	 * Creeper Mob. Name: "creeper". Health: 20. Default Follow Distance: 16
	 */
	CREEPER(EntityType.CREEPER.getEntityClass().getSimpleName(), 20, 16),
	
	/**
	 * Enderman Mob. Name: "enderman". Health: 40. Default Follow Distance: 16
	 */
	ENDERMAN(EntityType.ENDERMAN.getEntityClass().getSimpleName(), 40, 16),
	
	/**
	 * Ghast Mob. Name: "ghast". Health: 10. Default Follow Distance: 100
	 */
	GHAST(EntityType.GHAST.getEntityClass().getSimpleName(), 10, 100),
	
	/**
	 * Magma cube Mob. Name: "magmacube". Health: 4. Default Follow Distance: 16
	 */
	MAGMA_CUBE(EntityType.MAGMA_CUBE.getEntityClass().getSimpleName(), 4, 16),
	
	/**
	 * Zombie Pigman Mob. Name: "pigzombie". Health: 20. Default Follow Distance: 40
	 */
	PIG_ZOMBIE(EntityType.PIG_ZOMBIE.getEntityClass().getSimpleName(), 20, 40),
	
	/**
	 * Silverfish Mob. Name: "silverfish". Health: 8. Default Follow Distance: 16
	 */
	SILVERFISH(EntityType.SILVERFISH.getEntityClass().getSimpleName(), 8, 16),
	
	/**
	 * Skeleton Mob. Name: "skeleton". Health: 20. Default Follow Distance: 16
	 */
	SKELETON(EntityType.SKELETON.getEntityClass().getSimpleName(), 20, 16),
	
	/**
	 * Slime Mob. Name: "slime". Health: 4. Default Follow Distance: 16
	 */
	SLIME(EntityType.SLIME.getEntityClass().getSimpleName(), 4, 16),

	/**
	 * Spider Mob. Name: "spider". Health: 16. Default Follow Distance: 16
	 */
	SPIDER(EntityType.SPIDER.getEntityClass().getSimpleName(), 16, 16),
	
	/**
	 * Witch Mob. Name: "witch". Health: 26. Default Follow Distance: 16
	 */
	WITCH(EntityType.WITCH.getEntityClass().getSimpleName(), 26, 16),
	
	/**
	 * Wither Skeleton Mob. Name: "witherskeleton". Health: 20. Default Follow Distance: 16
	 */
	WITHER_SKELETON("wither" + EntityType.SKELETON.getEntityClass().getSimpleName(), 20, 16),

	/**
	 * Zombie Mob. Name: "zombie". Health: 20. Default Follow Distance: 40
	 */
	ZOMBIE(EntityType.ZOMBIE.getEntityClass().getSimpleName(), 20, 40);
	
	
	private String name;
	private int maxHealth;
	private int defaultFollowDistance;
	
	
	MobType(String name, int maxHealth, int defaultFollowDistance)
	{
		this.name = name.toLowerCase();
		this.maxHealth = maxHealth;
		this.defaultFollowDistance = defaultFollowDistance;
	}
	
	/**
	 * @return the mob's name (will be in all lowercase)
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return the mob's max health
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}
	
	/**
	 * @return the mob's default follow distance
	 */
	public int getDefaultFollowDistance()
	{
		return defaultFollowDistance;
	}
	
	/**
	 * Gets the MobType for the given mobName.
	 * @param mobName the name of the mob whose MobType you want. Not
	 * case sensitive.
	 * @return the MobType with the given mobName or null if no
	 * MobType has the given MobName
	 */
	public static MobType getMobType(String mobName)
	{
		MobType mobType = null;

		for(MobType tempMobType: MobType.values())
		{
			if(tempMobType.getName().equalsIgnoreCase(mobName))
			{
				mobType = tempMobType;
			}
		}
		
		return mobType;
	}
	
	/**
	 * Gets the mobType of the given Entity
	 * @param mob the mob whose MobType you want to get
	 * @return the Entity's MobType or null if the Entity has no
	 * associated MobType.
	 */
	public static MobType getEntitysMobType(Entity mob)
	{
		// get the entity type
		EntityType entityType = mob.getType();
		// get the mob name
		String mobName = entityType.getEntityClass().getSimpleName().toLowerCase();

		// special case for distinguishing between a skeleton and a wither skeleton
		// because bukkit does not have separate EntityTypes for it yet.
		if(entityType == EntityType.SKELETON)
		{
			Skeleton skeleton = (Skeleton) mob;
			
			if(skeleton.getSkeletonType() == SkeletonType.WITHER)
			{
				mobName = "wither" + mobName;
			}
		}
		
		return getMobType(mobName);
	}
	
	/**
	 * Checks if the LivingEntity is Hostile. This is to handle the
	 * cases where a mob may or may not be hostile to a player.
	 * Such mobs include: Rabbits, Wolves.
	 * @param mob - the mob to check.
	 * @return true if the mob is hostile 
	 */
	public static boolean potentiallyHostile(LivingEntity mob)
	{
		boolean potentiallyHostile = true;
		if(mob instanceof Rabbit)
		{
			Rabbit rabbit = (Rabbit) mob;
			if(rabbit.getRabbitType() != Rabbit.Type.THE_KILLER_BUNNY)
			{
				potentiallyHostile = false;
			} 
		}
		else if(mob instanceof Wolf)
		{
			Wolf wolf = (Wolf) mob;
			potentiallyHostile = wolf.isAngry();
		}
		
		return potentiallyHostile;
	}
}
