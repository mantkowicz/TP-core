package com.mantkowicz.tg.actors;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.managers.ActionManager;

public class Menu extends Group
{
	Array<Button> buttons;
	Image menuBar;
	
	Button show;
	
	final Group menuElements;
	
	public Menu(Image menuBar, Button show)
	{
		super();
		
		menuElements = new Group();
		
		this.menuBar = menuBar;
		this.show = show;
		
		buttons = new Array<Button>();
	}
	
	public void addButton(Button button)
	{
		this.buttons.add(button);
	}
	
	public void addButton(Button button, EventListener listener)
	{
		button.addListener(listener);
		addButton(button);
	}

	public void prepare()
	{
		menuElements.addActor(menuBar);
		
		show.setPosition(-10, menuBar.getHeight() - 140);
		
		show.addListener(
				new ClickListener() 
				  {
					  public void clicked(InputEvent event, float x, float y)
				 	  {
						  if(menuElements.getY() == 768)
							  menuElements.addAction(ActionManager.getInstance().getAction(ActionType.SHOW_MENU));
						  else
							  menuElements.addAction(ActionManager.getInstance().getAction(ActionType.HIDE_MENU));
				 	  }
				  }
			);
		
		float y = (130 * buttons.size) / 2.0f + ((menuBar.getHeight()-140) / 2.0f) - 130;
		
		for(Button button : buttons)
		{
			button.setPosition(-10, y);
			
			menuElements.addActor(button);
			
			y-= 130;
		}
		
		this.addActor(menuElements);
		this.addActor(show);
	}	
}
