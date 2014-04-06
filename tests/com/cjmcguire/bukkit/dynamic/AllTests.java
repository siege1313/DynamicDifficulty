package com.cjmcguire.bukkit.dynamic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cjmcguire.bukkit.dynamic.analyzer.TestAnalyzerTask;
import com.cjmcguire.bukkit.dynamic.commands.*;
import com.cjmcguire.bukkit.dynamic.controller.TestControllerListener;
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
	
	// com.cjmcguire,bukkit.dynamic.analyzer
	TestAnalyzerTask.class,

	// com.cjmcguire,bukkit.dynamic.commands
	TestAbstractDDCommand.class,
	TestChangeLevelCommand.class,
	TestChangeSettingCommand.class,
	TestDynamicCommand.class,
	TestDynamicCommandExecutor.class,
	TestInfoCommand.class,
	TestPlayerTargetableCommand.class,
	
	// com.cjmcguire,bukkit.dynamic.controller
	TestControllerListener.class,

	// com.cjmcguire,bukkit.dynamic.filehandlers
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
