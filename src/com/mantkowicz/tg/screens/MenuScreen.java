package com.mantkowicz.tg.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.main.Main;
import com.mantkowicz.tg.managers.FontManager;
import com.mantkowicz.tg.managers.HttpManager;
import com.mantkowicz.tg.managers.JobHandler;

public class MenuScreen extends BaseScreen
{
	HttpManager manager;
	
	public MenuScreen(Main game)
	{
		super(game);
		
		manager = new HttpManager();
		
		clearWithGray = true;
	}

	@Override
	protected void prepare() 
	{
		Table table = new Table();
		table.background( (createImage("background_g30")).getDrawable() );
				
		table.setSize(1000, 0);
		
		table.row().width(table.getWidth()).pad(75, 0, 50, 0);
		table.add().width(table.getWidth()).colspan(4);
		table.row();
		
		for(Job job : JobHandler.getInstance().jobs)
		{
			addJob(table, job, 70);
			table.row().width(table.getWidth()).pad(25, 0, 25, 0);
			table.add().width(table.getWidth()).colspan(table.getColumns());
			table.row();
		}
		
		ScrollPane scrollPane = this.createScroll(table, 1020, 630, true);
		scrollPane.layout();
		scrollPane.setScrollY( 80 );
		
		setCenter(scrollPane, -350);
		
		this.stage.addActor(scrollPane);
		
		Image bar = createImage("background_g50", false);
		bar.setSize(2000, 150);
		bar.setPosition(-1000, 250);
		
		Image log = createImage("logoShort", false);
		log.setPosition(-600, 275);
		
		Label loggedAs = label("Zalogowany jako: " + JobHandler.getInstance().getUser( game.usr_id ).login, true);
		loggedAs.setPosition(-470, log.getY() + (log.getHeight() - loggedAs.getHeight() )/2f);
		
		TextButton fakeLogout = new TextButton("    WYLOGUJ", game.skin, "logout");
		fakeLogout.setPosition(350, log.getY() + (log.getHeight() - fakeLogout.getHeight() )/2f);
		
		this.stage.addActor(bar);
		this.stage.addActor(log);
		this.stage.addActor(fakeLogout);
		this.stage.addActor(loggedAs);
		
		
	}
	
	@Override
	protected void step() 
	{
	}
	
	protected void addJob(Table table, final Job job, float rowHeight)
	{
		Table row = new Table();
		row.background( (createImage("background_white")).getDrawable() );
		
		int c1=120, c2=150, c3=550, c4=120;
		
		row.add().width(c1).height(25);
		row.add().width(c2).height(25);
		row.add( label( "#" + String.valueOf( job.id ), true, true) ).width(c3).height(15).pad(25, 0, 0, 0);
		row.add().width(c4).height(25);
		row.row();
				
		row.add().width(c1).height(75);
		row.add( createImage("user") ).width(c2).height(75);	
		row.add( label( JobHandler.getInstance().getUser( job.usr_id ).login ) ).width(c3).height(75);
		row.add().width(c4).height(75);
		row.row();
		
		row.add().width(c1).height(75);	
		row.add( createImage("clock") ).width(c2).height(75);	
		row.add( label(String.valueOf(job.date_start).substring(0, 10)) ).width(c3).height(75);
		row.add().width(c4).height(75);		
		row.row();
		
		row.add().width(c1).height(75);	
		row.add( createImage("star") ).width(c2).height(75);		
		row.add( label( String.valueOf(job.points) ) ).width(c3).height(75);
		row.add().width(c4).height(75);	
		row.row();
		
		row.add().width(c1).height(75);	
		row.add( createImage("fontName") ).width(c2).height(75);		
		row.add( label( JobHandler.getInstance().getFont(job.fnt_id).name ) ).width(c3).height(75);
		row.add().width(c4).height(75);	
		row.row();
		
		row.add().width(c1).height(1);	
		row.add( createImage("background_g50", false) ).width(c2 + c3).height(1).colspan(2).pad(20,0,0,0);	
		row.add().width(c4).height(1);	
		row.row();
				
		//preparing content
		String contentValue = job.content;
		
		if(contentValue.length() > 50)
		{
			contentValue = contentValue.substring(0, 50);
			contentValue += "...";
		}
				
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", 25);
				
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.GRAY;
		
		Label contentLabel = new Label( contentValue, labelStyle);
		contentLabel.setWrap(true);
		//--content prepared
		
		row.add().width(c1).height(75);	
		row.add( contentLabel ).width(c2 + c3).colspan(2);		
		row.add().width(c4).height(75);	
		row.row();
		
		
		row.add().width(c1).height(1);	
		row.add( createImage("background_g50", false) ).width(c2 + c3).height(1).colspan(2);	
		row.add().width(c4).height(1);	
		row.row();
		
		
		TextButton start = new TextButton("    START", game.skin, "button");
		
		start.addListener(
				new ClickListener()
				{
					public void clicked(InputEvent event, float x, float y)
					{
						nextScreen = new GameScreen(game, job);
						changeScreen = true;
					}
				}
		);
		
		row.add().width(c1).height(75);	
		row.add( start ).width(182).height(72).colspan(2).pad(14,c2 + c3 - 182,25,0).fill(false);	
		row.add().width(c4).height(75);	
		row.row();
		
		table.add(row);
	}
	
	Label label(String txt, boolean small, boolean right)
	{
		Label l;
		
		if(small)
			l = new Label( txt, game.skin, "small");
		else
			l = new Label( txt, game.skin, "default");
		
		if(right)
			l.setAlignment(Align.right);

		return l;
	}
	
	Label label(String txt)
	{
		return label(txt, false, false);
	}
	
	Label label(String txt, boolean small)
	{
		return label(txt, small, false);
	}
}
