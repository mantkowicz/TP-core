package com.mantkowicz.tg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.logger.Logger;

public class Indicator extends Actor
{
	Texture texture;
	
	float currentX, currentY;
	
	Array<Vector2> grid = new Array<Vector2>();
		
	public Indicator()
	{
		for(int i = -1000; i < 1000; i+=10)
		{
			for(int k = -1000; k < 1000; k+=10)
			{
				grid.add(new Vector2(i,k));
			}
		}
		//----
		
		texture = new Texture( Gdx.files.internal("1.png") );
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		this.setSize(texture.getWidth(), texture.getHeight());
		
		this.addListener(listener);
		
		this.setOrigin(this.getWidth(), this.getHeight());
		
		this.setUserObject( "DUPA" );
		this.debug();
	}
	
	public void setGrid(Array<Vector2> newGrid)
	{
		for(Vector2 v : newGrid) Logger.log(1, v.x + ", " + v.y);
		
		this.grid = newGrid;
	}
	
	@Override
	public void act(float delta)
	{
		updatePosition();
		this.toFront();
		//Logger.log(1, currentX + ", " + currentY);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		batch.draw(texture, currentX - texture.getWidth(), currentY - texture.getHeight());
	}
	
	private void updatePosition()
	{
		currentX = getX();
		currentY = getY();
		
		Vector2 posVec = new Vector2(getX(), getY());
		Vector2 target = new Vector2(0, 1000000);

		
		
		for(Vector2 v : grid)
		{
			//Logger.log(1, v.x + " ! " + v.y);
			//Logger.log("", v.dst(posVec) + " | " + target.dst(posVec));
			
			if( v.dst(posVec) < target.dst(posVec) )
			{
				currentX = v.x;
				currentY = v.y;
				
				target = v;
			}
			else if( v.dst(posVec) == target.dst(posVec) )
			{
				if( v.x > target.x && v.y > target.y )
				{
					currentX = v.x;
					currentY = v.y;
					
					target = v;
				}
			}
		}
	}
	
	DragListener listener = new DragListener()
	{
		public void drag(InputEvent event, float x, float y, int pointer)
		{		
			event.getTarget().setX( event.getTarget().getX() + x );
			event.getTarget().setY( event.getTarget().getY() + y );
		}
		
		public void dragStop(InputEvent event, float x, float y, int pointer)
		{
			setPosition(currentX, currentY);
			//Logger.log(3, "GORA");
		}
	};
}
