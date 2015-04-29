package com.pz.tagandfrag.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.activity.AfterGameActivity;
import com.pz.tagandfrag.activity.GameActivity;
import com.pz.tagandfrag.activity.StandbyActivity;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataReceiver;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataSender;
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
	private void addColumnToRow(TableRow row, String text, int gravity) {
		TextView column = new TextView(this.getActivity());
		//Ustawienei tekstu w kolumnie i jego rozmiaru
		column.setText(text);
		column.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
		column.setGravity(gravity);
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
		addColumnToRow(row_tittle, getString(R.string.nick), Gravity.LEFT);
		if (isMyTeam)
		{
			//Dodanie kolumny tytu�owej (przerwa)
			//addColumnToRow(row_tittle, "       ");
			//Dodanie kolumny tytu�owej (HP)
			addColumnToRow(row_tittle, getString(R.string.hp), Gravity.RIGHT);
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
			addColumnToRow(row, player.getName(), Gravity.LEFT);

			if (isMyTeam)
			{
				//Dodanie kolumny z ilo�ci� HP
				addColumnToRow(row, String.valueOf(player.getHealthPoints()), Gravity.RIGHT);
			}
			teamLayout.addView(row, i);
	        i++;
		}
		teamLayout.setColumnStretchable (1, true);
	}
	/**
	 * Aktualizuje tabele z listami graczy z obu dru�yn
	 * */
	private void updateTeamList() {
		getView().findViewById(R.id.progress_bar_team_fragment).setVisibility(ProgressBar.INVISIBLE);
		Log.e("POBRANO_GRACZY", "TAK");
		TableLayout myTeam = (TableLayout) getView().findViewById(R.id.table_my_team_team_fragment);
		TableLayout oppositeTeam = (TableLayout) getView().findViewById(R.id.table_opposite_team_team_fragment);
		updateTableLayout(myTeam, (ArrayList<Player>) DataManager.players, true);
		updateTableLayout(oppositeTeam, (ArrayList<Player>) DataManager.oppositePlayers, false);
	}
	
	/////////////////////////////////
	/* ��czno�� - REST Client */
	/**
	 * Zadanie pobieraj�ce list� graczy z w�asnej i przeciwnej dru�yny, od�wie�aj�ce list� aktywnych graczy oraz
	 * zliczaj�ce ilo�� aktywnych graczy (sprawdzenie warunku ko�ca gry)
	 */
	private Runnable updateTeamRunnable() {
		return new Runnable() {
			@Override
			public void run() {
				new UpdateTeamTask().execute();
				updateTeamList();
				if(countActivePlayersFromOppositeTeam() > 0) {
					updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
				}
				else {
					Intent intent = new Intent(getActivity(), AfterGameActivity.class);
					startActivity(intent);
				}
			}
			/**
			 * Zlicza ilo�� aktywnych graczy z przeciwnej dru�yny 
			 * @return liczba aktwynych graczy (ilo�� hp > 0) z przeciwnej dru�yny
			 * */
			private int countActivePlayersFromOppositeTeam() {
				int numberOfActivePlayersInTeam = 0;
				for(Player player : (ArrayList<Player>) DataManager.oppositePlayers) {
					if(player.getHealthPoints() > 0) {
						numberOfActivePlayersInTeam += 1;
					}
				}
				return numberOfActivePlayersInTeam;
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
		private BluetoothDataSender sender;
		
		/**
		 * Tworzy zadanie oraz inicjalicuje odbiornik danych bluetooth.
		 * @see BluetoothDataReceiver 
		 */
		public BluetoothReaderTask() {
			super();
			receiver = new BluetoothDataReceiver(DataManager.bluetoothService.getBluetoothSocket());
			sender = new BluetoothDataSender(DataManager.bluetoothService.getBluetoothSocket());
		}

		/**
		 * W tle odbiera strza�y oraz aktualizuje gracza (redukuje jego punkty �ycia).
		 */
		@Override
		protected Void doInBackground(Void... params) {
			while (receiver.hasNextLine()) {
				Log.e("Shoot", "Shoot from " + receiver.readMessageWithPrefix("SHT", false));
				DataManager.player.reduceHealth(DataManager.DAMAGE);
				Log.e("Player HP", String.valueOf(DataManager.player.getHealthPoints()));
				if(DataManager.player.getHealthPoints() == 0) {
					sender.blockWeaponAndTurnLedOff();
				}
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
