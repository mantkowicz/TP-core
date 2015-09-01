package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.logger.Logger;
import com.mantkowicz.tg.managers.FontManager;

public class CustomLabel
{
	Stage stage;
	public Job job;
	
	Label pattern;
	
	public Array<Label> glyphs;
	
	public int startId = -1;
	public int endId = -1;
	
	FloatArray xa = new FloatArray();
	Array<Glyph> gl = new Array<Glyph>();
	
	Drawable markedBackground;
	
	public int longPressedId = -1;
	
	public float kerningModificator = 0f;
	public float moveModificator = 0f;
	
	public CustomLabel(Job job, Stage stage, Drawable markedBackground)
	{
		this.stage = stage;
		this.job = job;
		
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", job.font_size);
		
		this.markedBackground = markedBackground;
		
		LabelStyle ls = new LabelStyle();
		ls.font = font;
		ls.fontColor = Color.GREEN;
		
		this.pattern = new Label(job.content, ls);
		
		this.pattern.setWrap(true);
		
		//this.lab.debug();
		this.pattern.setAlignment(Align.topLeft);
		this.pattern.setSize(job.width - job.padding, job.height - job.padding);
		this.pattern.setPosition(-job.width/2.0f, -job.height/2.0f);
		
		for(GlyphRun gr : pattern.getGlyphLayout().runs)
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
			labelStyle.fontColor = Color.BLACK;
			
			Label tempLabel = new Label(job.content.substring(i, i+1), labelStyle);
			//tempLabel.addListener( listener );
			
			tempLabel.id = i;
			
			tempLabel.setUserObject("Original");
			
			tempLabel.setAlignment(Align.center);
			
			glyphs.add( tempLabel );
		}
	}
	
	public void addToStage()
	{
		float prevW = 0;
		float row = glyphs.first().getHeight();
		int ctr = 0;
		
		for(Label l : glyphs)
		{
			longPressedId = -1;
			
			if( l.longPressed )
			{
				longPressedId = l.id;
				
				l.longPressed = false;
				
				break;
			}
		}
		
		for(Label l : glyphs)
		{
			//l.debug();
			if( l.getText().chars[0] == '\n' )
			{
				row += pattern.getStyle().font.getLineHeight();
				prevW = 0;
			}
			else
			{				
				boolean modifyKerning = false;
				//changing color
				if( startId != -1 && l.id == startId )
				{
					l.getStyle().background = markedBackground;
					
					modifyKerning = true;
				}
				else if( endId != -1 && l.id == endId)
				{
					l.getStyle().background = markedBackground;
					
					modifyKerning = true;
				}
				else if (startId != -1 && endId != -1 && l.id > startId && l.id < endId )
				{
					l.getStyle().background = markedBackground;
					
					modifyKerning = true;
				}
				else
				{
					l.getStyle().background = null;
					//l.getStyle().fontColor = Color.BLACK;
				}
				
				if( modifyKerning )
				{
					l.setWidth( l.getWidth() + kerningModificator );
					xa.set(ctr, xa.get(ctr) + kerningModificator );
				}
				
				prevW += xa.get(ctr);//lab.getGlyphLayout().runs.first().xAdvances.get(ctr);
				
				l.setX( pattern.getX() + prevW + gl.get(ctr).xoffset); //lab.getGlyphLayout().runs.first().glyphs.get(ctr).xoffset );
				l.setY( pattern.getY() + pattern.getHeight() - row );
							
				l.toBack();
				//l.setTouchable(Touchable.disabled);
				stage.addActor(l);
				
				ctr++;
			}
		}
		
		 kerningModificator = 0f;
	}
	
	public int getWordStart()
	{
		int id = this.longPressedId;
		
		while(id >= 0 )
		{
			if(this.glyphs.get(id).getText().chars[0] == ' ') break;
			if(this.glyphs.get(id).getText().chars[0] == '\n') break;
			
			id--;
		}
		
		return id + 1;
	}
	
	public int getWordEnd()
	{
		int id = this.longPressedId;
		
		while(id < this.glyphs.size )
		{
			if(this.glyphs.get(id).getText().chars[0] == ' ') break;
			if(this.glyphs.get(id).getText().chars[0] == '\n') break;
			
			id++;
		}

		return --id;
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
