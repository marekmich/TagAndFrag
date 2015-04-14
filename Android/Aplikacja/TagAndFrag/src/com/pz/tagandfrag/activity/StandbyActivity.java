package com.pz.tagandfrag.activity;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

public class StandbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
	}
	
	public void onStartGameButtonClicked(View view) {
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
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
	
	private void connectWithWeaponTask() {
		new Thread(connectWithWeaponRunnable()).start();
	}
	
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
			return null;
		}
	}
}
