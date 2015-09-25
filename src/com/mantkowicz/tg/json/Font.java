package com.mantkowicz.tg.json;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.mantkowicz.tg.managers.HttpManager;

public class Font 
{
	public int id;
	public int usr_id;
	public String name;
	public String path;
	
	public void download(HttpManager manager)
	{
		manager.getByte("http://www.kerning.mantkowicz.pl/ws.php?action=getFont&id=" + this.id);
	}
	
	public void save(HttpManager manager)
	{
		FileHandle fh = Gdx.files.local("files/fonts/" + this.id + "/font.ttf");
		fh.writeBytes(manager.getByteResponse(), false);
	}
}
