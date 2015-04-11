package com.pz.tagandfrag.activity;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
		DataManager.player.setHealthPoints(100);
		DataManager.player.setTeam(0);
		DataManager.players.clear();
		DataManager.teamList.clear();
		DataManager.preferences.setTeam(0);
		DataManager.preferences.saveAllDataToPreferences();
	}
	
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	
	//Dodaæ wyœwietlanie podsumowania gry
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za rozpoczêcie
	 * kolejnej rozgrywki
	 */
	public void onResetGameButtonClicked(View view) {
		Intent intent = new Intent(AfterGameActivity.this, ChooseTeamActivity.class);
		startActivity(intent);
	}
}
