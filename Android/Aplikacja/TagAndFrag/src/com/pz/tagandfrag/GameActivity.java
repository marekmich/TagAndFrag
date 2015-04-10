package com.pz.tagandfrag;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class GameActivity extends FragmentActivity {

	public static FragmentManager FRAGMENT_MANAGER;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game);
		FRAGMENT_MANAGER = getFragmentManager();
		ViewPager viewPager = (ViewPager) findViewById(R.id.game_activity);
		PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
		viewPager.setOffscreenPageLimit(PageAdapter.NUMBER_OF_PAGES - 1);
		viewPager.setAdapter(pageAdapter);
	}
	
}
