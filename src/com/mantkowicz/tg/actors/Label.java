package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mantkowicz.tg.logger.Logger;

public class Label extends com.badlogic.gdx.scenes.scene2d.ui.Label 
{
	public int id;
	public boolean longPressed = false;
	
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
	
	ActorGestureListener listener = new ActorGestureListener() 
	{
		public boolean longPress(Actor actor, float x, float y)
		{
			longPressed = true;
			
			return false;
		}
	};

}
