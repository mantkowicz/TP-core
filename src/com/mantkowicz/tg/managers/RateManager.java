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
import com.mantkowicz.tg.json.Job;

public class RateManager
{
	private static RateManager INSTANCE = new RateManager();
	public static RateManager getInstance()
	{
		return INSTANCE;
	}
		
	private RateManager()
	{
		
	}
	
	public final int MAX_SCORE = 100;
	
	public class Criterium
	{
		public String name;
		public int count;
		public int weight;
		
		public Criterium(String name, int count, int weight)
		{
			this.name = name;
			this.count = count;
			this.weight = weight;
		}
		
		public int getScore()
		{
			return count * weight;
		}
	}
	
	public Array<Criterium> criterias = new Array<RateManager.Criterium>();
	
	public int rate(Paragraph paragraph, Actor area)
	{
		criterias = new Array<RateManager.Criterium>();
		String paragraphString = paragraph.getCurrentString();
		
		checkRivers(area, paragraph.glyphs);	
		checkOneLetterWords(paragraphString);
		checkLeading(paragraph.job);		
		checkIndent(paragraph.job);
		checkLastRow(paragraph);
		checkStartPause(paragraphString);
		
		checkHyphenCount(paragraph);
		
		checkHyphenBefore(paragraph);
		checkHyphenAfter(paragraph);
		checkRightHoles(paragraph);	
		//------------------------------------------------------|
		
		return getCurrentRate();
	}

	public int getCurrentRate()
	{
		int score = MAX_SCORE;
		
		for(Criterium criterium : criterias)
		{
			score -= criterium.getScore();
		}
		
		return Math.max(score , 0);
	}
	
	private void checkRivers(Actor area, Array<Label> glyphs)
	{
		int rivers = 0;
		
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
		
		for(int i = 0; i < fd.length; i++)
			for(int j = 0; j < fd[0].length; j++)
			{
				fd1[i][j] = fd[i][j];
				fd2[i][j] = fd[i][j];
				fd3[i][j] = fd[i][j];
			}		
    		
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
    			if( fd1[i][j] == 0 && fd2[i][j] == 0 && fd3[i][j] == 0 )
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
    					rivers++;
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
        
        criterias.add( new Criterium("Liczba korytarzy", rivers, 10) );
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

	private void checkOneLetterWords(String text)
	{
		Array<String> lines = new Array<String>( text.split("\n") );
		int count = 0;
		
		for(String line : lines)
		{
			line = line.trim();
			
			if( line.charAt(line.length() - 2) == ' ')
			{
				if( line.charAt(line.length() - 1) != 'i' && String.valueOf( line.charAt(line.length() - 1) ).matches("[a-zA-Z]") )
				{
					count++;
				}
				else if( line.charAt(line.length() - 1) == 'i' )
				{
					line.replaceAll("\\s+","");
					
					if( line.length() > 40 )
					{
						count++;
					}
				}
			}
		}
		
		criterias.add( new Criterium("Wyrazy jednoliterowe\nna koñcu linii", count, 1) );
	}
	
	private void checkStartPause(String text)
	{
		Array<String> lines = new Array<String>( text.split("\n") );
		int count = 0;
		
		for(String line : lines)
		{
			line.trim();
			
			if( line.length() >= 50 && (line.charAt(0) == '–' || line.charAt(0) == '—' || line.charAt(0) == '-' )  )
			{
				count++;
			}
		}
		
		criterias.add( new Criterium("Pauza lub pó³pauza na pocz¹tku wiersza", count, 1) );
	}
	
	private void checkIndent(Job job)
	{
		int count = 0;
		
		float min = job.font_size, max = job.font_size;
		float precision = 0.1f * job.font_size;
		
		if( job.width / Gdx.graphics.getPpcX() > 11 ) //ile px w wierszu * px/cm = ile cm ma wiersz
		{
			max = 2 * job.font_size;
		}
		
		if( job.indent < min - precision || job.indent > max + precision )
		{
			count++;
		}
		
		criterias.add( new Criterium("B³êdne wciêcie akapitu", count, 1) );
	}
	
	private void checkLeading(Job job)
	{
		int count = 0;
		
		float leading = (job.lineHeight - job.font_size) * 2;
		
		if( leading < 2.6f || leading > 4 ) //2-3pt znalezc zrodlo - ile pt to jeden px?
		{
			count++;
		}
		
		criterias.add( new Criterium("Nieoptymalna interlinia", count, 1) );
	}
	
	private void checkRightHoles(Paragraph paragraph)
	{
		int count = 0;
		
		float minRowWidth = paragraph.job.width * 0.8f;
		
		float rowWidth = paragraph.job.indent; //bo pierwszy wiersz zaczyna sie wcieciem;
		
		for(Label glyph : paragraph.glyphs)
		{
			if( glyph.newLine )
			{
				if( rowWidth < minRowWidth )
				{
					count++;
				}
				
				rowWidth = 0;
			}
			
			rowWidth += glyph.getWidth();
		}
		
		//criterias.add( new Criterium("Nieregularna prawa strona g³êbiej ni¿ w 20% szerokoœci sk³adu", count, 0) );
	}
	
	private void checkLastRow(Paragraph paragraph)
	{
		int count = 0;
		
		float rowWidth = 0;
		
		for(int i = paragraph.glyphs.size - 1; i >= 0; i--)
		{
			rowWidth += paragraph.glyphs.get(i).getWidth();
			
			if( paragraph.glyphs.get(i).newLine )
			{
				if( rowWidth > paragraph.job.width - paragraph.job.indent )
				{
					count++;
				}
				
				break;
			}
		}
		
		criterias.add( new Criterium("Ostatni wiersz z³o¿ony na pe³en format", count, 1) );
	}
	
	private void checkHyphenCount(Paragraph paragraph)
	{
		int count = 0;
		
		//liczba literek a 
		
		criterias.add( new Criterium("B³êdna iloœæ przeniesieñ pod rz¹d", count, 0) );
	}
	
	private void checkHyphenAfter(Paragraph paragraph) 
	{
		int count = 0;
		
		boolean countLetters = false;
		int letters = 0;
		
		for(int i = 1; i < paragraph.glyphs.size; i++)
		{
			Label glyph = paragraph.glyphs.get(i);
			Label before = paragraph.glyphs.get(i-1);
			
			if( glyph.newLine && before.getHyphen() && !glyph.isSpace && !before.isSpace )
			{
				countLetters = true;
			}
			
			if( countLetters )
			{
				if( glyph.isSpace )
				{
					countLetters = false;
					
					if( letters < 3 ) count++;
					
					letters = 0;
				}
				else
				{
					letters++;
				}
				
			}
		}
		
		criterias.add( new Criterium("Przeniesione mniej ni¿ trzy znaki", count, 1) );
	}
	
	private void checkHyphenBefore(Paragraph paragraph) 
	{
		int count = 0;
		
		boolean countLetters = false;
		int letters = 0;
		
		for(int i = paragraph.glyphs.size - 2; i >= 0; i--)
		{
			Label glyph = paragraph.glyphs.get(i);
			Label after = paragraph.glyphs.get(i+1);
			
			if( after.newLine && glyph.getHyphen() && !glyph.isSpace && !after.isSpace )
			{
				countLetters = true;
			}
			
			if( countLetters )
			{
				if( glyph.isSpace )
				{
					countLetters = false;
					
					if( letters < 2 ) count++;
					
					letters = 0;
				}
				else
				{
					letters++;
				}
				
			}
		}
		
		criterias.add( new Criterium("Mniej ni¿ dwa znaki przed przeniesieniem", count, 1) );
	}
}
