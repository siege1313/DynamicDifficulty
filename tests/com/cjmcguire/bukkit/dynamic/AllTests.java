package com.cjmcguire.bukkit.dynamic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cjmcguire.bukkit.dynamic.analyzer.TestAnalyzerTask;
import com.cjmcguire.bukkit.dynamic.commands.*;
import com.cjmcguire.bukkit.dynamic.commands.core.*;
import com.cjmcguire.bukkit.dynamic.commands.scale.*;
import com.cjmcguire.bukkit.dynamic.controller.TestLootControllerListener;
import com.cjmcguire.bukkit.dynamic.controller.TestMobControllerListener;
import com.cjmcguire.bukkit.dynamic.filehandlers.TestPlayerFileHandler;
import com.cjmcguire.bukkit.dynamic.monitor.TestMonitorListener;
import com.cjmcguire.bukkit.dynamic.playerdata.*;

/**
 * Test Suite for the DynamicDifficulty plugin.
 * @author CJ McGuire
 */
@RunWith(Suite.class) 
@Suite.SuiteClasses({
	
	// com.cjmcguire.bukkit.dynamic
	
	// com.cjmcguire.bukkit.dynamic.analyzer
	TestAnalyzerTask.class,

	// com.cjmcguire.bukkit.dynamic.commands
	TestPlayerTargetableCommand.class,
	TestDynamicCommandExecutor.class,

	// com.cjmcguire.bukkit.dynamic.commands.core
	TestChangeLevelCommand.class,
	TestChangeSettingCommand.class,
	TestHelpCommand.class,
	TestInfoCommand.class,
	TestSetMaxIncrementCommand.class,

	// com.cjmcguire.bukkit.dynamic.commands.scale
	TestBooleanSetting.class,
	TestScaleAttackCommand.class,
	TestScaleDefenseCommand.class,
	TestScaleFollowDistanceCommand.class,
	TestScaleKnockbackCommand.class,
	TestScaleLootCommand.class,
	TestScaleSpeedCommand.class,
	TestScaleXPCommand.class,

	// com.cjmcguire.bukkit.dynamic.controller
	TestLootControllerListener.class,
	TestMobControllerListener.class,

	// com.cjmcguire.bukkit.dynamic.filehandlers
//	TestConfigFileHandler.class,
	TestPlayerFileHandler.class,
	
	// com.cjmcguire.bukkit.dynamic.monitor
	TestMonitorListener.class,
	
	// com.cjmcguire.bukkit.dynmaic.playerdata
	TestMobInfo.class,
	TestMobType.class,
	TestPlayerDataManager.class,
	TestPlayerInfo.class,
	TestSetting.class})

public class AllTests {}
