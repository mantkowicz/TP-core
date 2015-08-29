package com.mantkowicz.tg.managers;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class FontManager
{
	private static FontManager INSTANCE = new FontManager();
	public static FontManager getInstance()
	{
		return INSTANCE;
	}
	
	private FontManager()
	{

	}
	
	public BitmapFont generateFont(String fontPath, int fontSize)
	{ 		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator( Gdx.files.local(fontPath) );
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		
		parameter.characters = "qwertyuiopasdfghjklzxcvbnm�󹜳����QWERTYUIOPASDFGHJKLZXCVBNM�ӥ������1234567890-=[]\\;\',./!@#$%^&*()_+{}|:\"<>?";
		parameter.kerning = true;
		parameter.size = fontSize;
		parameter.magFilter = TextureFilter.Linear;
		parameter.minFilter = TextureFilter.Linear;
		
		return generator.generateFont(parameter);
	}
}
