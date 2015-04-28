package com.mantkowicz.tg.managers;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.mantkowicz.tg.logger.Logger;

public class JsonManager 
{
	class Jobs
	{
		
	}
	
	public void f()
	{
		Json json = new Json();
		//json.fromJson(Jobs.class, Gdx.files.local("files/jobs.json"));
		
		
		ArrayList<Json> list = json.fromJson(ArrayList.class, Gdx.files.local("files/jobs.json"));
		
		for(Json j : list)
		{
			Logger.log(this, "JSON : " + j);
		}
		
	}
}
