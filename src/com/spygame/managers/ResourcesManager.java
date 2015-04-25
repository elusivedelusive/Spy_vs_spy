package com.spygame.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.google.android.gms.internal.bt;
import com.google.android.gms.internal.hd;
import com.spygame.R;
import com.spygame.game.Main;

import android.app.Activity;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ResourcesManager {

	//false = normal size
	public static boolean SETTINGS_MAP_SIZE = false;
	public static boolean SETTINGS_MIRRORED_SPIES = true;
	public static int SETTINGS_STARTING_SPIES = 4;
	public static int SETTINGS_STARTING_EXPERIENCE_LEVEL = 3;
	
	public static boolean DISPLAY_SPY_NAMES = true;
	
	//variables
	private static final ResourcesManager INSTANCE = new ResourcesManager();
	
	public Context context;
	public Engine mEngine;
	public static Activity activity;
	public VertexBufferObjectManager vbom;
	public static ZoomCamera camera;
	public static float cameraWidth;
	public static float cameraHeight;
	public float cameraScaleFactorX = 1;
	public float cameraScaleFactorY = 1;
	
	public static Font spyNameFont;
	public static Font intelLevelFont;
	public static Font statisticsFont;
	public static Font loadingScreenFont;
	public static Font statisticsStarFont;
	public static Font pausedFont;
	
	static public TextureRegion russiaTR;
	static public TextureRegion usaTR;
	static public TextureRegion topMenuTR;
	static public TextureRegion leftArrowTR;
	static public TextureRegion pullDownTR;
	static public TextureRegion backgroundFixTR;
		
	//spies
	static public ITiledTextureRegion whiteSpyTR;
	static public ITiledTextureRegion blackSpyTR;
	static public ITiledTextureRegion whiteSpyHDTR;
	static public ITiledTextureRegion blackSpyHDTR;
	
	//Exp bar
	static public TextureRegion expBarEmptyTR;
	static public TextureRegion expBar1TR;
	static public TextureRegion expBar2TR;
	static public TextureRegion expBar3TR;
	static public TextureRegion expBarFullTR;
	
	static public TextureRegion pullMenuTR;
	static public TextureRegion transportBoxTR;
	static public TextureRegion endTurnBoxTR;
	static public TextureRegion scrollBoxTR;
	static public TextureRegion menuBoxTR;
	static public TextureRegion pauseMenuResumeBoxTR;
	static public TextureRegion pauseOptionsBoxTR;
	static public TextureRegion pauseMainMenuBoxTR;
	static public TextureRegion pauseQuitBoxTR;
	static public TextureRegion bottomMenuTR;
	static public TextureRegion spiesButtonTR;
	
	//buildings
	static public TextureRegion building_propaganda;
	static public TextureRegion building_academy;
	
	static public TextureRegion intelButtonTR;
	static public TextureRegion counterSpyButtonTR;
	static public TextureRegion sabotageButtonTR;
	static public TextureRegion assassinateButtonTR;
	static public TextureRegion hideButtonTR;
	static public TextureRegion listOfSpieTR;
	static public TextureRegion listOfSpiesBoxTR;
	static public TextureRegion listOfStatisticsTR;
	static public TextureRegion listOfSkillsTR;
	static public TextureRegion spyModelBoxTR;
	static public TextureRegion spyHudResumeButtonTR;
	static public TextureRegion plussButtonTR;
	static public TextureRegion levelUpButtonTR;
	static public TextureRegion levelUpFadedButtonTR;
	
	public static void prepareManager(Engine mEngine, Main gameLoop,
			ZoomCamera mCamera, VertexBufferObjectManager vbom) {
		
		getInstance().mEngine = mEngine;
		getInstance().activity = gameLoop;
		getInstance();
		ResourcesManager.camera = mCamera;
		getInstance().vbom = vbom;
		cameraWidth = mCamera.getCameraSceneWidth();
		cameraHeight = mCamera.getCameraSceneHeight();
		}

	public void loadGameResources(){
		loadGameGraphics();
		loadGameFonts();
	}
	
	public void loadGameFonts() {
		spyNameFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 15, true, android.graphics.Color.BLACK);
		spyNameFont.load();
		intelLevelFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 20, true, android.graphics.Color.BLACK);
		intelLevelFont.load();
		statisticsFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 25, true, android.graphics.Color.WHITE);
		statisticsFont.load();
		statisticsStarFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 35, true, android.graphics.Color.WHITE);
		statisticsStarFont.load();
		loadingScreenFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 40, true, android.graphics.Color.WHITE);
		loadingScreenFont.load();		
		pausedFont = FontFactory.createFromAsset(mEngine.getFontManager(), mEngine.getTextureManager(), 256, 256 ,getActivity().getAssets(), "font/typewriter.ttf", 60, true, android.graphics.Color.WHITE);
		spyNameFont.load();
		
	}

	public void loadGameGraphics(){
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		final BuildableBitmapTextureAtlas usaBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas russiaBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas smallBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas tinyBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 128, 128,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas menuBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas transportBoxBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 128, 128,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas testBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 300, 300,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas scrollBoxBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 512, 512,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas spyBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas pauseMenuBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas bottomMenuBitmapTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas spyHudTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas expBarTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		final BuildableBitmapTextureAtlas BGFixTextureAtlas = new BuildableBitmapTextureAtlas(mEngine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);
		//regions
		russiaTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(russiaBitmapTextureAtlas, activity, "russia_pastel.png");
		usaTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(usaBitmapTextureAtlas, activity, "usa_pastel.png");
		backgroundFixTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(BGFixTextureAtlas, activity, "background_pastel.png");
		
		//spies
		whiteSpyTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spyBitmapTextureAtlas, activity, "white_spy_tiledSheetSelected.png", 3, 4);
		blackSpyTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spyBitmapTextureAtlas, activity, "black_spy_tiledsheetSelected.png", 3, 4);
		whiteSpyHDTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spyBitmapTextureAtlas, activity, "white_spy_tiledSheet_HD.png", 3, 2);
		blackSpyHDTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(spyBitmapTextureAtlas, activity, "black_spy_tiledsheet_HD.png", 3, 2);
		//
		//Menu objects
		leftArrowTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tinyBitmapTextureAtlas, activity, "left_arrowSmall.png");
		transportBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(transportBoxBitmapTextureAtlas, activity, "transportBox.png");
		endTurnBoxTR =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(testBitmapTextureAtlas, activity, "endturnbox.png");
		menuBoxTR =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(testBitmapTextureAtlas, activity, "menubox.png");
		pullDownTR= BitmapTextureAtlasTextureRegionFactory.createFromAsset(smallBitmapTextureAtlas, activity, "pullDown.png");
		pullMenuTR= BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuBitmapTextureAtlas, activity, "pullMenu.png");
		
		//bottomMenuTR= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "bottomMenu.png");
		scrollBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(scrollBoxBitmapTextureAtlas, activity, "scrollBox.png");
		
		plussButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(expBarTextureAtlas, activity, "plussButton.png");
		levelUpButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(expBarTextureAtlas, activity, "levelUpButton.png");
		levelUpFadedButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(expBarTextureAtlas, activity, "levelUpButtonFaded.png");
		
		//test
		spiesButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "spiesButton2.png");
		intelButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "gatherIntelButton.png");
		counterSpyButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "counterSpyButton.png");
		assassinateButtonTR= BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "assassinateButton.png");
		hideButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "hideButton.png");
		sabotageButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "sabotageButton.png"); 
		
		//buildings
		building_propaganda =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "building_propaganda.png");
		building_academy =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(bottomMenuBitmapTextureAtlas, activity, "building_academy.png");
		//pause menu
		pauseMainMenuBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pauseMenuBitmapTextureAtlas, activity, "mainMenuBox.png");
		pauseMenuResumeBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pauseMenuBitmapTextureAtlas, activity, "resumeBox.png");
		pauseOptionsBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pauseMenuBitmapTextureAtlas, activity, "optionsBox.png");
		pauseQuitBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(pauseMenuBitmapTextureAtlas, activity, "quitBox.png");
		
		//spy HUD
		listOfSpiesBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "spyBox.png");
		listOfSpieTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "listOfSpies.png");
		listOfStatisticsTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "listOfStatistics.png");
		listOfSkillsTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "listOfSkills.png");
		spyModelBoxTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "spyModelBox.png");
		spyHudResumeButtonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "spyHudResumeButton.png"); 
		
		//exp bar
		expBarEmptyTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "expBarEmpty.png");
		expBar1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "expBar1.png");
		expBar2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "expBar2.png");
		expBar3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "expBar3.png");
		expBarFullTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(spyHudTextureAtlas, activity, "expBarFull.png");
		
		//DONT TOUCH
		try {
			usaBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			smallBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			tinyBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			menuBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			testBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			transportBoxBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			scrollBoxBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			spyBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			pauseMenuBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			bottomMenuBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			russiaBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			spyHudTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			expBarTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));
			BGFixTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 1));

		} catch (TextureAtlasBuilderException e) {
				e.printStackTrace();
			}

		expBarTextureAtlas.load();
		usaBitmapTextureAtlas.load();	
		smallBitmapTextureAtlas.load();
		tinyBitmapTextureAtlas.load();
		menuBitmapTextureAtlas.load();
		testBitmapTextureAtlas.load();
		transportBoxBitmapTextureAtlas.load();
		scrollBoxBitmapTextureAtlas.load();
		spyBitmapTextureAtlas.load();
		pauseMenuBitmapTextureAtlas.load();
		bottomMenuBitmapTextureAtlas.load();
		russiaBitmapTextureAtlas.load();
		spyHudTextureAtlas.load();
		BGFixTextureAtlas.load();
	}
	
	public void unloadGameTextures(){
		
	}
	
	public static ResourcesManager getInstance(){
		return INSTANCE;
	}
	public static Activity getActivity(){
		return getInstance().activity;
	}


}
