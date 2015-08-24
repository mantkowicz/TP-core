package com.mantkowicz.tg.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.mantkowicz.tg.actors.Character;
import com.mantkowicz.tg.actors.Menu;
import com.mantkowicz.tg.actors.Paragraph;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.CameraManager;
import com.mantkowicz.tg.managers.FontManager;
import com.mantkowicz.tg.managers.GestureManager;
import com.mantkowicz.tg.managers.ScreenShotManager;
import com.mantkowicz.tg.stage.MyStage;
import com.mantkowicz.tg.ui.Switch;

public class GameScreen extends BaseScreen
{
	Job job;
	/*
	Menu mainMenu;
	Menu sentenceMenu;
	
	GestureManager gm;
	
	ExtendViewport uiViewport;
	Stage uiStage;
	
	Switch s;
	
	Character label;
	
	Button markSentence, markCharacter, moveCamera;
	
	Paragraph paragraph;
	
	float lastValue = 0;
	
	final float STEP = 1;
	
	InputMultiplexer inputMultiplexer;
	
	GestureDetector gestureDetector;
	*/
	public GameScreen(Main game, Job job)
	{
		super(game);
		
		this.job = job;
		
		//this.uiViewport = new ExtendViewport(this.screenWidth, this.screenHeight);
		
		//this.uiStage = new MyStage();	
		
		//this.uiStage.setViewport(this.uiViewport);
	}

	@Override
	protected void prepare()
	{
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", job.font_size);
		
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;
		Label contentLabel = new Label(job.content, labelStyle);
		contentLabel.setWrap(true);
		
		contentLabel.debug();
		contentLabel.setAlignment(Align.topLeft);
		contentLabel.setSize(job.width, job.height);
		contentLabel.setPosition(-job.width/2.0f, -job.height/2.0f);
		
		this.stage.addActor(contentLabel);
		
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
		
		this.gm = new GestureManager(this.stage);
		
		gestureDetector = new GestureDetector(this.gm);
		
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(this.stage);
		inputMultiplexer.addProcessor(this.uiStage);
		//inputMultiplexer.addProcessor(gestureDetector);
		
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		CameraManager.getInstance().setCamera(this.stage.getCamera());
		*/
	}

	@Override
	protected void step()
	{
		if( Gdx.input.isKeyJustPressed( Keys.P) )
		{
			ScreenShotManager.saveScreenshot();
		}
		
		/*
		this.uiViewport.update(this.screenWidth, this.screenHeight);
		this.uiStage.act();
		this.uiStage.draw();
		
		
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
		
		CameraManager.getInstance().step();
		
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
}
