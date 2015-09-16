package com.mantkowicz.tg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.mantkowicz.tg.actors.Label;

public class ScreenShotManager 
{
    public static String getScreenshot(Actor area, Array<Label> glyphs)
    {
    	int width = (int)area.getWidth();
    	int height = (int)area.getHeight();
    	
    	
    	int[][] pd = new int[width][height];
    	
    	for(int i = 0; i < pd.length; i++)
    		for(int j = 0; j < pd[i].length; j++)
    			pd[i][j]=0;
    	
    	for(Label l : glyphs)
    	{    		
    		int x = (int) (l.getX() - area.getX() );
    		int y =  height - (int) ( l.getY() - area.getY() + l.glyph.yoffset + l.glyph.height ) - (int) l.lineHeight;
    		    		
    		int[][] pixels = l.getPixels();
    		
    		for(int i = 0; i < l.glyph.width; i++)
    			for(int j = 0; j < l.glyph.height; j++)
    				pd[x + i][y + j] = pixels[i][j];
    	}
    	/*
    	Label l = glyphs.first();
        
        int[][] pixels = l.getPixels();
                
        for(int i = 0; i < pixels.length; i++)
    		for(int j = 0; j < pixels[i].length; j++)
    			pd[i][j]=pixels[i][j];
    	
    	
    	*/
    	
    	Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
        
    	for(int x = 0; x < width; x++)
        	for(int y = 0; y < height; y++)
        		//if(pd[x][y] == 0) pixmap.drawPixel(x, y, Color.WHITE.toIntBits());
        		//else pixmap.drawPixel(x, y, Color.BLACK.toIntBits());
        	{
        		pixmap.drawPixel(x, y, pd[x][y]);
        	}
    	
    	
    	/* 
        for(int x = 0; x < width; x++)
        	for(int y = 0; y < height; y++)
        		pixmap.drawPixel(x, y, Color.WHITE.toIntBits());
        
        
        for(int x = 0; x < width; x++)
        	for(int y = 0; y < height; y++)
        		if(x == y) pixmap.drawPixel(x, y, Color.BLACK.toIntBits());
        */
        
		//pixmap.drawPixel(x, y, Color.BLACK.toIntBits());
        
                
        /*for(Label l : glyphs)
        {
        	int x = (int) (l.getX() - area.getX());
        	int y = (int) (l.getY() - area.getY());
        	
        	int[][] pixels = l.getPixels();
        	
        	for(int i = 0; i < pixels.length; i++)
        		for(int j = 0; j < pixels[i].length; j++)
        			if( pixels[i][j] != 0 ) Logger.log(1, "x = " + i + ", y = " + j);//pixmap.drawPixel(i,j, Color.BLACK.toIntBits());
        }*/
        
        FileHandle fh = Gdx.files.local("files/temp.png");
        PixmapIO.writePNG(fh, pixmap);
        pixmap.dispose();
        
        return String.valueOf( Base64Coder.encode( fh.readBytes() ) ); 
    }
}