package com.mantkowicz.tg.screens;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mantkowicz.tg.actors.CustomLabel;
import com.mantkowicz.tg.actors.Indicator;
import com.mantkowicz.tg.actors.Label;
import com.mantkowicz.tg.enums.IndicatorType;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.CameraManager;
import com.mantkowicz.tg.managers.GestureManager;
import com.mantkowicz.tg.managers.ScreenShotManager;
import com.mantkowicz.tg.stage.MyStage;

public class GameScreen extends BaseScreen
{
	Job job;
	CustomLabel l;
	Indicator ind;
	Indicator ind2;
	
	boolean checkMenuActions = false;
	/*
	Menu mainMenu;
	Menu sentenceMenu;
	
	
	
	*/ExtendViewport uiViewport;
	Stage uiStage;/*
	
	Switch s;
	
	Character label;
	
	Button markSentence, markCharacter, moveCamera;
	
	Paragraph paragraph;
	
	float lastValue = 0;
	
	final float STEP = 1;
	
	*/InputMultiplexer inputMultiplexer;
	
	GestureManager gm;
	GestureDetector gestureDetector;
	
	Button document, camera;
	Button shrinkButton, stretchButton, zoomInButton, zoomOutButton;
	Button menuShowButton, menuHideButton, homeButton, backButton, clearButton, settingsButton, uploadButton;
	
	Label cameraLabel, documentLabel;
	HashMap<Button, Label> buttonLabels;
	
	public GameScreen(Main game, Job job)
	{
		super(game);
		
		this.job = job;
		
		this.uiViewport = new ExtendViewport(this.screenWidth, this.screenHeight);
		
		this.uiStage = new MyStage();	
		
		this.uiStage.setViewport(this.uiViewport);
		
		this.nextScreen = new MenuScreen( this.game );
	}

	@Override
	protected void prepare()
	{	
		buttonLabels = new HashMap<Button, Label>();
		
		l = new CustomLabel(job, stage);
		
		CameraManager.getInstance().setCamera(this.stage.getCamera());
		
		ind = new Indicator(IndicatorType.START);
		ind2 = new Indicator(IndicatorType.END);
		
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
			
			stretchButton.setPosition(300, 320);
			shrinkButton.setPosition(375, 320);
			zoomInButton.setPosition(450, 320);
			zoomOutButton.setPosition(525, 320);	
			
			stretchButton.addListener(stretchListener);
			shrinkButton.addListener(shrinkListener);
			zoomInButton.addListener(zoomInListener);
			zoomOutButton.addListener(zoomOutListener);
			
			uiStage.addActor(stretchButton);
			uiStage.addActor(shrinkButton);
			uiStage.addActor(zoomInButton);
			uiStage.addActor(zoomOutButton);
		}
		
		camera = new Button(game.skin, "camera");
		document = new Button(game.skin, "document");
				
		camera.setPosition(450, 250);
		document.setPosition(450, 250);
		
		document.setVisible(false);
		
		camera.addListener(cameraListener);
		document.addListener(documentListener);
		
		uiStage.addActor(camera);
		uiStage.addActor(document);
		
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
		
		documentLabel.setVisible(false);
		
		uiStage.addActor(cameraLabel);
		uiStage.addActor(documentLabel);
		
		/*
		moveCamera = new Button(this.game.skin, "moveCamera");
		markSentence = new Button(this.game.skin, "markSentence");
		markCharacter = new Button(this.game.skin, "markCharacter");
		
		s = new Switch();
		s.addButton("CAMERA", moveCamera);
		s.addButton("SENTENCE", markSentence);
		s.addButton("CHARACTER", markCharacter);
		
		s.setPosition(400, 244);
		
		s.addToStage(uiStage);
		
				
		mainMenu = new Menu( createImage("verticalMenuBar"), new Button(this.game.skin, "menuShow") );
		
		mainMenu.addButton(new Button(this.game.skin, "home"));
		mainMenu.addButton(new Button(this.game.skin, "settings"), nextScreenListener);
		mainMenu.addButton(new Button(this.game.skin, "verify"));
		//mainMenu.addButton(new Button(this.game.skin, "next"));
		
		mainMenu.prepare();
		
		mainMenu.setPosition(543, -384);
		
		
		
		
		
		ClickListener previousListener = new ClickListener() 
		   {
		   		public void clicked(InputEvent event, float x, float y)
		   		{
		   			if( paragraph.getCurrentCharacter() != null )
		   			{
		   				
		   			}
		   			paragraph.deactivateAll();
		   			
		   		}
		   };
		
		
		
		
		
		
		
		
		sentenceMenu = new Menu( createImage("verticalMenuBar"), new Button(this.game.skin, "menuShow") );
		
		sentenceMenu.addButton(new Button(this.game.skin, "showAll"));//, showAllListener);
		sentenceMenu.addButton(new Button(this.game.skin, "sensitivity"));
		sentenceMenu.addButton(new Button(this.game.skin, "previous"), previousListener);
		//sentenceMenu.addButton(new Button(this.game.skin, "next"), nextListener);
		
		sentenceMenu.prepare();
		
		sentenceMenu.setPosition(543, -384);
		
		sentenceMenu.setVisible(false);
		
		uiStage.addActor(mainMenu);
		uiStage.addActor(sentenceMenu);
		
				
		
		BitmapFont font = new BitmapFont(Gdx.files.internal("fonts/big.fnt"), this.getAtlasRegion("big") );
		
		paragraph = new Paragraph("tutaj jest jedna linia\n tutaj kolejna\n a ta trzecia i ostatnia\n i czwarta na dok³adkê", font);
		
		this.stage.addActor(paragraph);
		
		nextScreen = new ResultScreen(this.game);
		*/
		this.gm = new GestureManager(this.stage);
		
		gestureDetector = new GestureDetector(this.gm);
		
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this.stage);
		
		inputMultiplexer.addProcessor(this.uiStage);
		
		inputMultiplexer.addProcessor(gestureDetector);
		//inputMultiplexer.addProcessor(gestureDetector);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
			
		
		l.addToStage();
		
		av = new Array<Vector2>();
		wv = new FloatArray();
		
		

		ind.setGrid(l.glyphs);
		stage.addActor(ind);
		
		ind2.setGrid(l.glyphs);
		stage.addActor(ind2);
	}
	
	Array<Vector2> av;
	FloatArray wv;

	@Override
	protected void step()
	{			
		l.addToStage();
		
		l.endId = 20;
		
		l.startId = ind.getCurrentId();
		
		l.endId = ind2.getCurrentId();
		
		ind.setMax(l.endId);
		ind2.setMin(l.startId);
		
		if( Gdx.input.isKeyJustPressed( Keys.P) )
		{
			ScreenShotManager.saveScreenshot();
		}
		
		if( Gdx.input.isKeyJustPressed( Keys.R) )
		{
			//l.toggle();
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
		
		/*
		
		if( this.paragraph.getCurrentCharacter() != null || this.paragraph.getCurrentSentence() != null )
		{
			this.mainMenu.setVisible(false);
			this.sentenceMenu.setVisible(true);
		}
		else
		{
			this.mainMenu.setVisible(true);
			this.sentenceMenu.setVisible(false);
		}
		
		paragraph.refreshCharacters();
		*/
		CameraManager.getInstance().step();
		/*
		if(s.changed)
		{
			if( s.getState() == "CAMERA" )
			{
				inputMultiplexer.addProcessor(gestureDetector);
				inputMultiplexer.removeProcessor(this.stage);
				
				Gdx.input.setInputProcessor(inputMultiplexer);
			}
			else if( s.getState() == "SENTENCE" )
			{
				inputMultiplexer.removeProcessor(gestureDetector);
				inputMultiplexer.addProcessor(this.stage);
				
				Gdx.input.setInputProcessor(inputMultiplexer);
			}
			else if( s.getState() == "CHARACTER" )
			{
				Gdx.input.setInputProcessor(inputMultiplexer);
			}
			s.changed = false;
		}*/
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
		}
	};
	
	ClickListener shrinkListener = new ClickListener() 
	{
		public void clicked(InputEvent event, float x, float y)
		{
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
		}
	};
}
