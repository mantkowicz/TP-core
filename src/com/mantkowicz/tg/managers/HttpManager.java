package com.mantkowicz.tg.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.mantkowicz.tg.enums.HttpState;

public class HttpManager implements HttpResponseListener 
{	
	public HttpState state;
	private String result;
	
	HttpRequest request;
	
	public HttpManager()
	{
		state = HttpState.IDLE;
		result = null;
		
		request = new HttpRequest();
		request.setContent("");
	}
	
	public void get(String url)
	{
		result = null;
		request.setMethod(Net.HttpMethods.GET);
		
		request.setUrl(url);
		Gdx.net.sendHttpRequest(request, this);
		
		state = HttpState.DOWNLOADING;
	}
		
	public String getResponse()
	{
		state = HttpState.IDLE;
				
		return result;
	}
	
	@Override
	public void handleHttpResponse(HttpResponse httpResponse) 
	{
		if( httpResponse.getStatus().getStatusCode() != 200 )
		{
			state = HttpState.ERROR;
		}
		else
		{
			state = HttpState.FINISHED;
			
			result = httpResponse.getResultAsString();
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
		return (result == null);
	}
}
