package com.pz.tagandfrag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.json.JSONException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.dialogs.ChooseWeaponDialog;
import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

public class LoginActivity extends Activity {

	public static PreferencesManager preferences;
	
	Game game;
	Player player;
	
	public static Collection<Team> teamList;
	/* Pole zawierajace wszystkich graczy, do test�w */
	public static Collection<Player> players;
	
	/* Bluetooth */
	private static final int REQUEST_ENABLE_BT = 0;
	public static BluetoothService bluetoothService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prepareActivity();
		initializeViewComponents();
	}
	
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	private void prepareActivity() {
		teamList = new ArrayList<Team>();
		bluetoothService = new BluetoothService();
		requestEnableBluetooth();
		
		
		preferences = new PreferencesManager(getApplicationContext());
		preferences.loadLoginDataFromPreferences();
		game = new Game();
		player = new Player(preferences.getNick(), preferences.getId());
		
		
		// BUUUUGGG
		DownloadTeamListTask task = new DownloadTeamListTask();
		task.execute();
	}
	
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	/**
	 * Zmienia wygl�d na podstawie pobranych ustawie� z pami�ci
	 * */
	private void initializeViewComponents() {
		setNewNickOnNickEditText();
		setPasswordOnPasswordEditText();

	}
	/**
	 * Ustawia nick lub podpowied� (zale�y od tego czy w preferencjach aplikacji jest przechowywany nick) w polu do kt�rego wpisuje si� nick
	 * */
	private void setNewNickOnNickEditText() {
		EditText nickEditText = (EditText) findViewById(R.id.edit_text_login);
		//Sprawdza czy nick pobrany z preferencji jest r�ny od hinta
		if(!preferences.getNick().equals(getResources().getString(R.string.preferences_name_default)))
		{
			nickEditText.setText(preferences.getNick());
		}
	}
	private void setPasswordOnPasswordEditText() {
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		//Sprawdza czy id pobrane z preferencji jest r�ne od zera
		if(preferences.getId() != 0)
		{
			passwordEditText.setText(String.valueOf(preferences.getId()));
		}
		else {
			
		}
	}
	
	/////////////////////////////////
	/* Listenery */		
	public void onNickConfirmButtonClicked(View view) {
		EditText nickEditText = (EditText) findViewById(R.id.edit_text_login);
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		
		//Sprawdzenie czy nick jest odpowiednio d�ugi
		if(nickEditText.getText().toString().length() >= 4 && 
				((preferences.getId() !=0 && passwordEditText.getText().toString().length() == 7) 
						|| preferences.getId()==0 ))
		{
			String nick = nickEditText.getText().toString();
			int password;
			player.setName(nick);
			preferences.setNick(nick);
			if(!TextUtils.isEmpty(passwordEditText.getText()))
			{
				password = Integer.valueOf(passwordEditText.getText().toString());
				player.setId(password);
				preferences.setId(password);
			}
			
			//Uruchomienie zadania w tle, kt�re przesy�a dane do serwera (r�wnie� weryfikuje odpowied� serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask(this);
			task.execute();
		}
		else if(passwordEditText.getText().toString().length() != 7)
		{
			//Kiedy has�o jest niew�a�ciwe
			Toast.makeText(this, R.string.password_length_wrong, Toast.LENGTH_LONG).show();
		}
		else
		{
			//Kiedy nick jest niew�a�ciwy
			Toast.makeText(this, R.string.nick_length_wrong, Toast.LENGTH_LONG).show();
		}
		setPasswordOnPasswordEditText();
	}
	
	/////////////////////////////////
	/* ��czno�� - Bluetooth */
	private void requestEnableBluetooth() {
		if (!bluetoothService.getBluetoothAdapter().isEnabled()) {
			Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
		}
	}
	
	/////////////////////////////////
	/* ��czno�� - REST Client */
	public Boolean sendNickToServer()
	{
		int id = preferences.getId();
		
		int newId = 0;
		try {
			newId = game.check(player);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d("ID", String.valueOf(newId));
		//NICK jest poprawny (sytuacja kiedy gracz gra� wcze�niej i podany nick znajduj� si� w bazie)
		if(newId == id && id > 0)
		{
			
			return true;
		}
		//NICK jest poprawny (sytuacja kiedy gracz nie gra� wcze�niej i podany nick nie jest zaj�ty)
		else if(newId > 0 && id == 0)
		{
			id = newId;
			player.setId(id);
			preferences.setId(id);
			return true;
		}
		//NICK nie jest poprawny (sytuacja kiedy gracz gra� wcze�niej i poda� inny nick, kt�ry jest zaj�ty przez kogo� innego)
		else if(newId == 0 && id > 0)
		{
			return false;
		}
		//NICK nie jest poprawny (sytuacja kiedy gracz nie gra� wcze�niej i poda� nick, kt�ry jest zaj�ty przez kogo� innego)
		else if(newId == 0 && id == 0)
		{
			return false;
		}
		//Sytuacj� pozosta�e (nigdy nie powinno doj�� do tego momentu)
		return false;
	}
	public void downloadTeamListFromServer() {
		// TODO komentarz
		ArrayList<Team> array = new ArrayList<Team>();
		try {
			array = new ArrayList<Team>(game.list());
		} catch (IOException e) {
			Log.d("IO", e.toString());
		} catch (JSONException e) {
			Log.d("JSON", e.toString());
		}
		Collections.sort(array);
		teamList = array;
	}
	/////////////////////////////////
	/* Prywatne klasy */
	private class NickCheckProgressBarTask extends AsyncTask<Void, Void, Void> {
		
		Boolean isSuccessful;
		Activity activity;
		public NickCheckProgressBarTask(Activity activity) {
			super();
			this.activity = activity;
			isSuccessful = false;
		}
		@Override
		protected void onPostExecute(Void result) {
			if(isSuccessful) {
				//Kiedy nick jest w�a�ciwy dane s� zapisywane do preferencji, progress bar jest chowany i przechodzi si� do kolejnego etapu
				preferences.saveLoginDataToPreferences();
				preferences.loadMACDataFromPreferences();

				if (preferences.getMAC() == null) {
					new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
				} else {
					Intent intent = new Intent(activity, ChooseTeamActivity.class);
			        startActivity(intent);
					Toast.makeText(activity, R.string.nick_correct, Toast.LENGTH_LONG).show();
				}
			}
			else {
				//Kiedy nick jest niew�a�ciwy (tzn. kiedy serever.gracz.id != android.gracz.id, czyli nick jest zaj�ty)
				Toast.makeText(activity, R.string.nick_incorrect, Toast.LENGTH_LONG).show();
			}
			findViewById(R.id.progress_bar_nick).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_nick).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			isSuccessful = sendNickToServer();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie pobrania listy dru�yn z serwera, 
	 * nast�pnie dodaje elementy tej list do pola wyboru 
	 * oraz ods�ania elementy zwi�zane z wyborem dru�yny
	 * */
	private class DownloadTeamListTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... arg0) {
			downloadTeamListFromServer();
			return null;
		}
	}
}
