package com.spygame.game.entities;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import com.spygame.managers.ResourcesManager;

public class PullMenu extends Sprite{

	// ====================================================
	// CONSTANTS
	// ====================================================
	//up position
	final float mTargetUpPosX = ResourcesManager.cameraWidth/2;
	final float mTargetUpPosY = ResourcesManager.cameraHeight + 80f;
	//down position
	final float mTargetDownPosX = ResourcesManager.cameraWidth/2;
	final float mTargetDownPosY = ResourcesManager.cameraHeight*0.91f;
	
	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	private static boolean isPulledDown = true;
	
	// ====================================================
	// Constructor
	// ====================================================
	public PullMenu() {
		super(ResourcesManager.cameraWidth/ 2f,  ResourcesManager.cameraHeight, ResourcesManager.pullMenuTR, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
	}
	
	// ====================================================
	// METHODS
	// ====================================================
	public void pullDown(final float pSeconds){
		final PullMenu thisObj = this;
		this.registerUpdateHandler(new IUpdateHandler(){
			float timeElapsed = 0f;
			
			@Override
			public void onUpdate(float pSecondsElapsed){
				timeElapsed += pSecondsElapsed;
				if(timeElapsed >= pSeconds){
					thisObj.unregisterUpdateHandler(this);
				}
			}
			
			@Override
			public void reset(){
				
			}
		});
		isPulledDown = true;
	}
	
	public void pullUp(final float pSeconds){
		final PullMenu thisObj = this;
		this.registerUpdateHandler(new IUpdateHandler(){
			float timeElapsed = 0f;
			
			@Override
			public void onUpdate(float pSecondsElapsed){
				timeElapsed += pSecondsElapsed;
				if(timeElapsed >= pSeconds){
					thisObj.unregisterUpdateHandler(this);
				}
			}
			
			@Override
			public void reset(){
				
			}
		});
		isPulledDown = false;
	}
	
	@Override
	public void onManagedUpdate(final float pSecondsElapsed){
		
		final float currentPosX = this.getX();
		final float currentPosY = this.getY();
		
		float targetPosX;
		float targetPosY;
		if(isPulledDown){
			targetPosX = this.mTargetUpPosX;
			targetPosY = this.mTargetUpPosY;
		} else {
			targetPosX = this.mTargetDownPosX;
			targetPosY = this.mTargetDownPosY;
		}
		
		if(currentPosX != targetPosX || currentPosY != targetPosY){
			final float diffX = targetPosX - currentPosX;
			final float dX = diffX/4f;
			
			final float diffY = targetPosY - currentPosY;
			final float dY = diffY / 4f;
			
			super.setPosition(currentPosX + dX, currentPosY + dY);
		}
	}

}
