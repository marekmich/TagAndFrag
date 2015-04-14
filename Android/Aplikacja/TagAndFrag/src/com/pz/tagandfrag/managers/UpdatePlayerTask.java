package com.pz.tagandfrag.managers;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Zadanie asynchroniczne aktualizuj¹ce gracza na serwerze.
 * @author Mateusz
 *
 */
public class UpdatePlayerTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		try {
			DataManager.game.updatePlayer(DataManager.player);
		} catch (IOException e) {
			Log.e("Updating player error", e.toString());
		}
		return null;
	}

}
