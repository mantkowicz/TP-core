package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mantkowicz.tg.actors.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.managers.FontManager;

public class CustomLabel
{
	public Array<Label> glyphs;
	
	int startId = -1;
	int endId = -1;
	
	Label lab;

	Job job;
	
	Stage stage;
	
	boolean labelVisible = true;
	
	
	
	FloatArray xa = new FloatArray();
	Array<Glyph> gl = new Array<Glyph>();
	
	public CustomLabel(Job job, Stage stage)
	{
		//job.content = "³¹Ÿæ¿ó³ êœæñ ó³³¹œ";
		//job.content = "mama";
		//JEDEN GLYPHRUN NA JEDNA LINIE!!!!!!!!!!!!!
		job.content = "jedna linia";
		job.content = "dwiee\nlinie\ni trzecia na dok³adkê!";
		log(job.content);
		this.job = job;
		this.stage = stage;
		
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", job.font_size);
		
		
		
		LabelStyle ls = new LabelStyle();
		ls.font = font;
		ls.fontColor = Color.GREEN;
		
		this.lab = new Label(job.content, ls);
		
		this.lab.setWrap(true);
		
		//this.lab.debug();
		this.lab.setAlignment(Align.topLeft);
		this.lab.setSize(job.width - job.padding, job.height - job.padding);
		this.lab.setPosition(-job.width/2.0f, -job.height/2.0f);
		
		for(GlyphRun gr : lab.getGlyphLayout().runs)
		{
			for(int i = 0; i < gr.xAdvances.size - 1; i++)
			{
				this.xa.add(gr.xAdvances.get(i));
			}
			
			for(int i = 0; i < gr.glyphs.size; i++)
			{
				this.gl.add(gr.glyphs.get(i));
			}
		}
		
		//this.stage.addActor(this.lab);
		
		glyphs = new Array<Label>();
		
		for(int i = 0; i < job.content.length(); i++)
		{
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = font;
			labelStyle.fontColor = Color.WHITE;
			
			Label tempLabel = new Label(job.content.substring(i, i+1), labelStyle);
			tempLabel.addListener( listener );
			
			tempLabel.id = i;
			
			tempLabel.setUserObject("Original");
			
			glyphs.add( tempLabel );
		}
	}
	
	public void addToStage()
	{
		float prevW = 0;
		float row = glyphs.first().getHeight();
		int ctr = 0;
		
		//log( glyphs.size + " | " + this.xa.size + " | " + this.gl.size);
		
		for(Label l : glyphs)
		{
			l.debug();
			if( l.getText().chars[0] == '\n' )
			{
				row += lab.getStyle().font.getLineHeight();
				prevW = 0;
			}
			else
			{
				//changing color
				if( startId != -1 && l.id == startId )
				{
					l.getStyle().fontColor = Color.BLUE;
				}
				else if( endId != -1 && l.id == endId)
				{
					l.getStyle().fontColor = Color.BLUE;
				}
				else if (startId != -1 && endId != -1 && l.id > startId && l.id < endId )
				{
					l.getStyle().fontColor = Color.BLUE;
				}
				else
				{
					l.getStyle().fontColor = Color.WHITE;
				}
				
				prevW += xa.get(ctr);//lab.getGlyphLayout().runs.first().xAdvances.get(ctr);
				
				l.setX( lab.getX() + prevW + gl.get(ctr).xoffset); //lab.getGlyphLayout().runs.first().glyphs.get(ctr).xoffset );
				l.setY( lab.getY() + lab.getHeight() - row );
							
				stage.addActor(l);
				
				ctr++;
			}
		}
	}
	
	public void toggle()
	{ 
		if(labelVisible)
		{			
			this.lab.remove();
			
			this.addToStage();
		}
		else
		{
			for(Label l : glyphs)
			{
				l.remove();
			}
			
			this.stage.addActor(this.lab);			
		}
		
		labelVisible = !labelVisible;
	}
	
	private void log(Object msg)
	{
		Logger.log(this, msg);
	}
	
	ClickListener listener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			int targetId = ( (Label)event.getTarget() ).id;
			
			if(startId == -1 && endId == -1)
			{
				startId = targetId;
			}
			else if(startId != -1 && endId == -1)
			{
				if(targetId <= startId)
				{
					endId = startId;
					startId = targetId; 
				}
				else
				{
					endId = targetId;
				}
			}
			else
			{
				startId = -1;
				endId = -1;
			}
		}
	};
}
