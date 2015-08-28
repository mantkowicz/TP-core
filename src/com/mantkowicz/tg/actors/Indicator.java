package com.mantkowicz.tg.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.enums.IndicatorType;
import com.mantkowicz.tg.logger.Logger;

public class Indicator extends Actor
{
	IndicatorType indicatorType;
	
	Texture texture;
	boolean up = false;
	float currentX, currentY;
	
	private FloatArray lWidths;
	
	Array<Vector2> grid = new Array<Vector2>();
	
	Array<Label> glyphs;
	
	int currentId = 0;
	
	int minId = -1;
	int maxId = -1;
	
	float currentGlyphWidth = 0;
	
	public Indicator(IndicatorType indicatorType)
	{	
		this.indicatorType = indicatorType;
		
		if(this.indicatorType == IndicatorType.START)
		{
			texture = new Texture( Gdx.files.internal("1.png") );
		}
		else
		{
			texture = new Texture( Gdx.files.internal("2.png") );
		}
		
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		this.setSize(texture.getWidth()*2, texture.getHeight()*2);
		
		this.addListener(listener);
				
		this.setUserObject( "DUPA" );
				
		this.debug();
	}
	
	public void updateGrid()
	{
		grid.clear();
		lWidths.clear();
		
		for(Label g : glyphs)
		{
			if(g.getText().chars[0] != '\n')
			{
				//log( g.getText().toString() + g.getX() + ", " + g.getY() );
				
				float x = g.getStyle().font.getDescent();
				
				grid.add(new Vector2( g.getX(), g.getY() - x ));
				
				lWidths.add(g.getWidth());
			}
			else
			{
				grid.add(null);
				
				lWidths.add(0);
			}
		}
	}
	
	public void setGrid(Array<Label> newGrid)
	{
		glyphs = newGrid;
		lWidths = new FloatArray(); 
		
		updateGrid();
		
		if(this.indicatorType == IndicatorType.START)
		{
			setPosition(grid.first().x, grid.first().y);
		}
		else
		{
			setPosition(grid.peek().x, grid.peek().y);
		}
		
		
		updatePosition();
		
		setPosition(currentX - getWidth()/2, currentY - getHeight()/2);
	}
		
	@Override
	public void act(float delta)
	{
		this.toFront();
		
		if( glyphs != null )
		{		
			updateGrid();
			updatePosition();
		}
		//Logger.log(1, currentX + ", " + currentY);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		if(this.indicatorType == IndicatorType.START)
		{
			batch.draw(texture, currentX - texture.getWidth(), currentY - texture.getHeight());
		}
		else
		{
			batch.draw(texture, currentX + currentGlyphWidth, currentY - texture.getHeight());
		}
		
	}
	
	public void setMin(int id)
	{
		this.minId = id;
	}
	
	public void setMax(int id)
	{
		this.maxId = id;
	}
	
	
	private void updatePosition()
	{
		//currentX = getX() + getWidth()/2;
		//currentY = getY() + getHeight()/2;
		float nextX = 0, nextY = 0;
		
		Vector2 posVec = new Vector2(getX(), getY());
		Vector2 target = new Vector2(0, 1000000);

		int vCtr = 0;
		
		for(Vector2 v : grid)
		{
			boolean outOfBounds = false;
			
			if( minId != -1 )
			{
				if( vCtr < minId ) outOfBounds = true;
			}
			
			if( maxId != -1 )
			{
				if( vCtr > maxId ) outOfBounds = true;
			}
		
			if(v != null && !outOfBounds )
			{
				//Logger.log(1, v.x + " ! " + v.y);
				//Logger.log("", v.dst(posVec) + " | " + target.dst(posVec));
				
				if( v.dst(posVec) < target.dst(posVec) )
				{
					nextX = v.x;
					nextY = v.y;
					
					target = v;
					
					currentId = vCtr;
					
					if(lWidths != null) currentGlyphWidth = lWidths.get(vCtr);
				}
				else if( v.dst(posVec) == target.dst(posVec) )
				{
					if( v.x > target.x && v.y > target.y )
					{
						nextX = v.x;
						nextY = v.y;
						
						target = v;
						
						currentId = vCtr;
						
						if(lWidths != null) currentGlyphWidth = lWidths.get(vCtr);
					}
				}
			}
			
			vCtr++;
		}
		
		setPosition(nextX, nextY);
		
	}
	
	public int getCurrentId()
	{
		return currentId;
	}
	
	DragListener listener = new DragListener()
	{
		public void dragStart(InputEvent event, float x, float y, int pointer)
		{
			currentX = getX() + getWidth()/2;
			currentY = getY() + getHeight()/2;
		}
		
		public void drag(InputEvent event, float x, float y, int pointer)
		{		
			currentX += x;
			currentY += y;
		}
		
		public void dragStop(InputEvent event, float x, float y, int pointer)
		{
		}
	};
}
