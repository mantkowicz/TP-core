package com.mantkowicz.tg.json;

public class Job 
{
	public int id;
	public int usr_id;
	public int fnt_id;
	public int points;
	public String date_start;
	public String date_end;
	public int font_size;
	public String content;
	public int width;
	public int height;
	public int padding;
	public int indent;
	public int lineHeight;
	public String hyphen;
	
	public Job clone()
	{
		Job job = new Job();
		
		job.id = this.id;
		job.usr_id = this.usr_id;
		job.fnt_id = this.fnt_id;
		job.points = this.points;
		job.date_start = this.date_start;
		job.date_end = this.date_end;
		job.font_size = this.font_size;
		job.content = this.content;
		job.width = this.width;
		job.height = this.height;
		job.padding = this.padding;
		job.indent = this.indent;
		job.lineHeight = this.lineHeight;		
		job.hyphen = this.hyphen;
		
		return job;
	}
}
