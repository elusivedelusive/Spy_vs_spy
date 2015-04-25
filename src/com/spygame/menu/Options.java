package com.spygame.menu;

import com.spygame.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Options extends PreferenceActivity{
	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.options);
		
	}
	
	private void displaySharedPreferences(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Options.this);
				
		boolean checkBoxRegionNames = prefs.getBoolean("pref_display_region_names_key", true);
		boolean checkBoxSpyNames = prefs.getBoolean("pref_display_spy_names_key", true);

	}

	@Override
	public void onBackPressed(){
		finish();
	}
}
