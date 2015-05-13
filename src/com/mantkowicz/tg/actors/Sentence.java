package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.managers.CameraManager;

public class Sentence extends Actor
{	
	public boolean isActive = false;
	
	public Array<Character> characters;
	Line line;
		   
	public String text = "";
	
	public Sentence(Line line, String text, BitmapFont font, float x)
	{
		super();
		
		this.text = text;
		
		this.line = line;
		
		characters = new Array<Character>();
		
		this.setPosition(x, this.line.y);
		
		GlyphLayout glyphLayout = new GlyphLayout();
		
		for(String character : text.split(""))
		{
			if(character != null && character.length() > 0)
			{
				Logger.log(this, "CHARACTER : $" + character +"$");
				
				Character c = new Character(this, character, font, x, this.line.y);
				
				//this.addActor(c);
				
				characters.add( c ); 
				
				glyphLayout.setText(font, character);
				
				x += glyphLayout.width;
			}
		}
		
		this.setSize(x - this.getX(), font.getData().ascent - font.getData().descent + font.getData().xHeight);
		
		this.addListener(sentenceListener);
	}
	
	ClickListener sentenceListener = new ClickListener() 
	   {
	   		public void clicked(InputEvent event, float x, float y)
	   		{
	   			Sentence target = (Sentence)event.getTarget();
	   				   			
	   			target.line.paragraph.deactivateAll();
	   			
	   			for(Character character : target.characters)
	   			{
	   				character.addListener(characterListener);
	   			}
	   			
	   			target.isActive = true;
	   			target.line.isActive = true;
	   			
	   			target.line.paragraph.sentencesToFront();
	   			
	   			target.toBack();
	   			
	   			CameraManager.getInstance().moveTo(target.getX() + (target.getWidth() / 2.0f), target.getY() + (target.getHeight() / 2.0f));
	   			CameraManager.getInstance().zoomTo(target.getWidth() + 1000.0f);
	   		}
	   };
	   
   ClickListener characterListener = new ClickListener() 
	   {
	   		public void clicked(InputEvent event, float x, float y)
	   		{
	   			Character target = (Character)event.getTarget();
	   			
	   			target.sentence.line.paragraph.deactivateAll();
	   			
	   			target.isActive = true;
	   			target.sentence.isActive = true;
	   			target.sentence.line.isActive = true;
	   			
	   			CameraManager.getInstance().moveTo(target.getX() + (target.getWidth() / 2.0f), target.getY() + (target.getHeight() / 2.0f));
	   			CameraManager.getInstance().zoomTo(target.sentence.getWidth() + 300.0f);
	   		}
	   };
}
