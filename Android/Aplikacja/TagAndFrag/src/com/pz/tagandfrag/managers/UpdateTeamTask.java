package com.pz.tagandfrag.managers;

import android.os.AsyncTask;

public class UpdateTeamTask extends AsyncTask<Void, Void, Void> {
	/**
	 * Klasa pobiera w tle list� dru�yn
	 * */
	@Override
	protected Void doInBackground(Void... params) {
		DataManager.getPlayers();
		return null;
	}

}
