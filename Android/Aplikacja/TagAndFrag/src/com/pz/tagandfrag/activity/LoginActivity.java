package com.pz.tagandfrag.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.dialogs.ChooseWeaponDialog;
import com.pz.tagandfrag.dialogs.ConnectionsErrorDialog;
import com.pz.tagandfrag.dialogs.ShowNewIdDialog;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.managers.PreferencesManager;
import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

/**
 * Aktywno�� odpowiedzialna za wy�wietlanie i obs�ug� ekranu logowania
 * @author �ukasz �urawski
 */
public class LoginActivity extends Activity {

	public static boolean GPS_ENABLED = false;
	public static boolean WIFI_ENABLED = false;
	
	/**
	 * Przygotowuj� statyczne pola, kt�re b�d� wykorzystywane w ca�ej aplikacji.
	 * Pobiera z pami�ci ustawienia aplikacji
	 * a nast�pnie modyfikuj� pola z nickiem i has�em na podstawie 
	 * ustawie� pobranych z pami�ci. 
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		prepareActivity();
		initializeViewComponents();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		if (WIFI_ENABLED && GPS_ENABLED) {
			new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
		} else {
			checkConnections();
		}
	}


	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	private void prepareActivity() {
		
		//Przygotowanie bluetootha
		DataManager.bluetoothService = new BluetoothService();
		forceEnableBluetooth();
		
		//Sprawdzam polaczenia
		checkConnections();
		
		//Przygotowanie klas pomocniczych
		DataManager.preferences = new PreferencesManager(getApplicationContext());
		DataManager.preferences.loadLoginDataFromPreferences();
		
		DataManager.game = new Game(DataManager.DAMAGE);
		DataManager.player = new Player(DataManager.preferences.getNick(), DataManager.preferences.getId());
		DataManager.teamList = new ArrayList<Team>();
		DataManager.players = new ArrayList<Player>();
		DataManager.oppositePlayers = new ArrayList<Player>();
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
		CheckBox alreadyHaveAccount = (CheckBox) findViewById(R.id.already_have_account_checkbox);
		
		//Sprawdza czy nick pobrany z preferencji jest r�ny od hinta
		if(!DataManager.preferences.getNick().equals(getResources().getString(R.string.preferences_name_default)))
		{
			nickEditText.setText(DataManager.preferences.getNick());
			alreadyHaveAccount.setChecked(true);
		}
	}
	/** 
	 * Ustawia has�o w polu do kt�rego wpisuje si� has�o lub zostawia puste*/
	private void setPasswordOnPasswordEditText() {
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		//Sprawdza czy id pobrane z preferencji jest r�ne od zera
		if(DataManager.preferences.getId() != 0)
		{
			passwordEditText.setText(String.valueOf(DataManager.preferences.getId()));
		} else {
			passwordEditText.setVisibility(EditText.INVISIBLE);
		}
	}
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas�uchuje  klikni�cia na przycisk odpowiedzialny za zalogowanie si� do serwera, wykonuje zadanie
	 * poprzez uruchomienia zadania w tle z klasy {@link NickCheckProgressBarTask}
	 */
	public void onNickConfirmButtonClicked(View view) {
		
		EditText nickEditText = (EditText) findViewById(R.id.edit_text_login);
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		
		Button loginButton = (Button) findViewById(R.id.button_login);
		loginButton.setEnabled(false);
		
		CheckBox checkBox = (CheckBox) findViewById(R.id.already_have_account_checkbox);
		
		//Sprawdzenie czy nick jest odpowiednio d�ugi oraz czy has�o ma w�a�ciw� d�ugo��
		if(nickEditText.getText().toString().length() >= 4 && 
				((DataManager.preferences.getId() !=0 && passwordEditText.getText().toString().length() == 7) 
						|| (DataManager.preferences.getId() !=0 && passwordEditText.getText().toString().length() == 0 && !(checkBox.isChecked())) 
						|| DataManager.preferences.getId()==0 ))
		{
			String nick = nickEditText.getText().toString();
			int password = 0;
			//Zapisanie do klasy pomocniczej oraz pami�ci nicku gracza
			DataManager.player.setName(nick);
			DataManager.preferences.setNick(nick);
			//Sprawdzenie czy pole z has�em jest niepuste
			if(!TextUtils.isEmpty(passwordEditText.getText()))
			{
				password = Integer.valueOf(passwordEditText.getText().toString());
				DataManager.player.setId(password);
				DataManager.preferences.setId(password);
			}
			if(!(checkBox.isChecked()))
			{
				DataManager.player.setId(0);
				DataManager.preferences.setId(0);
			}
			//Uruchomienie zadania w tle, kt�re przesy�a dane do serwera (r�wnie� weryfikuje odpowied� serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask(this);
			task.execute();
		}
		else if(passwordEditText.getText().toString().length() != 7)
		{
			//Kiedy has�o jest niew�a�ciwe
			Toast.makeText(this, R.string.password_length_wrong, Toast.LENGTH_LONG).show();
			if (!loginButton.isEnabled()) {
				loginButton.setEnabled(true);
			}
		}
		else
		{
			//Kiedy nick jest niew�a�ciwy
			Toast.makeText(this, R.string.nick_length_wrong, Toast.LENGTH_LONG).show();
			if (!loginButton.isEnabled()) {
				loginButton.setEnabled(true);
			}
		}
	}

	public void onAlreadyHaveAccountClicked(View view) {
		CheckBox alreadyHaveAccount = (CheckBox) findViewById(R.id.already_have_account_checkbox);
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);

		if (alreadyHaveAccount.isChecked()) {
			passwordEditText.setVisibility(EditText.VISIBLE);
		} else {
			passwordEditText.setVisibility(EditText.INVISIBLE);
		}
	}
		
	/////////////////////////////////
	/* ��czno�� - Bluetooth */
	/**
	 * Wymusza w��czenia bluetooth.
	 * */
	private void forceEnableBluetooth() {
		DataManager.bluetoothService.getBluetoothAdapter().enable();
	}
	
	/**
	 * Sprawdza konieczne polaczenia. Jezeli ktoregos nie ma, wyswietla dialog z przekierowaniem do ustawien.
	 * @see ConnectionsErrorDialog
	 */
	private void checkConnections() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (wifiManager.isWifiEnabled()) {
			WIFI_ENABLED = true;
		}
		
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			GPS_ENABLED = true;
		}
		
		if (!GPS_ENABLED || !WIFI_ENABLED) {
			new ConnectionsErrorDialog().show(getFragmentManager(), "C_ERR");
		}
	}
	
	/**
	 * Wybieranie broni - urz�dzenia z listy
	 * @param activity 
	 * */
	private void chooseWeapon(Activity activity) {
		new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
	}
	
	/**
	 * Pokazanie okienka z nowym ID
	 */
	private void showDialogWithNewId() {
		new ShowNewIdDialog().show(getFragmentManager(), "ID");
	}
	/////////////////////////////////
	/* ��czno�� - REST Client */
	/**
	 * Wysy�a nick i has�o do serwera w celu weryfikacji
	 * */
	private Boolean sendNickToServer()
	{
		int id = DataManager.preferences.getId();
		
		int newId = 0;
		try {
			newId = DataManager.game.check(DataManager.player);
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		//NICK jest poprawny (sytuacja kiedy gracz gra� wcze�niej i podany nick znajduj� si� w bazie)
		if(newId == id && id > 0)
		{
			return true;
		}
		//NICK jest poprawny (sytuacja kiedy gracz nie gra� wcze�niej i podany nick nie jest zaj�ty)
		else if(newId > 0 && id == 0)
		{
			id = newId;
			DataManager.player.setId(id);
			DataManager.preferences.setId(id);
			return true;
		}
		//NICK jest poprawny (sytuacja kiedy gracz gra� wcze�niej i chce za�o�y� nowe konto)
		else if(newId > 0 && id > 0)
		{
			id = newId;
			DataManager.player.setId(id);
			DataManager.preferences.setId(id);
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
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuj�ca zadanie weryfikacji nicku i has�a na serwerze
	 * nast�pnie wywo�uje okienko wyboru broni, by potem przej�� do {@link ChooseTeamActivity}
	 * */
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
				EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
				DataManager.preferences.saveLoginDataToPreferences();
				DataManager.preferences.loadMACDataFromPreferences();
				if (TextUtils.isEmpty(passwordEditText.getText())) {
					showDialogWithNewId();
				} else {
					chooseWeapon(activity);
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
}
