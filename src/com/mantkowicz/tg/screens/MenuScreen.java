package com.mantkowicz.tg.screens;

import com.badlogic.ashley.signals.Listener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.main.Main;

public class MenuScreen extends BaseScreen implements HttpResponseListener
{
	static class Data {

	    public Array<Job> jobs;

	}

	static class Job
	{
		public int id;
		public String points;
		public String usr_id;
		public String date_start;
		public String date_end;
		public String ofr_id;
		public String font_name;
		public String user_name;
	}
	
	boolean gotFont = false;
	
	int jobId;
	
	HttpRequest hr;
	
	MenuScreen menuScreen;
	
	Image logo;
	TextButton start, settings;
	
	public MenuScreen(Main game)
	{
		super(game);
		
		this.menuScreen = this;
	}

	@Override
	protected void prepare() 
	{
		logo = this.createImage("logo");
		logo.setPosition(-300, 100);
		
		this.start = new TextButton("start", this.game.skin, "start");
		this.start.setPosition(-200, -40);
		/*
		this.settings = new TextButton("settings", this.game.skin, "settings");
		this.settings.setPosition(-200, -160);
		
		this.nextScreen = new GameScreen( this.game );
		this.start.addListener( this.nextScreenListener );
				
			
		
		
		this.stage.addActor(this.settings); */
		Json json = new Json();
		final Data list = json.fromJson(Data.class, Gdx.files.local("files/jobs.json"));
		
		this.start.addListener(
				new ClickListener() 
				  {
					  public void clicked(InputEvent event, float x, float y)
				 	  {
							hr = new HttpRequest();
							
							jobId = list.jobs.first().id;
							
							hr.setUrl("http://www.mantkowicz.pl/tp/ws.php?action=getJobFontFile&id=" + jobId);
							hr.setMethod(Net.HttpMethods.GET);
							hr.setContent("");
							Gdx.net.sendHttpRequest(hr, menuScreen);
				 	  }
				  }
		);
		
		
		this.stage.addActor(this.logo);
		this.stage.addActor(this.start);
		
		

		
		
		
		
		
	}

	@Override
	protected void step() 
	{
	}

	@Override
	public void handleHttpResponse(HttpResponse httpResponse) {
	
		final int statusCode = httpResponse.getStatus().getStatusCode();
		
		FileHandle fh = null;
				
		if(!gotFont)
		{
			gotFont = true;
			
			fh = Gdx.files.local("files/font.ttf");
			
			hr = new HttpRequest();
			hr.setUrl("http://www.mantkowicz.pl/tp/ws.php?action=getJobPropertiesFile&id=" + jobId);
			hr.setMethod(Net.HttpMethods.GET);
			hr.setContent("");
			Gdx.net.sendHttpRequest(hr, menuScreen);
		}
		else
		{
			fh = Gdx.files.local("files/properties.json");
		}
		
		byte[] fileBytes = httpResponse.getResult();
		
		fh.writeBytes(fileBytes, false);
		
		Logger.log(this, statusCode);
		
	}

	@Override
	public void failed(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelled() {
		// TODO Auto-generated method stub
		
	}
}
