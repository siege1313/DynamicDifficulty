package com.cjmcguire.bukkit.dynamic.commands.scale;

/**
 * The BooleanSetting Enum is used to represent the boolean values
 * "true" and "false" while still allowing the boolean value to be
 * "null". It provides methods to match to the Enums to Strings as
 * well.
 * @author CJ McGuire
 */
public enum BooleanSetting 
{
	/**
	 * BooleanSetting for true. 
	 */
	TRUE("true", true),
	
	/**
	 * BooleanSetting for false.
	 */
	FALSE("false", false);
	
	
	private String name;
	private boolean value;
	
	
	BooleanSetting(String name, boolean value)
	{
		this.name = name;
		this.value = value;
	}
	
	/**
	 * @return a String representation of the BooleanSetting
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @return the boolean value of the BooleanSetting
	 */
	public boolean getValue()
	{
		return value;
	}
	
	/**
	 * Gets the BooleanSetting for the given booleanSettingName.
	 * @param booleanSettingName the name of the BooleanSetting you 
	 * want. Not case sensitive.
	 * @return the BooleanSetting with the given booleanSettingName 
	 * or null if no BooleanSetting has the given booleanSettingName
	 */
	public static BooleanSetting getBooleanSetting(String booleanSettingName)
	{
		BooleanSetting booleanSetting = null;

		for(BooleanSetting tempSetting: BooleanSetting.values())
		{
			if(tempSetting.getName().equalsIgnoreCase(booleanSettingName))
			{
				booleanSetting = tempSetting;
			}
		}
		
		return booleanSetting;
	}
}
