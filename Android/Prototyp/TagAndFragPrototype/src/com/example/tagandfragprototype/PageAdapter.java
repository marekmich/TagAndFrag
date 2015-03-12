package com.example.tagandfragprototype;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

	private final static int NUMBER_OF_PAGES = 3;
	private MapFragment mapFragment;
	private ListFragment listFragment;
	private LogFragment logFragment;
	
	public PageAdapter(FragmentManager fm) {
		super(fm);
		mapFragment = new MapFragment();
		listFragment = new ListFragment();
		logFragment = new LogFragment();
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return logFragment;
		case 1:
			return listFragment;
		case 2:
			return mapFragment;
		default:
			break;
		}
		return null;
	}

	@Override
	public int getCount() {
		return NUMBER_OF_PAGES;
	}

}
