package com.mantkowicz.tg.managers;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.tg.json.Job;
import com.mantkowicz.tg.logger.Logger;

public class JobHandler
{
	private static JobHandler INSTANCE = new JobHandler();
	public static JobHandler getInstance()
	{
		return INSTANCE;
	}
	
	Array<Job> jobs;
	
	private JobHandler()
	{
		jobs = new Array<Job>();
	}
	
	public void refresh(Array<Job> jobsList)
	{
		jobs = jobsList;
	}
	
	public void printJobs()
	{
		for(Job job : jobs) Logger.log(1, job.content);
	}
}
