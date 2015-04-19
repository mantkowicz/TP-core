package com.mantkowicz.tg.screens;

import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mantkowicz.tg.main.Main;

public class ResultScreen extends BaseScreen
{
	int result = 0;
	int counter = 0;
	
	Label label;
	Label tap;
	
	boolean labelHidden = true;
	
	private AlphaAction showAction;
	private AlphaAction hideAction;
	
	public ResultScreen(Main game)
	{
		super(game);
	}

	@Override
	protected void prepare()
	{
		label = new Label(result + "%", this.game.skin, "big");
		setCenter(label, -50);
		
		stage.addActor(label);
		
		tap = new Label("Tap to go back to menu", this.game.skin, "italic");
		setCenter(tap, -150);
		
		tap.getColor().a = 0;
		
		showAction = new AlphaAction();
		showAction.setAlpha(1);
		showAction.setDuration(1);
		
		hideAction = new AlphaAction();
		hideAction.setAlpha(0);
		hideAction.setDuration(1);
		
		this.nextScreen = new MenuScreen( this.game );
		stage.addListener(nextScreenListener);
	}

	@Override
	protected void step()
	{
		counter++;
		
		if(counter < 90)
		{
			result++;
			
			label.setText(result + "%");
			setCenter(label, -50);
		}
		if( counter > 90 )
		{
			labelHidden = false;
			
			this.stage.addActor(tap);
		}
		
		if( !labelHidden && tap.getActions().size <= 0 )
		{
			if( tap.getColor().a == 0 )
			{
				showAction.reset();
				tap.addAction(showAction);				
			}
			else if(tap.getColor().a == 1)
			{
				hideAction.reset();
				tap.addAction(hideAction);
			}
		}
	}
}
