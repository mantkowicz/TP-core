package com.mantkowicz.tg.managers;

import com.mantkowicz.tg.actors.Paragraph;

public class RateManager
{
	private static RateManager INSTANCE = new RateManager();
	public static RateManager getInstance()
	{
		return INSTANCE;
	}
	
	private RateManager()
	{
		
	}
	
	public int rate(Paragraph paragraph)
	{
		return 500;
	}
}
