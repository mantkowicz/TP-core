package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mantkowicz.tg.main.Main;

public class ResultScreen extends BaseScreen
{	
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
		
		/*if( image.getWidth() < 600 && image.getHeight() < 760)
		{
			//pass
		}
		else if( image.getWidth() < 600 && image.getHeight() > 760)
		{
			image.setScale( 760 / image.getHeight() );
		}
		else if( image.getWidth() > 600 && image.getHeight() < 760)
		{
			image.setScale( 600 / image.getWidth() );
		}
		else
		{
			image.setScale( (image.getWidth() > image.getHeight()) ? (600 / image.getWidth()) : (760 / image.getHeight())  );
		}
		
		image.setPosition(-640 + (640-image.getWidth()*image.getScaleX())/2f, -400 + (800 - image.getHeight()*image.getScaleY())/2f);
		
		Table table = new Table();
		table.setSize(600, 600);
		table.setPosition(0, -300);
		
		table.add( createLabel("Twoje wyniki:", "big") ).colspan(2).width(table.getWidth() - 40).pad(20);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.add( createRightLabel("kryterium nr 1:", "medium") ).width(table.getWidth()/2 - 50).pad(0, 50, 20, 0);
		table.add( createLabel("12", "medium") ).width(table.getWidth()/2 - 10).pad(0, 10, 20, 0);
		table.row();
		
		table.debug();
		*/
		
		image.setPosition(-image.getWidth()/2f,  -image.getHeight()/2f);
		((OrthographicCamera)stage.getCamera() ).zoom *= 1/2f;
		stage.addActor(image);
		//stage.addActor(table);
	}

	@Override
	protected void step()
	{
		stage.getBatch().setColor(Color.BLUE);
	}
}
