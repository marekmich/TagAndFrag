package com.pz.tagandfrag.activity;

import java.io.IOException;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataSender;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.DebugManager;
import com.pz.tagandfrag.managers.UpdateTeamTask;
import com.pz.tagandfrag.restclient.Player;

public class StandbyActivity extends Activity {

	// Okres aktualizacji w milisekundach
	public static final int UPDATE_PERIOD = 3000;
	public static final int MINIMUM_NUMBER_OF_PLAYERS = 1;
	private Handler updateTeamHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
		prepareActivity();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
	 * */
	private void prepareActivity() {
		createUpdateTeamHandler();
		connectWithWeaponTask();
	}
	/**
	 * Tworzy i ustawia handler na w¹tek, zajmuj¹cy siê updatowaniem listy graczy w dru¿ynach
	 * */
	private void createUpdateTeamHandler() {
		updateTeamHandler = new Handler();
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
	}
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Wyœwietlanie na podanej tabeli podanej listy graczy we wskazanym kolorze
	 * */
	private void updateTableLayout(TableLayout teamLayout, Collection<Player> playerList, int color) {
		int i = 0;
		teamLayout.removeAllViews();
		for(Player player : playerList) {
			TableRow row = new TableRow(this);
			row.setBackgroundColor(color);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			row.setLayoutParams(lp);
			TextView name = new TextView(this);
			name.setText(player.getName());
			row.addView(name);
			teamLayout.addView(row, i);
	        i++;
		}
	}
	/**
	 * Wyœwietlenie listy graczy, którzy do³¹czyli ju¿ do dru¿yn, w momencie gdy bêdzie ich w³aœciwa iloœæ w obu dru¿ynach (>=1)
	 * odblokowuje przycisk rozpoczêcia gry.
	 * */
	private void updateTeamList() {
		TableLayout myTeam = (TableLayout) findViewById(R.id.table_my_team_standby_activity);
		TableLayout oppositeTeam = (TableLayout) findViewById(R.id.table_opposite_team_standby_activity);
		updateTableLayout(myTeam, DataManager.players, Color.GREEN);
		updateTableLayout(oppositeTeam, DataManager.oppositePlayers, Color.RED);
		
		if(DataManager.players.size() >= MINIMUM_NUMBER_OF_PLAYERS 
				&& DataManager.oppositePlayers.size() >= MINIMUM_NUMBER_OF_PLAYERS) {
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.INVISIBLE);
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.VISIBLE);
		}
	}
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za rozpoczêcie nowej gry, wykonuje zadanie
	 * poprzez uruchomienia zadania w tle z klasy {@link StartGameProgressBarTask}
	 */
	public void onStartGameButtonClicked(View view) {
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	@Override
	public void onBackPressed() {
	}
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
	/**
	 * Zadanie pobieraj¹ce listê graczy z w³asnej i przeciwnej dru¿yny
	 */
	private Runnable updateTeamRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				new UpdateTeamTask().execute();
				updateTeamList();
				updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
			}
		};
	}
	/**
	 * Wy³¹cza handlera pobieraj¹cego graczy z dru¿yn, a nastêpnie wysy³a informacjê o tym, ¿e dru¿yny s¹ gotowe
	 */
	private void startGame() {
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		if(!DebugManager.withoutReady) {
			try {
				DataManager.game.ready(DataManager.player, DataManager.player.getTeam());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/////////////////////////////////
	/* £¹cznoœæ - Bluetooth */
	/**
	 * Tworzy zadanie ³¹czenia siê z bluetoothem w nowym w¹tku
	 * */
	private void connectWithWeaponTask() {
		if(!DebugManager.withoutBluetooth) {
			new Thread(connectWithWeaponRunnable()).start();
		}
	}
	/**
	 * £¹czy siê z broni¹ oraz nadaje jej nowy kod
	 * */
	private Runnable connectWithWeaponRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				String MAC = DataManager.preferences.getMAC();
				DataManager.bluetoothService.connectWithDeviceByMacAddress(MAC);
				sendNewCodeToWeapon();
			}
		};
	}
	/**
	 * Nadaje broni nowy kod
	 * */
	private void sendNewCodeToWeapon() {
		BluetoothDataSender dataSender = new BluetoothDataSender(DataManager.bluetoothService.getBluetoothSocket());
		dataSender.changeWeaponCode(DataManager.weaponCode);
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuj¹ca zadanie rozpoczêcia gry, by potem przejœæ do {@link GameActivity}
	 * */
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		//TODO dokumentacja
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(StandbyActivity.this, GameActivity.class);
			startActivity(intent);
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.VISIBLE);
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.GONE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {			
			startGame();
			return null;
		}
	}
}
