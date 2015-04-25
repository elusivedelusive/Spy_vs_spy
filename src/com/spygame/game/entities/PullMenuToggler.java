package com.spygame.game.entities;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.spygame.managers.ResourcesManager;

public class PullMenuToggler extends Sprite implements IUpdateHandler {

	// ====================================================
	// CONSTANTS
	// ====================================================
	//up position
	final float mTargetUpPosX = ResourcesManager.cameraWidth/2;
	final float mTargetUpPosY = ResourcesManager.cameraHeight;
	//down position
	final float mTargetDownPosX = ResourcesManager.cameraWidth/2;
	final float mTargetDownPosY = ResourcesManager.cameraHeight * 0.8f;
	
	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	private static boolean isPulledDown = true;
		
	private PullMenu pullMenu;
	private TransportBox transportBox;
	private EndTurnButton endTurnButton;
	private MenuButton menuButton;
	// ====================================================
	// Constructor
	// ====================================================
	public PullMenuToggler(PullMenu pullMenu, EndTurnButton etb, MenuButton menuButton) {
		super(ResourcesManager.cameraWidth/ 2f, ResourcesManager.cameraHeight, ResourcesManager.pullDownTR, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		this.pullMenu = pullMenu;
		//this.transportBox = transportBox;
		this.endTurnButton = etb;
		this.menuButton = menuButton;
	}
	// ====================================================
	// TOUCH INPUTS
	// ====================================================
	@Override
	public boolean onAreaTouched(final TouchEvent pTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		if(pTouchEvent.isActionDown()){
			if(isPulledDown){
				pullUp(0.8f);
				endTurnButton.pullUp(0.8f);
				pullMenu.pullUp(0.8f);
				menuButton.pullUp(0.8f);
				
			}else{
				pullDown(0.8f);
				endTurnButton.pullDown(0.8f);
				pullMenu.pullDown(0.8f);
				menuButton.pullDown(0.8f);
			}	
		}
		return true;
	}
	
	
	// ====================================================
	// METHODS
	// ====================================================
	
	//moves the menu into an up position
	public void emergencyPull(){
		pullDown(0.8f);
		endTurnButton.pullDown(0.8f);
		pullMenu.pullDown(0.8f);
		menuButton.pullDown(0.8f);
	}
	
	public void pullDown(final float pSeconds){
		final PullMenuToggler thisObj = this;
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
		final PullMenuToggler thisObj = this;
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
