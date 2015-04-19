package com.mantkowicz.tg.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;

public class SplashScreen extends BaseScreen
{
	private Image splashImage;
	private Label label;
	
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
		
		this.nextScreen = new MenuScreen( this.game );
		this.stage.addListener(this.nextScreenListener);
		
		this.stage.addActor(this.splashImage);
		this.stage.addActor(this.label);
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
}
