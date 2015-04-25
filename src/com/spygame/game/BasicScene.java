package com.spygame.game;

//
//This class is heavily based on the tutorials in AndEngine for Android Game Development Cookbook 
//by Brian Boyles and Jayme Schroeder
//
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.spygame.managers.ResourcesManager;

import android.app.Activity;

public abstract class BasicScene extends Scene {

	// Needs loadingScreen or not
	public final boolean hasLoadingScreen;
	// minimum time to display loading screen
	public final float minLoadingScreenTime;
	// how long loadingSCreen has been visible
	public float elapsedLoadingScreenTime = 0f;
	// true if loaded
	public boolean isLoaded = false;
	//disables loadScreen
	public boolean willDoLoadingScreen = true;

	// variables
	protected Engine mEngine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected VertexBufferObjectManager vbom;
	protected Camera mCamera;

	//Constructor that disables loading screen
	public BasicScene() {
		this(0f);
	}

	// Constructor. Sets loading screen time and bool hasLoadingScreen
	public BasicScene(final float loadingScreenMinTimeShown) {
		minLoadingScreenTime = loadingScreenMinTimeShown;
		hasLoadingScreen = (minLoadingScreenTime > 0f);
		setVarious();
	}

	// Sets the variables the scenes need
	public void setVarious() {
		this.resourcesManager = ResourcesManager.getInstance();
		this.mEngine = resourcesManager.mEngine;
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.mCamera = ResourcesManager.camera;
	}

	// Called by the SceneManager calls loadScene if necessary
	public void loadBaseScene() {
		if (!isLoaded) {
			onLoadScene();
			if(willDoLoadingScreen)
			{
				isLoaded = true;
				this.setIgnoreUpdate(true);
			}

		}
	}

	// Called by the SceneManager calls unloadScene if necessary
	public void unloadBaseScene() {
		if (isLoaded) {
			isLoaded = false;
			unloadScene();
		}
	}

	// unpauses screen before showing it
	public void onShowBaseScene() {
		this.setIgnoreUpdate(false);
		onShowScene();
	}

	// Pauses scene before hiding it
	public void onHideBaseScene() {
		this.setIgnoreUpdate(true);
		onHideScene();
	}

	// abstraction
	public abstract void onBackKeyPressed();

	public abstract Scene onLoadingScreenLoadAndShown();

	public abstract void onLoadingScreenUnloadAndHidden();

	public abstract void onLoadScene();

	public abstract void onShowScene();

	public abstract void onHideScene();

	public abstract void unloadScene();

}
