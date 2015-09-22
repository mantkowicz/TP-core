package com.mantkowicz.tg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.mantkowicz.tg.actors.Label;
import com.mantkowicz.tg.logger.Logger;

public class ScreenShotManager 
{
    public static String getScreenshot(Actor area, Array<Label> glyphs)
    {
    	Label aLabel = new Label("a", glyphs.first().getStyle());
    	
    	int width = (int)area.getWidth();
    	int height = (int)area.getHeight();
    	
    	
    	int[][] pd = new int[width][height];
    	int[][] fd = new int[width][height];
    	int[][] fd1 = new int[width][height];
    	int[][] fd2 = new int[width][height];
    	int[][] fd3 = new int[width][height];
    	
    	for(int i = 0; i < pd.length; i++)
    		for(int j = 0; j < pd[i].length; j++)
    			{
    			pd[i][j]=0;
    			fd[i][j]=0;
    			}
    	
    	for(Label l : glyphs)
    	{    		
    		int x = (int) (l.getX() - area.getX() );
    		int y =  height - (int) ( l.getY() - area.getY() + l.glyph.yoffset + l.glyph.height ) - (int) l.lineHeight;
    		    		
    		int[][] pixels = l.getPixels();
    		
    		for(int i = 0; i < l.glyph.width; i++)
    			for(int j = 0; j < l.glyph.height; j++)
    				{
    				if(x + i < 0 || x + i >= pd.length || x + i >= fd.length  || y + j < 0 || y + j >= pd.length || y + j >= fd.length) continue;
    				pd[x + i][y + j] = pixels[i][j];
    				fd[x + i][y + j] = 1;//Color.WHITE.toIntBits();
    				}
    	}
    	/*
    	Label l = glyphs.first();
        
        int[][] pixels = l.getPixels();
                
        for(int i = 0; i < pixels.length; i++)
    		for(int j = 0; j < pixels[i].length; j++)
    			pd[i][j]=pixels[i][j];
    	
    	
    	*/
    	
    	
    	//dilatation
    	
    	//prawa
    	/*for(int y = 0; y < fd[0].length; y++)
    		for(int x = fd.length - 1; x >= 0; x--)
    			if( fd[x][y] != 0 )
    			{
    				for(int i = x; i < fd.length; i++) fd[i][y] = 1;
    				break;
    			}
    	
    	//lewa
    	for(int y = 0; y < fd[0].length; y++)
    		for(int x = 0; x < fd.length; x++)
    			if( fd[x][y] != 0 )
    			{
    				for(int i = x; i >= 0; i--) fd[i][y] = 1;
    				break;
    			}
    	*/
    	//dol
    	outerloop:
    	for(int y = 0; y < fd[0].length; y++)
    		for(int x = 0; x < fd.length; x++)
    		{
    			if( fd[x][y] != 0 )
    			{
    				for(int j = y; j >= 0; j--)
    					for(int i = 0; i < fd.length; i++)
    						fd[i][j] = 1;
    				break outerloop;
    			}
    		}
    	
    	//gora
		outerloop:
    	for(int y = fd[0].length - 1; y >= 0; y--)
    		for(int x = 0; x < fd.length; x++)
    		{    		
    			if( fd[x][y] != 0 )
    			{
    				for(int j = fd[0].length - 1; j >= y; j--)
    					for(int i = 0; i < fd.length; i++)
    						fd[i][j] = 1;
    				break outerloop;
    			}
    		}
    	
    	int cW = (int) aLabel.getWidth();
    	int cH = glyphs.first().lineHeight;
    	
    	Logger.log(1, "CentralWidth = " + cW + " ,  CentralHeight = " + cH);

    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			fd1[i][j] = fd[i][j];
    			fd2[i][j] = fd[i][j];
    			fd3[i][j] = fd[i][j];
    		}
    	
    	
    	
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			for(int di = -cW/2; di <= cW/2; di++)
    				for(int dj = -cH/2; dj <= cH/2; dj++)
    				{
    					if( i+di<0 || i+di >= width || j+dj < 0 || j+dj >= height || (di == 0 && dj == 0)  ) continue;
    					else
    					{
    						if( fd1[i+di][j+dj] == 1 )
    						{
    							fd1[i][j] = 2;
    						}
    					}    					
    				}
    		}
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			for(int di = -cH/2; di <= cH/2; di++)
    				for(int dj = -cH/2; dj <= cH/2; dj++)
    				{
    					if( i+di<0 || i+di >= width || j+dj < 0 || j+dj >= height  ) continue;
    					else
    					{
    						if( fd2[i+di][j+dj] == 1 && di == -dj )
    						{
    							fd2[i][j] = 2;
    						}
    					}    					
    				}
    		}

    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{			
    			for(int di = -cH/2; di <= cH/2; di++)
    				for(int dj = -cH/2; dj <= cH/2; dj++)
    				{
    					if( i+di<0 || i+di >= width || j+dj < 0 || j+dj >= height || (di == 0 && dj == 0)  ) continue;
    					else
    					{
    						if( fd3[i+di][j+dj] == 1 && di == dj )
    						{
    							fd3[i][j] = 2;
    						}
    					}    					
    				}
    		}
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			if( fd1[i][j] == 0 || fd2[i][j] == 0 || fd3[i][j] == 0 )
    			{
    				fd[i][j] = 0;
    			}
    			else
    			{
    				fd[i][j] = 1;
    			}
    		}
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			if( fd[i][j] != 0 )
    			{
    				Array<Vector2> arr = new Array<Vector2>();
    				
    			}
    		}
    	
    	/*int[][] cel = new int[][]{ new int[]{0,0,1,0,0},
    		                       new int[]{0,0,1,0,0},
    		                       new int[]{1,1,1,1,1},
    		                       new int[]{0,0,1,0,0},
    		                       new int[]{0,0,1,0,0}
    	                         };
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			for(int di = -cel.length/2; di <= cel.length/2; di++)
    				for(int dj = -cel.length/2; dj <= cel.length/2; dj++)
    				{
    					if( i+di<0 || i+di >= width || j+dj < 0 || j+dj >= height || (di == 0 && dj == 0)  ) continue;
    					else
    					{
    						if( fd[i+di][j+dj] == 1 && ( cel[di+2][dj+2] == 1 ) )
    						{
    							fd[i][j] = 2;
    						}
    					}    					
    				}
    		}*/
    	    	
    	//
    	
    	for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    			if( fd[i][j] == 0 ) pd[i][j] = Color.RED.toIntBits();
    			
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