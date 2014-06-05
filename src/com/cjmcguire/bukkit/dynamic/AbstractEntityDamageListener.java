package com.cjmcguire.bukkit.dynamic;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
		// Get the damager and convert it to the shooter if it was 
		// originally the projectile
		Entity damager = event.getDamager();
		if(damager instanceof Projectile)
		{
			Projectile projectile = (Projectile) damager;
			damager = projectile.getShooter();
		}

		// Get the entity that was damaged.
		Entity damaged = event.getEntity();

		int damage = (int) event.getDamage();
	
		int alteredDamage = (int) event.getDamage();
		
		// If the entity that got damaged was the player and the 
		// entity doing the damage was a living entity (a mob)
		if(this.playerDamagedByLivingEntity(damaged, damager))
		{
			alteredDamage = this.playerDamagedAction((Player)damaged, (LivingEntity)damager, damage);
		}
		else if(this.livingEntityDamagedByPlayer(damaged, damager))
		{
			alteredDamage = this.livingEntityDamagedAction((LivingEntity) damaged, (Player) damager, damage);
		}
		
		event.setDamage((double)alteredDamage);
	}
	
	protected abstract int playerDamagedAction(Player player, LivingEntity mob, int damage);
	
	protected abstract int livingEntityDamagedAction(LivingEntity mob, Player player, int damage);
	
	protected boolean notInvincible(LivingEntity entity)
	{
		return entity.getNoDamageTicks() < entity.getMaximumNoDamageTicks();// / 2.0;
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
}
