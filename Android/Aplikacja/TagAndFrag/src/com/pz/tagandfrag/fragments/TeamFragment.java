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
import android.webkit.WebView.FindListener;
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
 * Klasa reprezentuj¹ca fragment z dru¿yn¹, hostowany przez GameActivity. 
 * @author Mateusz Wrzos, £ukasz ¯urawski
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
	 * Tworzy i ustawia handler na w¹tek, zajmuj¹cy siê updatowaniem listy graczy w dru¿ynach
	 * */
	private void createUpdateTeamHandler() {
		updateTeamHandler = new Handler();
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		updateTeamHandler.postDelayed(updateTeamRunnable(), UPDATE_PERIOD);
	}
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Dodaje kolumny z tekstem o okreœlonym rozmiarze do podanego wiersza
	 * @param row wiersz do, którego kolumna ma byæ podana
	 * @param text tekst, który ma byæ w tworzonej kolumnie
	 * */
	private void addColumnToRow(TableRow row, String text) {
		TextView column = new TextView(this.getActivity());
		//Ustawienei tekstu w kolumnie i jego rozmiaru
		column.setText(text);
		column.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
		row.addView(column);
	}
	/**
	 * Tworzy tabeele na podstawie listy graczy z danej dru¿yny
	 * @param teamLayout {@link TableLayout} na którym ma byæ wyœwietlona tabela
	 * @param playerList lista graczy z dru¿yny
	 * @param myTeam informacja czy dana tabela dotyczy dru¿yny gracza, czy dru¿yny przeciwnej
	 * */
	private void updateTableLayout(TableLayout teamLayout, ArrayList<Player> playerList, boolean myTeam) {
		int i = 1;
		teamLayout.removeAllViews();
		int color = 0;
		//Ustawienie w jakim kolorze maj¹ byæ wyœwietlane wiersze
		if(myTeam == true) {
			color = Color.GREEN;
		}
		else {
			color = Color.RED;
		}
		//Przygotowanie wiersza tytu³owego
		TableRow row_tittle = new TableRow(this.getActivity());
		TableRow.LayoutParams lp_tittle = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
		row_tittle.setLayoutParams(lp_tittle);
		
		//Dodanie kolumny tytu³owej (nick)
		addColumnToRow(row_tittle, getString(R.string.nick));
		if(color == Color.GREEN)
		{
			//Dodanie kolumny tytu³owej (przerwa)
			addColumnToRow(row_tittle, "    ");
			//Dodanie kolumny tytu³owej (HP)
			addColumnToRow(row_tittle, getString(R.string.hp));
		}
		teamLayout.addView(row_tittle, 0);
		//Posortowanie listy graczy w dru¿ynie
		Collections.sort(playerList);
		//Dodawanie kolejnych wierszy z danymi graczy
		for(Player player : playerList) {
			//Przygotowanie wiersza
			TableRow row = new TableRow(this.getActivity());
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			if(player.getHealthPoints() != 0) {
				row.setBackgroundColor(color);
			}
			else {
				row.setBackgroundColor(Color.GRAY);
			}
			row.setLayoutParams(lp);
			
			//Dodanie kolumny z nickiem gracza
			addColumnToRow(row, player.getName());

			if(color == Color.GREEN)
			{
				//Dodanie kolumny z przerw¹
				addColumnToRow(row, "    ");
				//Dodanie kolumny z iloœci¹ HP
				addColumnToRow(row, String.valueOf(player.getHealthPoints()));
			}
			teamLayout.addView(row, i);
	        i++;
		}
	}
	/**
	 * Aktualizuje tabele z listami graczy z obu dru¿yn
	 * */
	private void updateTeamList() {
		getView().findViewById(R.id.progress_bar_team_fragment).setVisibility(ProgressBar.INVISIBLE);
		TableLayout myTeam = (TableLayout) getView().findViewById(R.id.table_my_team_team_fragment);
		TableLayout oppositeTeam = (TableLayout) getView().findViewById(R.id.table_opposite_team_team_fragment);
		updateTableLayout(myTeam, (ArrayList<Player>) DataManager.players, true);
		updateTableLayout(oppositeTeam, (ArrayList<Player>) DataManager.oppositePlayers, false);
	}
	
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
	/**
	 * Zadanie pobieraj¹ce listê graczy z w³asnej i przeciwnej dru¿yny
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
	 * Asynchroniczne zadanie odbieraj¹ce dane z modu³u bluetooth.
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
		 * W tle odbiera strza³y oraz aktualizuje gracza (redukuje jego punkty ¿ycia).
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
		 * @return Runnable aktualizuj¹ce gracza.
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
