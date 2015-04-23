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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
 * Klasa reprezentuj�ca fragment z dru�yn�, hostowany przez GameActivity. 
 * @author Mateusz Wrzos, �ukasz �urawski
 * @see GameActivity
 *
 */
public class TeamFragment extends Fragment {

	private View view;
	
	// Okres aktualizacji w milisekundach
	public static final int UPDATE_PERIOD = 3000;
	private Handler updateTeamHandler;
	
	/**
	 * Tworzy fragment oraz Handler aktualizujacy druzyne.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.team_fragment, container, false);
		createUpdateTeamHandler();
		if(!DebugManager.withoutBluetooth) {
			new BluetoothReaderTask().execute();
		}
		return view;
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Tworzy i ustawia handler na w�tek, zajmuj�cy si� updatowaniem listy graczy w dru�ynach
	 * */
	private void createUpdateTeamHandler() {
		updateTeamHandler = new Handler();
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	/**
	 * Dodaje kolumny z tekstem o okre�lonym rozmiarze do podanego wiersza
	 * @param row wiersz do, kt�rego kolumna ma by� podana
	 * @param text tekst, kt�ry ma by� w tworzonej kolumnie
	 * @param gravity wyr�wnanie w poziomie
	 * */
	private void addColumnToRow(TableRow row, String text) {
		TextView column = new TextView(this.getActivity());
		//Ustawienei tekstu w kolumnie i jego rozmiaru
		column.setText(text);
		column.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
		row.addView(column);
	}
	/**
	 * Tworzy tabeele na podstawie listy graczy z danej dru�yny
	 * @param teamLayout {@link TableLayout} na kt�rym ma by� wy�wietlona tabela
	 * @param playerList lista graczy z dru�yny
	 * @param isMyTeam informacja czy dana tabela dotyczy dru�yny gracza, czy dru�yny przeciwnej
	 * */
	private void updateTableLayout(TableLayout teamLayout, ArrayList<Player> playerList, boolean isMyTeam) {
		int i = 1;
		teamLayout.removeAllViews();
		
		if (isMyTeam) {
			teamLayout.setBackgroundResource(R.layout.my_team_table_shape);
		} else {
			teamLayout.setBackgroundResource(R.layout.opposite_team_table_shape);
		}
		
		//Przygotowanie wiersza tytu�owego
		TableRow row_tittle = new TableRow(this.getActivity());
		TableRow.LayoutParams lp_tittle = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		row_tittle.setLayoutParams(lp_tittle);
		
		//Dodanie kolumny tytu�owej (nick)
		addColumnToRow(row_tittle, getString(R.string.nick));
		if (isMyTeam)
		{
			//Dodanie kolumny tytu�owej (przerwa)
			addColumnToRow(row_tittle, "       ");
			//Dodanie kolumny tytu�owej (HP)
			addColumnToRow(row_tittle, getString(R.string.hp));
		}
		teamLayout.addView(row_tittle, 0);
		
		//Posortowanie listy graczy w dru�ynie
		Collections.sort(playerList);
		//Dodawanie kolejnych wierszy z danymi graczy
		for(Player player : playerList) {
			//Przygotowanie wiersza
			TableRow row = new TableRow(this.getActivity());
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
		
			if (player.getHealthPoints() == 0) {
				row.setBackgroundColor(Color.GRAY);
			}

			row.setLayoutParams(lp);
			
			//Dodanie kolumny z nickiem gracza
			addColumnToRow(row, player.getName());

			if (isMyTeam)
			{
				/*
				 * M: to jest paskudne. Trzeba poprawic, przebudowac calosc przy uzyciu parametru Gravity
				 */
				//Dodanie kolumny z przerw�
				addColumnToRow(row, "       ");
				//Dodanie kolumny z ilo�ci� HP
				addColumnToRow(row, String.valueOf(player.getHealthPoints()));
			}
			teamLayout.addView(row, i);
	        i++;
		}
	}
	/**
	 * Aktualizuje tabele z listami graczy z obu dru�yn
	 * */
	private void updateTeamList() {
		getView().findViewById(R.id.progress_bar_team_fragment).setVisibility(ProgressBar.INVISIBLE);
		TableLayout myTeam = (TableLayout) getView().findViewById(R.id.table_my_team_team_fragment);
		TableLayout oppositeTeam = (TableLayout) getView().findViewById(R.id.table_opposite_team_team_fragment);
		updateTableLayout(myTeam, (ArrayList<Player>) DataManager.players, true);
		updateTableLayout(oppositeTeam, (ArrayList<Player>) DataManager.oppositePlayers, false);
	}
	
	/////////////////////////////////
	/* ��czno�� - REST Client */
	/**
	 * Zadanie pobieraj�ce list� graczy z w�asnej i przeciwnej dru�yny
	 */
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
	/////////////////////////////////
	/* Prywatne klasy */
	
	/**
	 * Asynchroniczne zadanie odbieraj�ce dane z modu�u bluetooth.
	 * @author Mateusz Wrzos
	 *
	 */
	private class BluetoothReaderTask extends AsyncTask<Void, Void, Void> {
		
		private BluetoothDataReceiver receiver;
		
		/**
		 * Tworzy zadanie oraz inicjalicuje odbiornik danych bluetooth.
		 * @see BluetoothDataReceiver 
		 */
		public BluetoothReaderTask() {
			super();
			receiver = new BluetoothDataReceiver(DataManager.bluetoothService.getBluetoothSocket());
		}

		/**
		 * W tle odbiera strza�y oraz aktualizuje gracza (redukuje jego punkty �ycia).
		 */
		@Override
		protected Void doInBackground(Void... params) {
			while (receiver.hasNextLine()) {
				Log.i("Shoot", "Shoot from " + receiver.readMessageWithPrefix("SHT", false));
				DataManager.player.reduceHealth(DataManager.DAMAGE);
				new Thread(updatePlayerRunnable()).start();
			}
			return null;
		}
		
		/**
		 * @return Runnable aktualizuj�ce gracza.
		 */
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
