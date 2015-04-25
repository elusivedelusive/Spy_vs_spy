package com.spygame.game;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.BaseGameActivity;

import com.spygame.R;
import com.spygame.managers.ResourcesManager;
import com.spygame.managers.SceneManager;

import android.view.KeyEvent;
import android.widget.RadioGroup;

public class Main extends BaseGameActivity {

	private static final Main INSTANCE = new Main();
	// variables
	private static ZoomCamera mCamera;
	private ResourcesManager resourcesManager;
	private static SceneManager sceneManager = new SceneManager();
	
	public static Main getInstance() {
		return INSTANCE;
	}
	// =============================================
	// CONSTANTS
	// =============================================

	static float WINDOW_HEIGHT_RES = 480;
	static float WINDOW_WIDTH_RES = 800;

	// ====================================================
	// ENGINE
	// ====================================================

	@Override
	public EngineOptions onCreateEngineOptions() {

		// sets size of game canvas
		mCamera = new ZoomCamera(0, 0, WINDOW_WIDTH_RES, WINDOW_HEIGHT_RES);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						WINDOW_WIDTH_RES, WINDOW_HEIGHT_RES), Main.mCamera);
		mCamera.setBoundsEnabled(true);
		mCamera.setBounds(0, 0, WINDOW_WIDTH_RES, WINDOW_HEIGHT_RES);
		
		engineOptions.getRenderOptions().setDithering(true);
		// Turn on MultiSampling to smooth the alias of hard-edge elements.
		engineOptions.getRenderOptions().getConfigChooserOptions()
				.setRequestedMultiSampling(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	// ensures the game runs at 25fps
	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 15);
	}

	// ====================================================
	// Resources
	// ====================================================
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {

		// Construct resourcesManager
		ResourcesManager.prepareManager(mEngine, this, mCamera,
				getVertexBufferObjectManager());
		resourcesManager = ResourcesManager.getInstance();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	// ====================================================
	// Scene
	// ====================================================
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {

		sceneManager.showGame();
		mEngine.registerUpdateHandler(new FPSLogger());
		
		pOnCreateSceneCallback.onCreateSceneFinished(null);
	}

	// ====================================================
	// Populate scene
	// ====================================================
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {

		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	// ====================================================
	// LIFECYCLE
	// ====================================================

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected synchronized void onResume() {
		super.onResume();
		// garbage collection
		System.gc();
	}

	// Ensures app is properly closed
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	// =============================================
	// BACK BUTTON
	// =============================================
	@Override
	public void onBackPressed() {
		// if resourcesmanager is set up
		if (ResourcesManager.getInstance().mEngine != null) {
		} else {
			System.exit(0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
		}
		return false;
	}
}
