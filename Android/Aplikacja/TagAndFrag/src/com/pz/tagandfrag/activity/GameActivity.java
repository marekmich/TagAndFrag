package com.pz.tagandfrag.activity;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.fragments.MapFragment;
import com.pz.tagandfrag.fragments.TeamFragment;

/**
 * Aktywnoœæ z rozgrywk¹.
 * Hostuje ona 2 fragmenty sk³adaj¹ce siê na rozgrywkê.
 * @see TeamFragment
 * @see MapFragment
 * @author Mateusz Wrzos
 *
 */
public class GameActivity extends FragmentActivity {

	/**
	 * Statyczne pole z FragmentManager'em dla tej aktywnoœci.
	 */
	public static FragmentManager FRAGMENT_MANAGER;

	/**
	 * Ustawia pole FRAGMENT_MANAGER, oraz PageAdapter i ustawia go na tej aktywnoœci.
	 * 
	 * @see PageAdapter
	 * @see ViewPager
	 */
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

	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.back_pressed_info), Toast.LENGTH_LONG).show();
	}
}
