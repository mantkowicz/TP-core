package com.mantkowicz.tg.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mantkowicz.tg.actors.Paragraph;
import com.mantkowicz.tg.json.Job;

public class CameraManager
{
	private static CameraManager INSTANCE = new CameraManager();
	public static CameraManager getInstance()
	{
		return INSTANCE;
	}
	
	private OrthographicCamera camera;
	
	private Vector2 target;
	private boolean move;
	
	private float targetWidth;
	private boolean zoom;
	
	private Job job;
	
	private CameraManager()
	{		
		this.camera = new OrthographicCamera();
		
		this.target = new Vector2();
		this.move = false;
		
		this.targetWidth = 0f;
		this.zoom = false;
	}
	
	public void setCamera(Camera camera)
	{
		this.camera = (OrthographicCamera)camera;
	}
	
	public void setCameraBoundingBox(Job job)
	{
		this.job = job;
	}
	
	public void moveTo(float x, float y)
	{
		this.target.set(x,y);
		this.move = true;
	}
	
	public void zoomTo(float width)
	{
		this.targetWidth = width;
		this.zoom = true;
	}
	
	public void stopMoving(Vector2 target)
	{
		this.move = false;
		
		if(target != null)
		{
			this.camera.position.x = target.x;
			this.camera.position.y = target.y;
		}
	}
	
	public void stopZooming()
	{
		this.zoom = false;
	}
	
	public void showParagraph(Paragraph paragraph)
	{
		if( paragraph.job.height > paragraph.job.width )
		{
			this.zoomTo(paragraph.job.height + 300);
		}
		else
		{
			this.zoomTo(paragraph.job.width + 300);
		}
		this.moveTo(0, 0);
	}
	
	public float getZoom()
	{
		return this.camera.zoom;
	}
	
	public void step()
	{		
		//MOVE
		if( isInBoundingBox() )
		{
			if(this.move && (camera.position.x != this.target.x || camera.position.y != this.target.y))
			{
				this.camera.position.x += (-camera.position.x + this.target.x ) * 0.1f;
				this.camera.position.y += (-camera.position.y + this.target.y ) * 0.1f;
			}
			if(this.move && Math.abs(camera.position.x - this.target.x) < 0.2f && Math.abs(camera.position.y - this.target.y) < 0.2f)
			{
				stopMoving(this.target);
			}
		}
		else
		{
			this.move = false;
			this.camera.position.set( moveToBoundingBox() );
		}
		
		//ZOOM
		float cameraViewWidth = this.camera.frustum.planePoints[1].x - this.camera.frustum.planePoints[0].x;

		if(this.zoom && Math.abs(cameraViewWidth - this.targetWidth) < 20)
		{
			stopZooming();
		}

		if(this.zoom && (cameraViewWidth > this.targetWidth))
		{
			this.camera.zoom -= 0.02;
		}
		else if(this.zoom && (cameraViewWidth < this.targetWidth))
		{
			this.camera.zoom += 0.02;
		}
	}
	
	public boolean isInBoundingBox()
	{
		return ( camera.position.x <= job.width/2f + 50 && camera.position.x >= -job.width/2f - 50 && camera.position.y >= -job.height/2f - 50 && camera.position.y <= job.height/2f + 50 );
	}

	public Vector3 moveToBoundingBox() 
	{
		return moveToBoundingBox(this.camera.position);
	}
	
	public Vector3 moveToBoundingBox(Vector3 position) 
	{
		Vector3 newPosition = new Vector3();
		
		newPosition.set(position);
		
		if( newPosition.x < -job.width/2f - 50 )
		{
			newPosition.x = -job.width/2f - 50;
		}
		else if( newPosition.x > job.width/2f + 50)
		{
			newPosition.x = job.width/2f + 50;
		}
		
		if( newPosition.y < -job.height/2f - 50)
		{
			newPosition.y = -job.height/2f - 50;
		}
		else if( newPosition.y > job.height/2f + 50 )
		{
			newPosition.y = job.height/2f + 50;
		}
		
		return newPosition;
		
	}
}
