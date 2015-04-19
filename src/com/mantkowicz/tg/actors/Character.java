package com.mantkowicz.tg.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mantkowicz.tg.managers.CameraManager;

public class Character extends Label
{
	public boolean isActive = false;
	
	Sentence sentence;
		
	private static LabelStyle prepareLabelStyle(BitmapFont font)
	{
		LabelStyle labelStyle = new LabelStyle(font, Color.WHITE);
		
		return labelStyle;
	}
	
	public Character(Sentence sentence, String text, BitmapFont font, float x, float y)
	{
		super(text, prepareLabelStyle(font));
		
		this.sentence = sentence;
		
		this.getColor().a = 0.50f;
		this.setPosition(x, y);
		//this.addListener(sentence.characterListener);
	}
}
