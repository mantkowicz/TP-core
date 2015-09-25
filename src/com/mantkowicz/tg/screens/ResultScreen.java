package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.RateManager;
import com.mantkowicz.tg.managers.RateManager.Criterium;

public class ResultScreen extends BaseScreen
{	
	Label label;
	
	public ResultScreen(Main game)
	{
		super(game);
	}

	@Override
	protected void prepare()
	{
		Texture texture = new Texture( Gdx.files.local("files/temp.png") );
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Image image = new Image( texture );
		
		Texture riverTexture = new Texture( Gdx.files.local("files/temp2.png") );
		riverTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Image river = new Image( riverTexture );
		
		if( image.getWidth() < 600 && image.getHeight() < 760)
		{
			//pass
		}
		else if( image.getWidth() < 600 && image.getHeight() > 760)
		{
			image.setScale( 760 / image.getHeight() );
			river.setScale( 760 / image.getHeight() );
		}
		else if( image.getWidth() > 600 && image.getHeight() < 760)
		{
			image.setScale( 600 / image.getWidth() );
			river.setScale( 600 / image.getWidth() );
		}
		else
		{
			image.setScale( (image.getWidth() > image.getHeight()) ? (600 / image.getWidth()) : (760 / image.getHeight())  );
			river.setScale( (image.getWidth() > image.getHeight()) ? (600 / image.getWidth()) : (760 / image.getHeight())  );
		}
		
		image.setPosition(-640 + (640-image.getWidth()*image.getScaleX())/2f, -400 + (800 - image.getHeight()*image.getScaleY())/2f);
		river.setPosition(-640 + (640-image.getWidth()*image.getScaleX())/2f, -400 + (800 - image.getHeight()*image.getScaleY())/2f);
		
		Table table = createResultTable();
				
		river.getColor().a = 0.4f;
		
		this.label = new Label("Dotknij aby rozpocz¹æ od nowa", this.game.skin, "medium");
		label.setAlignment(Align.center);
		this.setCenter(this.label, -370);
		label.setX( label.getX() + game.SCREEN_WIDTH/4f );
		
		this.label.addAction( this.getBlinkAction(1f, 0f, 0.75f) );	
		
		stage.addActor(image);
		stage.addActor(river);
		stage.addActor(label);
		
		stage.addActor(table);
		
		this.nextScreen = new SplashScreen(game);
		this.stage.addListener(this.nextScreenListener);
	}

	private Table createResultTable() 
	{
		Table table = new Table();
		table.setSize(600, 600);
		table.setPosition(20, -280);
		
		Table headTable = new Table();
		headTable.setBackground( new TextureRegionDrawable( getAtlasRegion("background_g30") ) );
				
		headTable.add( createCenterLabel("Kryterium", "medium", Color.BLACK) ).width(table.getWidth()/2 - 50).pad(0, 0, 0, 20);
		headTable.add( createCenterLabel("Liczba", "medium", Color.BLACK) ).width(table.getWidth()/4 - 10).pad(0, 0, 0, 0);
		headTable.add( createCenterLabel("Kara", "medium", Color.BLACK) ).width(table.getWidth()/4 - 10).pad(0, 20, 0, 0);
		headTable.row();
		
		table.add(headTable).colspan(3).pad(0, 0, 0, 0);
		table.row();
		
		for(Criterium criterium : RateManager.getInstance().criterias)
		{
			table.add( createCenterLabel(criterium.name, "smallWhite") ).width(table.getWidth()/2 - 50).pad(0, 0, 0, 20);
			table.add( createCenterLabel(String.valueOf( criterium.count ), "medium") ).width(table.getWidth()/4 - 10).pad(0, 0, 0, 0);
			table.add( createCenterLabel(String.valueOf( criterium.getScore() ), "medium") ).width(table.getWidth()/4 - 10).pad(0, 20, 0, 0);
			table.row();
		}
		
		Table footTable = new Table();
		footTable.setBackground( new TextureRegionDrawable( getAtlasRegion("background_g30") ) );
				
		footTable.add( createCenterLabel("SUMA", "medium", Color.BLACK) ).width(table.getWidth()/2 + table.getWidth()/4 - 60).pad(0, 20, 0, 0);
		
		Table sumTable = new Table();
		
		int score = RateManager.getInstance().getCurrentRate();
		
		if(score > 0.75f * RateManager.getInstance().MAX_SCORE)
		{
			sumTable.setBackground( new TextureRegionDrawable( getAtlasRegion("background_green") ) );
		}
		else if(score > 0.50f * RateManager.getInstance().MAX_SCORE)
		{
			sumTable.setBackground( new TextureRegionDrawable( getAtlasRegion("background_yellow") ) );
		}
		else
		{
			sumTable.setBackground( new TextureRegionDrawable( getAtlasRegion("background_red") ) );
		}
		
		sumTable.add( createCenterLabel(String.valueOf(score), "medium", Color.BLACK) );
		
		footTable.add(sumTable).width(table.getWidth()/4 - 10).pad(0, 20, 0, 0);
		
		footTable.row();
		
		table.add(footTable).colspan(3).pad(0, 0, 0, 0);
		table.row();
		
		return table;
	}

	@Override
	protected void step()
	{
		stage.getBatch().setColor(Color.BLUE);
	}
}
