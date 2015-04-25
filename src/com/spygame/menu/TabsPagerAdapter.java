package com.spygame.menu;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter{

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		
	}
	
	//returns a fragment
	@Override
	public Fragment getItem(int index){
		
		switch(index){
		case 0:
			//menu
			return new MenuFragment();
		case 1:
			//friends
			return new FriendsFragment();
		case 2:
			//active games
			return new GamesFragment();
		}
		
		return null;
	}
	
	@Override
	public int getCount(){
		//number of tabs
		return 3;
	}

}
