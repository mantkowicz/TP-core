package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label 
{
	public int id;
	
	public float xAdvance = 0f;
	public float xOffset = 0f;
	
	public float xAdvance_start = 0f;
	public float xOffset_start = 0f;
	
	public Glyph glyph;
	
	public boolean hardNewLine = false;
	public boolean newLine = false;
	public boolean longPressed = false;
	
	public int lineHeight;

	public Label(CharSequence text, LabelStyle style) 
	{
		super(text, style);
		this.addListener(listener);
	}

	public Label(CharSequence text, Skin skin, String fontName, Color color) 
	{
		super(text, skin, fontName, color);
	}

	public Label(CharSequence text, Skin skin, String fontName, String colorName) 
	{
		super(text, skin, fontName, colorName);
	}

	public Label(CharSequence text, Skin skin, String styleName) 
	{
		super(text, skin, styleName);
	}

	public Label(CharSequence text, Skin skin) 
	{
		super(text, skin);
	}
	
	public int[][] getPixels()
	{		
		//Pixmap pixmap = this.getBitmapFontCache().getFont().getRegions().get( this.pageId ).getTexture().getTextureData().consumePixmap();
		Pixmap pixmap = this.getBitmapFontCache().getFont().getRegion().getTexture().getTextureData().consumePixmap();  //getRegions().get( this.pageId ).  .getTexture().getTextureData().consumePixmap();
				
		int[][] pixels = new int[this.glyph.width][this.glyph.height];
		
		for(int i = 0 ; i < pixmap.getWidth(); i++)
			for(int j = 0 ; j < pixmap.getWidth(); j++)
				if( i >= this.glyph.srcX &&
					i < this.glyph.srcX + this.glyph.width &&
					j >= this.glyph.srcY &&
				    j < this.glyph.srcY + this.glyph.height) 
					
					pixels[i - this.glyph.srcX][j - this.glyph.srcY] = pixmap.getPixel(i, j);
		
		return pixels;
	}
	
	ActorGestureListener listener = new ActorGestureListener() 
	{
		public boolean longPress(Actor actor, float x, float y)
		{
			longPressed = true;
			
			return false;
		}
	};
}
