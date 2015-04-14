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
}
