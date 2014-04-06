/*
 * "DynamicDifficulty" is a plugin that implements dynamic difficulty 
 * on a Minecraft Bukkit Server.
 * Copyright (C) 2013, 2014  C.J. McGuire
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>. 
 */
package com.cjmcguire.bukkit.dynamic;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.cjmcguire.bukkit.dynamic.analyzer.AnalyzerTask;
import com.cjmcguire.bukkit.dynamic.commands.DynamicCommandExecutor;
import com.cjmcguire.bukkit.dynamic.controller.ControllerListener;
import com.cjmcguire.bukkit.dynamic.filehandlers.PlayerFileHandler;
import com.cjmcguire.bukkit.dynamic.monitor.MonitorListener;
import com.cjmcguire.bukkit.dynamic.playerdata.PlayerDataManager;

/**
 * DynamicDifficulty core plugin class. This plugin implements dynamic 
 * difficulty on a Minecraft Bukkit Server.
 * @author CJ McGuire
 */
public final class DynamicDifficulty extends JavaPlugin
{	
	
	private final static long TICKS_PER_MINUTE = 1200;
	private final static long HALF_MINUTE = TICKS_PER_MINUTE / 2;
//	private final static long TICKS_PER_SECOND = 20;
	
//	private ConfigFileHandler configFileHandler;
	
	/**
	 * Starts the DynamicDifficulty plugin.
	 */
	@Override
	public void onEnable()
	{
//		configFileHandler = new ConfigFileHandler(this);
		
//		MobInfo.setMaxIncrement(configFileHandler.getMaxIncrement());
		
		PlayerFileHandler playerFileHandler = new PlayerFileHandler(this);
		
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.setPlayerFileHandler(playerFileHandler);
		
		PluginManager pluginManager = this.getServer().getPluginManager();
		
		// register the fileHandler to handle events that deal with 
		// <player name>.yml files
		pluginManager.registerEvents(playerFileHandler, this);
			
		// set up the monitor
		pluginManager.registerEvents(new MonitorListener(), this);
			
		// set up the controller
		pluginManager.registerEvents(new ControllerListener(), this);
		
		// set up the analyzer
		AnalyzerTask analyzer = new AnalyzerTask();
//		long ticks = configFileHandler.getSecondsBetweenUpdates() * TICKS_PER_SECOND;
//		analyzer.runTaskTimer(this, 0, ticks);
		analyzer.runTaskTimer(this, 0, HALF_MINUTE);
		// register the commands
		this.getCommand(DynamicCommandExecutor.NAME).setExecutor(new DynamicCommandExecutor(this));
	}
	
	/**
	 * Closes the DynamicDifficulty plugin.
	 */
	@Override
	public void onDisable()
	{
		PlayerDataManager playerDataManager = PlayerDataManager.getInstance();
		playerDataManager.saveAllPlayerData();
		playerDataManager.clearPlayerData();
	}
}
