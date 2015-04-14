package com.pz.tagandfrag.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.activity.GameActivity;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.UpdateTeamTask;

/**
 * Klasa reprezentuj¹ca fragment z dru¿yn¹, hostowany przez GameActivity. 
 * @author Mateusz Wrzos
 * @see GameActivity
 *
 */
public class TeamFragment extends Fragment {

	private View view;
	
	// Okres aktualizacji w milisekundach
	public static final int UPDATE_PERIOD = 3000;
	private Handler updateTeamHandler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.team_fragment, container, false);
		updateTeamHandler = new Handler();
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
		return view;
	}


	private void updateTeamList() {
		// Tutaj mo¿na akutalizowaæ GUI
		Log.i("TEAM", DataManager.players.toString());
	}
	
	private Runnable updateTeamRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				new UpdateTeamTask().execute();
				updateTeamList();
				updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
			}
		};
	}

	private class BluetoothReaderTask extends AsyncTask<Void, Void, Void> {

		private InputStream inputStream;
		private Scanner inputScanner;
	
		public BluetoothReaderTask() {
			super();
			initializeStreamAndScanner();
		}

		@Override
		protected Void doInBackground(Void... params) {
			while (inputScanner.hasNextLine()) {

			}
			return null;
		}
		
		private void initializeStreamAndScanner() {
			try {
				inputStream = DataManager.bluetoothService.getBluetoothSocket().getInputStream();
			} catch (IOException e) {
				Log.e("INPUT STREAM", e.toString());
			}
			inputScanner = new Scanner(inputStream);
		}
	}
}
