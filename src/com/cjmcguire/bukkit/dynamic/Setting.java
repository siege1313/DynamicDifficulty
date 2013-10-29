package com.cjmcguire.bukkit.dynamic;

/**
 * The Setting Enum is used to represent three user settings in the
 * DynamicDifficulty plugin - AUTO, MANUAL, and DISABLED. It provides 
 * a String to represent the setting as well.
 * @author CJ McGuire
 */
public enum Setting 
{
	/**
	 * Setting for Auto. Auto is used when the plugin
	 * should automatically scale the difficulty for the player. 
	 */
	AUTO("auto"),
	
	/**
	 * Setting for Manual. Manual is used when the player
	 * should manually change their own difficulty settings.
	 */
	MANUAL("manual"),
	
	/**
	 * Setting for Off. Off is used when no dynamic difficulty
	 * should take place. Mob difficulty will behave as normal.
	 */
	DISABLED("disabled");
	
	
	private String name;
	
	
	Setting(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return a String representation of the Setting
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the Setting for the given settingName.
	 * @param settingName the name of the setting you want. Not
	 * case sensitive.
	 * @return the Setting with the given settingName or null if no
	 * Setting has the given settingName
	 */
	public static Setting getSetting(String settingName)
	{
		Setting setting = null;

		for(Setting tempSetting: Setting.values())
		{
			if(tempSetting.getName().equalsIgnoreCase(settingName))
			{
				setting = tempSetting;
			}
		}
		
		return setting;
	}
}
