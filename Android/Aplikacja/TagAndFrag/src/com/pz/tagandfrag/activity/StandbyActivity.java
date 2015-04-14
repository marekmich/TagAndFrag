package com.pz.tagandfrag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

public class StandbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
		connectWithWeaponTask();
	}
	
	public void onStartGameButtonClicked(View view) {
		Intent intent = new Intent(StandbyActivity.this, GameActivity.class);
		startActivity(intent);
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
	/*
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(ChooseTeamActivity.this, StandbyActivity.class);
			startActivity(intent);
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//Dopisaæ obs³ugê przypisania weaponcode - obs³uga bluetootha
			sendTeamToServerAndGetWeaponCode();
			return null;
		}
	}*/
}
