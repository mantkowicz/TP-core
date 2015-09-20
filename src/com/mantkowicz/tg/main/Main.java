package com.mantkowicz.tg.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mantkowicz.tg.screens.SplashScreen;

public class Main extends Game 
{
	public static boolean isMobile = false;
	
	public final int SCREEN_WIDTH = 1280;
	public final int SCREEN_HEIGHT = 800;
	
	public AssetManager resourcesManager;
	public Skin skin;
	
	public int usr_id; //to simulate being logged in
		
	@Override
	public void create()
	{
		this.usr_id = 2;
		
		this.resourcesManager = new AssetManager();
		this.resourcesManager.load("skin.atlas", TextureAtlas.class);
		this.resourcesManager.finishLoading();
		
		this.skin = new Skin( Gdx.files.internal("skin.json") );
		//this.skin = new Skin( Gdx.files.internal("skin.json"), this.resourcesManager.get("skin.atlas", TextureAtlas.class) );
		
		SplashScreen splash = new SplashScreen( this );
		
		this.setScreen( splash );
	}
	
	@Override
	public void render() 
	{
		try
		{
			super.render();
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
}
