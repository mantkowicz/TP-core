package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;
import com.mantkowicz.tg.network.Dictionary;
import com.mantkowicz.tg.network.Job;
import com.mantkowicz.tg.network.Result;

public class SplashScreen extends BaseScreen implements HttpResponseListener
{
	private Image splashImage;
	private Label label;
	
	HttpRequest hr;
	
	public SplashScreen(Main game)
	{
		super(game);
	}
	
	@Override
	protected void prepare()
	{		
		this.splashImage = this.createImage( "logo" );
		
		this.splashImage.getColor().a = 0;
		this.splashImage.addAction( ActionManager.getInstance().getAction(ActionType.SHOW) );
		this.setCenter(this.splashImage, -50);
		
		this.label = new Label("Tap to continue", this.game.skin, "italic");
		this.label.getColor().a = 0;
		this.setCenter(this.label, -150);
		
		this.nextScreen = new GameScreen( this.game );
//		this.nextScreen = new MenuScreen( this.game );
		this.stage.addListener(this.nextScreenListener);
		
		this.stage.addActor(this.splashImage);
		this.stage.addActor(this.label);
		
		hr = new HttpRequest();
		hr.setUrl("http://www.kerning.mantkowicz.pl/ws.php?action=getJobs");
		hr.setMethod(Net.HttpMethods.GET);
		hr.setContent("");
		Gdx.net.sendHttpRequest(hr, this);
	}

	@Override
	protected void step()
	{
		if( this.splashImage.getActions().size <= 0 )
		{
			if(this.label.getColor().a <= 0)
			{
				this.label.addAction( ActionManager.getInstance().getAction(ActionType.SHOW) );
			}
			else if(this.label.getColor().a >= 1)
			{
				this.label.addAction( ActionManager.getInstance().getAction(ActionType.HIDE) );
			}
		}
	}

	@Override
	public void handleHttpResponse(HttpResponse httpResponse) {

		final int statusCode = httpResponse.getStatus().getStatusCode();
		
		FileHandle fh = Gdx.files.local("files/jobs.json");
		
		byte[] fileBytes = httpResponse.getResult();
		
		fh.writeBytes(fileBytes, false);
		
		Logger.log(this, statusCode);
		
		Json json = new Json();
		Result result = json.fromJson(Result.class, Gdx.files.local("files/jobs.json"));
		
		Logger.log(this, result.value);
		
		
		Job[] jobs = new Job[]{};
		jobs = json.fromJson(Job[].class, result.value);
		
		Logger.log(this, jobs[0].date_start);
		
		Dictionary dict = json.fromJson( Dictionary.class, result.message);
		
		Logger.log(this, dict.users.get("1"));
		
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
