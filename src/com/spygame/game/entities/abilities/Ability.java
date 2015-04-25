package com.spygame.game.entities.abilities;

import java.util.Random;

import com.spygame.game.entities.Spy;
import com.spygame.game.entities.buildings.Building;

public abstract class Ability {
	
    // ===========================================================
    // VARIABLES AND OBJECTS
    // ===========================================================
	protected Spy spy;
	protected Random rand;
    // ===========================================================
    // CONSTRUCTOR
    // ===========================================================

	public Ability(Spy spy){
		this.spy = spy;	
		rand = new Random();
	}
	
    // ===========================================================
    // METHODS
    // ===========================================================
	
	public abstract boolean use(float abilityLevel);
}
