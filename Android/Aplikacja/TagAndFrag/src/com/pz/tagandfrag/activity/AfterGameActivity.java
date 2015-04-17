package com.pz.tagandfrag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;


/**
 * Aktywnoœæ odpowiedzialna za wyœwietlanie i obs³ugê ekranu zakoñczenia gry.
 * @author £ukasz ¯urawski
 */
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
	 * Czyszczenie parametrów gry po zakoñczeniu rozgrywki
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
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.back_pressed_info), Toast.LENGTH_LONG).show();
	}
}
