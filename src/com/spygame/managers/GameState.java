package com.spygame.managers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.spygame.game.GameScene;
import com.spygame.game.entities.Spy;
import com.spygame.game.entities.buildings.Building;

import android.util.Log;

public class GameState {

	public static JSONArray allSpies;
	public static JSONArray allBuildings;
	public static JSONArray allContinents;
	public static final String TAG = "EBTurn";

	public GameState() {
	}

	// Creates a JSON byte[] file
	public byte[] persist() {
		JSONObject returnValue = new JSONObject();
		JSONArray JSONSpies = new JSONArray(GameScene.getInstance().allSpies); 
		JSONArray JSONBuildings = new JSONArray(GameScene.getInstance().allBuildings);
		JSONArray JSONContinents = new JSONArray(GameScene.getInstance().allContinents);
		try {
			returnValue.put("allSpies", JSONSpies);
			returnValue.put("allBuildings", JSONBuildings);
			returnValue.put("allContinents", JSONContinents);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String stringTemp = returnValue.toString();

		Log.d(TAG, "===PERSISTING\n" + stringTemp);

		return stringTemp.getBytes(Charset.forName("UTF-16"));
	}

	// interprets a JSON byte[] file
	static public GameState unpersist(byte[] byteArray){
		
		if(byteArray == null){
			Log.d(TAG, "Empty array---possible bug.");
			return new GameState();
		}
		String stringTemp = null;
		try{
			stringTemp = new String(byteArray, "UTF-16");
		} catch (UnsupportedEncodingException e1){
			e1.printStackTrace();
			return null;
		}
		
		Log.d(TAG, "===UNPERSIST \n" + stringTemp);
		
		GameState returnState = new GameState();
		
		try{
			JSONObject object = new JSONObject(stringTemp);
			
			if(object.has("allBuildings")){
				returnState.allBuildings = object.getJSONArray("allBuildings");
			}
			if(object.has("allSpies")){
				returnState.allSpies = object.getJSONArray("allSpies");
			}
			if(object.has("allContinents")){
				returnState.allContinents = object.getJSONArray("allContinents");
			}

		} catch (JSONException e){
			e.printStackTrace();
		}
		return returnState;
	}
}
