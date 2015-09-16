package com.mantkowicz.tg.managers;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.json.Font;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.json.User;

public class JobHandler
{
	private static JobHandler INSTANCE = new JobHandler();
	public static JobHandler getInstance()
	{
		return INSTANCE;
	}
	
	public Array<Job> jobs;
	public Array<User> users;
	public Array<Font> fonts;
	
	private JobHandler()
	{
		jobs = new Array<Job>();
		users = new Array<User>();
		fonts = new Array<Font>();
	}
	
	public void refreshJobs(Array<Job> jobsList)
	{
		jobs = jobsList;
	}
	
	public void refreshUsers(Array<User> usersList)
	{
		users = usersList;
	}
	
	public void refreshFonts(Array<Font> fontsList)
	{
		fonts = new Array<Font>();
		for(Font font : fontsList) fonts.add(font);
	}
		
	public User getUser(int id)
	{
		for(User user : users)
		{
			if(user.id == id)
			{
				return user;
			}
		}
		
		return null;
	}
	
	public Font getFont(int id)
	{
		for(Font font : fonts)
		{
			if(font.id == id)
			{
				return font;
			}
		}
		
		return null;
	}
}
