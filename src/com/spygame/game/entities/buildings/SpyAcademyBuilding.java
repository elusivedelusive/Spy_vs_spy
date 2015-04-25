package com.spygame.game.entities.buildings;

import com.spygame.game.entities.GridSquare;
import com.spygame.managers.ResourcesManager;

public class SpyAcademyBuilding extends Building{

	public SpyAcademyBuilding(GridSquare region , int playerNumber, String GSname) {
		super(region, ResourcesManager.building_academy, playerNumber, GSname);
	}

	@Override
	public void effect() {
		
	}
	
}
