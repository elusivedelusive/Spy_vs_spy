package com.spygame.game.entities;

import java.util.ArrayList;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.spygame.managers.ResourcesManager;

import android.graphics.Color;

public abstract class Continent extends Sprite implements IUpdateHandler {

	// ====================================================
	// VARIABLES AND OBJECTS
	// ====================================================

	// up position
	float mTargetCentralPosX;
	float mTargetCentralPosY;
	// down position
	float mTargetNotCentralPosX;
	float mTargetNotCentralPosY;

	public boolean isCentral;

/*	private Vector2 swipeTouchDown;
	private Vector2 swipeTouchUp;*/

	public ArrayList<GridSquare> grid;
	Line drawGrid;
	ArrayList<Spy> spies;

	public Continent(float xpos, float ypos, TextureRegion tr) {
		super(xpos, ypos, tr, ResourcesManager.getInstance().mEngine
				.getVertexBufferObjectManager());

		// Grids

		spies = new ArrayList<Spy>();
	}

	// Map sizes: normal and big
	// Create grid
	public ArrayList<GridSquare> buildRectGrid(int pWidth, int pHeight,
			Continent cont, Continent cont2, String continentPrefix) {
		ArrayList<GridSquare> grid = new ArrayList<GridSquare>();
		boolean mapSize = ResourcesManager.SETTINGS_MAP_SIZE;
		int squareName = 1;
		float posX = 105;
		float posY = 105;
		
		int sizeX, sizeY, squareNumberX, squareNumberY;
		if (!mapSize) {
			sizeY = 105;
			sizeX = 96;
			squareNumberX = 7;
			squareNumberY = 4;
		} else {
			sizeY = 84;
			sizeX = 84;
			squareNumberX = 8;
			squareNumberY = 5;
		}
		// big map 84
		for (int i = 0; i < squareNumberY; i++) {
			posY = 105;
			posY += (i * sizeY);
			for (int j = 0; j < squareNumberX; j++) {
				posX = 105;
				posX += (j * sizeX);
				System.out.println("posX: " + posX + " posY: " + posY);
				grid.add(new GridSquare(posX, posY, 84, 84, cont, cont2, continentPrefix + squareName ));
				squareName++;
			}
		}
		return grid;
	}

	public static Line buildGrid(int pWidth, int pHeight) {
		Line grid = new Line(0, 0, 0, pHeight,
				ResourcesManager.getInstance().mEngine
						.getVertexBufferObjectManager());
		
		int xValue, yValue;
		String mapSize = "small";
		if (mapSize == "small") {
			xValue = 96;
			yValue = 105;
		} else {
			xValue = 84;
			yValue = 84;
		}

		// vertical
		int cont = 63;

		while (cont < pWidth + 10) {
			grid.attachChild(new Line(cont, 63, cont, pHeight, ResourcesManager
					.getInstance().mEngine.getVertexBufferObjectManager()));
			grid.getLastChild().setColor(Color.BLACK);
			cont += xValue;
		}

		// horizontal
		cont = 63;
		while (cont < pHeight) {

			grid.attachChild(new Line(63, cont, pWidth, cont, ResourcesManager
					.getInstance().mEngine.getVertexBufferObjectManager()));
			grid.getLastChild().setColor(Color.BLACK);
			cont += yValue;
		}

		return grid;
	}

	public ArrayList<GridSquare> getGrid() {
		return grid;
	}

	public ArrayList<Spy> getSpies() {
		return spies;
	}

	public boolean getIsCentral() {
		return isCentral;
	}

	public boolean detachSpy(Spy spy) {
		for (Spy s : spies) {
			if (spy == s) {
				this.detachChild(s);
				return true;
			}
		}
		return false;
	}

	public void addSpy(Spy spy) {
		spies.add(spy);
		this.attachChild(spy);
	}

	public abstract boolean isRussia();
	public abstract void swipe();
}
