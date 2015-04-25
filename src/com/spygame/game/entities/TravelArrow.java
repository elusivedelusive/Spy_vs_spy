package com.spygame.game.entities;

import org.andengine.entity.primitive.Line;
import org.andengine.util.adt.color.Color;

import com.spygame.managers.ResourcesManager;

public class TravelArrow extends Line{

	public TravelArrow(float pX1, float pY1, float pX2, float pY2) {
		super(pX1, pY1, pX2, pY2, 4, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		this.setColor(Color.BLACK);
		this.setAlpha(0.6f);
		this.setVisible(true);
	}
	
	public void changeArrow(float pX1, float pY1, float pX2, float pY2){
		this.setPosition(pX2, pY2, pX1, pY1);
		this.setVisible(true);
	}
	
	public void hideArrow(){
		this.setVisible(false);
	}

}
