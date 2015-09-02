package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.ActionManager;

public abstract class BaseScreen implements Screen 
{
	protected Main game;
	
	protected Stage stage;
	protected ExtendViewport viewport;
	
	protected int screenWidth;
	protected int screenHeight;
	
	boolean changeScreen = false;
	boolean clearWithGray = false;
	
	BaseScreen nextScreen;
	ClickListener nextScreenListener;
	
	protected abstract void prepare();
	protected abstract void step();
	
	public BaseScreen(Main game)
	{
		this.game = game;
		
		this.screenWidth = this.game.SCREEN_WIDTH;
		this.screenHeight = this.game.SCREEN_HEIGHT;
		
		this.nextScreenListener = new ClickListener() 
								  {
									  public void clicked(InputEvent event, float x, float y)
								 	  {
										  changeScreen = true;
								 	  }
								  };
	}
		
	@Override
	public void show() 
	{
		this.viewport = new ExtendViewport(this.screenWidth, this.screenHeight);
		
		this.stage = new Stage();	

		this.stage.setViewport(this.viewport);
		Gdx.input.setInputProcessor(this.stage);
		
		this.prepare();
	}

	@Override
	public void render(float delta) 
	{
		this.handleInput();		
		this.handleChangingScreen();
		
		if( clearWithGray )
		{
			Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		}
		else
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
		}
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.viewport.update(this.screenWidth, this.screenHeight);
		this.stage.act();
		this.stage.draw();
		
		this.step();
	}
	
	@Override
	public void resize(int width, int height) 
	{
		this.screenWidth = width;
		this.screenHeight = height;
	}

	@Override
	public void pause() 
	{
	}

	@Override
	public void resume() 
	{
	}

	@Override
	public void hide() 
	{	
	}

	@Override
	public void dispose() 
	{
	}
	
	private void handleInput()
	{
		if( Gdx.input.isKeyPressed( Keys.ESCAPE ) )
		{
			Gdx.app.exit();
		}
	}
	
	private void handleChangingScreen() 
	{
		if(this.changeScreen)
		{
			this.changeScreen = false;
			
			this.stage.addAction(ActionManager.getInstance().getAction( ActionType.HIDE_STAGE ));
		}
		
		if(ActionManager.getInstance().isActionFinished(ActionType.HIDE_STAGE))
		{
			ActionManager.getInstance().getAction( ActionType.HIDE_STAGE ).reset();
			
			this.game.setScreen(this.nextScreen);
		}
	}
	
	protected Image createImage(String regionName)
	{
		return createImage(regionName, true);
	}
	
	protected Image createImage(String regionName, boolean fixed)
	{
		AtlasRegion atlasRegion = this.getAtlasRegion(regionName);
		
		Image image =  new Image(atlasRegion);
		
		if(fixed)
		{
			image.setScaling(Scaling.none);
			image.setAlign(Align.center);
		}
		
		return image;
	}
	
	protected AtlasRegion getAtlasRegion(String regionName)
	{
		Array<TextureAtlas> textureAtlases = new Array<TextureAtlas>();
		this.game.resourcesManager.getAll(TextureAtlas.class, textureAtlases);
		
		for(TextureAtlas atlas: textureAtlases)
		{
			if( atlas.findRegion(regionName) != null )
			{
				return atlas.findRegion( regionName );
			}
		}
		log(regionName + " nie znaleziono!");
		return null;
	}
	
	protected RepeatAction getBlinkAction(float maxAlpha, float minAlpha, float duration)
	{
		AlphaAction showAction = new AlphaAction();
		showAction.setAlpha(maxAlpha);
		showAction.setDuration(duration);
		
		AlphaAction hideAction = new AlphaAction();
		hideAction.setAlpha(minAlpha);
		hideAction.setDuration(duration);
		
		return Actions.forever( new SequenceAction(hideAction, showAction) );
	}
	
	protected ScrollPane createScroll(Table table, float width, float height, boolean vertical)
	{
		final ScrollPane scroller = new ScrollPane(table, game.skin);
				
		if(vertical)
		{
			scroller.setScrollingDisabled(true, false);
		}
		else
		{
			scroller.setScrollingDisabled(false, true);
		}
		
        scroller.setFadeScrollBars(false);
        scroller.setSize(width, height);
        
        return scroller;
	}
	
	protected void setCenter(Actor actor, float y)
	{
		actor.setPosition(-(actor.getWidth() / 2.0f), y);
	}
	
	protected void log(Object message)
	{
		Logger.log(this, message);
	}
	
	protected void debug(Object message)
	{
		Logger.log(this, message);
	}
	
	protected void error(Object message)
	{
		Logger.log(this, message);
	}
}
