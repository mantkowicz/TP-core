package com.mantkowicz.tg.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mantkowicz.tg.screens.SplashScreen;

public class Main extends Game 
{
	public final int SCREEN_WIDTH = 1366;
	public final int SCREEN_HEIGHT = 768;
	
	public AssetManager resourcesManager;
	public Skin skin;
	
	@Override
	public void create()
	{
		this.resourcesManager = new AssetManager();
		this.resourcesManager.load("skin.atlas", TextureAtlas.class);
		this.resourcesManager.finishLoading();
		
		this.skin = new Skin( Gdx.files.internal("skin.json"), this.resourcesManager.get("skin.atlas", TextureAtlas.class) );
		
		SplashScreen splash = new SplashScreen( this );
		
		this.setScreen( splash );
	}
}
