package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.HttpManager;

public class MenuScreen extends BaseScreen implements HttpResponseListener
{
	static class Result
	{
		public int status;
		public String value;
		public String message;
	}
	
	static class Data 
	{
	    public Array<Job> jobs;
	}

	static class Job
	{
		public int id;
		public int usr_id;
		public int fnt_id;
		public int points;
		public String date_start;
		public String date_end;
		public String properties;
	}
	
	HttpManager manager;
	
	boolean gotFont = false;
	
	int jobId;
	
	HttpRequest hr;
	
	MenuScreen menuScreen;
	
	
	public MenuScreen(Main game)
	{
		super(game);
		
		this.menuScreen = this;
		
		manager = new HttpManager();
	}

	@Override
	protected void prepare() 
	{
		Json json = new Json();
		final Result result = json.fromJson(Result.class, Gdx.files.local("files/jobs.json"));
		final Data data = json.fromJson(Data.class, "{jobs : " + result.value + "}");
		
		Label title = new Label("Wybierz zlecenie", game.skin, "medium");
		setCenter(title, 350);
		
		Table table = new Table();
		table.debug();
				
		for(Job job : data.jobs)
		{
			table.add( new Label("#" + String.valueOf(job.id), game.skin) ).width(800).height(50).pad(25, 0, 0, 0);
			table.add( new Label(String.valueOf(job.points), game.skin) ).width(300).height(50).pad(25, 0, 0, 0);
			table.row();
			table.add( new Label("#" + String.valueOf(job.id), game.skin) ).width(1100).height(50).pad(25);
			table.row();
		}
		
		ScrollPane container = this.createScroll(table, 1200, 700, true);
		container.setPosition(-container.getWidth()/2f, -container.getHeight()/2f - 60);
		
		stage.addActor(container);
		stage.addActor(title);
	
		/*	
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
		)*/	
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
