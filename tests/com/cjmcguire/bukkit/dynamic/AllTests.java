package com.cjmcguire.bukkit.dynamic;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cjmcguire.bukkit.dynamic.analyzer.TestAnalyzerTask;
import com.cjmcguire.bukkit.dynamic.commands.TestAbstractDDCommand;
import com.cjmcguire.bukkit.dynamic.commands.TestChangeLevelCommand;
import com.cjmcguire.bukkit.dynamic.commands.TestChangeSettingCommand;
import com.cjmcguire.bukkit.dynamic.commands.TestDynamicCommand;
import com.cjmcguire.bukkit.dynamic.commands.TestDynamicCommandExecutor;
import com.cjmcguire.bukkit.dynamic.commands.TestInfoCommand;
import com.cjmcguire.bukkit.dynamic.controller.TestControllerListener;
import com.cjmcguire.bukkit.dynamic.monitor.TestMonitorListener;

/**
 * Test Suite for the DynamicDifficulty plugin
 * @author CJ McGuire
 */
@RunWith(Suite.class) 
@Suite.SuiteClasses({
	
	// com.cjmcguire.bukkit.dynamic
	TestDynamicDifficulty.class,
	TestMobInfo.class,
	TestMobType.class,
	TestPlayerFileHandler.class,
	TestPlayerInfo.class,
	TestSetting.class,
	
	// com.cjmcguire,bukkit.dynamic.analyzer
	TestAnalyzerTask.class,

	// com.cjmcguire,bukkit.dynamic.commands
	TestAbstractDDCommand.class,
	TestChangeLevelCommand.class,
	TestChangeSettingCommand.class,
	TestDynamicCommand.class,
	TestDynamicCommandExecutor.class,
	TestInfoCommand.class,
	
	// com.cjmcguire,bukkit.dynamic.controller
	TestControllerListener.class,
	
	// com.cjmcguire.bukkit.dynamic.monitor
	TestMonitorListener.class})

public class AllTests {}
