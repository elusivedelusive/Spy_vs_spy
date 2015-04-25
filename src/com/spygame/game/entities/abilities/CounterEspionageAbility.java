package com.spygame.game.entities.abilities;

import com.spygame.game.GameScene;
import com.spygame.game.entities.Action;
import com.spygame.game.entities.Spy;

public class CounterEspionageAbility extends Ability {
	public CounterEspionageAbility(Spy spy) {
		super(spy);
	}

	//Uses a threshold that is modified by spy attributes to 
	//determine the number needed to succeed. A random number is
	//generated and if it is higher than the threshold the action succeeds
	@Override
	public boolean use(float abilityLevel) {
		float attempt = rand.nextFloat();
		//abilityLevel / 10 to turn it into a float
		float threshold = 0.75f - (abilityLevel / 10);
		for (Spy s : GameScene.allSpies) {
			System.out.println("name: " + s.name + " currentGridSquare: " + s.currentGridSquare);
			if (s.currentGridSquare.equals(spy.currentGridSquare)
					&& s.playerNumber != spy.playerNumber
					&& !s.isVisibleToEnemy) {
				if (s.getAction().equals(Action.HIDE))
					threshold += (s.skills.get("STEALTH")/10);
				if(s.getAction().equals(Action.SABOTAGE) || s.getAction().equals(Action.ASSASSINATE)){
					threshold -= 0.3;
				}
				if (attempt > threshold) {
					s.isVisibleToEnemy = true;
					s.setVisible(true);
					return true;
				}
			}
		}
		return false;
	}
}
