package com.spygame.game.entities.abilities;

import com.spygame.game.GameScene;
import com.spygame.game.entities.Action;
import com.spygame.game.entities.GridSquare;
import com.spygame.game.entities.Spy;
import com.spygame.game.entities.buildings.Building;

public class GatherIntelAbility extends Ability {

	public GatherIntelAbility(Spy spy) {
		super(spy);
	}

	// contains a for loop for the 2 continents which calls to the useHelper
	// method
	@Override
	public boolean use(float abilityLevel) {
		if (spy.continentLocation == 1) {
			for (GridSquare GS : GameScene.RT.grid) {
				// finds the right gridSquare by comparing the spy's location
				if (GS.name.equals(spy.currentGridSquare)) {
					if (useHelper(GS, abilityLevel)) {
						return true;
					}
				}
			}
		} else if (spy.continentLocation == 2) {
			for (GridSquare GS : GameScene.UT.grid) {
				if (GS.name.equals(spy.currentGridSquare)) {
					if (useHelper(GS, abilityLevel)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// helper method for the use method that condences code
	public boolean useHelper(GridSquare GS, float abilityLevel) {
		//if intel is not complete
		if (GS.intelLevel < 100) {
			//find other spies in the same region
			int intelIncreaseAmount = 0;
			intelIncreaseAmount = (int)(5 + (5* abilityLevel));
			
			for (Spy s : GameScene.allSpies) {
				if (s.currentGridSquare.equals(GS.name)) {
					//if they are performing counter espionage half the intel gained
					if (s.chosenAction.equals(Action.COUNTER_ESPIONAGE)) {
						intelIncreaseAmount = (int)(5 + (5* abilityLevel) * 0.5f);
					}
				}
			}
			GS.setIntelLevel(GS.intelLevel += intelIncreaseAmount);
			if (GS.intelLevel > 100){
				GS.intelLevel = 100;
				if(checkBuildings(GS.name)){
					return true;
				}
			}
			return false;
		}
		return false;
	}

	private boolean checkBuildings(String name) {
		// find buildings in the same region and set them to be visible
		for (Building b : GameScene.allBuildings) {
			if (b.gridName.equals(name)) {
				b.setVisibleToEnemy(true);
				return true;
			}
		}
		return false;
	}

}
