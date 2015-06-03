package com.cjmcguire.bukkit.dynamic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.cjmcguire.bukkit.dynamic.playerdata.MobType;

/**
 * An abstract class for a Listener that listens to entity damage
 * events. It contains helper methods for carrying out actions
 * related to entity damage events.
 * @author CJ McGuire
 */
public abstract class AbstractEntityDamageListener implements Listener
{
	
	protected void onEntityDamageByEntityAction(EntityDamageByEntityEvent event)
	{
		// If the damager was originally a projectile, then convert
		// the damager to the shooter.
		Entity damager = event.getDamager();
		if(damager instanceof Projectile)
		{
			Projectile projectile = (Projectile) damager;
			ProjectileSource shooter = projectile.getShooter();
			if(shooter instanceof LivingEntity)
			{
				damager = (LivingEntity)shooter;
			}
		}

		// Get the entity that was damaged.
		Entity damaged = event.getEntity();
		// Get the original damage.
		int originalDamage = (int) event.getDamage();
		// Set the altered damage equal to the original damage
		int alteredDamage = originalDamage;
		
		// If the entity that got damaged was the player and the 
		// entity doing the damage was a living entity (a mob)
		if(this.playerDamagedByLivingEntity(damaged, damager))
		{
			alteredDamage = this.playerDamagedAction((Player)damaged, (LivingEntity)damager, originalDamage);
		}
		else if(this.livingEntityDamagedByPlayer(damaged, damager) && MobType.potentiallyHostile((LivingEntity) damaged))
		{
			alteredDamage = this.livingEntityDamagedAction((LivingEntity) damaged, (Player) damager, originalDamage);
		}
		
		event.setDamage((double)alteredDamage);
	}
	
	protected boolean playerDamagedByLivingEntity(Entity damaged, Entity damager)
	{
		boolean playerDamagedByLivingEntity = false;
		
		if(damaged instanceof Player && damager instanceof LivingEntity)
		{
			playerDamagedByLivingEntity = true;
		}
		
		return playerDamagedByLivingEntity;
	}
	
	protected boolean livingEntityDamagedByPlayer(Entity damaged, Entity damager)
	{
		boolean livingEntityDamagedByPlayer = false;
		
		if(damaged instanceof LivingEntity && damager instanceof Player)
		{
			livingEntityDamagedByPlayer = true;
		}
		
		return livingEntityDamagedByPlayer;
	}
	
	protected boolean notInvincible(LivingEntity entity)
	{
		return entity.getNoDamageTicks() < entity.getMaximumNoDamageTicks();// / 2.0;
	}
	
	protected abstract int playerDamagedAction(Player player, LivingEntity mob, int damage);
	
	protected abstract int livingEntityDamagedAction(LivingEntity mob, Player player, int damage);
}
