package com.pz.tagandfrag.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;


/**
 * Aktywno�� odpowiedzialna za wy�wietlanie i obs�ug� ekranu zako�czenia gry.
 * @author �ukasz �urawski
 */
public class AfterGameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_game);
		loadSummaryFromServer();
		resetGame();
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
		/*try {
			DataManager.game.updatePlayer(DataManager.player);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		DataManager.players.clear();
		DataManager.oppositePlayers.clear();
		DataManager.teamList.clear();
		DataManager.preferences.setTeam(0);
		DataManager.preferences.saveAllDataToPreferences();
	}
	/**
	 * Pobiera podsumowanie gry z serwera
	 * */
	private void loadSummaryFromServer() {
		WebView gameSummary = (WebView) findViewById(R.id.game_summary_full);
		gameSummary.getSettings().setTextSize(TextSize.SMALLEST);
		gameSummary.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		            Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
		    }
		 });
		
		int team = DataManager.player.getTeam();
		int oppositeTeam = team;
		if(team % 2 == 0) {
			oppositeTeam -= 1;
		}
		else {
			oppositeTeam += 1;
		}
		gameSummary.loadUrl(String.format(DataManager.serverAddress, team, oppositeTeam));
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	
	//Doda� wy�wietlanie podsumowania gry
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas�uchuje klikni�cia na przycisk odpowiedzialny za rozpocz�cie
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
