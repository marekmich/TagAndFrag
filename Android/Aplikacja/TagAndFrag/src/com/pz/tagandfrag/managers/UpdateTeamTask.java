package com.pz.tagandfrag.managers;

import android.os.AsyncTask;

public class UpdateTeamTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		DataManager.getPlayers();
		return null;
	}

}
