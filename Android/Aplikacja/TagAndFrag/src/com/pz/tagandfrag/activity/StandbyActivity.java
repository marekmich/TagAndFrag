package com.pz.tagandfrag.activity;

import java.io.IOException;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.UpdateTeamTask;
import com.pz.tagandfrag.restclient.Player;

public class StandbyActivity extends Activity {

	// Okres aktualizacji w milisekundach
	public static final int UPDATE_PERIOD = 3000;
	public static final int MINIMUM_NUMBER_OF_PLAYER = 1;
	private Handler updateTeamHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
		updateTeamHandler = new Handler();
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */

	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
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
	private void updateTeamList() {
		TableLayout myTeam = (TableLayout) findViewById(R.id.table_my_team_standby_activity);
		TableLayout oppositeTeam = (TableLayout) findViewById(R.id.table_opposite_team_standby_activity);
		updateTableLayout(myTeam, DataManager.players, Color.GREEN);
		updateTableLayout(oppositeTeam, DataManager.oppositePlayers, Color.RED);
		
		if(DataManager.players.size() >= MINIMUM_NUMBER_OF_PLAYER 
				&& DataManager.oppositePlayers.size() >= MINIMUM_NUMBER_OF_PLAYER) {
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.INVISIBLE);
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.VISIBLE);
		}
		Log.i("TEAM", DataManager.players.toString());
	}
	/////////////////////////////////
	/* Listenery */
	/**
	 * 
	 * */
	public void onStartGameButtonClicked(View view) {
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
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
	/////////////////////////////////
	/* £¹cznoœæ - Bluetooth */
	private void connectWithWeaponTask() {
		new Thread(connectWithWeaponRunnable()).start();
	}
	private Runnable connectWithWeaponRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				String MAC = DataManager.preferences.getMAC();
				DataManager.bluetoothService.connectWithDeviceByMacAddress(MAC);
			}
		};
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * 
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
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//Dopisaæ obs³ugê przypisania weaponcode - obs³uga bluetootha
			connectWithWeaponTask();
			try {
				DataManager.game.ready(DataManager.player, DataManager.player.getTeam());
			} catch (IOException e) {
				e.printStackTrace();
			}
			updateTeamHandler.removeCallbacks(updateTeamRunnable());
			return null;
		}
	}
}
