package com.spygame.game.entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import android.os.Handler;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.spygame.game.GameScene;
import com.spygame.game.entities.abilities.Ability;
import com.spygame.game.entities.abilities.AssassinateAbility;
import com.spygame.game.entities.abilities.CounterEspionageAbility;
import com.spygame.game.entities.abilities.GatherIntelAbility;
import com.spygame.game.entities.abilities.HideAbility;
import com.spygame.game.entities.abilities.SabotageAbility;
import com.spygame.game.entities.buildings.Building;
import com.spygame.managers.ResourcesManager;

public class Spy extends TiledSprite implements IUpdateHandler {

	// ====================================================
	// CONSTANTS
	// ====================================================

	// ====================================================
	// STATISTICS
	// ====================================================
	public int killCount = 0;
	public int intelGathered = 0;
	public int discoveriesMade = 0;

	// ====================================================
	// VARIABLES
	// ====================================================
	public boolean isVisibleToEnemy = false;
	public Player player;
	public int playerNumber;
	public String name;
	public int continentLocation;
	public Action chosenAction;
	public TravelArrow travelArrow;
	public Text nameText;
	public String currentGridSquare;
	public String goalGridSquare;
	public Building buildingTarget = null;
	public boolean travelingBetweenContinents = false;
	public boolean spyButtonIsSelected = false;
	public Text buttonNameText;
	public int level = GameScene.startingExperienceLevel;
	public int expLevel = 0;

	public GameButton myButton;
	Vector2 currentPosition;
	Vector2 goalPosition;
	Random random;
	public Map<String, Integer> skills = new HashMap<String, Integer>();

	Map<String, Ability> abilities = new HashMap<String, Ability>();
	{
		abilities.put("GATHER_INTEL", new GatherIntelAbility(this));
		abilities.put("COUNTER_ESPIONAGE", new CounterEspionageAbility(this));
		abilities.put("SABOTAGE", new SabotageAbility(this));
		abilities.put("ASSASSINATE", new AssassinateAbility(this));
		abilities.put("HIDE", new HideAbility(this));
	}

	// ====================================================
	// CONSTRUCTOR
	// ====================================================

	public Spy(float pX, float pY, Player player, int location, String name,
			String currentGridSquare, int skillToUpgrade, int skillUpgradeAmount) {
		super(pX, pY, ResourcesManager.whiteSpyTR, ResourcesManager
				.getInstance().mEngine.getVertexBufferObjectManager());
		this.setCurrentTileIndex(0);
		currentPosition = new Vector2(pX, pY);
		goalPosition = new Vector2(pX, pY);
		this.player = player;
		this.playerNumber = 1;
		this.continentLocation = location;
		this.name = name;
		travelArrow = new TravelArrow(0, 0, 0, 0);
		nameText = new Text(this.getX(), this.getY() + 35,
				ResourcesManager.spyNameFont, name,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		this.currentGridSquare = currentGridSquare;
		if (location == 1) {
			GameScene.RT.attachChild(travelArrow);
			if (ResourcesManager.DISPLAY_SPY_NAMES)
				GameScene.RT.attachChild(nameText);
		} else {
			GameScene.UT.attachChild(travelArrow);
			if (ResourcesManager.DISPLAY_SPY_NAMES)
				GameScene.UT.attachChild(nameText);
		}
		setStartingSkills(skillToUpgrade, skillUpgradeAmount);
		setNameText();
	}

	public Spy(float pX, float pY, Player player, int location,
			boolean isBlack, String name, String currentGridSquare,
			int skillToUpgrade, int skillUpgradeAmount) {
		super(pX, pY, ResourcesManager.blackSpyTR, ResourcesManager
				.getInstance().mEngine.getVertexBufferObjectManager());
		this.setCurrentTileIndex(0);
		currentPosition = new Vector2(pX, pY);
		goalPosition = new Vector2(pX, pY);
		// this.setScale(0.45f);
		this.player = player;
		this.playerNumber = 2;
		this.continentLocation = location;
		this.name = name;
		travelArrow = new TravelArrow(0, 0, 0, 0);
		nameText = new Text(this.getX(), this.getY() + 35,
				ResourcesManager.spyNameFont, name,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		this.currentGridSquare = currentGridSquare;
		if (location == 1) {
			GameScene.RT.attachChild(travelArrow);
			if (ResourcesManager.DISPLAY_SPY_NAMES)
				GameScene.RT.attachChild(nameText);
		} else {
			GameScene.UT.attachChild(travelArrow);
			if (ResourcesManager.DISPLAY_SPY_NAMES)
				GameScene.UT.attachChild(nameText);
		}
		setStartingSkills(skillToUpgrade, skillUpgradeAmount);
		setNameText();
	}

	// Sets the skills of starting skill/skills of a spy and changes the model
	// if the spy has a skill level higher than 3 in one category
	private void setStartingSkills(int skillToUpgrade, int skillUpgradeAmount) {

		boolean changeModel = false;

		if (skillUpgradeAmount > 2)
			changeModel = true;

		skills.put("COMBAT", 1);
		skills.put("ESPIONAGE", 1);
		skills.put("SABOTAGE", 1);
		skills.put("PERCEPTION", 1);
		skills.put("STEALTH", 1);

		switch (skillToUpgrade) {
		case 1:
			skills.put("COMBAT", skillUpgradeAmount);
			if (changeModel)
				setCurrentTileIndex(1);
			break;
		case 2:
			skills.put("ESPIONAGE", skillUpgradeAmount);
			if (changeModel)
				setCurrentTileIndex(5);
			break;
		case 3:
			skills.put("SABOTAGE", skillUpgradeAmount);
			if (changeModel)
				setCurrentTileIndex(4);
			break;
		case 4:
			skills.put("PERCEPTION", skillUpgradeAmount);
			if (changeModel)
				setCurrentTileIndex(2);
			break;
		case 5:
			skills.put("STEALTH", skillUpgradeAmount);
			if (changeModel)
				setCurrentTileIndex(3);
			break;
		default:
			break;
		}
	}

	// ====================================================
	// TOUCH INPUTS
	// ====================================================
	@Override
	public boolean onAreaTouched(final TouchEvent spyTouchEvent,
			final float spyTouchAreaLocalX, final float spyTouchAreaLocalY) {
		if (player.isMyTurn()) {
			this.setPosition(spyTouchEvent.getX() - (this.getWidth() / 2),
					spyTouchEvent.getY() - (this.getHeight() / 2));
			setNameText();
			if (spyTouchEvent.isActionUp()) {
				switch (continentLocation) {
				case 1:
					russiaTouch();
					break;

				case 2:
					usaTouch();
					break;
				}
			}

			// sets spy selected or not
			if (spyTouchEvent.isActionDown()) {
				this.setSelected();
			}
		}
		return true;
	}

	//touch method for spy on russian continent
	public void russiaTouch() {
		for (GridSquare GS : Russia.getInstance().getGrid()) {
			if (this.collidesWith(GS)) {
				goalGridSquare = GS.name;
				if (playerNumber == 1) {
					setMove(GS.getX(), GS.getY(), false);
				} else {
					setMove(GS.getX(), GS.getY(), false);
				}
			}
		}
		if (this.collidesWith(GameScene.transportBoxRT)) {
			setMove(GameScene.transportBoxRT.getX(),
					GameScene.transportBoxRT.getY(), true);
		} else if (this.collidesWith(GameScene.transportBoxUT)) {
			setMove(GameScene.transportBoxUT.getX(),
					GameScene.transportBoxUT.getY(), true);
		}

		this.setPosition(currentPosition.x, currentPosition.y);
		setNameText();
	}
	//touch method for spy on american continent
	public void usaTouch() {
		for (GridSquare GS : USA.getInstance().getGrid()) {
			if (this.collidesWith(GS)) {
				goalGridSquare = GS.name;
				if (playerNumber == 1) {
					setMove(GS.getX(), GS.getY(), false);
				} else {
					setMove(GS.getX(), GS.getY(), false);
				}
			}
		}

		if (this.collidesWith(GameScene.transportBoxRT)) {
			setMove(GameScene.transportBoxRT.getX(),
					GameScene.transportBoxRT.getY(), true);
		} else if (this.collidesWith(GameScene.transportBoxUT)) {
			setMove(GameScene.transportBoxUT.getX(),
					GameScene.transportBoxUT.getY(), true);
		}
		this.setPosition(currentPosition.x, currentPosition.y);
		setNameText();
	}

	// ====================================================
	// METHODS
	// ====================================================

	// sets the movement action
	public void setMove(float x, float y, boolean travelingFar) {
		if (travelingBetweenContinents && !travelingFar)
			TransportBox.unBookAirplane(this.playerNumber);

		// checks if transportBox movement action is allowed
		if (travelingFar) {
			if (!TransportBox.isPlaneFull(playerNumber)) {
				travelingBetweenContinents = true;
				TransportBox.bookAirplane(this.playerNumber);
				ChooseAction(Action.MOVE);
				goalPosition.set(x, y);
				this.travelArrow.changeArrow(currentPosition.x,
						currentPosition.y, goalPosition.x, goalPosition.y);
			} else {
				createToast("Plane full");
				travelingBetweenContinents = false;
				ChooseAction(Action.DO_NOTHING);
			}
		} else {
			// normal movement
			travelingBetweenContinents = false;
			if (!(goalGridSquare.equals(currentGridSquare))) {
				ChooseAction(Action.MOVE);
				goalPosition.set(x, y);
				this.travelArrow.changeArrow(currentPosition.x,
						currentPosition.y, goalPosition.x, goalPosition.y);
			}
		}
	}

	// performs the move action
	public void performMove() {
		// performs transportBox move operation
		if (travelingBetweenContinents) {
			if (continentLocation == 1) {
				if (GameScene.transportBoxRT.addSpyToTransit(this)) {
					this.setPosition(GameScene.transportBoxRT.getX(),
							ResourcesManager.cameraHeight / 2);
				}
			} else if (continentLocation == 2) {
				if (GameScene.transportBoxUT.addSpyToTransit(this)) {
					this.setPosition(GameScene.transportBoxUT.getX(),
							ResourcesManager.cameraHeight / 2);

				}
				currentPosition.set(goalPosition.x, goalPosition.y);
				currentGridSquare = goalGridSquare;
				setNameText();
			}

			// normal move operation
		} else {
			if (playerNumber == 1) {
				this.setPosition(goalPosition.x - 25, goalPosition.y);
				currentPosition.set(goalPosition.x - 25, goalPosition.y);
			} else {
				this.setPosition(goalPosition.x + 25, goalPosition.y);
				currentPosition.set(goalPosition.x + 25, goalPosition.y);
			}

			travelArrow.setVisible(false);
			setNameText();
			currentGridSquare = goalGridSquare;
			GameScene.transportBoxRT.removeSpy(this);
			GameScene.transportBoxUT.removeSpy(this);
		}
	}

	//creates a small message displayed in the lower middle part of the screen
	public void createToast(final String text) {
		ResourcesManager.getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				final Toast toast = Toast.makeText(ResourcesManager
						.getActivity().getBaseContext(), text,
						Toast.LENGTH_SHORT);
				toast.show();
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						toast.cancel();
					}
				}, 450);
				;
			}
		});
	}

	public void setVisibility(int playerNum) {
		if (playerNumber == playerNum) {
			this.setVisible(true);
			nameText.setVisible(true);
		} else if (isVisibleToEnemy) {
			this.setVisible(true);
			travelArrow.hideArrow();
			setNotSelected();
			nameText.setVisible(true);
		} else {
			this.setVisible(false);
			travelArrow.hideArrow();
			setNotSelected();
			nameText.setVisible(false);
		}
	}

	public boolean getVisibility() {
		return isVisibleToEnemy;
	}

	public Player getPlayer() {
		return player;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	//tries to pick an action, returns a toast detailing the result
	public void ChooseAction(Action action) {
		// hides the arrow when not performing a move action
		if (chosenAction == Action.MOVE && action != Action.MOVE) {
			travelArrow.hideArrow();
		}

		if (action != Action.MOVE && travelingBetweenContinents)
			TransportBox.unBookAirplane(this.playerNumber);
		switch (action) {
		case MOVE:
			createToast("Moving");
			chosenAction = action;
			break;
		case GATHER_INTEL:
			if (continentLocation == 3 || playerNumber == continentLocation) {
				createToast("Can't spy in this region");
				chosenAction = Action.DO_NOTHING;
				break;
			}
			createToast("Gathering intel");
			chosenAction = action;
			break;
		case COUNTER_ESPIONAGE:
			if (this.continentLocation != playerNumber) {
				createToast("Can't counter-spy in this region");
				chosenAction = Action.DO_NOTHING;
			}
			createToast("Counter-spying");
			chosenAction = action;
			break;
		case SABOTAGE:
			for (Building b : GameScene.allBuildings) {
				if (b.gridName.equals(this.currentGridSquare)) {
					if (b.isVisibleToEnemy) {
						this.buildingTarget = b;
						createToast("Sabotaging");
						chosenAction = Action.SABOTAGE;
					} else {
						createToast("No building detected");
						chosenAction = Action.DO_NOTHING;
					}
				}
			}
			if (continentLocation == 3 || playerNumber == continentLocation) {
				createToast("Can't sabotage in this region");
				chosenAction = Action.DO_NOTHING;
				break;
			}
			break;
		case ASSASSINATE:
			for (Spy s : GameScene.allSpies) {
				if (s.currentGridSquare.equals(this.currentGridSquare)
						&& s.isVisibleToEnemy
						&& this.playerNumber != s.playerNumber) {
					chosenAction = Action.ASSASSINATE;
					createToast("Assassinating");
				} else {
					createToast("No one to assassinate");
					chosenAction = Action.DO_NOTHING;
				}
				break;
			}

			chosenAction = action;
			break;
		case HIDE:
			if (continentLocation == 3) {
				createToast("Can't hide in this region");
				chosenAction = Action.DO_NOTHING;
			}
			createToast("Hiding");
			chosenAction = action;
			break;
		case DO_NOTHING:
			createToast("Doing nothing");
			break;
		}
	}

	public Action getAction() {
		if (chosenAction == null) {
			chosenAction = Action.DO_NOTHING;
		}
		return chosenAction;
	}

	//calls the relevant ability, increments exp and adds entries to the event log
	public void doAction(Action a) {
		if (chosenAction.equals(a)) {

			switch (chosenAction) {
			case MOVE:
				performMove();
				chosenAction = Action.DO_NOTHING;
				if (playerNumber == 1) {
					GameScene.eventLogp1.add(name + " moved");
				} else {
					GameScene.eventLogp2.add(name + " moved");
				}
				checkLevelUp();
				break;
			case GATHER_INTEL:
				// do not divide by 10 because this one is different
				if (abilities.get("GATHER_INTEL").use(skills.get("ESPIONAGE"))) {
					expLevel += 25;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name
								+ " discovered a building!");
					} else {
						GameScene.eventLogp2.add(name
								+ " discovered a building!");
					}
				} else {
					expLevel += 10;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name + " gathered intel");
					} else {
						GameScene.eventLogp2.add(name + " gathered intel");
					}
				}
				checkLevelUp();
				break;
			case COUNTER_ESPIONAGE:
				if (abilities.get("COUNTER_ESPIONAGE").use(
						skills.get("PERCEPTION"))) {
					expLevel += 25;
					discoveriesMade += 1;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name
								+ " discovered enemy spy!");
						GameScene.eventLogp2.add("An agent has been compromised!");
					} else {
						GameScene.eventLogp2.add(name
								+ " discovered enemy spy!");
						GameScene.eventLogp1.add("An agent has been compromised!");
					}
				} else {
					expLevel += 5;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name + " counter-spied");
					} else {
						GameScene.eventLogp2.add(name + " counter-spied");
					}
				}
				checkLevelUp();
				break;
			case SABOTAGE:
				if (abilities.get("SABOTAGE").use(skills.get("SABOTAGE"))) {
					expLevel += 100;

					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name
								+ " sabotaged a building!");
					} else {
						GameScene.eventLogp2.add(name
								+ " sabotaged a building!");
					}
				} else {
					expLevel += 5;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name + " failed at sabotage");
					} else {
						GameScene.eventLogp2.add(name + " failed at sabotage");
					}
				}
				checkLevelUp();
				break;
			case ASSASSINATE:
				if (abilities.get("ASSASSINATE").use(skills.get("COMBAT"))) {
					expLevel += 100;
					killCount += 1;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name
								+ " assassinated enemy spy!");
					} else {
						GameScene.eventLogp2.add(name
								+ " assassinated enemy spy!");
					}
				} else {
					expLevel += 5;
					if (playerNumber == 1) {
						GameScene.eventLogp1
								.add(name + " failed assassination");
					} else {
						GameScene.eventLogp2
								.add(name + " failed assassination");
					}
				}
				checkLevelUp();
				break;
			case HIDE:
				if (abilities.get("HIDE").use(skills.get("STEALTH"))) {
					expLevel += 15;
					if (playerNumber == 1) {
						GameScene.eventLogp1.add(name + " is no longer revealed to the enemy");
					} else {
						GameScene.eventLogp2.add(name + " is no longer revealed to the enemy");
					}
				}
				expLevel += 5;
				if (playerNumber == 1) {
					GameScene.eventLogp1.add(name + " is trying to hide");
				} else {
					GameScene.eventLogp2.add(name + " is trying to hide");
				}
				checkLevelUp();
				break;
			case DO_NOTHING:
				break;
			}
		}
	}

	// SEE GAMESCENE FOR DETAILS
	public void setSelected() {
		GameScene.setSelectedSpy(this);
	}

	public void setNotSelected() {
		GameScene.setSelectedSpy(null);
	}

	public void setNameText() {
		nameText.setPosition(this.getX(), this.getY() + 35);
		buttonNameText = new Text(0, 0, ResourcesManager.statisticsFont,
				"Agent\n" + this.name.subSequence(5, this.name.length()), 20,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		GameScene.GameHud.attachChild(buttonNameText);
		hideText();
	}

	public void hideText() {
		buttonNameText.setVisible(false);
	}

	//returns a button used to denote this spy in the spies overlay
	public GameButton getButton(float x, float y) {
		buttonNameText.setPosition(x, y);
		buttonNameText.setVisible(true);
		myButton = new GameButton(x, y, this) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					for (Spy spy : GameScene.allSpies) {
						spy.spyButtonIsSelected = false;
					}
					spyButtonIsSelected = true;

					GameScene.setActiveSpiesHud(this);
					System.out.println(name + " clicked");
				}
				return true;
			};
		};
		return myButton;
	}

	public boolean isSpyButtonIsSelected() {
		return spyButtonIsSelected;
	}

	public void setSpyButtonIsSelected(boolean spyButtonIsSelected) {
		this.spyButtonIsSelected = spyButtonIsSelected;
	}

	public void setNameText(float x, float y) {
		nameText.setPosition(x, y + 35);
	}

	public void checkLevelUp() {
		if (expLevel >= 100) {
			if (playerNumber == 1) {
				GameScene.eventLogp1.add(name + " leveled up!");
			} else {
				GameScene.eventLogp2.add(name + " leveled up!");
			}
		}
	}

	public void changeLocation(int chosenLoc) {
		this.detachSelf();
		travelArrow.detachSelf();
		nameText.detachSelf();
		if (chosenLoc == 1) {
			GameScene.UT.attachChild(this);
			GameScene.UT.attachChild(travelArrow);
			GameScene.UT.attachChild(nameText);
			setNameText();
		} else {
			GameScene.RT.attachChild(this);
			GameScene.RT.attachChild(travelArrow);
			GameScene.RT.attachChild(nameText);
			setNameText();
		}
	}

	//levels up a specified skill. if it reaches 3 or higher the model of the spy is changed
	public boolean levelUpSkill(String s) {
		if (skills.get(s) == 5) {
			createToast("Skill is already max level");
			return false;
		}
		skills.put(s, skills.get(s) + 1);
		if ((Integer) skills.get(s) >= 3) {

			if (s.equals("COMBAT")) {
				this.setCurrentTileIndex(1);
			} else if (s.equals("ESPIONAGE")) {
				this.setCurrentTileIndex(3);
			} else if (s.equals("SABOTAGE")) {
				this.setCurrentTileIndex(4);
			} else if (s.equals("PERCEPTION")) {
				this.setCurrentTileIndex(2);
			} else if (s.equals("STEALTH")) {
				this.setCurrentTileIndex(5);
			}
		}

		expLevel = expLevel - 100;
		level++;
		return true;
	}

	public void killSpy() {
		if (playerNumber == 1) {
			GameScene.eventLogp1.add(this.name + "has been assassinated!");
		} else {
			GameScene.eventLogp2.add(this.name + "has been assassinated!");
		}
		GameScene.deathList.add(this);
	}
}
