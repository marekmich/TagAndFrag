package com.example.tagandfragluigi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.tagandfragluigi.restclient.Game;
import com.example.tagandfragluigi.restclient.Player;

public class StartingActivity extends Activity {

	String nick;
	int id;
	int stage;
	
	Game game;
	Player player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prepareActivity();
		setContentView(R.layout.activity_starting);
		initializeViewComponents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_starting_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
	}

	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	private void prepareActivity() {
		stage = 0;
		loadDataFromPreferences();
		prepareRestClient();
	}
	/** 
	 * Pobiera dane z preferencji
	 * */
	private void loadDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		Context context = StartingActivity.this;
		SharedPreferences preferences = context.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Pobranie id gracza
		int defaultId = 0;
		id = preferences.getInt(getResources().getString(R.string.preferences_id), defaultId);
		//Pobranie nicku gracza
		String defaultName = getResources().getString(R.string.preferences_name_default);
		nick = preferences.getString(getResources().getString(R.string.preferences_name), defaultName);
	}
	/**
	 * Zapisuje dane do preferencji
	 * */
	private void saveDataToPreferences() {
		//Ustawienie uchwytu na preferencje
		Context context = StartingActivity.this;
		SharedPreferences preferences = context.getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Odpalenie (?) edytora
		SharedPreferences.Editor editor = preferences.edit();
		//Zapisanie id i nicku gracza do preferencji
		editor.putInt(getResources().getString(R.string.preferences_id), id);
		editor.putString(getResources().getString(R.string.preferences_name), nick);
		editor.commit();
	}
	/**
	 * Przechodzi pomi�dzy kolejnymi etapami wed�ug schematu (wys�any na PZ14)
	 * */
	private void nextStage()
	{
		//Etapy:
		//stage = 0: wybranie imienia gracza
		//stage = 1: po��czenie si� z broni�
		//stage = 2: wybranie dru�yny
		//stage = 3: potwierdzenie gotowo�ci do gry (w tym momencie gracze powinni by� ju� daleko od siebie) 
		//[po wci�ni�ciu potwierdzenia gotowo�ci nale�y odblokowa� bro�]
		stage++;
		blockButtons();
		changeComponentsVisibility();
	}
	/**
	 * Przygotowanie ustawie� z biblioteki klienta restowego do wsp�dziania 
	 * */
	private void prepareRestClient()
	{
		// TODO poprawi� komentarz?
		game = new Game();
		player = new Player();
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	/**
	 * Zmienia wygl�d na podstawie pobranych ustawie� z pami�ci
	 * */
	private void initializeViewComponents() {
		setNewNickOnNickEditText();
	}
	/**
	 * Blokuje przyciski, kt�re ju� nie powinny by� u�ywane
	 * */
	private void blockButtons() {
		//patrz na nextStage() je�li chodzi o etapy
		if(stage == 1)
		{
			//zablokowanie przycisku odpowiedzialnego za wys�anie nicku do serwera
			findViewById(R.id.nick_confirm_button).setEnabled(false);
		}
		else if(stage == 2)
		{
			//zablokowanie przycisku odpowiedzialnego za wybieranie broni
			findViewById(R.id.choose_weapon_button).setEnabled(false);
		}
		else if(stage == 3)
		{
			//zablokowanie przycisku odpowiedzialnego za wybieranie dru�yny
			findViewById(R.id.choose_team_button).setEnabled(false);
		}
	}
	/**
	 * Zmienia widoczno�� (ujawnia) kolejnych element�w po przej�ciu do kolejnych etap�w
	 * */
	private void changeComponentsVisibility() {
		//patrz na nextStage() je�li chodzi o etapy
		if(stage == 1)
		{
			//Ods�oni�cie p�l zwi�zanych z wyborem broni
			findViewById(R.id.choose_weapon_button).setVisibility(Button.VISIBLE);
		}
		else if(stage == 2)
		{
			findViewById(R.id.choose_team_spinner).setVisibility(Spinner.VISIBLE);
			findViewById(R.id.choose_team_button).setVisibility(Button.VISIBLE);
		}
		else if(stage == 3)
		{
			findViewById(R.id.start_game_button).setVisibility(Button.VISIBLE);
		}
	}
	/**
	 * Ustawia nick lub podpowied� (zale�y od tego czy w preferencjach aplikacji jest przechowywany nick) w polu do kt�rego wpisuje si� nick
	 * */
	private void setNewNickOnNickEditText() {
		EditText nickEditText = (EditText) findViewById(R.id.nick_edit_text);
		//Sprawdza czy nick pobrany z preferencji jest r�ny od hinta
		if(!nick.equals(getResources().getString(R.string.preferences_name_default)))
		{
			nickEditText.setText(nick);
		}
	}
	/**
	 * Dodaje dru�yny pobrane z serwera na list� rozwijaln�
	 */
	public void addItemOnChooseTeamSpinner() {
		Spinner spinner = (Spinner) findViewById(R.id.choose_team_spinner);
		
		// TODO obrobi� list� by pasowa�a do za�o�e�
		List<String> list = new ArrayList<String>();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za wys�anie nicku do serwera 
	 * poprzez uruchomienia zadania z klasy {@link NickCheckProgressBarTask}
	 */
	public void onNickConfirmButtonClicked(View view) {
		EditText nickEditText = (EditText) findViewById(R.id.nick_edit_text);
		//Sprawdzenie czy nick jest odpowiednio d�ugi
		if(nickEditText.getText().toString().length() >= 4)
		{
			nick = nickEditText.getText().toString();
			//Uruchomienie zadania w tle, kt�re przesy�a dane do serwera (r�wnie� weryfikuje odpowied� serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask(this);
			task.execute();
		}
		else
		{
			//Kiedy nick jest za kr�tki pojawia si� komunikat
			Toast.makeText(this, R.string.nick_too_short, Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za wybranie i po��czneie si� z broni�, 
	 * jednocz�snie wys�a ��danie do serwera o aktualn� list� dru�yn do serwera
	 * poprzez uruchomienia zadania z klasy {@link WeaponConnectProgressBarTask} oraz {@link DownloadTeamListTask}
	 */
	public void onChooseWeaponButtonClicked(View view) {
		// TODO po sprawdzeniu WeaponChooser odkomentowa� 
		//new WeaponChooser(this).buildWindow().show();
		WeaponConnectProgressBarTask firstTask = new WeaponConnectProgressBarTask();
		firstTask.execute();
		DownloadTeamListTask secondTask = new DownloadTeamListTask();
		secondTask.execute();
	}
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za wys�anie do serwera informacji o wybranej dru�ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		TeamConnectProgressBarTask task = new TeamConnectProgressBarTask();
		task.execute();
	}
	
	/**
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za zmian� kodu broni, 
	 * odblokowanie broni, a nast�pnie rozpocz�cie gry
	 * poprzez uruchomienia zadania z klasy {@link }
	 */
	public void onStartGameButtonClicked(View view) {
		// TODO prawdopodobnie b�dzie trzeba doda� parametr do konstruktora StartGameProgressBar wskazuj�cy na t� aktywno��
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
	        case R.id.show_id:
	            // TODO pokazywanie id
	            return true;
	        case R.id.change_id:
	        	// TODO zmiana id
	            return true;
	        case R.id.information:
	        	// TODO pokazanie inforamcji o aplikacji (wersja i te sprawy)
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	/////////////////////////////////
	/* ��czno�� - bluetooth */
	
	/////////////////////////////////
	/* ��czno�� - REST */
	/**
	 * Wys�anie do serwera gracz.name i gracz.id, a nast�pnie odebraniu gracz.id z serwera
	 * Wszystko ma na celu sprawdzenie czy nick jest wolny/zaj�ty oraz czy przynale�y do danej kom�rki (poprzez id si� to sprawdza)
	 * */
	public Boolean sendNickToServer()
	{
		int newId = 0;
		try {
			newId = game.check(nick, id);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
	 * Klasa wykonuje zadanie wys�ania nicku i id gracza do serwera w celu sprawdzenia,
	 * nast�pnie na bazie wyniku tego sprawdzenia:
	 * a) ods�ania elementy wyboru broni
	 * b) wy�wietla komunikat o b��dzie
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
				saveDataToPreferences();
				nextStage();
			}
			else {
				//Kiedy nick jest niew�a�ciwy (tzn. kiedy serever.gracz.id != android.gracz.id, czyli nick jest zaj�ty)
				Toast.makeText(activity, R.string.nick_incorrect, Toast.LENGTH_LONG).show();
			}
			findViewById(R.id.nick_confirm_progress_bar).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.nick_confirm_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			isSuccessful = sendNickToServer();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie po��czenia si� z broni� przez bluetootha
	 * */
	private class WeaponConnectProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO Obs�u�enie wyniku po��czenia! (sprawdzenie czy po��czenie si� powiod�o) - potrzebne?			
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.choose_weapon_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Po��czenie si� z broni�
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
		protected void onPostExecute(Void result) {
			findViewById(R.id.choose_weapon_progress_bar).setVisibility(ProgressBar.INVISIBLE);
			nextStage();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Pobranie z serwera listy dru�yn, a nast�pnie wrzucenie ich do spinnera
			//addItemOnChooseTeamSpinner();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie wys�ania informacji do serwera o wybranej dru�ynie
	 * nast�pnie ods�ania elementy potwierdzenia gotow�ci, jednocze�nie zas�aniaj�c pozosta�e
	 * */
	private class TeamConnectProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			findViewById(R.id.choose_team_progress_bar).setVisibility(ProgressBar.INVISIBLE);
			nextStage();
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.choose_team_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Wys�anie do serwera wybranej dru�yny
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie zmienienia kodu oraz odblokowania broni, a nast�pnie rozpocz�cia nowej gry
	 * */
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO przej�cie do GameActivity poprzez Intent
			findViewById(R.id.start_game_progress_bar).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.start_game_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO zmiana kodu broni
			
			return null;
		}
	}
	
	// TODO Poprawi� kod i przetestowa� (wybieranie broni poprzez bluetooth)
	/* Do poprawienia itd.
	private class WeaponChooser extends AlertDialog {
		private Context context;
		public WeaponChooser(Context context) {
			super(context);
			this.context = context;
		}

		public AlertDialog buildWindow() {
			final Set<BluetoothDevice> devices = bluetoothService.getBluetoothAdapter().getBondedDevices();
			final ArrayList<String> devicesList = new ArrayList<String>(devices.size());
			
			for (BluetoothDevice device : devices) {
				devicesList.add(device.getName());
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder	.setTitle("Tytul")
					.setSingleChoiceItems(toListString(devicesList), 0, null)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Integer position = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
							deviceTextView.setVisibility(TextView.VISIBLE);
							connectButton.setVisibility(Button.VISIBLE);
							
							for (BluetoothDevice device : devices) {
								if (devicesList.get(position).equals(device.getName())) {
									MAC = device.getAddress();
								}
							}
							deviceTextView.setText("");
							deviceTextView.append("Twoje urzadzenie: " + devicesList.get(position) + "\n");
							deviceTextView.append("MAC: " + MAC + "\n");
						}
					})
					.setNegativeButton("Cancel", null);
				
			return builder.create();
		}
		
		private String[] toListString(ArrayList<String> list) {
			String[] listString = new String[list.size()];
			listString = list.toArray(listString);
			return listString;
		}
	}*/
}
