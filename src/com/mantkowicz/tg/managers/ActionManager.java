package com.mantkowicz.tg.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.mantkowicz.tg.enums.ActionType;
import com.mantkowicz.tg.logger.Logger;

public class ActionManager
{
	private static ActionManager INSTANCE = new ActionManager();
	public static ActionManager getInstance()
	{
		return INSTANCE;
	}
	
	private HashMap<ActionType, Action> actions;
	
	private ActionManager()
	{
		this.actions = new HashMap<ActionType, Action>();
		
		//SHOW action
		AlphaAction showAction = new AlphaAction();
		showAction.setAlpha(1);
		showAction.setDuration(1);
		
		actions.put(ActionType.SHOW, showAction);
		
		//HIDE action
		AlphaAction hideAction = new AlphaAction();
		hideAction.setAlpha(0);
		hideAction.setDuration(1);
		
		actions.put(ActionType.HIDE, hideAction);
		
		//HIDE_STAGE action
		AlphaAction hideStageAction = new AlphaAction();
		hideStageAction.setAlpha(0);
		hideStageAction.setDuration(0.3f);
		
		actions.put(ActionType.HIDE_STAGE, hideStageAction);
		
		//HIDE_MENU action
		MoveToAction hideMenuAction = new MoveToAction();
		hideMenuAction.setPosition(0, 768);
		hideMenuAction.setDuration(0.3f);
		
		actions.put(ActionType.HIDE_MENU, hideMenuAction);
		
		//SHOW_MENU action
		MoveToAction showMenuAction = new MoveToAction();
		showMenuAction.setPosition(0, 0);
		showMenuAction.setDuration(0.3f);
		
		actions.put(ActionType.SHOW_MENU, showMenuAction);
	}
	
	public Action getAction(ActionType actionType)
	{
		Action action = this.actions.get(actionType);
		
		action.reset();
	
		return action; 
	}

	public boolean isActionFinished(ActionType actionType)
	{
		if(!(this.actions.get(actionType) instanceof AlphaAction)) 
		{
			Logger.log(this, "Remember of action type!");
			Gdx.app.exit();
		}
		
		AlphaAction action = (AlphaAction) this.actions.get(actionType);
		
		return action.getTime() >= action.getDuration();
	}
}
