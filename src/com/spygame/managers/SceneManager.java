package com.spygame.managers;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;

import com.spygame.game.BasicScene;
import com.spygame.game.GameScene;

import android.app.Activity;

public class SceneManager extends Activity {

	//
	//This class is heavily based on the tutorials in AndEngine for Android Game Development Cookbook 
	//by Brian Boyles and Jayme Schroeder
	//
	
	
	// ====================================================
	// CONSTRUCTOR AND INSTANCE GETTER
	// ====================================================
	// Global SceneManager instance
	public static final SceneManager INSTANCE = new SceneManager();

	//Convenience constructor
	public SceneManager() {
	}

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	// ========================================================
	// VARIABLES
	// ========================================================
	// Used when switching scenes
	public BasicScene mCurrentScene;
	public BasicScene mNextScene;
	// Used by loadingSCreen handler to ensure loading screen is shown correctly
	private int mNumFramesPassed = -1;
	// ensures it doesn't get registered twice
	private boolean mLoadingScreenRegistered = false;

	// an Update handler that shows the loading screen of the next scene before
	// calling its load method
	private IUpdateHandler mLoadingScreenHandler = new IUpdateHandler() {
		@Override
		public void onUpdate(float pSecondsElapsed) {
			// increment frames and time passed
			mNumFramesPassed++;
			//increment elapsed time
			mNextScene.elapsedLoadingScreenTime += pSecondsElapsed;

			// After first frame after loadingSCreen has been visible
			if (mNumFramesPassed == 1) {
				// hide and unload previous screen
				if (mCurrentScene != null) {
					mCurrentScene.onHideBaseScene();
					mCurrentScene.unloadBaseScene();
				}
				mNextScene.loadBaseScene();
			}
			// on first frame and loading screen has been shown for min time
			if (mNumFramesPassed > 1
					&& mNextScene.elapsedLoadingScreenTime >= mNextScene.minLoadingScreenTime) {
				// removes loadingScreen
				mNextScene.clearChildScene();
				// tells Next scene to unload loading scene
				mNextScene.onLoadingScreenLoadAndShown();
				// show new scene
				mNextScene.onShowBaseScene();
				// sets next scene to current scene
				mCurrentScene = mNextScene;
				// reset handler
				mNextScene.elapsedLoadingScreenTime = 0f;
				mNumFramesPassed = -1;
				ResourcesManager.getInstance().mEngine.unregisterUpdateHandler(this);
				mLoadingScreenRegistered = false;
			}
		}

		@Override
		public void reset() {
		}
	};

	//set to true in showLayer of camera had HUD before the layer was shown
	private boolean mCameraHadHud = false;
	public boolean isLayerShown = false;
	private Scene mPlaceHolderModalScene;

	// ========================================================
	// PUBLIC METHODS
	// ========================================================

	//The process of switching from current scene to a new scene
	public void setScene(final BasicScene pScene) {
		// Reset the camera
		ResourcesManager.getInstance().mEngine.getCamera().set(0f, 0f,
				ResourcesManager.getInstance().cameraWidth,
				ResourcesManager.getInstance().cameraHeight);
		// if the scene has a loading screen
		if (pScene.hasLoadingScreen) {
			// set loading screen as a child to the scene
			pScene.setChildScene(pScene.onLoadingScreenLoadAndShown(), true,
					true, true);
			// ensures loading screen update handler is only registered if
			// necessary
			if (mLoadingScreenRegistered) {
				mNumFramesPassed = -1;
				mNextScene.clearChildScene();
				mNextScene.onLoadingScreenUnloadAndHidden();
			} else {
				ResourcesManager.getInstance().mEngine.registerUpdateHandler(mLoadingScreenHandler);
				mLoadingScreenRegistered = true;
			}
			// set nextScene to the pScene
			mNextScene = pScene;
			// set scene as engines scene
			ResourcesManager.getInstance().mEngine.setScene(pScene);
			return;
		}
		// if the scene does not have a loading screen
		// set nextScene to scene and apply it
		mNextScene = pScene;
		ResourcesManager.getInstance().mEngine.setScene(pScene);
		// hide old scene
		if (mCurrentScene != null) {
			mCurrentScene.onHideBaseScene();
			mCurrentScene.unloadBaseScene();
		}
		// load and show new scene
		mNextScene.loadBaseScene();
		mNextScene.onShowBaseScene();
		mCurrentScene = mNextScene;
	}

	public void showGame(){
		setScene(new GameScene());
	}
}
