package com.pz.tagandfrag;

import android.app.Activity;
import android.os.Bundle;

public class AfterGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_game);
		resetGame();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	private void resetGame() {
		//Reset kodu broni
		TagAndFragContainer.player.setHealthPoints(100);
		TagAndFragContainer.player.setTeam(0);
		TagAndFragContainer.players.clear();
		TagAndFragContainer.teamList.clear();
		TagAndFragContainer.players.clear();
		TagAndFragContainer.preferences.setTeam(0);
		TagAndFragContainer.preferences.saveAllDataToPreferences();
	}
	
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	
	/////////////////////////////////
	/* Listenery */
	
}
