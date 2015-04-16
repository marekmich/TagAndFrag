package com.pz.tagandfrag.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.activity.GameActivity;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataReceiver;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.DebugManager;
import com.pz.tagandfrag.managers.UpdateTeamTask;
import com.pz.tagandfrag.restclient.Player;

/**
 * Klasa reprezentująca fragment z drużyną, hostowany przez GameActivity. 
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
		if(!DebugManager.withoutBluetooth) {
			new BluetoothReaderTask().execute();
		}
		return view;
	}

	private void updateTableLayout(TableLayout teamLayout, ArrayList<Player> playerList, int color) {
		int i = 1;
		teamLayout.removeAllViews();
		
		TableRow row_tittle = new TableRow(this.getActivity());
		TableRow.LayoutParams lp_tittle = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
		row_tittle.setLayoutParams(lp_tittle);
		TextView name_tittle = new TextView(this.getActivity());
		name_tittle.setText("Nick");
		row_tittle.addView(name_tittle);
		if(color == Color.GREEN)
		{
			TextView hp_tittle = new TextView(this.getActivity());
			hp_tittle.setText("HP");
			row_tittle.addView(hp_tittle);
		}
		teamLayout.addView(row_tittle, 0);
		Collections.sort(playerList);
		
		for(Player player : playerList) {
			TableRow row = new TableRow(this.getActivity());
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			if(player.getHealthPoints() != 0) {
				row.setBackgroundColor(color);
			}
			else {
				row.setBackgroundColor(Color.GRAY);
			}
			row.setLayoutParams(lp);
			TextView name = new TextView(this.getActivity());
			name.setText(player.getName());
			row.addView(name);

			if(DataManager.player.getTeam() == player.getTeam())
			{
				TextView hp = new TextView(this.getActivity());
				hp.setText(String.valueOf(player.getHealthPoints()));
				row.addView(hp);
			}
			teamLayout.addView(row, i);
	        i++;
		}
	}
	private void updateTeamList() {
		TableLayout myTeam = (TableLayout) getView().findViewById(R.id.table_my_team_team_fragment);
		TableLayout oppositeTeam = (TableLayout) getView().findViewById(R.id.table_opposite_team_team_fragment);
		//myTeam.setVisibility(LinearLayout.VISIBLE);
		updateTableLayout(myTeam, (ArrayList<Player>) DataManager.players, Color.GREEN);
		//oppositeTeam.setVisibility(LinearLayout.VISIBLE);
		updateTableLayout(oppositeTeam, (ArrayList<Player>) DataManager.oppositePlayers, Color.RED);
		
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
		
		private BluetoothDataReceiver receiver;
		
		public BluetoothReaderTask() {
			super();
			receiver = new BluetoothDataReceiver(DataManager.bluetoothService.getBluetoothSocket());
		}

		@Override
		protected Void doInBackground(Void... params) {
			while (receiver.hasNextLine()) {
				Log.i("Shoot", "Shoot from " + receiver.readMessageWithPrefix("SHT", false));
				DataManager.player.reduceHealth(DataManager.DAMAGE);
				new Thread(updatePlayerRunnable()).start();
			}
			return null;
		}
		
		private Runnable updatePlayerRunnable() {
			return new Runnable() {
				
				@Override
				public void run() {
					try {
						DataManager.game.updatePlayer(DataManager.player);
					} catch (IOException e) {
						e.printStackTrace();
					}					
				}
			};
		}
	}
}
