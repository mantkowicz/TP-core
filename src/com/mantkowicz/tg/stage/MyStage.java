package com.mantkowicz.tg.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mantkowicz.tg.actors.Character;
import com.mantkowicz.tg.actors.Line;
import com.mantkowicz.tg.actors.Paragraph;
import com.mantkowicz.tg.actors.Sentence;

public class MyStage extends Stage
{
	public MyStage()
	{
		super();
	}
	
	public void addActor(Paragraph paragraph)
	{
		for(Line l : paragraph.lines)
		{
			for(Sentence s : l.sentences)
			{
				for(Character c : s.characters)
				{
					this.addActor(c);
				}
				
				this.addActor(s);
			}
		}
	}
}
