package com.mantkowicz.tg.logger;

import java.util.HashMap;

import com.mantkowicz.tg.enums.LogLevel;

public class Logger
{
	static HashMap<LogLevel, Boolean> log;
	
	static
	{
		log = new HashMap<LogLevel, Boolean>();
		
		log.put(LogLevel.LOG, true);
		log.put(LogLevel.DEBUG, true);
		log.put(LogLevel.ERROR, true);
	}
	
	public static void log(Object object, Object message)
	{
		log(object, message, LogLevel.LOG);
	}
	
	public static void log(Object object, Object message, LogLevel logLevel)
	{
		if(log.get(logLevel))
		{
			System.out.println("|" + logLevel.toString() + "|" + object.getClass() + "| " + message.toString());
		}
	}
}
