package com.pz.tagandfrag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

public class AfterGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_game);
		resetGame();
		loadSummaryFromServer();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Czyszczenie parametr�w gry po zako�czeniu rozgrywki
	 * */
	private void resetGame() {
		//Reset kodu broni
		DataManager.player.setHealthPoints(100);
		DataManager.player.setTeam(0);
		DataManager.players.clear();
		DataManager.teamList.clear();
		DataManager.preferences.setTeam(0);
		DataManager.preferences.saveAllDataToPreferences();
	}
	/**
	 * Pobiera podsumowanie gry z serwera
	 * */
	private void loadSummaryFromServer() {
		WebView gameSummary = (WebView) findViewById(R.id.game_summary_full);
		gameSummary.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		            Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
		    }
		 });
		gameSummary.loadUrl(DataManager.serverAddress);
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	
	//Doda� wy�wietlanie podsumowania gry
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za rozpocz�cie
	 * kolejnej rozgrywki
	 */
	public void onResetGameButtonClicked(View view) {
		Intent intent = new Intent(AfterGameActivity.this, ChooseTeamActivity.class);
		startActivity(intent);
	}
}
