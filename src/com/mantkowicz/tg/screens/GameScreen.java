package com.mantkowicz.tg.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mantkowicz.tg.actors.CustomLabel;
import com.mantkowicz.tg.actors.Indicator;
import com.mantkowicz.tg.actors.Label;
import com.mantkowicz.tg.enums.IndicatorType;
import com.mantkowicz.tg.enums.ZoomType;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.CameraManager;
import com.mantkowicz.tg.managers.GestureManager;
import com.mantkowicz.tg.managers.ScreenShotManager;

public class GameScreen extends BaseScreen
{
	ExtendViewport uiViewport;
	Stage uiStage;
	
	InputMultiplexer inputMultiplexer;
	
	GestureManager gestureManager;
	GestureDetector gestureDetector;
		
	Job job;
	
	CustomLabel paragraph;
	
	Button document, camera, cancel;
	Button shrinkButton, stretchButton, zoomInButton, zoomOutButton, cancelSmall;
	Button menuShowButton, menuHideButton, homeButton, backButton, clearButton, settingsButton, uploadButton;
	
	Label cameraLabel, documentLabel, cancelLabel;
	HashMap<Button, Label> buttonLabels;
	
	Indicator indicatorStart;
	Indicator indicatorEnd;
	
	boolean checkMenuActions = false;
	boolean zoomModeControlRemoved = true;

	public GameScreen(Main game, Job job)
	{
		super(game);
		
		clearWithGray = true;
		
		this.job = job;
		
		this.uiViewport = new ExtendViewport(this.screenWidth, this.screenHeight);
		
		this.uiStage = new Stage();	
		
		this.uiStage.setViewport(this.uiViewport);
		
		this.nextScreen = new MenuScreen( this.game );
		
		buttonLabels = new HashMap<Button, Label>();
	}

	@Override
	protected void prepare()
	{	
		Image paperShadow = new Image( getAtlasRegion("background_g50") );
		paperShadow.setSize(job.width + 2*job.padding, job.height + 2*job.padding);
		paperShadow.setPosition(-job.width/2.0f - job.padding + 5, -job.height/2.0f - job.padding - 5);
		
		stage.addActor(paperShadow);
		
		Image paperBorder = new Image( getAtlasRegion("background_black") );
		paperBorder.setSize(job.width + 2*job.padding + 2, job.height + 2*job.padding + 2);
		paperBorder.setPosition(-job.width/2.0f - job.padding - 1, -job.height/2.0f - job.padding - 1);
		
		stage.addActor(paperBorder);
		
		Image paperPadding = new Image( getAtlasRegion("background_white") );
		paperPadding.setSize(job.width + 2*job.padding, job.height + 2*job.padding);
		paperPadding.setPosition(-job.width/2.0f - job.padding, -job.height/2.0f - job.padding);
		
		stage.addActor(paperPadding);
		
		Image paperPaddingBorder = new Image( getAtlasRegion("background_g30") );
		paperPaddingBorder.setSize(job.width + 2, job.height + 2);
		paperPaddingBorder.setPosition(-job.width/2.0f - 1, -job.height/2.0f - 1);
		
		stage.addActor(paperPaddingBorder);
		
		Image paper = new Image( getAtlasRegion("background_white") );
		paper.setSize(job.width, job.height);
		paper.setPosition(-job.width/2.0f, -job.height/2.0f);
		
		stage.addActor(paper);
		
		Image markedBackground = new Image( getAtlasRegion("background_blue") );
		
		paragraph = new CustomLabel(job, stage, markedBackground.getDrawable());
		
		paragraph.addToStage();
		
		
		indicatorStart = new Indicator(IndicatorType.START);
		indicatorStart.setGrid(paragraph.glyphs);
		indicatorStart.setVisible(false);
		
		stage.addActor(indicatorStart);
		
		indicatorEnd = new Indicator(IndicatorType.END);	
		indicatorEnd.setGrid(paragraph.glyphs);
		indicatorEnd.setVisible(false);
		
		stage.addActor(indicatorEnd);
		
		
		createUi();
		
		
		this.gestureManager = new GestureManager(this.stage, paragraph);
		
		gestureDetector = new GestureDetector(this.gestureManager);
		
		inputMultiplexer = new InputMultiplexer();
		
		inputMultiplexer.addProcessor(this.stage);
		inputMultiplexer.addProcessor(this.uiStage);
		inputMultiplexer.addProcessor(gestureDetector);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		CameraManager.getInstance().setCamera(this.stage.getCamera());
		CameraManager.getInstance().showParagraph(paragraph);
	}
	
	@Override
	protected void step()
	{			
		paragraph.addToStage();
		
		if( paragraph.longPressedId != -1 )
		{
			indicatorStart.setCurrentId( paragraph.getWordStart() );
			indicatorEnd.setCurrentId( paragraph.getWordEnd() );
			
			indicatorStart.setVisible(true);
			indicatorEnd.setVisible(true);
		}
		
		if( indicatorStart.isVisible() && indicatorEnd.isVisible() )
		{
			paragraph.startId = indicatorStart.getCurrentId();
			indicatorEnd.setMin(paragraph.startId);
			
			paragraph.endId = indicatorEnd.getCurrentId();
			indicatorStart.setMax(paragraph.endId);
			
			if(game.isMobile && zoomModeControlRemoved)
			{
				uiStage.addActor(camera);
				uiStage.addActor(cameraLabel);
				
				uiStage.addActor(document);
				uiStage.addActor(documentLabel);
				
				uiStage.addActor(cancel);
				uiStage.addActor(cancelLabel);
				
				zoomModeControlRemoved = false;
			}
		}
		else
		{
			paragraph.startId = -1;
			paragraph.endId = -1;
			
			if(game.isMobile && !zoomModeControlRemoved)
			{
				camera.remove();
				cameraLabel.remove();
				
				document.remove();
				documentLabel.remove();
				
				cancel.remove();
				cancelLabel.remove();
				
				zoomModeControlRemoved = true;
			}
		}
		
		
		if( Gdx.input.isKeyJustPressed( Keys.P) )
		{
			ScreenShotManager.saveScreenshot();
		}
		
		if( Gdx.input.isKeyJustPressed( Keys.R) )
		{
			indicatorStart.setVisible(true);
			indicatorEnd.setVisible(true);
		}
		
		if( Gdx.input.isKeyJustPressed( Keys.T) )
		{
			indicatorStart.setVisible(false);
			indicatorEnd.setVisible(false);
		}
				
		
		this.uiViewport.update(this.screenWidth, this.screenHeight);
		this.uiStage.act();
		this.uiStage.draw();
		
		if( checkMenuActions )
		{			
			if( (homeButton.getActions().size != 0 || uploadButton.getActions().size != 0) && (menuHideButton.getListeners().size != 0 || menuShowButton.getListeners().size != 0) )
			{
				menuHideButton.clearListeners();
				menuShowButton.clearListeners();
			}
			else if( (homeButton.getActions().size == 0 || uploadButton.getActions().size == 0) && (menuHideButton.getListeners().size == 0 && menuShowButton.getListeners().size == 0) )
			{
				if( homeButton.getY() == -800 )
				{
					menuShowButton.addListener(menuShowListener);
					
					checkMenuActions = false;
				}
				else if(uploadButton.getY() == -300)
				{
					menuHideButton.addListener(menuHideListener);
					
					checkMenuActions = false;
				}
			}
		}
		
		CameraManager.getInstance().step();
	}	
	
	private void createUi()
	{
		menuShowButton = new Button(this.game.skin, "menuShow");
		menuHideButton = new Button(this.game.skin, "menuHide");
		homeButton = new Button(this.game.skin, "home");
		backButton = new Button(this.game.skin, "back");
		clearButton = new Button(this.game.skin, "clear");
		settingsButton = new Button(this.game.skin, "settings");
		uploadButton = new Button(this.game.skin, "upload");
		
		menuShowButton.addListener(menuShowListener);
		menuHideButton.addListener(menuHideListener);
		homeButton.addListener(homeListener);
		backButton.addListener(backListener);
		clearButton.addListener(clearListener);
		settingsButton.addListener(settingsListener);
		uploadButton.addListener(uploadListener);
		
		menuShowButton.setPosition(450, -350);
		menuHideButton.setPosition(450, -350);
		homeButton.setPosition(-550, -800);
		backButton.setPosition(-350, -800);
		clearButton.setPosition(-150, -800);
		settingsButton.setPosition(50, -800);
		uploadButton.setPosition(250, -800);
		
		menuHideButton.setVisible(false);
		
		createButtonLabel(homeButton, "powrót\n do menu");
		createButtonLabel(backButton, "cofnij\n akcjê");
		createButtonLabel(clearButton, "zacznij\n od nowa");
		createButtonLabel(settingsButton, "ustawienia");
		createButtonLabel(uploadButton, "wyœlij\n rozwi¹zanie");
		
		uiStage.addActor(buttonLabels.get(homeButton));
		uiStage.addActor(buttonLabels.get(backButton));
		uiStage.addActor(buttonLabels.get(clearButton));
		uiStage.addActor(buttonLabels.get(settingsButton));
		uiStage.addActor(buttonLabels.get(uploadButton));
		
		uiStage.addActor(menuShowButton);
		uiStage.addActor(menuHideButton);
		uiStage.addActor(homeButton);
		uiStage.addActor(backButton);
		uiStage.addActor(clearButton);
		uiStage.addActor(settingsButton);
		uiStage.addActor(uploadButton);
		
		if( !Main.isMobile )
		{
			shrinkButton = new Button(this.game.skin, "shrink");
			stretchButton = new Button(this.game.skin, "stretch");
			zoomInButton = new Button(this.game.skin, "zoomIn");
			zoomOutButton = new Button(this.game.skin, "zoomOut");
			cancelSmall = new Button(this.game.skin, "cancelSmall");
			
			cancelSmall.setPosition(225, 320);
			stretchButton.setPosition(300, 320);
			shrinkButton.setPosition(375, 320);
			zoomInButton.setPosition(450, 320);
			zoomOutButton.setPosition(525, 320);	
			
			cancelSmall.addListener(cancelListener);
			stretchButton.addListener(stretchListener);
			shrinkButton.addListener(shrinkListener);
			zoomInButton.addListener(zoomInListener);
			zoomOutButton.addListener(zoomOutListener);
			
			uiStage.addActor(cancelSmall);
			uiStage.addActor(stretchButton);
			uiStage.addActor(shrinkButton);
			uiStage.addActor(zoomInButton);
			uiStage.addActor(zoomOutButton);
		}
		else
		{
			camera = new Button(game.skin, "camera");
			document = new Button(game.skin, "document");
			cancel = new Button(game.skin, "cancel");
					
			camera.setPosition(450, 250);
			document.setPosition(450, 250);
			cancel.setPosition(450, 50);
					
			document.setVisible(false);
			
			camera.addListener(cameraListener);
			document.addListener(documentListener);
			cancel.addListener(cancelListener);
			
			cameraLabel = new Label("przybli¿enie\nkamery", game.skin, "small");
			cameraLabel.setAlignment(Align.center);
			cameraLabel.setWrap(true);
			cameraLabel.setWidth(140);
			cameraLabel.setPosition(430, 230 - cameraLabel.getHeight());
			
			documentLabel = new Label("modyfikacja\nkerningu", game.skin, "small");
			documentLabel.setAlignment(Align.center);
			documentLabel.setWrap(true);
			documentLabel.setWidth(140);
			documentLabel.setPosition(430, 230 - documentLabel.getHeight());
			
			cancelLabel = new Label("anuluj\nzaznaczenie", game.skin, "small");
			cancelLabel.setAlignment(Align.center);
			cancelLabel.setWrap(true);
			cancelLabel.setWidth(140);
			cancelLabel.setPosition(430, 40 - cancelLabel.getHeight());
			
			documentLabel.setVisible(false);
		}
	}
	
	
	void createButtonLabel(Button button, String text)
	{
		Label label = new Label(text, game.skin, "small");
		label.setWrap(true);
		label.setAlignment(Align.center);
		label.setWidth(button.getWidth() + 30);
		label.getColor().a = 0;
		
		label.setPosition(button.getX() - 15, -320 - label.getHeight());

		buttonLabels.put(button, label);
	}
	
	SequenceAction createButtonInAction(Button button, float delay)
	{
		MoveToAction action1 = new MoveToAction();
		action1.setPosition(button.getX(), button.getY());
		action1.setDuration(delay);
		
		MoveToAction action2 = new MoveToAction();
		action2.setPosition(button.getX(), button.getY() + 550);
		action2.setDuration(0.2f);
		
		MoveToAction action3 = new MoveToAction();
		action3.setPosition(button.getX(), button.getY() + 500);
		action3.setDuration(0.1f);
		
		return new SequenceAction( action1, action2, action3 );
	}
	
	SequenceAction createButtonOutAction(Button button, float delay)
	{
		MoveToAction action1 = new MoveToAction();
		action1.setPosition(button.getX(), button.getY());
		action1.setDuration(delay);
		
		MoveToAction action2 = new MoveToAction();
		action2.setPosition(button.getX(), -800);
		action2.setDuration(0.2f);
				
		return new SequenceAction( action1, action2 );
	}
	
	SequenceAction createLabelInAction(Label label)
	{
		MoveToAction action1 = new MoveToAction();
		action1.setPosition(label.getX(), label.getY());
		action1.setDuration(0.4f);
		
		AlphaAction action2 = new AlphaAction();
		action2.setAlpha(1);
		action2.setDuration(0.15f);
		
		return new SequenceAction( action1, action2 );
	}
	
	AlphaAction createLabelOutAction()
	{		
		AlphaAction action = new AlphaAction();
		action.setAlpha(0);
		action.setDuration(0.1f);
		
		return action;
	}
	
	ClickListener menuShowListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			if(menuShowButton != null) menuShowButton.setVisible(false);
			if(menuHideButton != null) menuHideButton.setVisible(true);
			
			homeButton.addAction( createButtonInAction(homeButton, 0) );
			backButton.addAction( createButtonInAction(backButton, 0.01f) );
			clearButton.addAction( createButtonInAction(clearButton, 0.02f) );
			settingsButton.addAction( createButtonInAction(settingsButton, 0.03f) );
			uploadButton.addAction( createButtonInAction(uploadButton, 0.04f) );
			
			buttonLabels.get(homeButton).addAction( createLabelInAction( buttonLabels.get(homeButton) ) );
			buttonLabels.get(backButton).addAction( createLabelInAction( buttonLabels.get(backButton) ) );
			buttonLabels.get(clearButton).addAction( createLabelInAction( buttonLabels.get(clearButton) ) );
			buttonLabels.get(settingsButton).addAction( createLabelInAction( buttonLabels.get(settingsButton) ) );
			buttonLabels.get(uploadButton).addAction( createLabelInAction( buttonLabels.get(uploadButton) ) );
			
			checkMenuActions = true;
		}
	};
	
	ClickListener menuHideListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			if(menuHideButton != null) menuHideButton.setVisible(false);
			if(menuShowButton != null) menuShowButton.setVisible(true);
			
			homeButton.addAction( createButtonOutAction(homeButton, 0.08f) );
			backButton.addAction( createButtonOutAction(backButton, 0.06f) );
			clearButton.addAction( createButtonOutAction(clearButton, 0.04f) );
			settingsButton.addAction( createButtonOutAction(settingsButton, 0.02f) );
			uploadButton.addAction( createButtonOutAction(uploadButton, 0) );	
			
			buttonLabels.get(homeButton).addAction( createLabelOutAction() );
			buttonLabels.get(backButton).addAction( createLabelOutAction() );
			buttonLabels.get(clearButton).addAction( createLabelOutAction() );
			buttonLabels.get(settingsButton).addAction( createLabelOutAction() );
			buttonLabels.get(uploadButton).addAction( createLabelOutAction() );
			
			checkMenuActions = true;
		}
	};
	
	ClickListener homeListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			changeScreen = true;
		}
	};
	
	ClickListener backListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
		}
	};
	
	ClickListener clearListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
		}
	};
	
	ClickListener settingsListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
		}
	};
	
	ClickListener uploadListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
		}
	};
	
	ClickListener stretchListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			paragraph.kerningModificator = 1f;
		}
	};
	
	ClickListener shrinkListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			paragraph.kerningModificator = -1f;
		}
	};
	
	ClickListener zoomInListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			((OrthographicCamera)stage.getCamera()).zoom -= 0.05f;
		}
	};
	
	ClickListener zoomOutListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			((OrthographicCamera)stage.getCamera()).zoom += 0.05f;
		}
	};
	
	ClickListener cameraListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			document.setVisible(true);
			documentLabel.setVisible(true);
			
			camera.setVisible(false);
			cameraLabel.setVisible(false);
			
			gestureManager.zoomType = ZoomType.KERNING;
		}
	};
	
	ClickListener documentListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			document.setVisible(false);
			documentLabel.setVisible(false);
			
			camera.setVisible(true);
			cameraLabel.setVisible(true);
			
			gestureManager.zoomType = ZoomType.CAMERA;
		}
	};
	
	ClickListener cancelListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
			indicatorStart.setVisible(false);
			indicatorEnd.setVisible(false);
			
			gestureManager.zoomType = ZoomType.CAMERA;
			gestureManager.lastDistance = 0;
		}
	};
}
