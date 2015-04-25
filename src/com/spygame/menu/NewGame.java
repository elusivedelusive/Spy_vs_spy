package com.spygame.menu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.drive.internal.r;
import com.spygame.R;

public class NewGame extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game);
		
	}

	public void goCustomGame(View v){
		Intent i = new Intent(this, com.spygame.game.Main.class);  
		startActivity(i);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
}
