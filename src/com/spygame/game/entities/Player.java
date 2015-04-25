package com.spygame.game.entities;

import com.spygame.game.GameScene;
import com.spygame.game.entities.buildings.Building;

public class Player {

//	====================================
//	VARIABLES AND OBJECTS
//	====================================
	private boolean myTurn;
	private boolean isPlayerOne;

	private String name;

//	====================================
//	CONSTRUCTOR
//	====================================
	
	public Player(String name, boolean isPlayerOne){
		this.name = name;
		this.isPlayerOne = isPlayerOne;
		setMyTurn(isPlayerOne);
	}

//	====================================
//	METHODS
//	====================================	
	//If there is no buildings the return statement does not get executed and the togglyVictory method is triggered
	public void hasWon(){
		for(Building b: GameScene.allBuildings){
			if(isPlayerOne){
				if(b.playerNumber == 2){
					return;
				}
			}else {
				if(b.playerNumber == 1){
					return;
				}
			}
		}
		GameScene.toggleVictory(name);
	}
	
	public boolean isMyTurn() {
		return myTurn;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
	
	public boolean isPlayerOne() {
		return isPlayerOne;
	}
	
	public String getName() {
		return name;
	}
	
}
