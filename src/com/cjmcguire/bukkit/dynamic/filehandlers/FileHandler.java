package com.cjmcguire.bukkit.dynamic.filehandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.cjmcguire.bukkit.dynamic.DynamicDifficulty;

/**
 * An abstract class for a FileHandler. A FileHandler's job is to 
 * handle all of the interactions associated with a particular type 
 * of config yml file.
 * @author CJ McGuire
 */
public abstract class FileHandler 
{
	// The name of the .yml file
	private final String configFileName;
	
	// The actual .yml file
	protected File configFile;
	
	// The File Configuration object obtained from the .yml file
	protected FileConfiguration config;
	
	protected DynamicDifficulty plugin;
	
	private boolean runningWithHead;

	/**
	 * Initializes this FileHandler.
	 * @param plugin a reference to the DynamicDifficulty plugin that 
	 * uses this FileHandler. If null is passed in, then this 
	 * FileHandler is running without its head.
	 * @param configFileName the name of the config yml file that this
	 * FileHandler is supposed to handle.
	 */
	public FileHandler(DynamicDifficulty plugin, String configFileName)
	{
		this.plugin = plugin;
		this.configFileName = configFileName;
		
		if(this.plugin != null)
		{
			runningWithHead = true;
		}
		else
		{
			runningWithHead = false;
		}
		
		this.initializeConfigFile();
		this.createConfigFile();
		this.initializeConfig();
	}
	
	private void initializeConfigFile()
	{
		if(this.isRunningWithHead())
		{
			configFile = new File(plugin.getDataFolder(), configFileName);
		}
		else
		{
			configFile = new File(configFileName);
		}
	}
	
	private void createConfigFile()
	{
		if(!configFile.exists() && this.isRunningWithHead())
		{
			configFile.getParentFile().mkdirs();
			
			try 
			{
				InputStream in = plugin.getResource(configFileName);
				OutputStream out = new FileOutputStream(configFile);
				
				byte[] buffer = new byte[1024];
				
				for(int length = in.read(buffer); length > 0; length = in.read(buffer))
				{
					out.write(buffer, 0, length);
				}
				
	            out.close();
	            in.close();
	        } 
			catch (IOException e) 
			{
				plugin.getLogger().info("Could not close input and output streams to " + 
										configFileName + " in DynamicDifficulty.jar and " +
										configFileName + " in the plugin's root folder");
	        }
		}
	}
	
	private void initializeConfig()
	{
		config = YamlConfiguration.loadConfiguration(configFile);
		
		if(this.isRunningWithHead())
		{
			
			// fix dataDefaultFile if it has garbage data.
			InputStream in = plugin.getResource(configFileName);
			FileConfiguration tempConfig = YamlConfiguration.loadConfiguration(in);
			config.setDefaults(tempConfig);
			
			try 
			{
				in.close();
			}
			catch (IOException e) 
			{
				plugin.getLogger().info("Could not close input stream to " + configFileName + 
									 " in DynamicDifficulty.jar");
			}
		}
	}
	
	/**
	 * @return true if this FileHandler is running with its head, 
	 * false if it running headless.
	 */
	public boolean isRunningWithHead()
	{
		return runningWithHead;
	}
	
	/**
	 * @return the FileConfiguration this FileHandler is responsible 
	 * for.
	 */
	protected FileConfiguration getFileConfig()
	{
		return config;
	}
}