package com.pz.tagandfrag;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.dialogs.ChooseWeaponDialog;
import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

/**
 * Klasa odpowiedzialna za wyœwietlanie i obs³ugê ekranu logowania
 * @author £ukasz ¯urawski
 */
public class LoginActivity extends Activity {

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
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
	 * */
	private void prepareActivity() {
		
		//Przygotowanie bluetootha
		bluetoothService = new BluetoothService();
		requestEnableBluetooth();
		
		//Przygotowanie klas pomocniczych
		TagAndFragContainer.preferences = new PreferencesManager(getApplicationContext());
		TagAndFragContainer.preferences.loadLoginDataFromPreferences();
		
		TagAndFragContainer.game = new Game(10);
		TagAndFragContainer.player = new Player(TagAndFragContainer.preferences.getNick(), TagAndFragContainer.preferences.getId());
		TagAndFragContainer.teamList = new ArrayList<Team>();
		TagAndFragContainer.players = new ArrayList<Player>();
	}
	
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Zmienia wygl¹d na podstawie pobranych ustawieñ z pamiêci
	 * */
	private void initializeViewComponents() {
		setNewNickOnNickEditText();
		setPasswordOnPasswordEditText();
	}
	/**
	 * Ustawia nick lub podpowiedŸ (zale¿y od tego czy w preferencjach aplikacji jest przechowywany nick) w polu do którego wpisuje siê nick
	 * */
	private void setNewNickOnNickEditText() {
		EditText nickEditText = (EditText) findViewById(R.id.edit_text_login);
		//Sprawdza czy nick pobrany z preferencji jest ró¿ny od hinta
		if(!TagAndFragContainer.preferences.getNick().equals(getResources().getString(R.string.preferences_name_default)))
		{
			nickEditText.setText(TagAndFragContainer.preferences.getNick());
		}
	}
	/** 
	 * Ustawia has³o w polu do którego wpisuje siê has³o lub zostawia puste*/
	private void setPasswordOnPasswordEditText() {
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		//Sprawdza czy id pobrane z preferencji jest ró¿ne od zera
		if(TagAndFragContainer.preferences.getId() != 0)
		{
			passwordEditText.setText(String.valueOf(TagAndFragContainer.preferences.getId()));
		}
	}
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za zalogowanie siê do serwera, wykonuje zadanie
	 * poprzez uruchomienia zadania w tle z klasy {@link NickCheckProgressBarTask}
	 */
	public void onNickConfirmButtonClicked(View view) {
		EditText nickEditText = (EditText) findViewById(R.id.edit_text_login);
		EditText passwordEditText = (EditText) findViewById(R.id.edit_text_password);
		
		//Sprawdzenie czy nick jest odpowiednio d³ugi oraz czy has³o ma w³aœciw¹ d³ugoœæ
		if(nickEditText.getText().toString().length() >= 4 && 
				((TagAndFragContainer.preferences.getId() !=0 && passwordEditText.getText().toString().length() == 7) 
						|| TagAndFragContainer.preferences.getId()==0 ))
		{
			String nick = nickEditText.getText().toString();
			int password = 0;
			//Zapisanie do klasy pomocniczej oraz pamiêci nicku gracza
			TagAndFragContainer.player.setName(nick);
			TagAndFragContainer.preferences.setNick(nick);
			//Sprawdzenie czy pole z has³em jest niepuste
			if(!TextUtils.isEmpty(passwordEditText.getText()))
			{
				password = Integer.valueOf(passwordEditText.getText().toString());
				TagAndFragContainer.player.setId(password);
				TagAndFragContainer.preferences.setId(password);
			}
			//Uruchomienie zadania w tle, które przesy³a dane do serwera (równie¿ weryfikuje odpowiedŸ serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask(this);
			task.execute();
		}
		else if(passwordEditText.getText().toString().length() != 7)
		{
			//Kiedy has³o jest niew³aœciwe
			Toast.makeText(this, R.string.password_length_wrong, Toast.LENGTH_LONG).show();
		}
		else
		{
			//Kiedy nick jest niew³aœciwy
			Toast.makeText(this, R.string.nick_length_wrong, Toast.LENGTH_LONG).show();
		}
		setPasswordOnPasswordEditText();
	}
	
	/////////////////////////////////
	/* £¹cznoœæ - Bluetooth */
	/**
	 * Wymusza w³¹czenia bluetootha
	 * */
	private void requestEnableBluetooth() {
		if (!bluetoothService.getBluetoothAdapter().isEnabled()) {
			Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
		}
	}
	/**
	 * Wybieranie broni - urz¹dzenia z listy
	 * @param activity 
	 * */
	private void chooseWeapon(Activity activity) {
		new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
	}
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
	/**
	 * Wysy³a nick i has³o do serwera w celu weryfikacji
	 * */
	private Boolean sendNickToServer()
	{
		int id = TagAndFragContainer.preferences.getId();
		
		int newId = 0;
		try {
			newId = TagAndFragContainer.game.check(TagAndFragContainer.player);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		//NICK jest poprawny (sytuacja kiedy gracz gra³ wczeœniej i podany nick znajdujê siê w bazie)
		if(newId == id && id > 0)
		{
			
			return true;
		}
		//NICK jest poprawny (sytuacja kiedy gracz nie gra³ wczeœniej i podany nick nie jest zajêty)
		else if(newId > 0 && id == 0)
		{
			id = newId;
			TagAndFragContainer.player.setId(id);
			TagAndFragContainer.preferences.setId(id);
			return true;
		}
		//NICK nie jest poprawny (sytuacja kiedy gracz gra³ wczeœniej i poda³ inny nick, który jest zajêty przez kogoœ innego)
		else if(newId == 0 && id > 0)
		{
			return false;
		}
		//NICK nie jest poprawny (sytuacja kiedy gracz nie gra³ wczeœniej i poda³ nick, który jest zajêty przez kogoœ innego)
		else if(newId == 0 && id == 0)
		{
			return false;
		}
		//Sytuacjê pozosta³e (nigdy nie powinno dojœæ do tego momentu)
		return false;
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuj¹ca zadanie weryfikacji nicku i has³a na serwerze
	 * nastêpnie wywo³uje okienko wyboru broni, by potem przejœæ do {@link ChooseTeamActivity}
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
				//Kiedy nick jest w³aœciwy dane s¹ zapisywane do preferencji, progress bar jest chowany i przechodzi siê do kolejnego etapu
				TagAndFragContainer.preferences.saveLoginDataToPreferences();
				TagAndFragContainer.preferences.loadMACDataFromPreferences();
				chooseWeapon(activity);
			}
			else {
				//Kiedy nick jest niew³aœciwy (tzn. kiedy serever.gracz.id != android.gracz.id, czyli nick jest zajêty)
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
