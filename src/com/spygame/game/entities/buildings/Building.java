package com.spygame.game.entities.buildings;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.spygame.game.GameScene;
import com.spygame.game.entities.GridSquare;
import com.spygame.managers.ResourcesManager;

public abstract class Building extends Sprite{

	public boolean isVisibleToEnemy = false;
	public int playerNumber;
	public String gridName;
	
	public Building(GridSquare region, ITextureRegion tr, int playernumber, String gridName) {
		super(region.getX(), region.getY(), tr, ResourcesManager.getInstance().mEngine.getVertexBufferObjectManager());
		this.playerNumber = playernumber;
		setVisibility(playernumber);
		this.gridName = gridName;
	}
	
	public void setVisibility(int playerNum){
		if(playerNumber == playerNum){
			this.setVisible(true);
		} else if (isVisibleToEnemy){
			this.setVisible(true);
		} else {
			this.setVisible(false);
		}
	}
	
	public void setVisibleToEnemy(boolean visibleToEnemy){
		this.isVisibleToEnemy = visibleToEnemy;
		this.setVisible(true);
	}
	
	public void destroy(){
		this.detachSelf();
		GameScene.allBuildings.remove(this);
	}
	
	public abstract void effect();
}
