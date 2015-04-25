package com.spygame.menu;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.spygame.R;

public class MenuMain extends FragmentActivity implements
		ActionBar.TabListener {

	ViewPager mViewPager;
	TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	private String[] tabs = { "Menu", "Friends", "Active Games" };
	 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//Initialisation
		mViewPager = (ViewPager) findViewById(R.id.pager);
		final ActionBar actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		
		mViewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		//adding tabs
		for(String tab_name:tabs){
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
			
			@Override
			public void onPageSelected(int position){
				actionBar.setSelectedNavigationItem(position);
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2){
			}
			
	          @Override
	            public void onPageScrollStateChanged(int arg0) {
	            }
		});
	}
			
    public void goGame(View v){
		Intent i = new Intent(this, com.spygame.game.Main.class);  
		startActivity(i);
    }
    
    public void goMPGame(View v){
		Intent i = new Intent(this, com.spygame.managers.MultiplayerManager.class);  
		startActivity(i);
    }
    
    public void quitApp(View v){
    	System.exit(1);
    	//finish();
    }
    
    public void goOptions(View v){
		Intent i = new Intent(this, com.spygame.menu.Options.class);  
		startActivity(i);
    }
    
    public void goAbout(View v){
		Intent i = new Intent(this, com.spygame.menu.About.class);  
		startActivity(i);
    }
    
    public void goTutorial(View v){
		Intent i = new Intent(this, com.spygame.menu.Tutorial.class);  
		startActivity(i);
    }
    
    public void goCustomGame(View v){
		Intent i = new Intent(this, com.spygame.menu.NewGame.class);  
		startActivity(i);
    }
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
}

