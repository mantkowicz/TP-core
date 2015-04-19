package com.mantkowicz.tg.screens;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mantkowicz.tg.main.Main;

public class MenuScreen extends BaseScreen
{
	Image logo;
	TextButton start, settings;
	
	public MenuScreen(Main game)
	{
		super(game);
	}

	@Override
	protected void prepare() 
	{
		logo = this.createImage("logo");
		logo.setPosition(-300, 100);
		
		this.start = new TextButton("start", this.game.skin, "start");
		this.start.setPosition(-200, -40);
		
		this.settings = new TextButton("settings", this.game.skin, "settings");
		this.settings.setPosition(-200, -160);
		
		this.nextScreen = new GameScreen( this.game );
		this.start.addListener( this.nextScreenListener );
				
		this.stage.addActor(this.logo);	
		this.stage.addActor(this.start);
		this.stage.addActor(this.settings);
	}

	@Override
	protected void step() 
	{
	}
}
