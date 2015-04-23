package com.pz.tagandfrag.activity;

import java.io.IOException;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.bluetoothservice.BluetoothDataSender;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.DebugManager;
import com.pz.tagandfrag.managers.UpdateTeamTask;
import com.pz.tagandfrag.restclient.Player;

/**
 * Aktywno�� odpowiedzialna za wy�wietlenie ekranu z list� graczy w dru�ynach oraz
 * obs�ug� rozpocz�cia gry
 * @author �ukasz �urawski
 */
public class StandbyActivity extends Activity {

	/**
	 * Statyczne pole z interwa�em aktualizacji w milisekundach.
	 */
	public static final int UPDATE_PERIOD = 3000;
	/**
	 * Statyczne pole z minimaln� ilo�ci� graczy w ka�dej dru�ynie do
	 * rozpocz�cia rozgrywki.
	 * */
	public static final int MINIMUM_NUMBER_OF_PLAYERS = 1;
	private Handler updateTeamHandler;
	
	/**
	 * Tworzy Handler aktualizujacy druzyne oraz uruchamia zadanie
	 * ��cz�ce si� z broni� i zmieniaj�ce kod tej broni.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_standby);
		prepareActivity();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	private void prepareActivity() {
		createUpdateTeamHandler();
		connectWithWeaponTask();
	}
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
	 * */
	private void addColumnToRow(TableRow row, String text) {
		TextView column = new TextView(this);
		//Ustawienei tekstu w kolumnie i jego rozmiaru
		column.setText(text);
		column.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_size));
		row.addView(column);
	}
	/**
	 * Tworzy tabele na podstawie listy graczy z danej dru�yny
	 * @param teamLayout {@link TableLayout} na kt�rym ma by� wy�wietlona tabela
	 * @param playerList lista graczy z dru�yny
	 * @param myTeam informacja czy dana tabela dotyczy dru�yny gracza, czy dru�yny przeciwnej
	 * */
	private void updateTableLayout(TableLayout teamLayout, Collection<Player> playerList, boolean myTeam) {
		
		teamLayout.removeAllViews();
		TableRow titleRow = new TableRow(this);
		addColumnToRow(titleRow, "");
		if(myTeam) addColumnToRow(titleRow, getString(R.string.my_team));
		else addColumnToRow(titleRow, getString(R.string.opposite_team));
		teamLayout.addView(titleRow, 0);
		int i = 1;
		for(Player player : playerList) {
			TableRow row = new TableRow(this);
			row.setBackgroundResource(R.layout.cell_shape);
			TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
			row.setLayoutParams(lp);
			addColumnToRow(row, String.valueOf(i) + ". ");
			addColumnToRow(row, player.getName());
			teamLayout.addView(row, i);
	        i++;
		}
	}
	/**
	 * Wy�wietlenie listy graczy, kt�rzy do��czyli ju� do dru�yn, w momencie gdy b�dzie ich w�a�ciwa ilo�� w obu dru�ynach (>=1)
	 * odblokowuje przycisk rozpocz�cia gry.
	 * */
	private void updateTeamList() {
		TableLayout myTeam = (TableLayout) findViewById(R.id.table_my_team_standby_activity);
		TableLayout oppositeTeam = (TableLayout) findViewById(R.id.table_opposite_team_standby_activity);
		updateTableLayout(myTeam, DataManager.players, true);
		updateTableLayout(oppositeTeam, DataManager.oppositePlayers, false);
		
		if(DataManager.players.size() >= MINIMUM_NUMBER_OF_PLAYERS 
				&& DataManager.oppositePlayers.size() >= MINIMUM_NUMBER_OF_PLAYERS) {
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.INVISIBLE);
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.VISIBLE);
		}
	}
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za rozpocz�cie nowej gry, wykonuje zadanie
	 * poprzez uruchomienia zadania w tle z klasy {@link StartGameProgressBarTask}
	 */
	public void onStartGameButtonClicked(View view) {
		updateTeamHandler.removeCallbacks(updateTeamRunnable());
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.back_pressed_info), Toast.LENGTH_LONG).show();
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
	/**
	 * Wy��cza handlera pobieraj�cego graczy z dru�yn, a nast�pnie wysy�a informacj� o tym, �e dru�yny s� gotowe
	 */
	private void startGame() {
		if(!DebugManager.withoutReady) {
			try {
				DataManager.game.ready(DataManager.player, DataManager.player.getTeam());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/////////////////////////////////
	/* ��czno�� - Bluetooth */
	/**
	 * Tworzy zadanie ��czenia si� z bluetoothem w nowym w�tku
	 * */
	private void connectWithWeaponTask() {
		if(!DebugManager.withoutBluetooth) {
			new Thread(connectWithWeaponRunnable()).start();
		}
	}
	/**
	 * ��czy si� z broni� oraz nadaje jej nowy kod
	 * */
	private Runnable connectWithWeaponRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				String MAC = DataManager.preferences.getMAC();
				DataManager.bluetoothService.connectWithDeviceByMacAddress(MAC);
				sendNewCodeToWeapon();
			}
		};
	}
	/**
	 * Nadaje broni nowy kod
	 * */
	private void sendNewCodeToWeapon() {
		BluetoothDataSender dataSender = new BluetoothDataSender(DataManager.bluetoothService.getBluetoothSocket());
		dataSender.changeWeaponCode(DataManager.weaponCode);
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuj�ca zadanie rozpocz�cia gry, by potem przej�� do {@link GameActivity}
	 * */
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		//TODO dokumentacja
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(StandbyActivity.this, GameActivity.class);
			startActivity(intent);
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_standby).setVisibility(ProgressBar.VISIBLE);
			findViewById(R.id.button_start_game).setVisibility(ProgressBar.GONE);
			findViewById(R.id.table_my_team_standby_activity).setVisibility(TableLayout.INVISIBLE);
			findViewById(R.id.table_opposite_team_standby_activity).setVisibility(TableLayout.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {			
			startGame();
			return null;
		}
	}
}
