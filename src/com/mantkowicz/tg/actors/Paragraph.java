package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.managers.FontManager;

public class Paragraph
{
	public Array<Line> lines;
	
	public Paragraph(String text, BitmapFont font)
	{
		//font = FontManager.getInstance().generateFont("font.ttf", 44);
		
		lines = new Array<Line>();
		
		for(String line : text.split("\n"))
		{
			if(line != null && line.length() > 0)
			{
				Logger.log(this, "LINE : $" + line +"$");
				
				lines.add( new Line(this, line, font) );
			}
		}
	}
	
	public Character getCurrentCharacter()
	{
		for(Line l : lines)
		{
			if(!l.isActive) continue;
			
			for(Sentence s : l.sentences)
			{
				if(!s.isActive) continue;
				
				for(Character c : s.characters)
				{
					if(c.isActive)
					{
						return c;
					}
				}
			}
		}
		
		return null;
	}
	
	public Sentence getCurrentSentence()
	{
		for(Line l : lines)
		{
			if(!l.isActive) continue;
			
			for(Sentence s : l.sentences)
			{
				if(s.isActive)
				{
					return s;
				}
			}
		}
		
		return null;
	}

	public void sentencesToFront()
	{
		for(Line l : lines)
		{
			for(Sentence s : l.sentences)
			{
				s.toFront();
			}
		}
	}
	
	public void refreshCharacters()
	{
		for(Line l : lines)
		{
			for(Sentence s : l.sentences)
			{		
				if(s.isActive) s.setDebug(true);
				else s.setDebug(false);
				
				for(Character c : s.characters)
				{
					c.getColor().a = 0.5f;
					
					if(c.isActive)
					{
						c.getColor().a = 1.0f; 
					}
				}
			}
		}//Logger.log(this, sum);
	}
	
	public void deactivateAll()
	{
		for(Line l : lines)
		{
			l.isActive = false;
			
			for(Sentence s : l.sentences)
			{
				s.isActive = false;
				
				for(Character c : s.characters)
				{
					c.isActive = false;
					c.clearActions();
					
					for(Action action : c.getActions())
					{
						c.removeAction(action);
					}
				}
			}
		}
	}
}
