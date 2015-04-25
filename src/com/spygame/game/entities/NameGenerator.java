package com.spygame.game.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import com.spygame.R;
import com.spygame.managers.ResourcesManager;

public class NameGenerator {
	public static ArrayList<String> codenames = new ArrayList<String>();
	public String[] adapter;
	Random r;
	public static ArrayList<String> usedNames = new ArrayList<String>();
	
	//gets the array of names stored in the XML file strings
	public NameGenerator (){
		adapter = ResourcesManager.getActivity().getBaseContext().getResources().getStringArray(R.array.codenameLetters);
		
		codenames.addAll(Arrays.asList(adapter));
		adapter = ResourcesManager.getActivity().getBaseContext().getResources().getStringArray(R.array.codenames);
		codenames.addAll(Arrays.asList(adapter));
		r = new Random();
	}
	
	//returns a unique name from the list
	public String getName(){
		while(true){
			String name = codenames.get(r.nextInt(codenames.size()));
			if(!usedNames.contains(name)){
				usedNames.add(name);
				return "Agent " + name;
			}
		}
		
	}
}
