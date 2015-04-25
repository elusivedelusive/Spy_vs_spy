package com.spygame.game.entities;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import com.spygame.game.GameScene;
import com.spygame.managers.ResourcesManager;

public class MenuButton  extends Sprite {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final EndTurnButton INSTANCE = new EndTurnButton();

	GameScene gameScene;
	public static EndTurnButton getInstance() {
		return INSTANCE;
	}

	// up position
	final float mTargetUpPosX = ResourcesManager.menuBoxTR.getWidth() / 2f;
	final float mTargetUpPosY = (ResourcesManager.cameraHeight - ResourcesManager.menuBoxTR.getHeight() / 2) + 80;
	// down position
	final float mTargetDownPosX = ResourcesManager.menuBoxTR.getWidth() / 2f;
	final float mTargetDownPosY = ResourcesManager.cameraHeight - ResourcesManager.menuBoxTR.getHeight() / 2;
	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	private static boolean isPulledDown = true;

	// ====================================================
	// Constructor
	// ====================================================
	public MenuButton (GameScene game) {
		super(ResourcesManager.menuBoxTR.getWidth() / 2f,
				(ResourcesManager.cameraHeight - ResourcesManager.menuBoxTR.getHeight() / 2) + 80,
				ResourcesManager.menuBoxTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		this.gameScene = game;
	}

	// ====================================================
	// METHODS
	// ====================================================
	public void pullDown(final float pSeconds) {
		final  MenuButton  thisObj = this;
		this.registerUpdateHandler(new IUpdateHandler() {
			float timeElapsed = 0f;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				timeElapsed += pSecondsElapsed;
				if (timeElapsed >= pSeconds) {
					thisObj.unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {

			}
		});
		isPulledDown = true;
	}

	public void pullUp(final float pSeconds) {
		final MenuButton thisObj = this;
		this.registerUpdateHandler(new IUpdateHandler() {
			float timeElapsed = 0f;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				timeElapsed += pSecondsElapsed;
				if (timeElapsed >= pSeconds) {
					thisObj.unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {

			}
		});
		isPulledDown = false;
	}

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {

		final float currentPosX = this.getX();
		final float currentPosY = this.getY();

		float targetPosX;
		float targetPosY;
		if (isPulledDown) {
			targetPosX = this.mTargetUpPosX;
			targetPosY = this.mTargetUpPosY;
		} else {
			targetPosX = this.mTargetDownPosX;
			targetPosY = this.mTargetDownPosY;
		}

		if (currentPosX != targetPosX || currentPosY != targetPosY) {
			final float diffX = targetPosX - currentPosX;
			final float dX = diffX / 4f;

			final float diffY = targetPosY - currentPosY;
			final float dY = diffY / 4f;

			super.setPosition(currentPosX + dX, currentPosY + dY);
		}
	}

	//=================================================
	//TOUCH EVENT
	//=================================================
	@Override
	public boolean onAreaTouched(final TouchEvent spyTouchEvent,
			final float spyTouchAreaLocalX, final float spyTouchAreaLocalY) {
		if(spyTouchEvent.isActionUp()){
			gameScene.toggleMenuHud();
			System.out.println("toggling");
		}
		return true;
	}
}
