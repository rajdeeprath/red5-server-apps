package org.red5.core.utils;

public class DVRUtils {

	public static String getStreamFullName(String name)
	{
		String fullName = null;
		
		if(name.indexOf(":") == -1)
		{
			fullName = "flv" + ":" + name;
		}
		else
		{
			fullName = name;
		}
		
		return fullName;
	}
}
