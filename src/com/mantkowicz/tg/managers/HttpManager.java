package com.mantkowicz.tg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.mantkowicz.tg.enums.HttpState;
import com.mantkowicz.tg.json.Offer;

public class HttpManager implements HttpResponseListener 
{	
	public HttpState state;
	private String result;
	private byte[] byteResult;
	
	HttpRequest request;
	
	boolean getByte;
	
	String currentUrl = "";
	
	public float errorCode;
	
	public HttpManager()
	{
		state = HttpState.IDLE;
		result = null;
		byteResult = null;
		
		request = new HttpRequest();
		request.setContent("");
	}
	
	public void send(String url, Offer offer)
	{
		request.setMethod(Net.HttpMethods.POST);
		
		String content = "job_id=" + offer.job_id + "&usr_id=" + offer.usr_id + "&date=" + offer.date + "&html=" + offer.html + "&score=" + offer.score;
		
		request.setContent(content);
		
		currentUrl = url; //for debug only
		
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, this);
		
		state = HttpState.DOWNLOADING;
	}
	
	public void get(String url)
	{
		result = null;
		byteResult = null;
		
		request.setMethod(Net.HttpMethods.GET);
		
		currentUrl = url; //for debug only
		
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, this);
		
		state = HttpState.DOWNLOADING;
	}
	
	public void getString(String url)
	{
		getByte = false;
		get(url);
	}
	
	public void getByte(String url)
	{
		getByte = true;
		get(url);
	}
		
	public String getResponse()
	{
		state = HttpState.IDLE;
				
		return result;
	}
	
	public void resetState()
	{
		state = HttpState.IDLE;
	}
	
	public byte[] getByteResponse()
	{
		state = HttpState.IDLE;
				
		return byteResult;
	}
	
	@Override
	public void handleHttpResponse(HttpResponse httpResponse) 
	{
		if( httpResponse.getStatus().getStatusCode() != 200 )
		{
			state = HttpState.ERROR;
			errorCode = httpResponse.getStatus().getStatusCode();
		}
		else
		{
			state = HttpState.FINISHED;
			
			if( !getByte ) 
			{
				result = httpResponse.getResultAsString();
			}
			else 
			{
				byteResult = httpResponse.getResult();
			}
		}	
	}

	@Override
	public void failed(Throwable t) 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void cancelled() 
	{
		// TODO Auto-generated method stub	
	}

	public boolean isResultNull() 
	{
		return (result == null && byteResult == null);
	}
}
