package com.pz.tagandfrag.managers;

import java.io.IOException;

import org.json.JSONException;

import android.os.AsyncTask;
import android.util.Log;

public class UpdateTeamTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		try {
			DataManager.players = DataManager.game.getByTeam(DataManager.player.getTeam());
		} catch (IOException e) {
			Log.e("Team.IOex", e.toString());
		} catch (JSONException e) {
			Log.e("Team.JSON", e.toString());
		}
		return null;
	}

}
