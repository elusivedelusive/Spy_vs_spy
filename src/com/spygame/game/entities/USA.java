package com.spygame.game.entities;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;

import com.spygame.game.GameScene;
import com.spygame.managers.ResourcesManager;

public class USA extends Continent {

	private static final USA INSTANCE = new USA();

	public static USA getInstance() {
		return INSTANCE;
	}

	// ====================================================
	// Constructor
	// ====================================================
	public USA() {
		super(-ResourcesManager.cameraWidth,
				ResourcesManager.cameraHeight * 0.5f, ResourcesManager.usaTR);

		isCentral = true;

		grid = new ArrayList<GridSquare>();
		grid = buildRectGrid(737,
				480, this, Russia.getInstance(), "U");
		drawGrid = buildGrid(737,
				480);
		this.attachChild(drawGrid);

		mTargetNotCentralPosY = ResourcesManager.cameraHeight * 0.5f;
		mTargetNotCentralPosX = -ResourcesManager.cameraWidth;
		mTargetCentralPosY = ResourcesManager.cameraHeight * 0.5f;
		mTargetCentralPosX = (ResourcesManager.cameraWidth * 0.5f);// * 0.96f;
	}

	// ====================================================
	// METHODS
	// ====================================================

	@Override
	public void onManagedUpdate(final float pSecondsElapsed) {

		final float currentPosX = this.getX();
		final float currentPosY = this.getY();

		float targetPosX;
		float targetPosY;
		if (isCentral) {
			targetPosX = this.mTargetNotCentralPosX;
			targetPosY = this.mTargetNotCentralPosY;
		} else {
			targetPosX = this.mTargetCentralPosX;
			targetPosY = this.mTargetCentralPosY;
			for (Spy spy : spies) {
				spy.setVisible(true);
			}
		}

		if (currentPosX != targetPosX || currentPosY != targetPosY) {
			final float diffX = targetPosX - currentPosX;
			final float dX = diffX / 4f;

			final float diffY = targetPosY - currentPosY;
			final float dY = diffY / 4f;

			super.setPosition(currentPosX + dX, currentPosY + dY);
		}
	}

	@Override
	public void swipe() {
		final USA thisObj = this;
		this.registerUpdateHandler(new IUpdateHandler() {
			float timeElapsed = 0f;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				timeElapsed += pSecondsElapsed;
				if (timeElapsed >= 1f) {
					thisObj.unregisterUpdateHandler(this);
				}
			}

			@Override
			public void reset() {

			}
		});
		if (isCentral)
			isCentral = false;
		else
			isCentral = true;
	}

	@Override
	public boolean isRussia() {
		return false;
	}

}
