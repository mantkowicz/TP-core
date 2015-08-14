package com.mantkowicz.tg.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	}

	@Override
	protected void prepare() 
	{
		Table table = new Table();
		table.debug();
		
		table.setSize(1200, 0);
		
		for(Job job : JobHandler.getInstance().jobs)
		{
			addJob(table, job, 30);
			table.row().width(table.getWidth()).pad(50, 0, 50, 0);
			table.add().width(table.getWidth()).colspan(table.getColumns());
			table.row();
		}
		
		ScrollPane scrollPane = this.createScroll(table, 1220, 500, true);
		scrollPane.debug();
		
		setCenter(scrollPane, -300);
		
		this.stage.addActor(scrollPane);
	}

	@Override
	protected void step() 
	{
	}
	
	protected void addJob(Table table, Job job, float rowHeight)
	{
		table.add( new Label('#' + String.valueOf(job.id), game.skin, "default") ).width(100).height(rowHeight);
		table.add().colspan(2).width(800).height(rowHeight);
		table.add( new Label( JobHandler.getInstance().getUser(job.usr_id).login, game.skin, "default" ) ).width(300).height(rowHeight);
		table.row();
		
		//--date start
		table.add().width(100).height(rowHeight);
		
		Label start = (new Label( "Data rozpoczêcia:     ", game.skin, "default" ));
		start.setAlignment(Align.right);
		table.add( start ).width(300).height(rowHeight);
		
		table.add( new Label(String.valueOf(job.date_start).substring(0, 10), game.skin, "default") ).width(500).height(rowHeight);
		table.add().width(300).height(rowHeight);
		table.row();
		
		//--date end
		table.add().width(100).height(rowHeight);
		
		Label end = (new Label( "Data zakoñczenia:     ", game.skin, "default" ));
		end.setAlignment(Align.right);
		table.add( end ).width(300).height(rowHeight);

		String endDate = String.valueOf(job.date_end).substring(0, 10).equals("0000-00-00") ? "-" : String.valueOf(job.date_end).substring(0, 10);
		
		table.add( new Label(endDate, game.skin, "default") ).width(500).height(rowHeight);
		table.add().width(300).height(rowHeight);
		table.row();
		
		//--points
		table.add().width(100).height(rowHeight);
		
		Label points = (new Label( "Punkty:     ", game.skin, "default" ));
		points.setAlignment(Align.right);
		table.add( points ).width(300).height(rowHeight);

		table.add( new Label(String.valueOf(job.points), game.skin, "default") ).width(500).height(rowHeight);
		table.add().width(300).height(rowHeight);
		table.row();
		
		//--font
		table.add().width(100).height(rowHeight);
		
		Label fontName = (new Label( "Czcionka:     ", game.skin, "default" ));
		fontName.setAlignment(Align.right);
		table.add( fontName ).width(300).height(rowHeight);

		table.add( new Label( JobHandler.getInstance().getFont(job.fnt_id).name, game.skin, "default") ).width(500).height(rowHeight);
		table.add().width(300).height(rowHeight);
		table.row();
		
		//--content
		table.add().width(100).height(rowHeight);
		
		Label content = (new Label( "Tekst:     ", game.skin, "default" ));
		content.setAlignment(Align.right);
		table.add( content ).width(300).height(rowHeight);

		String contentValue = job.content;
		
		if(contentValue.length() > 50)
		{
			contentValue = contentValue.substring(0, 50);
		}
		log("Teraz generujemy font o id = " + job.fnt_id);
		BitmapFont font = FontManager.getInstance().generateFont("files/fonts/" + job.fnt_id + "/font.ttf", 25);
				
		LabelStyle labelStyle = new LabelStyle();
		labelStyle.font = font;
		labelStyle.fontColor = Color.WHITE;
		
		Label contentLabel = new Label( contentValue, labelStyle);
		//Label contentLabel = new Label( contentValue, game.skin, "default");
		contentLabel.setWrap(true);
		
		table.add( contentLabel ).width(500).height(rowHeight);
		table.add().width(300).height(rowHeight);
		table.row();
	}
}
