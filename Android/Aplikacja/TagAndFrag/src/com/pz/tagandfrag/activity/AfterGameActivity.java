package com.pz.tagandfrag.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataSender;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.DebugManager;


/**
 * Aktywnoœæ odpowiedzialna za wyœwietlanie i obs³ugê ekranu zakoñczenia gry.
 * @author £ukasz ¯urawski
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
	 * Czyszczenie parametrów gry po zakoñczeniu rozgrywki
	 * */
	private void resetGame() {
		//Reset kodu broni
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(!DebugManager.withoutBluetooth) {
					BluetoothDataSender sender = new BluetoothDataSender(DataManager.bluetoothService.getBluetoothSocket());
					sender.changeWeaponCode(0);
					sender.blockWeaponAndTurnLedOff();
				}
				try {
					DataManager.game.end(DataManager.player);
				} catch (IOException e) {
					e.printStackTrace();
				}
				DataManager.player.setHealthPoints(100);
				DataManager.player.setTeam(0);
				DataManager.players.clear();
				DataManager.oppositePlayers.clear();
				DataManager.teamList.clear();
				DataManager.preferences.setTeam(0);
				DataManager.preferences.saveAllDataToPreferences();
			}
		}).start();
	}
	/**
	 * Pobiera podsumowanie gry z serwera
	 * */
	private void loadSummaryFromServer() {
		WebView gameSummary = (WebView) findViewById(R.id.game_summary_full);
		gameSummary.setBackgroundColor(Color.TRANSPARENT);
		gameSummary.getSettings().setJavaScriptEnabled(true);
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
	/* Listenery */
	/**
	 * Nas³uchuje klikniêcia na przycisk odpowiedzialny za rozpoczêcie
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
