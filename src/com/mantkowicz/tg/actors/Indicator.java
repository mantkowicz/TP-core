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
	
	private FloatArray lWidths;
	
	Array<Vector2> grid = new Array<Vector2>();
	
	Array<Label> glyphs;
	
	int currentId = 0;
	
	Integer minId = null;
	Integer maxId = null;
	
	boolean isBeingDragged = false;
	
	Vector2 sDragVec = new Vector2();
	Vector2 cDragVec = new Vector2();
	
	public Indicator(IndicatorType indicatorType)
	{	
		this.indicatorType = indicatorType;
		
		if(this.indicatorType == IndicatorType.START)
		{
			texture = new Texture( Gdx.files.internal("1.png") );
			setUserObject("dupa");
		}
		else
		{
			texture = new Texture( Gdx.files.internal("2.png") );
		}
		
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		this.setSize(texture.getWidth()*2, texture.getHeight()*2);
		
		this.addListener(listener);
				
		//this.debug();
	}
	
	@Override
	public float getX()
	{
		return super.getX() + getWidth()/2f;
	}
	
	@Override
	public float getY()
	{
		return super.getY() + getHeight()/2f;
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		super.setPosition(x - getWidth()/2f, y - getHeight()/2f);
	}
	
	
	
	
	public void updateGrid()
	{
		grid.clear();
		lWidths.clear();
		
		for(Label g : glyphs)
		{
			if(g.getText().chars[0] != '\n')
			{				
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
			currentId = 0;
		}
		else
		{
			currentId = grid.size - 1;
		}
	}
		
	@Override
	public void act(float delta)
	{
		this.toFront();

		if( glyphs != null && !isBeingDragged )
		{
			updateGrid();
			changePosition(1);
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) 
	{
		if(this.indicatorType == IndicatorType.START)
		{
			batch.draw(texture, grid.get(currentId).x - texture.getWidth(), grid.get(currentId).y - texture.getHeight() );
		}
		else
		{
			batch.draw(texture, grid.get(currentId).x + lWidths.get(currentId), grid.get(currentId).y - texture.getHeight() );
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
	
	private void changePosition( int x)
	{
		if( indicatorType == IndicatorType.START )
		{
			setPosition(grid.get(currentId).x, grid.get(currentId).y);
		}
		else
		{
			setPosition(grid.get(currentId).x + lWidths.get(currentId), grid.get(currentId).y);
		}
	}
	
	private void updateIndex(int x)
	{
		Vector2 target = new Vector2(0, 1000000);

		for(int i = 0; i < grid.size; i++)
		{
			Vector2 v = grid.get(i);
			
			if( (v == null) || (minId != null && i < minId) || (maxId != null && i > maxId) ) 
			{
				continue;
			}
			
			float vD = v.dst(cDragVec);
			float tD = target.dst(cDragVec);
			
			if( (vD < tD) || (vD == tD && (v.x > target.x && v.y > target.y)) )
			{			
				target = v;
				
				currentId = i;
			}
		}
	}
	
	public int getCurrentId()
	{
		return currentId;
	}
	
	public void setCurrentId(int id)
	{
		this.currentId = id;
	}
	
	int globalC = 0;
	DragListener listener = new DragListener()
	{
		public void dragStart(InputEvent event, float x, float y, int pointer)
		{
			sDragVec.set(getX(), getY());
			isBeingDragged = true;
		}
		
		public void drag(InputEvent event, float x, float y, int pointer)
		{	
			cDragVec.set( sDragVec.x + x , sDragVec.y + y);
			
			if( indicatorType == IndicatorType.END )
			{
				cDragVec.x -= getWidth()/2f;
			}
			
			updateIndex(0);
		}
		
		public void dragStop(InputEvent event, float x, float y, int pointer)
		{
			cDragVec.set( getX(), getY() );
			
			isBeingDragged = false;
		}
	};
}
