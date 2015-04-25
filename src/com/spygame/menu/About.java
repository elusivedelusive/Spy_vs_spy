package com.spygame.menu;

import com.spygame.R;

import android.app.Activity;
import android.os.Bundle;

public class About extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
	}

	@Override
	public void onBackPressed(){
		finish();
	}

}
