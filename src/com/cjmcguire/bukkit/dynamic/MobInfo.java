package com.cjmcguire.bukkit.dynamic;

import java.util.ArrayList;

/**
 * Holds all of the dynamic difficulty information for a given mob such as
 * the difficulty setting, auto performance level and manual performance
 * level.
 * @author CJ McGuire
 */
public class MobInfo 
{
	/**
	 * The maximum number of units that a player's auto performance level can 
	 * change in a single update. (10 units)
	 */
	public final static int MAX_INCREMENT = 10;
	
	/**
	 * The maximum performance level that a player can have. (200 units)
	 */
	public final static int MAX_PERFORMANCE_LEVEL = 200;
	
	/**
	 * The minimum performance level that a player can have. (50 units)
	 */
	public final static int MIN_PERFORMANCE_LEVEL = 50;
	
	private final static double MAX_PLAYER_HEALTH = 20.0;
	
	private final MobType mobType;
	private Setting setting;
	
	private double estimatedPerformanceLevel;
	private double autoPerformanceLevel;
	private double manualPerformanceLevel;
	
	private int damagePlayerGave;
	private int damagePlayerReceived;
	
	private ArrayList<Integer> interactedWithIDs;
	
	/**
	 * Initializes this MobInfo with the given MobType
	 * @param mobType the type of mob for this MobInfo
	 */
	public MobInfo(MobType mobType)
	{
		this.mobType = mobType;
		this.setting = Setting.AUTO;
		
		estimatedPerformanceLevel = 100;
		autoPerformanceLevel = 100;
		manualPerformanceLevel = 100;
		
		damagePlayerGave = 0;
		damagePlayerReceived = 0;
		
		interactedWithIDs = new ArrayList<Integer>();
	}
	
	/**
	 * @return this MobInfo's MobType
	 */
	public MobType getMobType()
	{
		return mobType;
	}
	
	/**
	 * @return this Mob's difficulty Setting
	 */
	public Setting getSetting()
	{
		return setting;
	}

	/**
	 * Sets the difficulty setting to the given setting
	 * @param setting the new value for the setting
	 */
	public void setSetting(Setting setting) 
	{
		this.setting = setting;
	}
	
	/**
	 * @return the estimated performance level for this Mob
	 */
	public double getEstimatedPerformanceLevel() 
	{
		return estimatedPerformanceLevel;
	}

	/**
	 * Sets estimatedPerformanceLevel to the given estimatedPerformanceLevel.
	 * estimatedPerformanceLevel must be between .5 and 2 (inclusive)
	 * @param estimatedPerformanceLevel the new value for estimatedPerformanceLevel
	 */
	public void setEstimatedPerformanceLevel(double estimatedPerformanceLevel)
	{
		this.estimatedPerformanceLevel = estimatedPerformanceLevel;
		
		if(this.estimatedPerformanceLevel < MIN_PERFORMANCE_LEVEL)
		{
			this.estimatedPerformanceLevel = MIN_PERFORMANCE_LEVEL;
		}
		
		if(this.estimatedPerformanceLevel > MAX_PERFORMANCE_LEVEL)
		{
			this.estimatedPerformanceLevel = MAX_PERFORMANCE_LEVEL;
		}
	}
	
	/**
	 * Updates the estimated performance level for this Mob, using the following algorithm:
	 * Let ESL = the player’s estimated performance level for a given mob type.
	 * Let G = the total damage given to all mobs of a given mob type. 
	 * Let MH = the max hp for the mob. 
	 * Let R = the total damage received from all mobs of a given mob type. 
	 * Let PH = the player's max hp. 
	 * Let N = number of mobs of a given type that a player has interacted with. 
	 * ESL = 100 + ((G/MH – R/PH) / N) * 100
	 * Note that ESL must be between MIN_PERFORMANCE_LEVEL and MAX_PERFORMANCE_LEVEL.
	 * Also note that ESL will not be updated if N < 3.
	 */
	public void updateEstimatedPerformanceLevel()
	{
		if(interactedWithIDs.size() >= 3)
		{			
			double interactedWith = interactedWithIDs.size();
			double damageGiven = this.getDamagePlayerGave();
			double damageReceived = this.getDamagePlayerReceived();
			double maxMobHealth = mobType.getMaxHealth();
			
			double performalLevel = 100.0 + ((damageGiven/maxMobHealth - damageReceived/MAX_PLAYER_HEALTH) / interactedWith) * 100;
			
			this.setEstimatedPerformanceLevel(performalLevel);
		}
	}

	/**
	 * @return the auto performance level for this Mob
	 */
	public double getAutoPerformanceLevel() 
	{
		return autoPerformanceLevel;
	}
	
	/**
	 * Sets autoPerformanceLevel to the given autoPerformanceLevel
	 * @param autoPerformanceLevel the new value for autoPerformanceLevel
	 */
	public void setAutoPerformanceLevel(double autoPerformanceLevel)
	{
		this.autoPerformanceLevel = autoPerformanceLevel;
		
		if(this.autoPerformanceLevel < MIN_PERFORMANCE_LEVEL)
		{
			this.autoPerformanceLevel = MIN_PERFORMANCE_LEVEL;
		}
		
		if(this.autoPerformanceLevel > MAX_PERFORMANCE_LEVEL)
		{
			this.autoPerformanceLevel = MAX_PERFORMANCE_LEVEL;
		}
	}
	
	/**
	 * Updates the auto performance level for this Mob dynamically.
	 * autoPerformanceLevel will shift toward estimatedPerformanceLevel.
	 * autoPerformanceLevel cannot shift more than 10 in a single update.
	 */
	public void updateAutoPerformanceLevel()
	{
		//estimated vastly greater than auto
		if(estimatedPerformanceLevel > autoPerformanceLevel + MAX_INCREMENT)
		{
			this.setAutoPerformanceLevel(autoPerformanceLevel + MAX_INCREMENT);
		}
		//estimated close to auto
		else if(estimatedPerformanceLevel <= autoPerformanceLevel + MAX_INCREMENT && estimatedPerformanceLevel >= autoPerformanceLevel - MAX_INCREMENT)
		{
			this.setAutoPerformanceLevel(estimatedPerformanceLevel);
		}
		//estimated vastly less than auto
		else if(estimatedPerformanceLevel < autoPerformanceLevel - MAX_INCREMENT)
		{
			this.setAutoPerformanceLevel(autoPerformanceLevel - MAX_INCREMENT);
		}
	}

	/**
	 * @return the manual performance level for this Mob
	 */
	public double getManualPerformanceLevel()
	{
		return manualPerformanceLevel;
	}

	/**
	 * Sets manualPerformanceLevel to the given manualPerformanceLevel
	 * @param manualPerformanceLevel the new value for manualPerformanceLevel
	 */
	public void setManualPerformanceLevel(double manualPerformanceLevel) 
	{
		this.manualPerformanceLevel = manualPerformanceLevel;
		
		if(this.manualPerformanceLevel < MIN_PERFORMANCE_LEVEL)
		{
			this.manualPerformanceLevel = MIN_PERFORMANCE_LEVEL;
		}
		
		if(this.manualPerformanceLevel > MAX_PERFORMANCE_LEVEL)
		{
			this.manualPerformanceLevel = MAX_PERFORMANCE_LEVEL;
		}
	}
	
	/**
	 * @return the performance level currently in use based on the player's setting.
	 * If the player's setting is auto, this method will return the player's
	 * auto performance level. If the player's setting is set to manual, this 
	 * method will return the player's manual performance level. If the player's
	 * setting is disabled, this method will return 100.
	 */
	public double getPerformanceLevelInUse()
	{
		double performanceLevel = 100;
		
		if(setting == Setting.AUTO)
		{
			performanceLevel = this.getAutoPerformanceLevel();
		}
		else if(setting == Setting.MANUAL)
		{
			performanceLevel = this.getManualPerformanceLevel();
		}
		
		return performanceLevel;
	}

	/**
	 * @return the total amount of damage that a player has given to all
	 * mobs of this type
	 */
	public int getDamagePlayerGave()
	{
		return damagePlayerGave;
	}
	
	/**
	 * Adds the given amount to the total amount of damage that a player
	 * has given to all mobs of a certain type. Note that the total amount of
	 * damage a player has given cannot be greater than the total number of mobs
	 * interacted with * the mob's health.
	 * @param amount the amount of damage to add
	 */
	public void addToDamagePlayerGave(int amount)
	{
		damagePlayerGave += amount;
		if(damagePlayerGave > this.getNumberInteractedWith() * mobType.getMaxHealth())
		{
			damagePlayerGave = this.getNumberInteractedWith() * mobType.getMaxHealth();
		}
	}
	
	/**
	 * @return the total amount of damage that a player has received from all
	 * mobs of this type
	 */
	public int getDamagePlayerReceived() 
	{
		return damagePlayerReceived;
	}
	
	/**
	 * Adds the given amount to the total amount of damage that a player
	 * has received from all mobs of this type
	 * @param amount the amount of damage to add
	 */
	public void addToDamagePlayerReceived(int amount)
	{
		damagePlayerReceived += amount;
	}
	
	/**
	 * @return the mobs that the player has interacted with.
	 */
	public int getNumberInteractedWith()
	{
		return interactedWithIDs.size();
	}
	
	/**
	 * Adds the given mob ID to the IDs of mobs the player has
	 * interacted with 
	 * @param id the ID of the mob that this Player interacted with
	 * @return true if the id was added to the list of IDs. False if 
	 * it was not added due to it already being in the list.
	 */
	public boolean addIDToInteractedWithIDs(int id)
	{
		boolean wasAdded = false;
		
		if(!interactedWithIDs.contains(id))
		{
			interactedWithIDs.add(id);
			wasAdded = true;
		}
		
		return wasAdded;
	}
}
