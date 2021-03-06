package com.mantkowicz.tg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.mantkowicz.tg.actors.Label;
import com.mantkowicz.tg.logger.Logger;

public class ScreenShotManager 
{
    public static String getScreenshot(Actor area, Array<Label> glyphs)
    {    	
    	int width = (int)area.getWidth();
    	int height = (int)area.getHeight();
    	    	
    	int[][] pd = new int[width][height];
    	    	
    	for(Label l : glyphs)
    	{    		
    		int x = (int) (l.getX() - area.getX() );
    		int y =  height - (int) ( l.getY() - area.getY() + l.glyph.yoffset + l.glyph.height ) - (int) l.lineHeight;
    		    		
    		int[][] pixels = l.getPixels();
    		    		
    		for(int i = 0; i < l.glyph.width; i++)
    			for(int j = 0; j < l.glyph.height; j++)
    				{
	    				if(x + i < 0 || x + i >= pd.length || y + j < 0 || y + j >= pd.length) continue;
	    				pd[x + i][y + j] = pixels[i][j];
    				}
    	}   	    	
    	
    			
    	Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
        
    	for(int x = 0; x < width; x++)
        	for(int y = 0; y < height; y++)
        		pixmap.drawPixel(x, y, pd[x][y]);
    	
        
        FileHandle fh = Gdx.files.local("files/temp.png");
        PixmapIO.writePNG(fh, pixmap);
        pixmap.dispose();
        
        return String.valueOf( Base64Coder.encode( fh.readBytes() ) ); 
    }
}