package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.logger.Logger;

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
		
		for(String sentence : text.split(" "))
		{
			if(sentence != null && sentence.length() > 0)
			{
				Logger.log(this, "SENTENCE : $" + sentence +"$");
				
				float x = -600;
				
				for(int i = 0; i < sentences.size; i++)
				{					
					FloatArray advance = new FloatArray();
					FloatArray position = new FloatArray();
					
					font.computeGlyphAdvancesAndPositions(sentences.get(i).text + " ", advance, position);
					
					for(float f : advance.items)
					{
						x += f;
					}
				}
				Logger.log(this, "SENTENCE " + sentence + " P : " + x);
				sentences.add( new Sentence(this, sentence, font, x) );
			}
		}
	}

}
