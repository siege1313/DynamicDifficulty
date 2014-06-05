package com.cjmcguire.bukkit.dynamic.filehandlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * An abstract class for a FileHandler. A FileHandler's job is to 
 * handle all of the interactions associated with a particular type 
 * of yml file.
 * @author CJ McGuire
 */
public abstract class FileHandler 
{
	// The name of the yml file.
	private final String configFileName;
	
	// The actual yml file.
	protected File configFile;
	
	// The Configuration object obtained from the yml file.
	protected FileConfiguration config;
	
	protected Plugin plugin;
	
	private boolean runningWithHead;

	/**
	 * Initializes this FileHandler.
	 * @param plugin a reference to the plugin that uses this 
	 * FileHandler. If null is passed in, then this FileHandler is 
	 * running without its head.
	 * @param configFileName the name of the yml file that this
	 * FileHandler is supposed to handle.
	 */
	public FileHandler(Plugin plugin, String configFileName)
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
		this.setUpConfigFile();
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
	
	private void setUpConfigFile()
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
			// Fix dataDefaultFile if it has garbage data.
			InputStream in = plugin.getResource(configFileName);
			InputStreamReader inReader = new InputStreamReader(in);
			
			FileConfiguration tempConfig = YamlConfiguration.loadConfiguration(inReader);
			config.setDefaults(tempConfig);
			
			try 
			{
				inReader.close();
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