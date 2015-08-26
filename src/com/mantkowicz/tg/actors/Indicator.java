package com.mantkowicz.tg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.mantkowicz.tg.logger.Logger;

public class Indicator extends Actor
{
	Texture texture;
	
	public Indicator()
	{
		texture = new Texture( Gdx.files.internal("1.png") );
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		this.setSize(texture.getWidth(), texture.getHeight());
		
		this.addListener(listener);
		
		this.setOrigin(this.getWidth()/2, this.getHeight()/2);
		
		this.setUserObject( "DUPA" );
		this.debug();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		batch.draw(texture, this.getX(), this.getY());
	}
	
	DragListener listener = new DragListener()
	{
		public void drag(InputEvent event, float x, float y, int pointer)
		{
			Logger.log(1, event.getTarget().getUserObject() );
			Logger.log(8, x + ", " + y);
			Logger.log(4, Gdx.input.getX() + "; " + Gdx.input.getY());			
			event.getTarget().setX( event.getTarget().getX() + x - getWidth()/2 );
			event.getTarget().setY( event.getTarget().getY() + y - getHeight()/2 );
		}
	};
}
