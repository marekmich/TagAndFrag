package com.pz.tagandfrag.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pz.tagandfrag.R;

public class StandbyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
	}
	
	public void onStartGameButtonClicked(View view) {
		Intent intent = new Intent(StandbyActivity.this, GameActivity.class);
		startActivity(intent);
	}
}
