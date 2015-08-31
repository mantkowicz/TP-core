package com.mantkowicz.tg.managers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mantkowicz.tg.actors.CustomLabel;
import com.mantkowicz.tg.enums.ZoomType;

public class GestureManager implements GestureListener 
{
	public float initialDistance=0, distance=0, lastDistance=0;
	
	private float initialScale = 1;
	
	public ZoomType zoomType;
	
	Stage stage;
	CustomLabel paragraph;
	
	public GestureManager(Stage stage, CustomLabel paragraph)
	{
		this.stage = stage;
		this.paragraph = paragraph;
		
		zoomType = ZoomType.CAMERA;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) 
	{
		initialScale = ((OrthographicCamera)stage.getCamera()).zoom;
		CameraManager.getInstance().stopMoving(null);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) 
	{		
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		CameraManager.getInstance().moveTo(this.stage.getCamera().position.x - velocityX * 0.5f, this.stage.getCamera().position.y + velocityY * 0.5f);
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {

		stage.getCamera().position.x -= deltaX;
		stage.getCamera().position.y += deltaY;
		
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) 
	{		
		if( zoomType == ZoomType.CAMERA )
		{
			this.initialDistance = initialDistance;
			this.distance = distance;
			
			((OrthographicCamera)stage.getCamera()).zoom = initialScale * initialDistance/distance;
			
			((OrthographicCamera)stage.getCamera()).zoom = (float) MathUtils.clamp(((OrthographicCamera)stage.getCamera()).zoom, 0.1, 4);
		}
		else
		{
			if(lastDistance == 0)
			{
				lastDistance = initialDistance;
			}
			
			if( distance > lastDistance + 10 )
			{
				paragraph.kerningModificator = 0.1f;
				lastDistance = distance;
			}
			else if( distance < lastDistance - 10 )
			{
				paragraph.kerningModificator = -0.1f;
				lastDistance = distance;
			}
		}
		
        return true;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}
}
