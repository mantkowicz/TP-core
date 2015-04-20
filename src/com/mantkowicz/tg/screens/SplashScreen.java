package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;

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
		//this.nextScreen = new MenuScreen( this.game );
		this.stage.addListener(this.nextScreenListener);
		
		this.stage.addActor(this.splashImage);
		this.stage.addActor(this.label);
		
		hr = new HttpRequest();
		hr.setUrl("http://www.mantkowicz.pl/tp/font.ttf");
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
		
		FileHandle fh = Gdx.files.local("font.ttf");
		
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
