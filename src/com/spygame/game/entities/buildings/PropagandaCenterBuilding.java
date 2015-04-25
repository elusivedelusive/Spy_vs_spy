package com.spygame.game.entities.buildings;

import com.spygame.game.entities.GridSquare;
import com.spygame.managers.ResourcesManager;

public class PropagandaCenterBuilding extends Building{

	public PropagandaCenterBuilding(GridSquare region, int playerNumber, String GSname) {
		super(region, ResourcesManager.building_propaganda, playerNumber, GSname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void effect() {
		// TODO Auto-generated method stub
		
	}

}
