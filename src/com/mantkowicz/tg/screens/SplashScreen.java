package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.enums.HttpState;
import com.mantkowicz.tg.enums.ScreenPhase;
import com.mantkowicz.tg.json.Font;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.json.User;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;
import com.mantkowicz.tg.managers.HttpManager;
import com.mantkowicz.tg.managers.JobHandler;
import com.mantkowicz.tg.network.FontResult;
import com.mantkowicz.tg.network.JobResult;
import com.mantkowicz.tg.network.Response;
import com.mantkowicz.tg.network.UserResult;


public class SplashScreen extends BaseScreen
{
	ScreenPhase phase = ScreenPhase.DOWNLOADING_JOBS;
	
	private Image splashImage;
	private Label label;
	
	private HttpManager manager;
	
	private Dict dict;
	private Array<String> fontsToDownload;
	
	private Array<Font> fonts;
	private float fontsCount;
		
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
		
		this.splashImage = this.createImage( "logo" );
		
		this.splashImage.getColor().a = 0;
		this.splashImage.addAction( ActionManager.getInstance().getAction(ActionType.SHOW) );
		this.setCenter(this.splashImage, -50);
		
		this.label = new Label("Loading jobs list", this.game.skin, "italic");
		label.setWidth(1000);
		label.setAlignment(Align.center);
		this.setCenter(this.label, -150);
		
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
				Json json = new Json();
				final Response response = json.fromJson(Response.class, manager.getResponse());
				
				final JobResult result = json.fromJson(JobResult.class, response.value);
								
				JobHandler.getInstance().refreshJobs(result.value);
				
				JobHandler.getInstance().printJobs();
				
				phase = ScreenPhase.DOWNLOADING_USERS_LIST;
				
				manager.get("http://www.kerning.mantkowicz.pl/ws.php?action=getUsers");
			}
		}
		
		if(phase == ScreenPhase.DOWNLOADING_USERS_LIST)
		{
			label.setText("Downloading users list");
			setCenter(label, -150);
			
			if( manager.state == HttpState.FINISHED && !manager.isResultNull() )
			{
				Json json = new Json();
				final Response response = json.fromJson(Response.class, manager.getResponse());

				UserResult result = json.fromJson(UserResult.class, response.value);
								
				JobHandler.getInstance().refreshUsers(result.value);
				
				JobHandler.getInstance().printUsers();
				
				phase = ScreenPhase.DOWNLOADING_FONTS_LIST;
				
				manager.get("http://www.kerning.mantkowicz.pl/ws.php?action=getFonts");
			}
		}

		else if(phase == ScreenPhase.DOWNLOADING_FONTS_LIST)
		{
			label.setText("Downloading font files list");
			setCenter(label, -150);
			
			if( manager.state == HttpState.FINISHED && !manager.isResultNull() )
			{
				Json json = new Json();
				final Response response = json.fromJson(Response.class, manager.getResponse());				
				
				FontResult result = json.fromJson(FontResult.class, response.value);
				
				fonts = result.value;
				fontsCount = fonts.size;
				
				JobHandler.getInstance().refreshFonts(result.value);
				JobHandler.getInstance().printFonts();
				
				phase = ScreenPhase.DOWNLOADING_FONTS;
			}
		}
		
		else if(phase == ScreenPhase.DOWNLOADING_FONTS)
		{
			label.setText("Downloading font files (" + String.valueOf( (int) ( ((fontsCount - fonts.size) / fontsCount) * 100 )  ) + "%)");
			setCenter(label, -150);
			
			if(fonts.size == 0)
			{
				this.stage.addListener(this.nextScreenListener);
				
				this.label.setText("Tap to continue");
				this.label.addAction( this.getBlinkAction(1f, 0f, 0.75f) );
				
				phase = ScreenPhase.FINISHED;
			}
			else
			{
				if( fonts.size > 0 )
				{
					if( !Gdx.files.local("files/fonts/" + fonts.peek().id + "/font.ttf").exists() )
					{
						if(manager.state == HttpState.IDLE)
						{
							fonts.peek().download(manager);
							
						}
						else if( manager.state == HttpState.FINISHED && !manager.isResultNull() )
						{
							fonts.pop().save(manager);
						}
						else
						{
							//pass
						}
					}
					else
					{
						fonts.pop();
					}
				}
			}
		}

	}

}
