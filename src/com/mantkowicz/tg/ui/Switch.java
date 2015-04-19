package com.mantkowicz.tg.ui;

import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class Switch extends Actor
{
	Array<Button> buttons;
	HashMap<Button, String> states;
	
	public boolean changed = true;
	
	public Switch()
	{
		buttons = new Array<Button>();
		states = new HashMap<Button, String>();
	}
	
	public void addButton(String state, Button button)
	{
		states.put(button, state);
		buttons.add(button);
		
		button.setVisible(false);
		button.addListener(buttonListener);
	}
	
	public void addToStage(Stage stage)
	{
		for(Button button : buttons)
		{
			stage.addActor(button);
		}
		
		buttons.first().setVisible(true);
	}
	
	public String getState()
	{
		for(Button button : buttons)
		{
			if(button.isVisible())
			{
				return (String)states.get(button);
			}
		}
		
		return null;
	}
	
	@Override
	public void setPosition(float x, float y)
	{
		for(Button button: buttons)
		{
			button.setPosition(x, y);
		}
		
		super.setPosition(x, y);
	}
	
	@Override
	public void setSize(float width, float height)
	{
		for(Button button: buttons)
		{
			button.setSize(width, height);
		}
		
		super.setSize(width, height);
	}
	
	ClickListener buttonListener = new ClickListener() 
	  {
		  public void clicked(InputEvent event, float x, float y)
	 	  {			  
			  for(int i = 0; i < buttons.size; i++)
			  {
				  if( buttons.get(i).isVisible() )
				  {
					  buttons.get(i).setVisible(false);
					  
					  if( i < buttons.size - 1 )
					  {
						  buttons.get(i+1).setVisible(true);
					  }
					  else
					  {
						  buttons.get(0).setVisible(true);
					  }
					  
					  break;
				  }
			  }
			  
			  changed = true;
	 	  }
	  };
}
