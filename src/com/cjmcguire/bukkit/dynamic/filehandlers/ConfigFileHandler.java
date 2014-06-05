/*package com.cjmcguire.bukkit.dynamic.filehandlers;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * The ConfigFileHandler is responsible for handling all of the i/o 
 * that goes on with the config.yml file.
 * @author CJ McGuire
 *
public class ConfigFileHandler extends FileHandler
{
	private final static String SECONDS_BETWEEN_UPDATES = "secondsBetweenUpdates";
	private final static String MAX_INCREMENT = "maxIncrement";
	private final static String MIN_PERFORMANCE_LEVEL = "minPerformanceLevel";
	private final static String MAX_PERFORMANCE_LEVEL = "maxPerformanceLevel";
	
	private final static String MOB_ATTRIBUTES_ATTACK_DAMAGE = "mobAttributes.attackDamage";
	private final static String MOB_ATTRIBUTES_SPEED = "mobAttributes.speed";
	private final static String MOB_ATTRIBUTES_KNOCKBACK_RESISTANCE = "mobAttributes.knockbackResistance";
	private final static String MOB_ATTRIBUTES_MAX_FOLLOW_DISTANCE = "mobAttributes.maxFollowDistance";
	
	private final static String CONFIG_FILE_NAME = "config.yml";

	/**
	 * Initializes the ConfigFileHandler.
	 * @param plugin a reference to the DynamicDifficulty plugin that 
	 * uses this ConfigFileHandler
	 *
	public ConfigFileHandler(DynamicDifficulty plugin)
	{
		super(plugin, CONFIG_FILE_NAME);
	}
	
	/**
	 * @return the seconds to wait between updates. This value is 
	 * found in the config.yml.
	 *
	public int getSecondsBetweenUpdates()
	{
		return config.getInt(SECONDS_BETWEEN_UPDATES);
	}
	
	/**
	 * @return the max increment that a performance level can change. 
	 * This value is found in the config.yml.
	 *
	public int getMaxIncrement()
	{
		return config.getInt(MAX_INCREMENT);
	}
	
	/**
	 * @return the minimum possible value for a performance level. 
	 * This value is found in the config.yml.
	 *
	public int getMinPerformanceLevel()
	{
		return config.getInt(MIN_PERFORMANCE_LEVEL);
	}
	
	/**
	 * @return the maximum possible value for a performance level. 
	 * This value is found in the config.yml.
	 *
	public int getMaxPerformanceLevel()
	{
		return config.getInt(MAX_PERFORMANCE_LEVEL);
	}
	
	/**
	 * @return true if the plugin should scale mobs' attack damage.
	 * false if it should not. This value is found in the config.yml.
	 *
	public boolean shouldScaleAttackDamage()
	{
		return config.getBoolean(MOB_ATTRIBUTES_ATTACK_DAMAGE);
	}
	
	/**
	 * @return true if the plugin should scale mobs' speed.
	 * false if it should not. This value is found in the config.yml.
	 *
	public boolean shouldScaleSpeed()
	{
		return config.getBoolean(MOB_ATTRIBUTES_SPEED);
	}
	
	/**
	 * @return true if the plugin should scale mobs' knockback 
	 * resistance.
	 * false if it should not. This value is found in the config.yml.
	 *
	public boolean shouldScaleKnockbackResistance()
	{
		return config.getBoolean(MOB_ATTRIBUTES_KNOCKBACK_RESISTANCE);
	}
	
	/**
	 * @return true if the plugin should scale mobs' max follow 
	 * distance. false if it should not. This value is found in the 
	 * config.yml.
	 *
	public boolean shouldScaleMaxFollowDistance()
	{
		return config.getBoolean(MOB_ATTRIBUTES_MAX_FOLLOW_DISTANCE);
	}
}
*/