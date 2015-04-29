package com.pz.tagandfrag.managers;

import android.os.AsyncTask;

/**
 * Zadanie asynchroniczne aktualizuj¹ce listê graczy w dru¿ynie gracza i w dru¿ynie przeciwnej.
 * @author Mateusz
 */
public class UpdateTeamTask extends AsyncTask<Void, Void, Void> {
	/**
	 * Klasa pobiera w tle listê dru¿yn
	 * */
	@Override
	protected Void doInBackground(Void... params) {
		DataManager.getPlayers();
		return null;
	}

}
