package com.spygame.game.entities;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;

import com.spygame.game.GameScene;
import com.spygame.managers.ResourcesManager;

public class GridSquare extends Rectangle {

	Continent myCont;
	Continent otherCont;
	public int intelLevel = 0;
	public String name;
	private Text intelLevelText;
//
	public GridSquare(float pX, float pY, float pWidth, float pHeight,
			Continent myCont, Continent otherCont, String name) {
		super(pX, pY, pWidth, pHeight, ResourcesManager.getInstance().mEngine
				.getVertexBufferObjectManager());
		this.myCont = myCont;
		this.name = name;
		this.otherCont = otherCont;
		intelLevelText = new Text(this.getX(), this.getY(),
				ResourcesManager.intelLevelFont, "0123456789",
				10,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		myCont.attachChild(intelLevelText);
		if(myCont.isRussia())
			intelLevelText.setVisible(false);
		setIntelLevel(0);
	}

	public int getIntelLevel() {
		return intelLevel;
	}

	public void setIntelLevel(int intelLevel) {
		this.intelLevel = intelLevel;
		//intelLevelText.detachSelf();
		intelLevelText.setText(intelLevel + "");
		intelLevelText.setPosition(this.getX(), this.getY());
		//GameScene.getInstance().attachChild(intelLevelText);
	}

	public void setIntelVisibility(int playerNum) {
		if (playerNum == 1) {
			if(myCont.isRussia())
				intelLevelText.setVisible(false);
				
			if(!myCont.isRussia())
				intelLevelText.setVisible(true);
		} else {
			if(myCont.isRussia()){
				intelLevelText.setVisible(true);
			}
				
			if(!myCont.isRussia()){
				intelLevelText.setVisible(false);
			}
				
		}
	}
}
