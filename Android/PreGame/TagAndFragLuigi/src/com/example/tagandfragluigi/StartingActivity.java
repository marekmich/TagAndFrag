package com.example.tagandfragluigi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.tagandfragluigi.restclient.Team;

public class StartingActivity extends Activity {
	
	String nick;
	int id;
	int stage;
	
	int weaponCode;
	
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
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
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
	 * Przechodzi pomiêdzy kolejnymi etapami wed³ug schematu (wys³any na PZ14)
	 * */
	private void nextStage()
	{
		//Etapy:
		//stage = 0: wybranie imienia gracza
		//stage = 1: po³¹czenie siê z broni¹
		//stage = 2: wybranie dru¿yny
		//stage = 3: potwierdzenie gotowoœci do gry (w tym momencie gracze powinni byæ ju¿ daleko od siebie) 
		//[po wciœniêciu potwierdzenia gotowoœci nale¿y odblokowaæ broñ]
		stage++;
		blockButtons();
		changeComponentsVisibility();
	}
	/**
	 * Przygotowanie ustawieñ z biblioteki klienta restowego do wspó³dziania 
	 * */
	private void prepareRestClient()
	{
		// TODO poprawiæ komentarz?
		game = new Game();
		player = new Player(nick, id);
	}
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Zmienia wygl¹d na podstawie pobranych ustawieñ z pamiêci
	 * */
	private void initializeViewComponents() {
		setNewNickOnNickEditText();

	}
	/**
	 * Blokuje przyciski, które ju¿ nie powinny byæ u¿ywane
	 * */
	private void blockButtons() {
		//patrz na nextStage() jeœli chodzi o etapy
		if(stage == 1)
		{
			//zablokowanie przycisku odpowiedzialnego za wys³anie nicku do serwera
			findViewById(R.id.nick_confirm_button).setEnabled(false);
		}
		else if(stage == 2)
		{
			//zablokowanie przycisku odpowiedzialnego za wybieranie broni
			findViewById(R.id.choose_weapon_button).setEnabled(false);
		}
		else if(stage == 3)
		{
			//zablokowanie przycisku odpowiedzialnego za wybieranie dru¿yny
			findViewById(R.id.choose_team_button).setEnabled(false);
		}
	}
	/**
	 * Zmienia widocznoœæ (ujawnia) kolejnych elementów po przejœciu do kolejnych etapów
	 * */
	private void changeComponentsVisibility() {
		//patrz na nextStage() jeœli chodzi o etapy
		if(stage == 1)
		{
			//Ods³oniêcie pól zwi¹zanych z wyborem broni
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
	 * Ustawia nick lub podpowiedŸ (zale¿y od tego czy w preferencjach aplikacji jest przechowywany nick) w polu do którego wpisuje siê nick
	 * */
	private void setNewNickOnNickEditText() {
		EditText nickEditText = (EditText) findViewById(R.id.nick_edit_text);
		//Sprawdza czy nick pobrany z preferencji jest ró¿ny od hinta
		if(!nick.equals(getResources().getString(R.string.preferences_name_default)))
		{
			nickEditText.setText(nick);
		}
	}
	/**
	 * Dodaje dru¿yny pobrane z serwera na listê rozwijaln¹
	 */
	public void addItemOnChooseTeamSpinner(ArrayList<Team> teamList) {
		Spinner spinner = (Spinner) findViewById(R.id.choose_team_spinner);
		
		List<String> list = new ArrayList<String>();
		for( Team team : teamList) {
			list.add(team.toString());
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wys³anie nicku do serwera 
	 * poprzez uruchomienia zadania z klasy {@link NickCheckProgressBarTask}
	 */
	public void onNickConfirmButtonClicked(View view) {
		EditText nickEditText = (EditText) findViewById(R.id.nick_edit_text);
		//Sprawdzenie czy nick jest odpowiednio d³ugi
		if(nickEditText.getText().toString().length() >= 4)
		{
			nick = nickEditText.getText().toString();
			player.setName(nick);
			//Uruchomienie zadania w tle, które przesy³a dane do serwera (równie¿ weryfikuje odpowiedŸ serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask(this);
			task.execute();
		}
		else
		{
			//Kiedy nick jest za krótki pojawia siê komunikat
			Toast.makeText(this, R.string.nick_too_short, Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wybranie i po³¹czneie siê z broni¹, 
	 * jednoczêsnie wys³a ¿¹danie do serwera o aktualn¹ listê dru¿yn do serwera
	 * poprzez uruchomienia zadania z klasy {@link WeaponConnectProgressBarTask} oraz {@link DownloadTeamListTask}
	 */
	public void onChooseWeaponButtonClicked(View view) {
		// TODO po sprawdzeniu WeaponChooser odkomentowaæ 
		//new WeaponChooser(this).buildWindow().show();
		WeaponConnectProgressBarTask firstTask = new WeaponConnectProgressBarTask();
		firstTask.execute();
		DownloadTeamListTask secondTask = new DownloadTeamListTask();
		secondTask.execute();
	}
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wys³anie do serwera informacji o wybranej dru¿ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		TeamConnectProgressBarTask task = new TeamConnectProgressBarTask();
		task.execute();
	}
	
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za zmianê kodu broni, 
	 * odblokowanie broni, a nastêpnie rozpoczêcie gry
	 * poprzez uruchomienia zadania z klasy {@link }
	 */
	public void onStartGameButtonClicked(View view) {
		// TODO prawdopodobnie bêdzie trzeba dodaæ parametr do konstruktora StartGameProgressBar wskazuj¹cy na t¹ aktywnoœæ
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
	        case R.id.show_id:
	        	ShowIDDialog dialog = new ShowIDDialog();
	        	dialog.show(this.getFragmentManager(), "show-id");
	            return true;
	        case R.id.change_id:
	        	// TODO zmiana id
	            return true;
	        case R.id.information:
	        	// TODO pokazanie inforamcji o aplikacji (wersja i te sprawy)
	        case R.id.help:
	        	// TODO pokazanie helpa
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	/////////////////////////////////
	/* £¹cznoœæ - bluetooth */
	
	/////////////////////////////////
	/* £¹cznoœæ - REST */
	/**
	 * Wys³anie do serwera gracz.name i gracz.id, a nastêpnie odebraniu gracz.id z serwera
	 * Wszystko ma na celu sprawdzenie czy nick jest wolny/zajêty oraz czy przynale¿y do danej komórki (poprzez id siê to sprawdza)
	 * */
	public Boolean sendNickToServer()
	{
		int newId = 0;
		try {
			newId = game.check(player);
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
			player.setId(id);
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
	public ArrayList<Team> downloadTeamListFromServer() {
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
		return array;
	}
	
	public int sendTeamToServerAndGetWeaponCode() {
		
		Spinner spinner = (Spinner) findViewById(R.id.choose_team_spinner);
		String team = spinner.getSelectedItem().toString();
		String[] splitedTeam = team.split(". ");
		Integer teamNumber = Integer.valueOf(splitedTeam[0]);
		player.setTeam(teamNumber);
		int weaponCode = 0;
		try {
			weaponCode = game.team(player);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("CODE", String.valueOf(weaponCode));
		return weaponCode;
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuje zadanie wys³ania nicku i id gracza do serwera w celu sprawdzenia,
	 * nastêpnie na bazie wyniku tego sprawdzenia:
	 * a) ods³ania elementy wyboru broni
	 * b) wyœwietla komunikat o b³êdzie
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
				// TODO rozwi¹zaæ problem
				saveDataToPreferences();
				nextStage();
			}
			else {
				//Kiedy nick jest niew³aœciwy (tzn. kiedy serever.gracz.id != android.gracz.id, czyli nick jest zajêty)
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
	 * Klasa wykonuje zadanie po³¹czenia siê z broni¹ przez bluetootha
	 * */
	private class WeaponConnectProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO Obs³u¿enie wyniku po³¹czenia! (sprawdzenie czy po³¹czenie siê powiod³o) - potrzebne?			
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.choose_weapon_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Po³¹czenie siê z broni¹
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie pobrania listy dru¿yn z serwera, 
	 * nastêpnie dodaje elementy tej list do pola wyboru 
	 * oraz ods³ania elementy zwi¹zane z wyborem dru¿yny
	 * */
	private class DownloadTeamListTask extends AsyncTask<Void, Void, Void> {
		ArrayList<Team> array;
		@Override
		protected void onPostExecute(Void result) {
			addItemOnChooseTeamSpinner(array);
			findViewById(R.id.choose_weapon_progress_bar).setVisibility(ProgressBar.INVISIBLE);
			nextStage();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			array = downloadTeamListFromServer();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie wys³ania informacji do serwera o wybranej dru¿ynie
	 * nastêpnie ods³ania elementy potwierdzenia gotowœci, jednoczeœnie zas³aniaj¹c pozosta³e
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
			sendTeamToServerAndGetWeaponCode();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie zmienienia kodu oraz odblokowania broni, a nastêpnie rozpoczêcia nowej gry
	 * */
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO przejœcie do GameActivity poprzez Intent
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
	
	// TODO Poprawiæ kod i przetestowaæ (wybieranie broni poprzez bluetooth)
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
	/*
	 * 
	 * */
	private class ShowIDDialog extends DialogFragment {
		
		public ShowIDDialog() {
			super();
		}

		// TODO komentarz
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			// Przekazywanie aktywnosci jako parametr nie bylo konieczne
			// Jezeli wywolamy getActivity() w kontekscie fragmentu, zwroci on
			// aktywnosc ktora go hostuje, czyli StartingActivity
			// (powinno zadzialac rowniez podanie StartingActivity.this)
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View view = inflater.inflate(R.layout.dialog_change_id, null);
			builder.setView(view);
			
			// Wystarczy ze z zainicjalizowanego widoku pobierzemy nasz edit text. 
			final EditText yourIdEditText = (EditText) view.findViewById(R.id.your_id_edit_text);
			yourIdEditText.setText(String.valueOf(id));
			
			builder.setPositiveButton(R.string.your_id_positive_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						id = Integer.valueOf(yourIdEditText.getText().toString());
					}
				});
			
			builder.setNegativeButton(R.string.your_id_negative_button, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ShowIDDialog.this.getDialog().cancel();	
					}
				});
			
			// Moze jakis lepszy tytu³? I nie zahardkodowany.
			builder.setTitle("Czy chcesz zmieniæ ID?");
			return builder.create();
		}
	}
}
