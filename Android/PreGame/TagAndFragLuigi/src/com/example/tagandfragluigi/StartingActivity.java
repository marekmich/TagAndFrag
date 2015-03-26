package com.example.tagandfragluigi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StartingActivity extends Activity {

	String nick;
	int id;
	int stage;
	
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
		getMenuInflater().inflate(R.menu.starting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
	 * */
	private void prepareActivity() {
		stage = 0;
		loadDataFromPreferences();
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
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * ???
	 * */
	private void initializeViewComponents() {
		// TODO napisaæ poprawny komentarz
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
			// TODO zrobiæ
			//Ods³oniêcie pól zwi¹zanych z wyborem dru¿yny
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
	
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wys³anie nicku do serwera 
	 */
	public void onNickConfirmButtonClicked(View view) {
		EditText nickEditText = (EditText) findViewById(R.id.nick_edit_text);
		//Sprawdzenie czy nick jest odpowiednio d³ugi
		if(nickEditText.getText().toString().length() >= 4)
		{
			nick = nickEditText.getText().toString();
			//Uruchomienie zadania w tle, które przesy³a dane do serwera (równie¿ weryfikuje odpowiedŸ serwera)
			NickCheckProgressBarTask task = new NickCheckProgressBarTask();
			task.execute();
		}
		else
		{
			//Kiedy nick jest za krótki pojawia siê komunikat
			Toast.makeText(this, "Nick jest za krótki", Toast.LENGTH_LONG).show();;
		}
	}
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wybranie i po³¹czneie siê z broni¹ 
	 */
	public void onChooseWeaponButtonClicked(View view) {
		// TODO po sprawdzeniu WeaponChooser odkomentowaæ 
		//new WeaponChooser(this).buildWindow().show();
		WeaponConnectProgressBarTask task = new WeaponConnectProgressBarTask();
		task.execute();
	}
	/////////////////////////////////
	/* £¹cznoœæ - bluetooth */
	
	/////////////////////////////////
	/* £¹cznoœæ - REST */
	
	/////////////////////////////////
	/* Prywatne klasy */
	private class NickCheckProgressBarTask extends AsyncTask<Void, Void, Void> {
		// TODO komentarze + zrobiæ wysy³anie!
		
		public NickCheckProgressBarTask() {
			super();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Obs³u¿enie wyników ¿¹dania! (sprawdzenie czy wynik jest poprawny)
			saveDataToPreferences();
			findViewById(R.id.nick_confirm_progress_bar).setVisibility(ProgressBar.INVISIBLE);
			nextStage();
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.nick_confirm_progress_bar).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Wyslanie ¿¹dania!
			return null;
		}
	}
	
	private class WeaponConnectProgressBarTask extends AsyncTask<Void, Void, Void> {
		// TODO komentarze + zrobiæ wysy³anie!
		
		public WeaponConnectProgressBarTask() {
			super();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Obs³u¿enie wyników ¿¹dania! (sprawdzenie czy po³¹czenie siê powiod³o)
			findViewById(R.id.choose_weapon_progress_bar).setVisibility(ProgressBar.INVISIBLE);
			nextStage();
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
}
