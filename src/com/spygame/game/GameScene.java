package com.spygame.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import com.spygame.game.entities.Action;
import com.spygame.game.entities.Continent;
import com.spygame.game.entities.EndTurnButton;
import com.spygame.game.entities.GameButton;
import com.spygame.game.entities.MenuButton;
import com.spygame.game.entities.NameGenerator;
import com.spygame.game.entities.Player;
import com.spygame.game.entities.PullMenu;
import com.spygame.game.entities.PullMenuToggler;
import com.spygame.game.entities.Russia;
import com.spygame.game.entities.Spy;
import com.spygame.game.entities.TransportBox;
import com.spygame.game.entities.USA;
import com.spygame.game.entities.buildings.Building;
import com.spygame.game.entities.buildings.PropagandaCenterBuilding;
import com.spygame.game.entities.buildings.SpyAcademyBuilding;
import com.spygame.managers.ResourcesManager;

public class GameScene extends BasicScene implements IOnSceneTouchListener,
		IScrollDetectorListener, IPinchZoomDetectorListener {

	// ===========================================
	// CONSTANTS
	// ===========================================
	public final static NameGenerator nameGen = new NameGenerator();
	public static float spyBlue = 0;

	// entity collections. Holds all objects relating to them
	final Entity gameHudEntity = new Entity();
	final Entity menuHudEntity = new Entity();
	final Entity spiesHudEntity = new Entity();
	final static Entity VictoryEntity = new Entity();
	final static Entity eventLogEntity = new Entity();
	final static Entity abilityButtons = new Entity();
	final Entity optionsHudEntity = new Entity();

	// used for setting unique starting skills
	public ArrayList<Integer> skillRandomiser = new ArrayList<Integer>();
	// ===========================================
	// VARIABLES AND OBJECTS
	// ===========================================

	// placeHolder - starting settings
	public static int startingExperienceLevel = 3;
	public static boolean skillsMirrored = false;
	public static int startingSpies = 3;

	Random r = new Random();
	// players
	public final static Player playerOne = new Player("Player 1", true);
	public final static Player playerTwo = new Player("Player 2", false);

	// currently selected spy
	public static Spy selectedSpy;

	// lists
	public static ArrayList<Spy> allSpies;
	public static ArrayList<Spy> deathList;
	public static ArrayList<Building> allBuildings;
	public static ArrayList<Continent> allContinents;
	public static ArrayList<String> eventLogp1 = new ArrayList<String>();
	public static ArrayList<String> eventLogp2 = new ArrayList<String>();

	// Global instance
	private static final GameScene INSTANCE = new GameScene();

	// Global instance constructor
	public static GameScene getInstance() {
		return INSTANCE;
	}

	// GameHud
	public static HUD GameHud = new HUD();

	// used for closing the gameScene
	public BasicScene thisBaseScene = this;

	// Objects for loading Scene
	private Text LoadingText;
	private Scene LoadingScene;

	// regions and transportboxes
	public static Russia RT;
	public static USA UT;
	public static TransportBox transportBoxRT;
	public static TransportBox transportBoxUT;

	// menu HUD buttons and object
	GameButton returnToGameButton;
	GameButton quitButton;
	GameButton mainMenuButton;
	GameButton optionsButton;
	private Rectangle fadeRectangle;

	// event log
	public static Text playerName;
	public static Text eventsText;
	public static GameButton eventStartTurn;
	private static Rectangle eventFadeRectangle;
	public static GameButton turnSwitchButton;

	// victory overlay
	private static Rectangle victoryFadeRectangle;
	public static GameButton doneVictoryButton;
	public static Text victorName;

	// options HUD buttons and objects
	GameButton returnToMenuButton;

	// pull down menu buttons and objects
	EndTurnButton endTurnButton;
	MenuButton menuButton;
	public static PullMenuToggler pullObj;

	// spy HUD buttons and objects
	GameButton returnToGameButtonSpy;

	// ability buttons
	static GameButton gatherIntelButton;
	static GameButton sabotageButton;
	static GameButton counterEspionageButton;
	static GameButton hideButton;
	static GameButton assassinateButton;

	// spies HUD
	private Rectangle fadeRectangleSpyHUD;
	static int spyListCounter;
	public static Text skillCombatText;
	public static Text skillEspionageText;
	public static Text skillSabotageText;
	public static Text skillPerceptionText;
	public static Text skillStealthText;
	public static Text spyNameText;
	public static Text statisticOne;
	public static Text statisticTwo;
	public static Text statisticThree;
	static TiledSprite spyModelBlack;
	static TiledSprite spyModelWhite;
	public static Sprite expBarEmpty;
	public static Sprite expBar1;
	public static Sprite expBar2;
	public static Sprite expBar3;
	public static Sprite expBarFull;
	public static Text expText;
	public static GameButton plusButton1, plusButton2, plusButton3,
			plusButton4, plusButton5;
	public static GameButton levelUpButton;
	public static GameButton levelUpFadedButton;
	ArrayList<GameButton> spyList;

	// general booleans
	public static boolean RussiaToggled = true;
	public static boolean menuToggled = false;
	public static boolean spiesMenuToggled = false;
	public static boolean optionsToggled = false;
	public static boolean eventLogToggled = false;

	// zoom and scroll
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;

	// ===========================================
	// CONSTRUCTORS
	// ===========================================

	public GameScene() {
		this(1f);
	};

	public GameScene(float pMinSecsLoadingShown) {
		super(pMinSecsLoadingShown);
		// set touch attributes
		this.setTouchAreaBindingOnActionDownEnabled(true);
	}

	// ===========================================
	// METHODS
	// ===========================================

	@Override
	public Scene onLoadingScreenLoadAndShown() {
		// setup loading screen
		LoadingScene = new Scene();
		LoadingScene.setBackgroundEnabled(true);
		return LoadingScene;
	}

	@Override
	public void onLoadingScreenUnloadAndHidden() {
		// detach loading resources
		LoadingText.detachSelf();
		LoadingText = null;
		LoadingScene = null;
	}

	// initialises the game. Calls all other initialising methods
	@Override
	public void onLoadScene() {
		resourcesManager.loadGameResources();

		//Creates a rectangle behind all other sprites that matches the colour 
		//of the sprite background. It covers up the black background during
		//continent movement
		Sprite bgFix = new Sprite(0, 0, ResourcesManager.backgroundFixTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		bgFix.setScale(3);
		this.attachChild(bgFix);
		this.setBackground(new Background(
				new org.andengine.util.adt.color.Color(9, 111, 145)));
		this.setBackgroundEnabled(true);

		
		// regions
		RT = new Russia();
		UT = new USA();
		allContinents = new ArrayList<Continent>();
		allContinents.add(RT);
		allContinents.add(UT);
		Entity entRegionsLayer = new Entity();
		entRegionsLayer.attachChild(RT);
		entRegionsLayer.attachChild(UT);
		this.attachChild(entRegionsLayer);
		this.registerTouchArea(entRegionsLayer);

		//list for removing spies from 
		deathList = new ArrayList<Spy>();
		
		// the two transportBoxes
		transportBoxRT = new TransportBox();
		transportBoxUT = new TransportBox();
		
		// menu objects
		endTurnButton = new EndTurnButton();
		final PullMenu pullMenu = new PullMenu();
		menuButton = new MenuButton(this);

		pullObj = new PullMenuToggler(pullMenu, endTurnButton, menuButton);

		// region Switch buttons
		final GameButton regionSwitchLeft = new GameButton(
				ResourcesManager.leftArrowTR.getWidth() / 2f,
				ResourcesManager.leftArrowTR.getHeight() / 2, 1) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {

					UT.swipe();
					RT.swipe();
					RussiaToggled = false;
				}
				return true;
			}
		};

		final GameButton regionSwitchRight = new GameButton(
				ResourcesManager.cameraWidth
						- (ResourcesManager.leftArrowTR.getWidth() / 2f),
				ResourcesManager.leftArrowTR.getHeight() / 2, 2) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {

					RT.swipe();
					UT.swipe();
					RussiaToggled = true;
				}

				return true;
			}
		};
		RT.attachChild(regionSwitchLeft);
		UT.attachChild(regionSwitchRight);
		this.registerTouchArea(regionSwitchLeft);
		this.registerTouchArea(regionSwitchRight);
		
		//theses are used for  zooming and scrolling
		mScrollDetector = new SurfaceScrollDetector(this);
		mPinchZoomDetector = new PinchZoomDetector(this);
		this.setOnAreaTouchTraversalFrontToBack();
		this.setOnSceneTouchListener(this);

		//turns on touch events
		this.setTouchAreaBindingOnActionDownEnabled(true);

		// Menu
		Entity entMenuLayer = new Entity();
		entMenuLayer.attachChild(pullObj);
		entMenuLayer.attachChild(pullMenu);
		gameHudEntity.attachChild(entMenuLayer);
		
		
		setUpMenuHud();

		Entity entMenuButtonsLayer = new Entity();
		entMenuButtonsLayer.attachChild(endTurnButton);
		entMenuButtonsLayer.attachChild(menuButton);

		gameHudEntity.attachChild(entMenuButtonsLayer);

		//GAMEHUD
		GameHud.registerTouchArea(endTurnButton);
		GameHud.registerTouchArea(menuButton);
		GameHud.registerTouchArea(pullObj);

		//turns on touch events for game hud
		GameHud.setTouchAreaBindingOnActionDownEnabled(true);
		GameHud.setTouchAreaBindingOnActionMoveEnabled(true);

		//buttons to open the spies overlay
		final GameButton spiesButtonRussia = new GameButton(
				ResourcesManager.cameraWidth
						- (ResourcesManager.spiesButtonTR.getWidth() / 2),
				ResourcesManager.spiesButtonTR.getHeight() / 2,
				ResourcesManager.spiesButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					toggleSpiesHud();
				}

				return true;
			}
		};
		final GameButton spiesButtonUSA = new GameButton(
				ResourcesManager.spiesButtonTR.getWidth() / 2,
				ResourcesManager.spiesButtonTR.getHeight() / 2,
				ResourcesManager.spiesButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					toggleSpiesHud();
				}

				return true;
			}
		};
		RT.attachChild(spiesButtonRussia);
		this.registerTouchArea(spiesButtonRussia);
		UT.attachChild(spiesButtonUSA);
		this.registerTouchArea(spiesButtonUSA);

		
		RT.attachChild(transportBoxRT);
		UT.attachChild(transportBoxUT);

		// attaching and registering ability buttons
		setUpAbilityButtons();
		setUpMenuHud();
		setUpSpiesHud();
		GameHud.attachChild(abilityButtons);

		GameHud.attachChild(gameHudEntity);
		menuHudEntity.setVisible(false);
		GameHud.attachChild(menuHudEntity);
		spiesHudEntity.setVisible(false);
		GameHud.attachChild(spiesHudEntity);
		GameHud.attachChild(optionsHudEntity);
		optionsHudEntity.setVisible(false);

		// init method calls
		initBuildings();

		initSpies();

		initEventLog();

		initVictoryScreen();

		toggleEventLog();
	}

	//initialises the victory overlay resources
	private void initVictoryScreen() {
		victoryFadeRectangle = new Rectangle(0, 0,
				ResourcesManager.cameraWidth, ResourcesManager.cameraHeight,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				System.out.println("stop poking me");
				return true;
			}
		};
		victoryFadeRectangle.setColor(0f, 0f, 0f, 0.95f);
		victoryFadeRectangle.setPosition(RT.getX(), RT.getY());

		doneVictoryButton = new GameButton(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 2,
				ResourcesManager.endTurnBoxTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					goMainMenu();
				}
				return true;
			}
		};

		victorName = new Text(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 7,
				ResourcesManager.statisticsStarFont,
				"abcdefghijklmnopqrstuvwxyz", 50,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		VictoryEntity.attachChild(victoryFadeRectangle);
		VictoryEntity.attachChild(doneVictoryButton);
		VictoryEntity.attachChild(victorName);
		VictoryEntity.setVisible(false);
		GameHud.attachChild(VictoryEntity);

	}

	//initialises the event log overlay resources
	private void initEventLog() {
		eventFadeRectangle = new Rectangle(0, 0, ResourcesManager.cameraWidth,
				ResourcesManager.cameraHeight,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				System.out.println("stop poking me");
				return true;
			}
		};
		eventFadeRectangle.setColor(0f, 0f, 0f, 0.95f);
		eventFadeRectangle.setPosition(RT.getX(), RT.getY());

		playerName = new Text(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 7,
				ResourcesManager.statisticsStarFont,
				"abcdefghijklmnopqrstuvwxyz", 50,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		eventsText = new Text(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 5f,
				ResourcesManager.statisticsFont,
				"abcdefghijklmnopqrstuvwxyz124567890", 800,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		eventStartTurn = new GameButton(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 1,
				ResourcesManager.pauseMenuResumeBoxTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					toggleEventLog();
				}
				return true;
			}
		};

		turnSwitchButton = new GameButton(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.cameraHeight / 8) * 1,
				ResourcesManager.pauseMenuResumeBoxTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					turnSwitchButton.setVisible(false);
					eventFadeRectangle.setAlpha(0.85f);
					eventsText.setVisible(true);
					GameHud.unregisterTouchArea(turnSwitchButton);
				}
				return true;
			}
		};

		eventsText.setText("");
		eventLogEntity.attachChild(eventFadeRectangle);
		eventLogEntity.attachChild(playerName);
		eventLogEntity.attachChild(eventsText);
		eventLogEntity.attachChild(eventStartTurn);
		eventLogEntity.setVisible(true);
	}

	// initialises the ability button entity
	private void setUpAbilityButtons() {
		gatherIntelButton = new GameButton(ResourcesManager.cameraWidth / 2,
				(ResourcesManager.intelButtonTR.getHeight() / 2),
				ResourcesManager.intelButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					selectedSpy.ChooseAction(Action.GATHER_INTEL);
				}

				return true;
			}
		};

		counterEspionageButton = new GameButton(gatherIntelButton.getX()
				- ResourcesManager.counterSpyButtonTR.getWidth() - 5,
				(ResourcesManager.counterSpyButtonTR.getHeight() / 2),
				ResourcesManager.counterSpyButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					selectedSpy.ChooseAction(Action.COUNTER_ESPIONAGE);
				}

				return true;
			}
		};

		hideButton = new GameButton(counterEspionageButton.getX()
				- ResourcesManager.hideButtonTR.getWidth() - 5,
				(ResourcesManager.hideButtonTR.getHeight() / 2),
				ResourcesManager.hideButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					selectedSpy.ChooseAction(Action.HIDE);
				}
				return true;
			}
		};

		sabotageButton = new GameButton(gatherIntelButton.getX()
				+ ResourcesManager.sabotageButtonTR.getWidth() + 5,
				(ResourcesManager.sabotageButtonTR.getHeight() / 2),
				ResourcesManager.sabotageButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					selectedSpy.ChooseAction(Action.SABOTAGE);
				}
				return true;
			}
		};

		assassinateButton = new GameButton(sabotageButton.getX()
				+ ResourcesManager.assassinateButtonTR.getWidth() + 5,
				(ResourcesManager.assassinateButtonTR.getHeight() / 2),
				ResourcesManager.assassinateButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float spyTouchAreaLocalX,
					final float spyTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					selectedSpy.ChooseAction(Action.ASSASSINATE);
				}

				return true;
			}
		};

		abilityButtons.attachChild(gatherIntelButton);
		abilityButtons.attachChild(counterEspionageButton);
		abilityButtons.attachChild(hideButton);
		abilityButtons.attachChild(sabotageButton);
		abilityButtons.attachChild(assassinateButton);

		abilityButtons.setVisible(false);
	}

	// initialises the menu HUD
	private void setUpSpiesHud() {

		fadeRectangleSpyHUD = new Rectangle(0, 0, ResourcesManager.cameraWidth,
				ResourcesManager.cameraHeight,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				return true;
			}
		};
		spiesHudEntity.attachChild(fadeRectangleSpyHUD);

		fadeRectangleSpyHUD.setColor(0f, 0f, 0f, 0.8f);
		fadeRectangleSpyHUD.setPosition(RT.getX(), RT.getY());
		GameHud.setTouchAreaBindingOnActionDownEnabled(true);

		returnToGameButtonSpy = new GameButton(ResourcesManager.cameraWidth
				- (ResourcesManager.spyHudResumeButtonTR.getWidth() / 2),
				ResourcesManager.spyHudResumeButtonTR.getHeight() / 2,
				ResourcesManager.spyHudResumeButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					returnToGame();
				}
				return true;
			}

			private void returnToGame() {
				toggleSpiesHud();

			}
		};

		final Sprite spiesListBox = new Sprite(
				ResourcesManager.listOfSpieTR.getWidth() / 2,
				ResourcesManager.listOfSpieTR.getHeight() / 2,
				ResourcesManager.listOfSpieTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesHudEntity.attachChild(spiesListBox);

		final Sprite statisticsListBox = new Sprite(
				ResourcesManager.listOfSpieTR.getWidth()
						+ ResourcesManager.listOfStatisticsTR.getWidth() / 2,
				ResourcesManager.listOfStatisticsTR.getHeight() / 2,
				ResourcesManager.listOfStatisticsTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesHudEntity.attachChild(statisticsListBox);

		statisticOne = new Text(statisticsListBox.getX(),
				(ResourcesManager.cameraHeight / 4) * 3,
				ResourcesManager.statisticsFont, "Kill count: 0123456789", 22,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		statisticOne.setText("");
		spiesHudEntity.attachChild(statisticOne);

		statisticTwo = new Text(statisticsListBox.getX(),
				((ResourcesManager.cameraHeight / 4) * 3) - 30,
				ResourcesManager.statisticsFont, "Intel gathered: 0123456789",
				35,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		statisticTwo.setText("");
		spiesHudEntity.attachChild(statisticTwo);

		statisticThree = new Text(statisticsListBox.getX(),
				((ResourcesManager.cameraHeight / 4) * 3) - 60,
				ResourcesManager.statisticsFont,
				"Discoveries made: 0123456789", 35,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		statisticThree.setText("");
		spiesHudEntity.attachChild(statisticThree);

		final Sprite skillsListBox = new Sprite(
				ResourcesManager.listOfSpieTR.getWidth()
						+ ResourcesManager.listOfStatisticsTR.getWidth()
						+ ResourcesManager.listOfSkillsTR.getWidth() / 2,
				ResourcesManager.listOfSkillsTR.getHeight() / 2,
				ResourcesManager.listOfSkillsTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesHudEntity.attachChild(skillsListBox);

		skillCombatText = new Text(skillsListBox.getX() + 20,
				((ResourcesManager.listOfSkillsTR.getHeight() / 10) * 8) - 12,
				ResourcesManager.statisticsStarFont, "*****", 5,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		skillCombatText.setText("");
		spiesHudEntity.attachChild(skillCombatText);

		skillEspionageText = new Text(skillCombatText.getX(),
				skillCombatText.getY() - 35,
				ResourcesManager.statisticsStarFont, "*****", 5,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		skillEspionageText.setText("");
		spiesHudEntity.attachChild(skillEspionageText);

		skillSabotageText = new Text(skillCombatText.getX(),
				skillEspionageText.getY() - 35,
				ResourcesManager.statisticsStarFont, "*****", 5,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		skillSabotageText.setText("");
		spiesHudEntity.attachChild(skillSabotageText);

		skillPerceptionText = new Text(skillCombatText.getX(),
				skillSabotageText.getY() - 35,
				ResourcesManager.statisticsStarFont, "*****", 5,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		skillPerceptionText.setText("");
		spiesHudEntity.attachChild(skillPerceptionText);

		skillStealthText = new Text(skillCombatText.getX(),
				skillPerceptionText.getY() - 35,
				ResourcesManager.statisticsStarFont, "*****", 5,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		skillStealthText.setText("");
		spiesHudEntity.attachChild(skillStealthText);

		final Sprite spyModelBox = new Sprite(
				ResourcesManager.listOfSpieTR.getWidth()
						+ ResourcesManager.listOfStatisticsTR.getWidth()
						+ ResourcesManager.spyModelBoxTR.getWidth() / 2,
				ResourcesManager.cameraHeight
						- (ResourcesManager.spyModelBoxTR.getHeight() / 2),
				ResourcesManager.spyModelBoxTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesHudEntity.attachChild(spyModelBox);

		final Sprite levelUpBox = new Sprite(
				ResourcesManager.listOfSpieTR.getWidth()
						+ ResourcesManager.listOfStatisticsTR.getWidth()
						+ ResourcesManager.spyModelBoxTR.getWidth()
						+ ResourcesManager.spyModelBoxTR.getWidth() / 2,
				ResourcesManager.cameraHeight
						- (ResourcesManager.spyModelBoxTR.getHeight() / 2),
				ResourcesManager.spyModelBoxTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spiesHudEntity.attachChild(levelUpBox);

		spiesHudEntity.attachChild(returnToGameButtonSpy);

		spyModelWhite = new TiledSprite(spyModelBox.getX(),
				spyModelBox.getY() - 15, ResourcesManager.whiteSpyHDTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spyModelBlack = new TiledSprite(spyModelBox.getX(),
				spyModelBox.getY() - 15, ResourcesManager.blackSpyHDTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		spyModelWhite.setCurrentTileIndex(0);
		spyModelBlack.setCurrentTileIndex(0);
		spyModelBlack.setVisible(false);
		spyModelWhite.setVisible(false);

		spyNameText = new Text(spyModelWhite.getX(), spyModelWhite.getY() + 75,
				ResourcesManager.statisticsFont, "Agent unknown", 35,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		spyNameText.setText("");
		spiesHudEntity.attachChild(spyNameText);

		expBarEmpty = new Sprite(levelUpBox.getX(), levelUpBox.getY() + 40,
				ResourcesManager.expBarEmptyTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		expBar1 = new Sprite(levelUpBox.getX(), levelUpBox.getY() + 40,
				ResourcesManager.expBar1TR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		expBar2 = new Sprite(levelUpBox.getX(), levelUpBox.getY() + 40,
				ResourcesManager.expBar2TR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		expBar3 = new Sprite(levelUpBox.getX(), levelUpBox.getY() + 40,
				ResourcesManager.expBar3TR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		expBarFull = new Sprite(levelUpBox.getX(), levelUpBox.getY() + 40,
				ResourcesManager.expBarFullTR,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		expBarEmpty.setVisible(false);
		expBar1.setVisible(false);
		expBar2.setVisible(false);
		expBar3.setVisible(false);
		expBarFull.setVisible(false);

		expText = new Text(expBarEmpty.getX(), expBarEmpty.getY() + 30,
				ResourcesManager.statisticsFont, "EXP: 01257/01257", 35,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		expText.setText("");
		spiesHudEntity.attachChild(expText);

		plusButton1 = new GameButton(skillsListBox.getX() + 150,
				((ResourcesManager.listOfSkillsTR.getHeight() / 10) * 8) - 12,
				ResourcesManager.plussButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pTouchEvent.isActionUp()) {
					for (Spy spy : allSpies) {
						if (spy.spyButtonIsSelected) {
							if (spy.levelUpSkill("COMBAT")) {

								unregisterPlusButtons();
								skillCombatText.setText(skillCombatText
										.getText() + "*");
								expText.setText("EXP: 0/100");
								expBarEmpty.setVisible(true);
								expBar1.setVisible(false);
								expBar2.setVisible(false);
								expBar3.setVisible(false);
								expBarFull.setVisible(false);
							}
						}
					}

				}
				return true;
			}

		};

		plusButton2 = new GameButton(skillsListBox.getX() + 150,
				plusButton1.getY() - 35, ResourcesManager.plussButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pTouchEvent.isActionUp()) {
					for (Spy spy : allSpies) {
						if (spy.spyButtonIsSelected) {
							if (spy.levelUpSkill("ESPIONAGE")) {
								skillEspionageText.setText(skillEspionageText
										.getText() + "*");
								unregisterPlusButtons();
								expText.setText("EXP: 0/100");
								expBarEmpty.setVisible(true);
								expBar1.setVisible(false);
								expBar2.setVisible(false);
								expBar3.setVisible(false);
								expBarFull.setVisible(false);
							}

						}
					}

				}
				return true;
			}

		};

		plusButton3 = new GameButton(skillsListBox.getX() + 150,
				plusButton2.getY() - 35, ResourcesManager.plussButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					for (Spy spy : allSpies) {
						if (spy.spyButtonIsSelected) {
							if (spy.levelUpSkill("SABOTAGE")) {
								unregisterPlusButtons();
								skillSabotageText.setText(skillSabotageText
										.getText() + "*");
								expText.setText("EXP: 0/100");
								expBarEmpty.setVisible(true);
								expBar1.setVisible(false);
								expBar2.setVisible(false);
								expBar3.setVisible(false);
								expBarFull.setVisible(false);
							}

						}
					}

				}
				return true;
			}
		};

		plusButton4 = new GameButton(skillsListBox.getX() + 150,
				plusButton3.getY() - 35, ResourcesManager.plussButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					for (Spy spy : allSpies) {
						if (spy.spyButtonIsSelected) {
							if (spy.levelUpSkill("PERCEPTION")) {
								unregisterPlusButtons();
								skillPerceptionText.setText(skillPerceptionText
										.getText() + "*");
								expText.setText("EXP: 0/100");
								expBarEmpty.setVisible(true);
								expBar1.setVisible(false);
								expBar2.setVisible(false);
								expBar3.setVisible(false);
								expBarFull.setVisible(false);
							}
						}
					}

				}
				return true;
			}
		};

		plusButton5 = new GameButton(skillsListBox.getX() + 150,
				plusButton4.getY() - 35, ResourcesManager.plussButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					for (Spy spy : allSpies) {
						if (spy.spyButtonIsSelected) {
							if (spy.levelUpSkill("STEALTH")) {
								unregisterPlusButtons();
								skillStealthText.setText(skillStealthText
										.getText() + "*");
								expText.setText("EXP: 0/100");
								expBarEmpty.setVisible(true);
								expBar1.setVisible(false);
								expBar2.setVisible(false);
								expBar3.setVisible(false);
								expBarFull.setVisible(false);
							}
						}
					}

				}
				return true;
			}

		};
		spiesHudEntity.attachChild(plusButton1);
		spiesHudEntity.attachChild(plusButton2);
		spiesHudEntity.attachChild(plusButton3);
		spiesHudEntity.attachChild(plusButton4);
		spiesHudEntity.attachChild(plusButton5);

		levelUpButton = new GameButton(levelUpBox.getX(),
				levelUpBox.getY() - 30, ResourcesManager.levelUpButtonTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					System.out.println("shuld be working");
					plusButton1.setVisible(true);
					plusButton2.setVisible(true);
					plusButton3.setVisible(true);
					plusButton4.setVisible(true);
					plusButton5.setVisible(true);
					GameHud.registerTouchArea(plusButton1);
					GameHud.registerTouchArea(plusButton2);
					GameHud.registerTouchArea(plusButton3);
					GameHud.registerTouchArea(plusButton4);
					GameHud.registerTouchArea(plusButton5);
					levelUpButton.setVisible(false);
					levelUpFadedButton.setVisible(true);
				}
				return true;
			}
		};
		levelUpButton.setVisible(false);

		levelUpFadedButton = new GameButton(levelUpBox.getX(),
				levelUpBox.getY() - 30, ResourcesManager.levelUpFadedButtonTR) {
		};

		spiesHudEntity.attachChild(levelUpButton);
		spiesHudEntity.attachChild(levelUpFadedButton);

		spyList = new ArrayList<GameButton>(5);
	}

	//is used in conjuncton with levelling up
	public static void unregisterPlusButtons() {
		GameHud.unregisterTouchArea(plusButton1);
		GameHud.unregisterTouchArea(plusButton2);
		GameHud.unregisterTouchArea(plusButton3);
		GameHud.unregisterTouchArea(plusButton4);
		GameHud.unregisterTouchArea(plusButton5);
		plusButton1.setVisible(false);
		plusButton2.setVisible(false);
		plusButton3.setVisible(false);
		plusButton4.setVisible(false);
		plusButton5.setVisible(false);
		GameHud.unregisterTouchArea(levelUpButton);
		levelUpButton.setVisible(false);
		levelUpFadedButton.setVisible(true);
	}

	// initialises the menu and options HUD
	private void setUpMenuHud() {
		fadeRectangle = new Rectangle(0, 0, ResourcesManager.cameraWidth,
				ResourcesManager.cameraHeight,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				return true;
			}
		};
		// MENU HUD
		menuHudEntity.attachChild(fadeRectangle);

		fadeRectangle.setColor(0f, 0f, 0f, 0.8f);
		fadeRectangle.setPosition(RT.getX(), RT.getY());
		GameHud.setTouchAreaBindingOnActionDownEnabled(true);

		Text pauseText = new Text(ResourcesManager.cameraWidth / 2f,
				(ResourcesManager.cameraHeight / 4) * 3.5f,
				ResourcesManager.pausedFont, "Paused", 7,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		menuHudEntity.attachChild(pauseText);

		returnToGameButton = new GameButton(ResourcesManager.cameraWidth / 2f,
				(ResourcesManager.cameraHeight / 4) * 2.5f,
				ResourcesManager.pauseMenuResumeBoxTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					toggleMenuHud();
				}
				return true;
			}
		};

		optionsButton = new GameButton(ResourcesManager.cameraWidth / 2f,
				returnToGameButton.getY() - 80,
				ResourcesManager.pauseOptionsBoxTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					// optionsToggle();
				}
				return true;
			}
		};

		mainMenuButton = new GameButton(ResourcesManager.cameraWidth / 2f,
				optionsButton.getY() - 80, ResourcesManager.pauseMainMenuBoxTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					goMainMenu();
				}
				return true;
			}
		};

		quitButton = new GameButton(ResourcesManager.cameraWidth / 2f,
				mainMenuButton.getY() - 80, ResourcesManager.pauseQuitBoxTR) {

			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					quit();
				}
				return true;
			}
		};

		menuHudEntity.attachChild(returnToGameButton);
		menuHudEntity.attachChild(quitButton);
		menuHudEntity.attachChild(mainMenuButton);
		menuHudEntity.attachChild(optionsButton);

		// options HUD UNDER CONSTRUCTION
		returnToMenuButton = new GameButton(ResourcesManager.cameraWidth / 2f,
				(ResourcesManager.cameraHeight / 4) * 2.5f,
				ResourcesManager.spyHudResumeButtonTR) {
			@Override
			public boolean onAreaTouched(final TouchEvent pTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pTouchEvent.isActionUp()) {
					optionsToggle();
				}
				return true;
			}
		};

		Text optionsText = new Text(ResourcesManager.cameraWidth / 2f,
				(ResourcesManager.cameraHeight / 4) * 3.5f,
				ResourcesManager.pausedFont, "Options", 7,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());

		optionsHudEntity.attachChild(returnToMenuButton);
		optionsHudEntity.attachChild(optionsText);
	}

	// initialises buildings based on game settings
	public void initBuildings() {
		allBuildings = new ArrayList<Building>();

		int temp = r.nextInt(RT.getGrid().size());
		Building b = new PropagandaCenterBuilding(RT.getGrid().get(temp), 1, RT
				.getGrid().get(temp).name);
		allBuildings.add(b);
		RT.attachChild(b);
		b.setZIndex(1);

		int tempp = r.nextInt(RT.getGrid().size());
		Building bb = new SpyAcademyBuilding(RT.getGrid().get(tempp), 1, RT
				.getGrid().get(tempp).name);
		allBuildings.add(bb);
		RT.attachChild(bb);
		bb.setZIndex(1);

		int temp2 = r.nextInt(UT.getGrid().size());
		Building b2 = new PropagandaCenterBuilding(UT.getGrid().get(temp2), 2,
				UT.getGrid().get(temp2).name);
		allBuildings.add(b2);
		UT.attachChild(b2);
		b2.setVisible(false);
		b2.setZIndex(1);

		int tempp2 = r.nextInt(UT.getGrid().size());
		Building bb2 = new SpyAcademyBuilding(UT.getGrid().get(tempp2), 2, UT
				.getGrid().get(tempp2).name);
		allBuildings.add(bb2);
		UT.attachChild(bb2);
		bb2.setVisible(false);
		bb2.setZIndex(1);
	}

	// initialises spies based on game settings
	public void initSpies() {
		skillRandomiser.add(1);
		skillRandomiser.add(2);
		skillRandomiser.add(3);
		skillRandomiser.add(4);
		skillRandomiser.add(5);
		allSpies = new ArrayList<Spy>();

		Collections.shuffle(skillRandomiser);
		// if both teams have the same specialists
		if (skillsMirrored) {
			for (int i = 0; i < startingSpies; i++) {
				int temp = r.nextInt(RT.getGrid().size());
				Spy s = new Spy(RT.getGrid().get(temp).getX() - 25, RT
						.getGrid().get(temp).getY(), GameScene.playerOne, 1,
						false, nameGen.getName(), RT.getGrid().get(temp).name,
						skillRandomiser.get(i), startingExperienceLevel);
				allSpies.add(s);
				RT.attachChild(s);

				int temp2 = r.nextInt(UT.getGrid().size());
				Spy sp = new Spy(UT.getGrid().get(temp2).getX() + 25, UT
						.getGrid().get(temp2).getY(), GameScene.playerTwo, 2,
						true, nameGen.getName(), UT.getGrid().get(temp2).name,
						skillRandomiser.get(i), startingExperienceLevel);
				allSpies.add(sp);
				UT.attachChild(sp);
			}

			int temp = r.nextInt(RT.getGrid().size());
			Spy s = new Spy(RT.getGrid().get(temp).getX() + 25, RT.getGrid()
					.get(temp).getY(), GameScene.playerTwo, 1, true,
					nameGen.getName(), RT.getGrid().get(temp).name,
					skillRandomiser.get(skillRandomiser.size() - 1),
					startingExperienceLevel);
			allSpies.add(s);
			RT.attachChild(s);
			int temp2 = r.nextInt(RT.getGrid().size());
			Spy sp = new Spy(UT.getGrid().get(temp2).getX() - 25, UT.getGrid()
					.get(temp2).getY(), GameScene.playerOne, 2,
					nameGen.getName(), UT.getGrid().get(temp2).name,
					skillRandomiser.get(skillRandomiser.size() - 1),
					startingExperienceLevel);
			allSpies.add(sp);
			UT.attachChild(sp);
		} else {

			@SuppressWarnings("unchecked")
			ArrayList<Integer> copy = (ArrayList<Integer>) skillRandomiser
					.clone();
			Collections.shuffle(copy);

			int spyNumCounter = 0;
			for (int i = 0; i < startingSpies; i++) {
				int temp = r.nextInt(RT.getGrid().size());
				Spy s = new Spy(RT.getGrid().get(temp).getX() - 25, RT
						.getGrid().get(temp).getY(), GameScene.playerOne, 1,
						nameGen.getName(), RT.getGrid().get(temp).name,
						skillRandomiser.get(i), startingExperienceLevel);
				allSpies.add(s);
				RT.attachChild(s);

				int temp2 = r.nextInt(UT.getGrid().size());
				Spy sp = new Spy(UT.getGrid().get(temp2).getX() + 25, UT
						.getGrid().get(temp2).getY(), GameScene.playerTwo, 2,
						true, nameGen.getName(), UT.getGrid().get(temp).name,
						copy.get(i), startingExperienceLevel);
				allSpies.add(sp);
				UT.attachChild(sp);
				spyNumCounter = i;
			}
			int temp = r.nextInt(RT.getGrid().size());
			// white
			int temp2 = r.nextInt(RT.getGrid().size() - 1);
			Spy sp = new Spy(UT.getGrid().get(temp2).getX() - 25, UT.getGrid()
					.get(temp2).getY(), GameScene.playerOne, 2,
					nameGen.getName(), UT.getGrid().get(temp).name,
					skillRandomiser.get(spyNumCounter + 1),
					startingExperienceLevel);
			allSpies.add(sp);
			UT.attachChild(sp);
			// black
			Spy s = new Spy(RT.getGrid().get(temp).getX() + 25, RT.getGrid()
					.get(temp).getY(), GameScene.playerTwo, 1, true,
					nameGen.getName(), RT.getGrid().get(temp).name,
					copy.get(spyNumCounter + 1), startingExperienceLevel);
			allSpies.add(s);
			RT.attachChild(s);
		}

		for (Spy spy : allSpies) {
			this.registerTouchArea(spy);
			spy.setVisibility(1);
			spy.setZIndex(100);
		}
	}

	//BasicScene method that sets the entities that are shown on the screen
	@Override
	public void onShowScene() {
		ResourcesManager.getInstance().mEngine.getCamera().setHUD(GameHud);
		ResourcesManager.getInstance().mEngine.setScene(this);
	}

	//when call hides everything displayed by the camera
	@Override
	public void onHideScene() {
		ResourcesManager.getInstance().mEngine.getCamera().setHUD(null);
	}

	//unloads the resources used in this scene
	@Override
	public void unloadScene() {
		// Detach and unload
		ResourcesManager.getInstance().mEngine
				.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						thisBaseScene.detachChildren();
						thisBaseScene.clearEntityModifiers();
						thisBaseScene.clearTouchAreas();
						thisBaseScene.clearUpdateHandlers();
					}
				});

	}

	//manages back key touch events
	@Override
	public void onBackKeyPressed() {
		if (spiesMenuToggled) {
			toggleSpiesHud();
		} else if (menuToggled) {
			toggleMenuHud();
		} else {
			System.exit(1);
		}
	}

	// toggles the menuHUD
	// changes between showing the menuHUD and the standard gameHUD
	public void toggleMenuHud() {
		if (menuToggled) {
			// unregister/register touch areas
			GameHud.unregisterTouchArea(returnToGameButton);
			GameHud.unregisterTouchArea(quitButton);
			GameHud.unregisterTouchArea(mainMenuButton);
			GameHud.unregisterTouchArea(optionsButton);
			GameHud.unregisterTouchArea(fadeRectangle);

			menuHudEntity.setVisible(false);
			menuToggled = false;
		} else {
			pullObj.emergencyPull();
			// unregister/register touch areas
			GameHud.registerTouchArea(returnToGameButton);
			GameHud.registerTouchArea(quitButton);
			GameHud.registerTouchArea(mainMenuButton);
			GameHud.registerTouchArea(optionsButton);
			GameHud.registerTouchArea(fadeRectangle);

			menuHudEntity.setVisible(true);
			menuToggled = true;
		}

	}

	// toggles the spies HUD
	public void toggleSpiesHud() {
		if (spiesMenuToggled) {
			while (spyListCounter != 1) {
				GameHud.unregisterTouchArea(spiesHudEntity.getLastChild());
				spiesHudEntity.detachChild(spiesHudEntity.getLastChild());
				spyListCounter--;
			}

			for (Spy spy : allSpies)
				spy.hideText();
			GameHud.unregisterTouchArea(returnToGameButtonSpy);
			GameHud.unregisterTouchArea(fadeRectangleSpyHUD);
			spiesHudEntity.setVisible(false);
			spiesMenuToggled = false;
			GameHud.detachChild(spyModelWhite);
			GameHud.detachChild(spyModelBlack);
			GameHud.detachChild(expBarEmpty);
			GameHud.detachChild(expBar1);
			GameHud.detachChild(expBar2);
			GameHud.detachChild(expBar3);
			GameHud.detachChild(expBarFull);
		} else {
			pullObj.emergencyPull();
			spyListCounter = 1;
			for (Spy spy : allSpies) {
				if (spy.player.isMyTurn()) {
					spiesHudEntity
							.attachChild(spy.getButton(
									16 + (ResourcesManager.listOfSpiesBoxTR
											.getWidth() / 2),
									480 - (80 * spyListCounter)));
					GameHud.registerTouchArea(spiesHudEntity.getLastChild());
					spyListCounter++;
				}
			}
			if (selectedSpy != null)
				setActiveSpiesHud(selectedSpy.myButton);
			GameHud.registerTouchArea(returnToGameButtonSpy);
			GameHud.registerTouchArea(fadeRectangleSpyHUD);
			spiesMenuToggled = true;

			spiesHudEntity.setVisible(true);
			GameHud.attachChild(spyModelWhite);
			GameHud.attachChild(spyModelBlack);
			GameHud.attachChild(expBarEmpty);
			GameHud.attachChild(expBar1);
			GameHud.attachChild(expBar2);
			GameHud.attachChild(expBar3);
			GameHud.attachChild(expBarFull);
		}
		// 2
	}

	// Sets the values of a selected spy inside the spies HUD
	// Changes the information displayed in the spies HUD
	public static void setActiveSpiesHud(GameButton gameButton) {
		String temp = "*****";
		skillCombatText.setText(temp.subSequence(0,
				gameButton.spy.skills.get("COMBAT")));
		skillEspionageText.setText(temp.subSequence(0,
				gameButton.spy.skills.get("ESPIONAGE")));
		skillSabotageText.setText(temp.subSequence(0,
				gameButton.spy.skills.get("SABOTAGE")));
		skillPerceptionText.setText(temp.subSequence(0,
				gameButton.spy.skills.get("PERCEPTION")));
		skillStealthText.setText(temp.subSequence(0,
				gameButton.spy.skills.get("STEALTH")));

		spyNameText.setText(gameButton.spy.name);
		expText.setText("EXP: " + gameButton.spy.expLevel + "/" + 100);
		statisticOne.setText("Kill Count: " + gameButton.spy.killCount);
		statisticTwo.setText("Intel Gathered: " + gameButton.spy.intelGathered);
		statisticThree.setText("Discoveries Made: "
				+ gameButton.spy.discoveriesMade);

		if (gameButton.spy.expLevel < 10) {
			expBarEmpty.setVisible(true);
			expBar1.setVisible(false);
			expBar2.setVisible(false);
			expBar3.setVisible(false);
			expBarFull.setVisible(false);
			unregisterPlusButtons();
		} else if (gameButton.spy.expLevel < 35) {
			expBarEmpty.setVisible(false);
			expBar1.setVisible(true);
			expBar2.setVisible(false);
			expBar3.setVisible(false);
			expBarFull.setVisible(false);
			unregisterPlusButtons();
		} else if (gameButton.spy.expLevel < 60) {
			expBarEmpty.setVisible(false);
			expBar1.setVisible(false);
			expBar2.setVisible(true);
			expBar3.setVisible(false);
			expBarFull.setVisible(false);
			unregisterPlusButtons();
		} else if (gameButton.spy.expLevel < 99) {
			expBarEmpty.setVisible(false);
			expBar1.setVisible(false);
			expBar2.setVisible(false);
			expBar3.setVisible(true);
			expBarFull.setVisible(false);
			unregisterPlusButtons();
		} else {
			expBarEmpty.setVisible(false);
			expBar1.setVisible(false);
			expBar2.setVisible(false);
			expBar3.setVisible(false);
			expBarFull.setVisible(true);
			levelUpButton.setVisible(true);
			GameHud.registerTouchArea(levelUpButton);
			levelUpFadedButton.setVisible(false);
		}

		if (gameButton.spy.playerNumber == 1) {
			if (gameButton.spy.getCurrentTileIndex() > 5){
				spyModelWhite.setCurrentTileIndex(gameButton.spy.getCurrentTileIndex()-6);
			} else {
				spyModelWhite.setCurrentTileIndex(gameButton.spy.getCurrentTileIndex());
			}
				
			spyModelBlack.setVisible(false);
			spyModelWhite.setVisible(true);

		} else {
			if (gameButton.spy.getCurrentTileIndex() > 5){
				spyModelBlack.setCurrentTileIndex(gameButton.spy.getCurrentTileIndex()-6);
			} else {
				spyModelBlack.setCurrentTileIndex(gameButton.spy.getCurrentTileIndex());
			}
			spyModelBlack.setVisible(true);
			spyModelWhite.setVisible(false);
		}

		plusButton1.setVisible(false);
		plusButton2.setVisible(false);
		plusButton3.setVisible(false);
		plusButton4.setVisible(false);
		plusButton5.setVisible(false);
	}

	// resets the spies HUD at the end of turn
	// resets the spiesHUD
	public static void emptySpiesHud() {
		skillCombatText.setText("*");
		skillEspionageText.setText("*");
		skillSabotageText.setText("*");
		skillPerceptionText.setText("*");
		skillStealthText.setText("*");

		spyNameText.setText("");
		expText.setText("");
		statisticOne.setText("Kill Count: ");
		statisticTwo.setText("Intel Gathered: ");
		statisticThree.setText("Discoveries Made: ");

		expBarEmpty.setVisible(true);
		expBar1.setVisible(false);
		expBar2.setVisible(false);
		expBar3.setVisible(false);
		expBarFull.setVisible(false);

		spyModelBlack.setVisible(false);
		spyModelWhite.setVisible(false);

		plusButton1.setVisible(false);
		plusButton2.setVisible(false);
		plusButton3.setVisible(false);
		plusButton4.setVisible(false);
		plusButton5.setVisible(false);
	}

	// closes the gamescene and exits the application
	private void quit() {
		ResourcesManager.getActivity().finish();

	}

	// closes the gamescene
	private void goMainMenu() {
		System.exit(0);
	}

	//toggles the victory screen on and off
	public static void toggleVictory(String victor) {
		pullObj.emergencyPull();
		victorName.setText(victor + "has won the game!");
		VictoryEntity.setVisible(true);
		GameHud.registerTouchArea(doneVictoryButton);
	}

	//toggles the event log screen on and off
	public void toggleEventLog() {
		if (eventLogToggled) {
			GameHud.unregisterTouchArea(eventStartTurn);
			GameHud.unregisterTouchArea(eventFadeRectangle);
			GameHud.detachChild(eventLogEntity);

			eventLogToggled = false;

		} else {
			//kills spies on the death list
			//this avoids concurrent mod error
			for(Spy s: deathList){
				if(allSpies.contains(s))
					allSpies.remove(s);
			}
			deathList.clear();
			
			GameHud.registerTouchArea(turnSwitchButton);
			turnSwitchButton.setVisible(true);
			eventFadeRectangle.setAlpha(0.95f);
			eventsText.setVisible(false);
			String temp = "";
			if (playerOne.isMyTurn()) {
				playerName.setText(playerOne.getName() + "'s turn");
				for (String s : eventLogp1)
					temp += s + "\n";
				eventLogp1.clear();
			} else {
				playerName.setText(playerTwo.getName() + "'s turn");
				for (String s : eventLogp2)
					temp += s + "\n";
				eventLogp2.clear();
			}
			
			eventsText.setText(temp);
			pullObj.emergencyPull();
			
			System.out.println("eventLogToggled");

			GameHud.attachChild(eventLogEntity);
			GameHud.registerTouchArea(eventStartTurn);
			GameHud.registerTouchArea(eventFadeRectangle);
			eventLogToggled = true;
		}
	}

	// toggles the options HUD
	private void optionsToggle() {
		if (optionsToggled) {
			GameHud.registerTouchArea(quitButton);
			GameHud.registerTouchArea(mainMenuButton);
			GameHud.registerTouchArea(optionsButton);
			GameHud.registerTouchArea(returnToGameButton);
			quitButton.setVisible(true);
			mainMenuButton.setVisible(true);
			optionsButton.setVisible(true);
			returnToGameButton.setVisible(true);

			GameHud.unregisterTouchArea(returnToMenuButton);
			optionsHudEntity.setVisible(false);
			optionsToggled = false;
		} else {
			GameHud.unregisterTouchArea(quitButton);
			GameHud.unregisterTouchArea(mainMenuButton);
			GameHud.unregisterTouchArea(optionsButton);
			GameHud.unregisterTouchArea(returnToGameButton);
			quitButton.setVisible(false);
			mainMenuButton.setVisible(false);
			optionsButton.setVisible(false);
			returnToGameButton.setVisible(false);

			GameHud.registerTouchArea(returnToMenuButton);

			optionsHudEntity.setVisible(true);

			optionsToggled = true;
		}

	}

	// called from the spy touch method. Sets the currently selected spy
	// sets argument spy to be the selected one. If already selected the spy
	// becomes unselected
	// if given null the previously selected spy will be unselected
	public static void setSelectedSpy(Spy spy) {
		if (spy != null && spy != selectedSpy) {
			if (selectedSpy != null)
				selectedSpy.setCurrentTileIndex(selectedSpy
						.getCurrentTileIndex() - 6);
			selectedSpy = spy;
			setAbilitiesVisible(true);
			spy.setCurrentTileIndex(spy.getCurrentTileIndex() + 6);

		} else {
			if (selectedSpy != null) {
				selectedSpy.setCurrentTileIndex(selectedSpy
						.getCurrentTileIndex() - 6);
			}

			selectedSpy = null;
			setAbilitiesVisible(false);
		}
	}

	// toggles ability button visibility
	// turns on and off visiblity and touchareas depending on if a spy is
	// selected or not
	private static void setAbilitiesVisible(boolean b) {
		if (b) {
			abilityButtons.setVisible(true);
			// abilityButtons.setIgnoreUpdate(false);
			GameHud.registerTouchArea(gatherIntelButton);
			GameHud.registerTouchArea(sabotageButton);
			GameHud.registerTouchArea(counterEspionageButton);
			GameHud.registerTouchArea(hideButton);
			GameHud.registerTouchArea(assassinateButton);

		} else {
			abilityButtons.setVisible(false);
			// abilityButtons.setIgnoreUpdate(true);
			GameHud.unregisterTouchArea(gatherIntelButton);
			GameHud.unregisterTouchArea(sabotageButton);
			GameHud.unregisterTouchArea(counterEspionageButton);
			GameHud.unregisterTouchArea(hideButton);
			GameHud.unregisterTouchArea(assassinateButton);
		}

	}

	// ===========================================
	// ZOOM AND TOUCH METHODS
	// ===========================================

	@Override
	public void onPinchZoomStarted(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pSceneTouchEvent) {
		ResourcesManager.getInstance();
		this.mPinchZoomStartedCameraZoomFactor = ResourcesManager.camera
				.getZoomFactor();
	}

	@Override
	public void onPinchZoom(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		ResourcesManager.getInstance();
		ResourcesManager.camera
				.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
						* pZoomFactor);
		if (ResourcesManager.camera.getZoomFactor() < 1) {
			ResourcesManager.camera.setZoomFactor(1);
		}
	}

	@Override
	public void onPinchZoomFinished(PinchZoomDetector pPinchZoomDetector,
			TouchEvent pTouchEvent, float pZoomFactor) {
		ResourcesManager.getInstance();
		ResourcesManager.camera
				.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor
						* pZoomFactor);
		if (ResourcesManager.camera.getZoomFactor() < 1) {
			ResourcesManager.camera.setZoomFactor(1);
		}
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		ResourcesManager.getInstance();
		final float zoomFactor = ResourcesManager.camera.getZoomFactor();
		ResourcesManager.getInstance();
		ResourcesManager.camera.offsetCenter(-pDistanceX / zoomFactor,
				pDistanceY / zoomFactor);

	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		ResourcesManager.getInstance();
		final float zoomFactor = ResourcesManager.camera.getZoomFactor();
		ResourcesManager.getInstance();
		ResourcesManager.camera.offsetCenter(-pDistanceX / zoomFactor,
				pDistanceY / zoomFactor);

	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID,
			float pDistanceX, float pDistanceY) {
		ResourcesManager.getInstance();
		final float zoomFactor = ResourcesManager.camera.getZoomFactor();
		ResourcesManager.getInstance();
		ResourcesManager.camera.offsetCenter(-pDistanceX / zoomFactor,
				pDistanceY / zoomFactor);

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if (this.mPinchZoomDetector.isZooming()) {
			this.mScrollDetector.setEnabled(false);
		} else {
			if (pSceneTouchEvent.isActionDown()) {
				this.mScrollDetector.setEnabled(true);
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}
		return true;
	}

}
