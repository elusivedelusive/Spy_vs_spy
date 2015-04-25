package com.spygame.game.entities;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;

import android.widget.Toast;

import com.spygame.game.GameScene;
import com.spygame.game.entities.buildings.Building;
import com.spygame.managers.ResourcesManager;

public class EndTurnButton extends Sprite {

	// ====================================================
	// CONSTANTS
	// ====================================================
	private static final EndTurnButton INSTANCE = new EndTurnButton();
	private static ArrayList<Action> actionlist = new ArrayList<Action>();

	public static EndTurnButton getInstance() {
		return INSTANCE;
	}

	// up position
	final float mTargetUpPosX = ResourcesManager.cameraWidth
			- (ResourcesManager.endTurnBoxTR.getWidth() / 2f);
	final float mTargetUpPosY = (ResourcesManager.cameraHeight - (ResourcesManager.endTurnBoxTR
			.getHeight() / 2)) + 80f;
	// down position
	final float mTargetDownPosX = ResourcesManager.cameraWidth
			- (ResourcesManager.endTurnBoxTR.getWidth() / 2f);
	final float mTargetDownPosY = ResourcesManager.cameraHeight
			- (ResourcesManager.endTurnBoxTR.getHeight() / 2);
	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	private static boolean isPulledDown = true;

	// ====================================================
	// Constructor
	// ====================================================
	public EndTurnButton() {
		super(ResourcesManager.cameraWidth
				- (ResourcesManager.endTurnBoxTR.getWidth() / 2f),
				(ResourcesManager.cameraHeight - (ResourcesManager.endTurnBoxTR
						.getHeight() / 2)) + 80f,
				ResourcesManager.endTurnBoxTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
	}

	// ====================================================
	// METHODS
	// ====================================================
	//moves button up and down
	public void pullDown(final float pSeconds) {
		final EndTurnButton thisObj = this;
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
		final EndTurnButton thisObj = this;
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
	//calls all necessary processes for ending a turn
	private void endTurn() {
		GameScene.emptySpiesHud();
		// dummy
		if (GameScene.playerOne.isMyTurn()) {
			GameScene.playerOne.setMyTurn(false);
			GameScene.playerTwo.setMyTurn(true);
			setVisibility(2);
		} else {
			GameScene.playerTwo.setMyTurn(false);
			GameScene.playerOne.setMyTurn(true);

			setVisibility(1);
			doActions();
			TransportBox.moveSpies();
		}
		GameScene.getInstance().toggleEventLog();
	}

	/**
	 * 
	 * SETS VISIBILITY OF SPIES, BUILDINGS AND GRIDS AT THE END OF A TURN
	 * 
	 * @param i
	 */
	private void setVisibility(int i) {
		for (Spy s : GameScene.allSpies) {
			s.setVisibility(i);
			actionlist.add(s.getAction());
		}
		for (Building b : GameScene.allBuildings) {
			b.setVisibility(i);
		}
		for (GridSquare GS : GameScene.RT.grid) {
			GS.setIntelVisibility(i);
		}
		for (GridSquare GS : GameScene.UT.grid) {
			GS.setIntelVisibility(i);
		}
	}

	/**
	 * CALLS ON SPIES TO PERFORM THEIR ACTIONS
	 */
	private void doActions() {
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.HIDE);
		}
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.ASSASSINATE);
		}
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.SABOTAGE);
		}
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.GATHER_INTEL);
		}
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.COUNTER_ESPIONAGE);
		}
		for (Spy s : GameScene.allSpies) {
			s.doAction(Action.MOVE);
		}
	}

	// =================================================
	// TOUCH EVENT
	// =================================================
	@Override
	public boolean onAreaTouched(final TouchEvent spyTouchEvent,
			final float spyTouchAreaLocalX, final float spyTouchAreaLocalY) {
		if (spyTouchEvent.isActionUp()) {
			endTurn();
			System.out.println("turn ended");
		}
		return true;
	}
}
