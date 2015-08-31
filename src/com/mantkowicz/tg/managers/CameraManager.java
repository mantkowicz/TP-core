package com.mantkowicz.tg.managers;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.tg.actors.CustomLabel;

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
	
	public void showParagraph(CustomLabel paragraph)
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
		if(this.move && (camera.position.x != this.target.x || camera.position.y != this.target.y))
		{
			this.camera.position.x += (-camera.position.x + this.target.x ) * 0.1f;
			this.camera.position.y += (-camera.position.y + this.target.y ) * 0.1f;
		}
		if(this.move && Math.abs(camera.position.x - this.target.x) < 0.2f && Math.abs(camera.position.y - this.target.y) < 0.2f)
		{
			stopMoving(this.target);
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
}
