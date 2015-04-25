package com.spygame.game.entities;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;

import com.spygame.game.GameScene;
import com.spygame.managers.ResourcesManager;

public class TransportBox extends Sprite implements IUpdateHandler {

	// ====================================================
	// CONSTANTS
	// ====================================================

	static final int MAXIUM_SPIES_IN_TRANSIT = 2;
	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	public static ArrayList<Spy> spiesInTransit;
	public static boolean isInRussia = true;
	public static boolean whiteSpySlotFull = false;
	public static boolean blackSpySlotFull = false;

	// ====================================================
	// Constructor
	// ====================================================
	public TransportBox() {
		super(ResourcesManager.transportBoxTR.getWidth() / 2,
				ResourcesManager.cameraHeight / 2,
				ResourcesManager.transportBoxTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesInTransit = new ArrayList<Spy>();
	}

	// ====================================================
	// METHODS
	// ====================================================

	public static boolean isPlaneFull(int spyNumber) {
		if (spyNumber == 1) {
			return whiteSpySlotFull;
		} else {
			return blackSpySlotFull;
		}
	}

	// Returns true if successful
	public boolean addSpyToTransit(Spy spy) {
		if (spy.playerNumber == 1) {
			spiesInTransit.add(spy);
			whiteSpySlotFull = true;
			return true;
		} else {
			spiesInTransit.add(spy);
			blackSpySlotFull = true;
			return true;
		}
	}

	//moves spies to the opposite airport
	public static void moveSpies() {
		for (Spy s : spiesInTransit) {
			s.detachSelf();
			s.travelArrow.detachSelf();
			s.nameText.detachSelf();
			if (isInRussia) {
				GameScene.UT.attachChild(s.nameText);
				GameScene.UT.attachChild(s.travelArrow);
				GameScene.UT.attachChild(s);
				s.continentLocation = 2;
				isInRussia = false;
			} else {
				GameScene.RT.attachChild(s.nameText);
				GameScene.RT.attachChild(s.travelArrow);
				GameScene.RT.attachChild(s);
				s.continentLocation = 1;
				isInRussia = true;
			}
			s.setNameText();
		}
	}
	
	public static void bookAirplane(int spyNumber){
		if (spyNumber == 1) {
			whiteSpySlotFull = true;
		} else {
			blackSpySlotFull = true;
		}
	}
	
	public static void unBookAirplane(int spyNumber){
		if (spyNumber == 1) {
			whiteSpySlotFull = false;
		} else {
			blackSpySlotFull = false;
		}
	}

	public void removeSpy(Spy spy) {
		if (spy.playerNumber == 1) {
			whiteSpySlotFull = false;
		} else {
			blackSpySlotFull = false;
		}
		spiesInTransit.remove(spy);
	}

}
