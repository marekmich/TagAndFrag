package com.pz.tagandfrag.managers;

import java.io.IOException;

import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateTeamTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		DataManager.getPlayers();
		return null;
	}

}
