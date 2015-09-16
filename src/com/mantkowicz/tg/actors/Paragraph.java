package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.Glyph;
import com.badlogic.gdx.graphics.g2d.GlyphLayout.GlyphRun;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.managers.FontManager;

public class Paragraph
{
	Stage stage;
	
	private Job job_backup;
	
	public Job job;
	
	Label pattern;
	
	public Array<Label> glyphs;
	
	public int startId = -1;
	public int endId = -1;
	
	Drawable markedBackground;
	
	public int longPressedId = -1;
	
	public float kerningModificator = 0f;
	public float offsetModificator = 0f;
	
	public Paragraph(Job job, Stage stage, Drawable markedBackground)
	{
		this.stage = stage;
		
		this.job_backup = job;
		this.job = this.job_backup.clone();
		
		this.markedBackground = markedBackground;
		
		restart();
	}
	
	public void restart()
	{
		this.job = this.job_backup.clone();
				
		refresh(true);
	}
	
	public void increaseFontSize()
	{
		this.job.font_size++;
		
		if( this.job.lineHeight < this.job.font_size ) this.job.lineHeight = this.job.font_size;
		
		refresh(false);
	}
	
	public void decreaseFontSize()
	{
		this.job.font_size--;
		
		refresh(false);
	}
	
	private void refresh(boolean restart)
	{
		if( glyphs != null )
		{
			for(Label glyph : glyphs)
			{
				glyph.remove();
			}
		}
		
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", job.font_size);
		
		LabelStyle ls = new LabelStyle();
		ls.font = font;
		ls.fontColor = Color.GREEN;
		
		this.pattern = new Label(job.content, ls);
		
		this.pattern.setWrap(true);
		
		//this.lab.debug();
		this.pattern.setAlignment(Align.topLeft);
		this.pattern.setSize(job.width - job.padding, job.height - job.padding);
		this.pattern.setPosition(-job.width/2.0f, -job.height/2.0f);
		
		FloatArray xa = new FloatArray();
		FloatArray xo = new FloatArray();
		Array<Glyph> gl = new Array<Glyph>();
		
		for(GlyphRun gr : pattern.getGlyphLayout().runs)
		{
			for(int i = 0; i < gr.xAdvances.size - 1; i++)
			{
				xa.add(gr.xAdvances.get(i));
			}
			
			for(int i = 0; i < gr.glyphs.size; i++)
			{
				gl.add(gr.glyphs.get(i));
			}
		}
		
		if( !restart )
		{
			for(int i = 0; i < glyphs.size; i++)
			{
				xa.set(i, xa.get(i) + glyphs.get(i).xAdvance - glyphs.get(i).xAdvance_start);
				xo.add(glyphs.get(i).xOffset - glyphs.get(i).xOffset_start);
			}
		}
		
		glyphs = new Array<Label>();
		
		int ctr = 0;

		for(int i = 0; i < job.content.length(); i++)
		{
			if(job.content.substring(i, i+1).equals("\n"))
			{
				if(i > 0) 
				{
					glyphs.peek().newLine = true;
					glyphs.peek().hardNewLine = true;
				}
				
				continue;
			}
			
			LabelStyle labelStyle = new LabelStyle();
			labelStyle.font = font;
			labelStyle.fontColor = Color.BLACK;
			
			Label tempLabel = new Label(job.content.substring(i, i+1), labelStyle);
			
			tempLabel.id = i;
			
			tempLabel.xAdvance = xa.get(ctr);
			tempLabel.xAdvance_start = xa.get(ctr);
			
			tempLabel.xOffset = gl.get(ctr).xoffset;
			tempLabel.xOffset_start = gl.get(ctr).xoffset;
			tempLabel.glyph = gl.get(ctr);
			tempLabel.lineHeight = job.lineHeight;
			
			if(xo.size > 0) tempLabel.xOffset += xo.get(ctr);
			
			tempLabel.setUserObject("Original");
			
			tempLabel.setAlignment(Align.center);
						
			glyphs.add( tempLabel );
			
			ctr++;
		}
	}
		
	public void addToStage()
	{
		float prevW = 0;
		float row = job.lineHeight; //glyphs.first().getHeight();
		
		handleLongPress();		
		updateNewLines();
		
		for(Label l : glyphs)
		{		
			if( l.newLine )
			{
				row += job.lineHeight; //pattern.getStyle().font.getLineHeight();
				prevW = 0;
			}
			
			if( l.id == 0 ) prevW += job.indent;
			
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
				l.xAdvance += kerningModificator;  //xa.set(ctr, xa.get(ctr) + kerningModificator );
				l.xOffset += offsetModificator;
			}
			
			//prevW += xa.get(ctr);//lab.getGlyphLayout().runs.first().xAdvances.get(ctr);
			
			if( !l.newLine ) prevW += l.xAdvance;
			
			l.setX( pattern.getX() + prevW + l.xOffset); //gl.get(ctr).xoffset); //lab.getGlyphLayout().runs.first().glyphs.get(ctr).xoffset );
			l.setY( pattern.getY() + pattern.getHeight() - row );
							
			stage.addActor(l);				
		}
		
		 kerningModificator = 0f;
		 offsetModificator = 0f;
	}
	
	private void handleLongPress()
	{
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
	}
	
	private void updateNewLines()
	{
		float prevW = 0;
		
		for(Label l : glyphs)
		{
			if(l.newLine && !l.hardNewLine)
			{
				l.newLine = false;
			}
		}
		
		for(int i = 0; i < glyphs.size; i++)
		{
			Label l = glyphs.get(i);
			
			prevW += l.xAdvance;
			
			if( prevW > job.width - job.padding * 2 )
			{
				if( l.getText().chars[0] != ' ' )
				{
					glyphs.get( getWordStart(l.id) ).newLine = true;
					
					prevW = 0;
					i = getWordStart(l.id);
				}
			}
		}
	}
	
	public int getWordStart()
	{
		return getWordStart(this.longPressedId);
	}
	
	public int getWordStart(int id)
	{		
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
