package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.mantkowicz.tg.logger.Logger;
//github.com/mantkowicz/TP-core.git
import com.badlogic.gdx.utils.Array;

public class Line
{
	public boolean isActive = false;
	
	public Array<Sentence> sentences;
	Paragraph paragraph;
	
	public static int counter = 0;
	
	public float y = 200;
	
	public Line(Paragraph paragraph, String text, BitmapFont font)
	{
		this.paragraph = paragraph;
		
		y -= counter * 100;
		counter++;
		
		this.sentences = new Array<Sentence>();
		
		GlyphLayout glyphLayout = new GlyphLayout();
		
		for(String sentence : text.split(" "))
		{
			if(sentence != null && sentence.length() > 0)
			{
				Logger.log(this, "SENTENCE : $" + sentence +"$");
				
				float x = -600;
				
				for(int i = 0; i < sentences.size; i++)
				{		
					glyphLayout.setText(font, sentences.get(i).text + " ");

					x += glyphLayout.width;
				}
				Logger.log(this, "SENTENCE " + sentence + " P : " + x);
				sentences.add( new Sentence(this, sentence, font, x) );
			}
		}
	}

}
