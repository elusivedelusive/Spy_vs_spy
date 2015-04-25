package com.spygame.game.entities;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import com.spygame.managers.ResourcesManager;

public class Russia extends Continent {

	private static final Russia INSTANCE = new Russia();

	public static Russia getInstance() {
		return INSTANCE;
	}
	// ====================================================
	// Constructor
	// ====================================================
	public Russia() {
		super((ResourcesManager.cameraWidth * 0.5f),// * 0.99f,
				ResourcesManager.cameraHeight * 0.5f, ResourcesManager.russiaTR);

		grid = new ArrayList<GridSquare>();
		grid = buildRectGrid(737,
				480, this, USA.getInstance(), "R");
		drawGrid = buildGrid(737,
				480);
		this.attachChild(drawGrid);
		
		isCentral = false;

		mTargetCentralPosX = (ResourcesManager.cameraWidth * 0.5f);// * 0.99f;
		mTargetCentralPosY = ResourcesManager.cameraHeight * 0.5f;
		mTargetNotCentralPosX = ResourcesManager.cameraWidth * 2;
		mTargetNotCentralPosY = ResourcesManager.cameraHeight * 0.5f;
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
			targetPosX = mTargetNotCentralPosX;
			targetPosY = mTargetNotCentralPosY;
		} else {
			targetPosX = mTargetCentralPosX;
			targetPosY = mTargetCentralPosY;
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

	//toggles the position of the continent
	@Override
	public void swipe() {
		final Russia thisObj = this;
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
		return true;
	}

}
