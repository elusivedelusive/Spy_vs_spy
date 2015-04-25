package com.spygame.game.entities;

import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.TextureRegion;

import com.spygame.game.GameScene;
import com.spygame.managers.ResourcesManager;

public abstract class GameButton extends Sprite{

	// ====================================================
	// CONSTANTS
	// ====================================================

	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================
	public boolean mIsEnabled = true;
	public Spy spy;
	// ====================================================
	// CONSTRUCTOR
	// ====================================================
	
	//used for region arrow switch buttons
	public GameButton(final float pX, final float pY, int direction) {
		super(pX, pY, ResourcesManager.leftArrowTR, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		if(direction == 2)
			this.setRotation(180f);
	}
	
	//used for various
	public GameButton(final float pX, final float pY, TextureRegion tr) {
		super(pX, pY, tr, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		this.setScale(1.3f);
	}
	
	//used for spy HUD 
	public GameButton(final float pX, final float pY, Spy spy){
		super(pX, pY, ResourcesManager.listOfSpiesBoxTR, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		this.spy = spy;
	}
	
	// ====================================================
	// ABSTRACT METHOD
	// ====================================================
	//public abstract void onClick();
	
}