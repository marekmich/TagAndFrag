package com.pz.tagandfrag.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.restclient.Team;

/**
 * Aktywnoœæ odpowiedzialna za obs³ugê i wyœwietlenie ekranu wyboru dru¿yñ
 * @author £ukasz ¯urawski
 */
public class ChooseTeamActivity extends Activity {
	
	/**
	 * Pobiera z pamiêci ostatnio wybran¹ dru¿ynê (jeœli istnieje) wpp. przypisuje zero,
	 * a nastêpnie uruchamia zadanie odpowiedzialne za pobranie listy dostêpnych dru¿yn.
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_team);
		prepareActivity();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
	 * */
	public void prepareActivity() {
		
		//Przygotowanie klas pomocniczych
		DataManager.preferences.loadTeamDataFromPreferences();
		
		//Pobranie listy dru¿yn
		//BuG?
		DownloadTeamListTask task = new DownloadTeamListTask();
		task.execute();
	}
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Tworzy listê dostêpnych dru¿yn na bazie
	 * */
    public void addRadioButtons() {
    	//Dodaæ przycisk odœwie¿ania
    	RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
    	radioGroupTeam.removeAllViews();
    	
        List<Team> teamList = new ArrayList<Team>(DataManager.teamList);
        
        //Jeœli apka siê po³o¿y³a lub gracz wyszed³ z gry
        //To na górze jest pokazywana ostatnio wybrana dru¿yna
        if(DataManager.preferences.getTeam() > 0) {
        	int myTeam = DataManager.preferences.getTeam();
        	int myVisibleTeam = (myTeam % 2 == 0) ? myTeam / 2 : (int) Math.ceil(((double) myTeam / 2));

        	RadioButton radioButtonTeam = new RadioButton(this);
        	radioButtonTeam.setId(DataManager.preferences.getTeam());
        	radioButtonTeam.setTextSize(23f);
        	radioButtonTeam.setTypeface(radioButtonTeam.getTypeface(), Typeface.BOLD);
        	radioButtonTeam.setText(String.format("%d. dru¿yna (w trakcie gry)", myVisibleTeam));
            if (myTeam % 2 == 0) {
            	radioButtonTeam.setShadowLayer(30, 0, 0, Color.BLUE);
            } else { 
            	radioButtonTeam.setShadowLayer(30, 0, 0, Color.RED);
            }
            radioButtonTeam.setChecked(true);
            radioGroupTeam.addView(radioButtonTeam);
        }
        
        //Wrzucenie wszystkich teamów na listê wybieraln¹
        for (Team team : teamList) {
        	if(DataManager.preferences.getTeam() != team.getId())
        	{
	            RadioButton radioButtonTeam = new RadioButton(this);
	            int visibleTeamId = (team.getId() % 2 == 0) ? team.getId() / 2 : (int) Math.ceil(((double) team.getId() / 2));
            	radioButtonTeam.setId(team.getId());
	            radioButtonTeam.setText(String.format("%d) Iloœæ graczy: %d", visibleTeamId, team.getSize()));
	            radioButtonTeam.setTextSize(22f);
	            radioButtonTeam.setTypeface(radioButtonTeam.getTypeface(), Typeface.BOLD);

	            if (team.getId() % 2 == 0) {
	            	radioButtonTeam.setShadowLayer(15, 0, 0, Color.RED);
	            } else { 
	            	radioButtonTeam.setShadowLayer(15, 0, 0, Color.BLUE);
	            }
	            
	            radioGroupTeam.addView(radioButtonTeam);
        	}
        }
    }
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje  klikniêcia na przycisk odpowiedzialny za wys³anie do serwera informacji o wybranej dru¿ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		
		//Pobranie, który team zosta³ wybrany
		RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
		Integer teamNumber = radioGroupTeam.getCheckedRadioButtonId();
		//Zapisanie zespo³u do klas pomocniczych i pamiêci
		DataManager.player.setTeam(teamNumber);
		DataManager.preferences.setTeam(teamNumber);
		DataManager.preferences.saveTeamDataFromPreferences();
		//Rozpoczêcie nowej gry
		SendTeamProgressBarTask task = new SendTeamProgressBarTask();
		task.execute();
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.back_pressed_info), Toast.LENGTH_LONG).show();
	}
	
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
	/**
	 * Wys³anie do serwera numeru wybranej dru¿yny, odebranie kodu broni
	 * */
	private int sendTeamToServerAndGetWeaponCode() {
		int weaponCode = 0;
		try {
			weaponCode = DataManager.game.team(DataManager.player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return weaponCode;
	}
	/**
	 * Pobiera z serwera listê dru¿yn
	 */
	private void downloadTeamListFromServer() {
		ArrayList<Team> array = new ArrayList<Team>();
		try {
			array = new ArrayList<Team>(DataManager.game.list());
		} catch (IOException e) {
			Log.d("IO", e.toString());
		} catch (JSONException e) {
			Log.d("JSON", e.toString());
		}
		Collections.sort(array);
		DataManager.teamList = array;
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuje zadanie wys³ania do serwera informacji o wybranej dru¿ynie
	 * nastêpnie pobrania kodu broni
	 * */
	private class SendTeamProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(ChooseTeamActivity.this, StandbyActivity.class);
			startActivity(intent);
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			DataManager.weaponCode = sendTeamToServerAndGetWeaponCode();
			return null;
		}
	}
	/**
	 * Klasa wykonuje zadanie pobrania listy dru¿yn z serwera, 
	 * nastêpnie dodaje elementy tej list do pola wyboru 
	 * oraz ods³ania elementy zwi¹zane z wyborem dru¿yny
	 * */
	private class DownloadTeamListTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected void onPostExecute(Void result) {
			findViewById(R.id.progress_bar_team_download).setVisibility(ProgressBar.INVISIBLE);
			findViewById(R.id.scroll_view).setVisibility(ProgressBar.VISIBLE);
			addRadioButtons();
			findViewById(R.id.button_team).setVisibility(Button.VISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_team_download).setVisibility(ProgressBar.VISIBLE);
			findViewById(R.id.scroll_view).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			downloadTeamListFromServer();
			return null;
		}
	}
}
