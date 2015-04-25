package com.spygame.game.entities.abilities;

import com.spygame.game.GameScene;
import com.spygame.game.entities.Action;
import com.spygame.game.entities.Spy;

public class HideAbility extends Ability {
	public HideAbility(Spy spy) {
		super(spy);
	}

	//Uses a threshold that is modified by spy attributes to 
	//determine the number needed to succeed. A random number is
	//generated and if it is higher than the threshold the action succeeds
	@Override
	public boolean use(float abilityLevel) {
		float attempt = rand.nextFloat();
		// abilityLevel / 10 to turn it into a float
		float threshold = 0.7f - (abilityLevel / 10);
		for (Spy s : GameScene.allSpies) {
			if (s.currentGridSquare.equals(spy.currentGridSquare)
					&& s.playerNumber != spy.playerNumber) {
				if (s.getAction().equals(Action.COUNTER_ESPIONAGE))
					threshold += (s.skills.get("PERCEPTION") / 10);
				if (attempt > threshold) {
					spy.isVisibleToEnemy = false;
					return true;
				}
			}
		}
		return false;
	}
}
