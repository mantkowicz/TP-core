package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.enums.HttpState;
import com.mantkowicz.tg.enums.ScreenPhase;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;
import com.mantkowicz.tg.managers.HttpManager;
import com.mantkowicz.tg.network.Result;


public class SplashScreen extends BaseScreen
{
	ScreenPhase phase = ScreenPhase.DOWNLOADING_JOBS;
	
	private Image splashImage;
	private Label label;
	
	private HttpManager manager;
	
	private Preferences preferences;
	
	private Dict dict;
	private Array<String> fontsToDownload;
		
	public SplashScreen(Main game)
	{
		super(game);
		
		manager = new HttpManager();
		manager.get("http://www.kerning.mantkowicz.pl/ws.php?action=getJobs&ignore_counter=1");
	}
	
	@Override
	protected void prepare()
	{		
		fontsToDownload = new Array<String>();
		
		preferences = Gdx.app.getPreferences("USERS");
		
		this.splashImage = this.createImage( "logo" );
		
		this.splashImage.getColor().a = 0;
		this.splashImage.addAction( ActionManager.getInstance().getAction(ActionType.SHOW) );
		this.setCenter(this.splashImage, -50);
		
		this.label = new Label("Loading jobs list", this.game.skin, "italic");
		label.setWidth(1000);
		label.setAlignment(Align.center);
		this.setCenter(this.label, -150);
		
		//this.nextScreen = new GameScreen( this.game );
		this.nextScreen = new MenuScreen( this.game );
		
		this.stage.addActor(this.splashImage);
		this.stage.addActor(this.label);
	}

	@Override
	protected void step()
	{	
		if(phase == ScreenPhase.DOWNLOADING_JOBS)
		{
			if( manager.state == HttpState.FINISHED && !manager.isResultNull() )
			{
				FileHandle fh = Gdx.files.local("files/jobs.json");
				fh.writeBytes(manager.getResult(), false);
				
				Json json = new Json();
				final Result result = json.fromJson(Result.class, Gdx.files.local("files/jobs.json"));
				dict = json.fromJson(Dict.class, result.message);
				
				for(String fontID : dict.fonts.keySet())
				{
					fontsToDownload.add(fontID);
				}
				
				for(String userID : dict.users.keySet())
				{
					preferences.putString(userID, dict.users.get(userID));
				}
				
				phase = ScreenPhase.DOWNLOADING_FONTS;
			}
		}

		else if(phase == ScreenPhase.DOWNLOADING_FONTS)
		{
			label.setText("Downloading font files");
			setCenter(label, -150);
			
			if(fontsToDownload.size == 0)
			{
				this.stage.addListener(this.nextScreenListener);
				
				this.label.setText("Tap to continue");
				this.label.addAction( this.getBlinkAction(1f, 0f, 0.75f) );
				
				phase = ScreenPhase.FINISHED;
			}
			else
			{
				if(manager.state == HttpState.IDLE)
				{
					if(fontsToDownload.size > 0)
					{
						String fontID = fontsToDownload.peek();
						
						if( !Gdx.files.local("files/fonts/"+fontID+"/font.ttf").exists())
						{
							manager.get("http://www.kerning.mantkowicz.pl/ws.php?action=getFont&id="+fontID);
						}
						else
						{
							fontsToDownload.pop();
						}
					}
				}
				else if( manager.state == HttpState.FINISHED && !manager.isResultNull() )
				{
					String fontID = fontsToDownload.pop();
					
					FileHandle fh = Gdx.files.local("files/fonts/"+fontID+"/font.ttf");
					fh.writeBytes(manager.getResult(), false);
				}
			}
		}

	}

}
