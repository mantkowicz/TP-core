package com.mantkowicz.tg.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.logger.Logger;

public class CustomLabel
{
	Array<Label> glyphs;
	public boolean removed = false;
	
	LabelStyle labelStyle;
	
	public CustomLabel(String text, LabelStyle labelStyle)
	{
		this.labelStyle = labelStyle;
		
		glyphs = new Array<Label>();
		
		for(int i = 0; i < text.length(); i++)
		{
			glyphs.add( new Label(text.substring(i, i+1), labelStyle) );
		}
	}
	
	public void addToStage(Stage stage)
	{
		removed = false;
		int prevW = -200;
		for(Label l : glyphs)
		{
			l.debug();
			log("char = " + l.getText().charAt(0));
			float k = l.getGlyphLayout().runs.first().glyphs.first().width;
			l.setWidth(k);
			l.setX( l.getX() + prevW );
			
			if( l.getWidth() == 0.0f)
			{
				log("mamy spacje!");
				
				float temp1 = ( new Label(". .", this.labelStyle) ).getWidth();
				float temp2 = (new Label(".", this.labelStyle) ).getWidth();
				
				l.setWidth( temp1 - temp2 - temp2 );
			}
			
			prevW += l.getWidth();
			stage.addActor(l);
			
			log("."+l.getText()+"." + ", " + l.getWidth());
		}
	}
	
	public void toggle(Stage stage)
	{ 
		if(!removed)
		{
			removed = true;
			for(Label l : glyphs)
			{
				l.remove();
			}
		}
		else
		{
			this.addToStage(stage);
		}
	}
	
	private void log(Object msg)
	{
		Logger.log(this, msg);
	}
}
