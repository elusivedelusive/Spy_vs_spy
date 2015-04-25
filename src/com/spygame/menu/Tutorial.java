package com.spygame.menu;

import android.app.Activity;
import android.os.Bundle;

import com.spygame.R;

public class Tutorial  extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		
	}

	@Override
	public void onBackPressed(){
		finish();
	}

}
