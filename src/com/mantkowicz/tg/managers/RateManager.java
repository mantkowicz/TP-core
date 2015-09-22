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
import com.mantkowicz.tg.actors.Label;
import com.mantkowicz.tg.actors.Paragraph;
import com.mantkowicz.tg.logger.Logger;

public class RateManager
{
	private static RateManager INSTANCE = new RateManager();
	public static RateManager getInstance()
	{
		return INSTANCE;
	}
	
	private int rivers;
	
	private RateManager()
	{
		
	}
	
	public int rate(Paragraph paragraph)
	{
		return 500;
	}
	
	public void checkRivers(Actor area, Array<Label> glyphs)
	{
		int width = (int)area.getWidth();
    	int height = (int)area.getHeight();
    	
    	int[][] fd = new int[width][height];
    	int[][] fd1 = new int[width][height];
    	int[][] fd2 = new int[width][height];
    	int[][] fd3 = new int[width][height];
    	
		for(Label l : glyphs)
    	{    		
    		int x = (int) (l.getX() - area.getX() );
    		int y =  height - (int) ( l.getY() - area.getY() + l.glyph.yoffset + l.glyph.height ) - (int) l.lineHeight;
    		    		
    		for(int i = 0; i < l.glyph.width; i++)
    			for(int j = 0; j < l.glyph.height; j++)
    				{
	    				if(x + i < 0 || x + i >= fd.length  || y + j < 0 || y + j >= fd[0].length) continue;
	    				fd[x + i][y + j] = 1;
    				}
    	}
		
		//--------------------usuwanie obramowania------------------------------------------------------------------------|		
		cutBorder(fd, glyphs.first().lineHeight);
		
		fd1 = fd.clone();
		fd2 = fd.clone();
		fd3 = fd.clone();
    		
    		
		//--------------------dylatacja-----------------------------------------------------------------------------------|	
		int cW = (int) (new Label("a", glyphs.first().getStyle())).getWidth();
		int cH = glyphs.first().lineHeight;
		
        int[][] ce = new int[cW][cH];
        	
        //---1
    	for(int i = 0; i < ce.length; i++)           //  1 1 1
    		for(int j = 0; j < ce[0].length; j++)    //  1 1 1
    			ce[i][j] = 1;                        //  1 1 1
        	
        dilate(fd1, ce);
        	
        //---2
    	for(int i = 0; i < ce.length; i++)                       // 1 0 0
    		for(int j = 0; j < ce[0].length; j++)                // 0 1 0
    			if( j == ((cH-1)*i) / (cW-1) ) ce[i][j] = 1;     // 0 0 1
    			else ce[i][j] = 0;
        	
        dilate(fd2, ce);
        
        //---3
    	for(int i = 0; i < ce.length; i++)                                      // 0 0 1
    		for(int j = 0; j < ce[0].length; j++)                               // 0 1 0
    			if( j == ((cH-1)*i) / (cW-1) ) ce[ce.length - 1 - i][j] = 1;    // 1 0 0
    			else ce[ce.length - 1 - i][j] = 0;

    	dilate(fd3, ce);
        	
    	//--~4 - element centralny do czyszczenia resztek
       	int[][] cr = new int[][]{ new int[]{1,1,1}, new int[]{1,1,1}, new int[]{1,1,1}  };
        
       
       	//--------------------laczenie macierzy po dylatacji--------------------------------------------------------------|		
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
		
		//--~4 - czyszczenie resztek
		dilate(fd, cr);
		
		
		//--------------------usuwanie obszarow nizszych niz jeden lineHeight - algorytm Smitha-------------------------|	
		for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    		{
    			if( fd[i][j] == 0 )
    			{
    				fd[i][j] = 5;
    				
    				Array<Vector2> heap = new Array<Vector2>();
    				
    				heap.add(new Vector2(i,j));
    				
    				Array<Vector2> arr = smith(fd, heap);
    				
    				float yMin = arr.first().y, yMax = arr.first().y;
    				
    				for(Vector2 v : arr)
    				{
    					if( v.y < yMin ) yMin = v.y;
    					if( v.y > yMax ) yMax = v.y;
    				}
    				
    				if(yMax - yMin <= 2*glyphs.first().lineHeight)
    				{
    					for(Vector2 v : arr)
        				{
    						fd[(int) v.x][(int) v.y] = 2;
        				}
    				}
    				else
    				{
    					this.rivers++; //obszar zaliczony = nowa rzeka!
    				}
    			}
    		}
		
		//--------------------zaznaczanie pozostalych obszarow na czerwono-----------------------------------------------|	
		for(int i = 0; i < width; i++)
    		for(int j = 0; j < height; j++)
    			if( fd[i][j] == 5 ) fd[i][j] = Color.RED.toIntBits();
    			else fd[i][j] = 0;
		
		
		//--------------------zapis wynikow wizualnych do pliku do nalozenia na tekst----------------------------------|	
		Pixmap pixmap = new Pixmap(width, height, Format.RGB888);
        
    	for(int x = 0; x < width; x++)
        	for(int y = 0; y < height; y++)
        		if( fd[x][y] != 0 ) pixmap.drawPixel(x, y, fd[x][y]);
        
        FileHandle fh = Gdx.files.local("files/temp2.png");
        PixmapIO.writePNG(fh, pixmap);
        pixmap.dispose();
        
        Logger.log(this, "ILOSC RZEK = " + this.rivers);
        this.rivers = 0;
	}
	
	private void cutBorder(int[][] fd, float lineHeight)
	{
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
    		
		//prawa
		for(int y = 0; y < fd[0].length + lineHeight; y += lineHeight)
			for(int x = fd.length - 1; x >= 0; x--)
    		{
    			boolean cut = false;
    			
    			for(int yy = y; yy < y + lineHeight; yy++)
    			{
    				if( yy < 0 || yy >= fd[0].length) continue;
    				
    				if( fd[x][yy] != 0 )
    				{
    					cut = true;
    					break;
    				}
    			}
    			
    			if(cut)
    			{
    				for(int yy = y; yy < y + lineHeight; yy++)
        			{
        				if( yy < 0 || yy >= fd[0].length) continue;
        				
        				for(int i = x; i < fd.length; i++) fd[i][yy] = 1;
        			}
    				
    				break;
    			}
    		}
    	
    	//lewa
    	for(int y = 0; y < fd[0].length + lineHeight; y += lineHeight)
    		for(int x = 0; x < fd.length; x++)
    		{
    			boolean cut = false;
    			
    			for(int yy = y; yy < y + lineHeight; yy++)
    			{
    				if( yy < 0 || yy >= fd[0].length) continue;
    				
    				if( fd[x][yy] != 0 )
    				{
    					cut = true;
    					break;
    				}
    			}
    			
    			if(cut)
    			{
    				for(int yy = y; yy < y + lineHeight; yy++)
        			{
        				if( yy < 0 || yy >= fd[0].length) continue;
        				
        				for(int i = x; i >= 0; i--) fd[i][yy] = 1;
        			}
    				
    				break;
    			}
    		}
	}
	
	private void dilate(int[][] array, int[][] centerElement)
	{
		for(int i = 0; i < array.length; i++)
    		for(int j = 0; j < array[0].length; j++)
    		{
    			for(int di = 0; di < centerElement.length; di++)
    				for(int dj = 0; dj < centerElement[0].length; dj++)
    				{
    					int xOffset =  (int)Math.floor(centerElement.length/2);
    					int yOffset =  (int)Math.floor(centerElement[0].length/2);
    					
    					if( i + di - xOffset < 0 || 
    					    i + di - xOffset >= array.length || 
    					    j + dj - yOffset < 0 || 
    					    j + dj - yOffset >= array[0].length || 
    					    (di == xOffset && dj == yOffset)  
    					  ) continue;
    					else
    					{
    						if( array[i + di - xOffset][j + dj - yOffset] == 1 && ( centerElement[di][dj] == 1 ) )
    						{
    							array[i][j] = 2;
    						}
    					}    					
    				}
    		}
	}
	
	private Array<Vector2> smith(int[][] fd, Array<Vector2> heap) 
	{
		Array<Vector2> array = new Array<Vector2>();
		
		while(heap.size > 0)
		{
			Vector2 p = heap.pop();
			
			array.add(p);
			
			//kolorowanie segmentu
			for(int i = 0; p.x + i >= 0; i--)
			{
				if( fd[(int)p.x + i][(int)p.y] == 0 )
				{
					fd[(int)p.x + i][(int)p.y] = 5;
					heap.add(new Vector2((int)p.x + i,(int)p.y));
				}
				else if( fd[(int)p.x + i][(int)p.y] != 5 ) break;
			}
			
			for(int i = 0; p.x + i < fd.length; i++)
			{
				if( fd[(int)p.x + i][(int)p.y] == 0 )
				{
					fd[(int)p.x + i][(int)p.y] = 5;
					heap.add(new Vector2((int)p.x + i,(int)p.y));
				}
				else if( fd[(int)p.x + i][(int)p.y] != 5 ) break;
			}
			
			
			//dodawanie gornych i dolnych sasiadow
			if( p.y + 1 < fd[0].length )
			{
				for(int i = -1; i <= 1; i++)
				{
					if( p.x + i >= 0 && p.x + i < fd.length )
					{
						if( fd[(int)p.x + i][(int)p.y + 1] == 0 )
						{
							fd[(int)p.x + i][(int)p.y + 1] = 5;
							heap.add(new Vector2((int)p.x + i, (int)p.y + 1));
						}
					}
				}
			}
			
			if( p.y - 1 >= 0 )
			{
				for(int i = -1; i <= 1; i++)
				{
					if( p.x + i >= 0 && p.x + i < fd.length )
					{
						if( fd[(int)p.x + i][(int)p.y - 1] == 0 )
						{
							fd[(int)p.x + i][(int)p.y - 1] = 5;
							heap.add(new Vector2((int)p.x + i, (int)p.y - 1));
						}
					}
				}
			}
		}
		
		return array;
	}
}
