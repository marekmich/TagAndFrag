package com.pz.tagandfrag.activity;

import com.pz.tagandfrag.fragments.MapFragment;
import com.pz.tagandfrag.fragments.TeamFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * PageAdapter zawieraj¹cy strony do MapFragment i TeamFragment.
 * Strony te s¹ zawierane we ViewPager aktywnoœci rozgrywki.
 * @author Mateusz Wrzos
 * @see MapFragment
 * @see TeamFragment
 *
 */
public class PageAdapter extends FragmentPagerAdapter {

	/**
	 * Statyczne pole zawieraj¹ce iloœæ stron.
	 */
	public final static int NUMBER_OF_PAGES = 2;
	private MapFragment mapFragment;
	private TeamFragment teamFragment;
	
	public PageAdapter(FragmentManager fm) {
		super(fm);
		mapFragment = new MapFragment();
		teamFragment = new TeamFragment();
	}

	/**
	 * Zwraca odpowiadaj¹cy fragment.
	 */
	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {
		case 0:
			return teamFragment;
		case 1:
			return mapFragment;
		default:
			break;
		}
		return null;
	}

	/**
	 * Zwraca liczbê stron.
	 */
	@Override
	public int getCount() {
		return NUMBER_OF_PAGES;
	}

}
