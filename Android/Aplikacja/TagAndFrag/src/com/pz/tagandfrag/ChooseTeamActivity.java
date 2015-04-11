package com.pz.tagandfrag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.pz.tagandfrag.restclient.Team;


public class ChooseTeamActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_team);
		prepareActivity();
		initializeViewComponents();
	}
	/////////////////////////////////
	/* Ustawienia aplikacji */
	/**
	 * Przygotowujê dane z których bêdzie korzystaæ aplikacja
	 * */
	public void prepareActivity() {
		
		//Przygotowanie klas pomocniczych
		TagAndFragContainer.preferences.loadTeamDataFromPreferences();
	}
	/////////////////////////////////
	/* Zmiany w wygl¹dzie */
	/**
	 * Zmienia wygl¹d na podstawie pobranych ustawieñ z pamiêci
	 * */
	public void initializeViewComponents() {
		addRadioButtons();
	}
	/**
	 * Tworzy listê dostêpnych dru¿yn na bazie
	 * */
    public void addRadioButtons() {
    	//Dodaæ przycisk odœwie¿ania
    	RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
    	radioGroupTeam.removeAllViews();
    	
        List<Team> teamList = new ArrayList<Team>(TagAndFragContainer.teamList);
        
        //Jeœli apka siê po³o¿y³a lub gracz wyszed³ z gry
        //To na górze jest pokazywana ostatnio wybrana dru¿yna
        if(TagAndFragContainer.preferences.getTeam() > 0) {
        	RadioButton radioButtonTeam = new RadioButton(this);
            radioButtonTeam.setId(TagAndFragContainer.preferences.getTeam());
            radioButtonTeam.setText(String.format("%d. dru¿yna (w trakcie gry)", 
            		TagAndFragContainer.preferences.getTeam()));
            radioButtonTeam.setChecked(true);
            radioGroupTeam.addView(radioButtonTeam);
        }
        
        //Wrzucenie wszystkich teamów na listê wybieraln¹
        for (Team team : teamList) {
        	if(TagAndFragContainer.preferences.getTeam() != team.getId())
        	{
	            RadioButton radioButtonTeam = new RadioButton(this);
	            radioButtonTeam.setId(team.getId());
	            radioButtonTeam.setText(team.toString());
	            radioGroupTeam.addView(radioButtonTeam);
        	}
        }
    }
	/////////////////////////////////
	/* Listenery */
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wys³anie do serwera informacji o wybranej dru¿ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		
		//Pobranie, który team zosta³ wybrany
		RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
		Integer teamNumber = radioGroupTeam.getCheckedRadioButtonId();
		//Zapisanie zespo³u do klas pomocniczych i pamiêci
		TagAndFragContainer.player.setTeam(teamNumber);
		TagAndFragContainer.preferences.setTeam(teamNumber);
		TagAndFragContainer.preferences.saveTeamDataFromPreferences();
		//Rozpoczêcie nowej gry
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	/////////////////////////////////
	/* £¹cznoœæ - REST Client */
	/**
	 * Wys³anie do serwera numeru wybranej dru¿yny, odebranie kodu broni
	 * */
	private int sendTeamToServerAndGetWeaponCode() {
		int weaponCode = 0;
		try {
			weaponCode = TagAndFragContainer.game.team(TagAndFragContainer.player);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return weaponCode;
	}
	/////////////////////////////////
	/* Prywatne klasy */
	/**
	 * Klasa wykonuje zadanie rozpoczêcia nowej gry, 
	 * najpierw pobiera nowy kod strza³u, utawia na broni nowy kod strza³u
	 * a nastêpnie pobiera listê wszystkich graczy z dru¿yny danego gracza
	 * */
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			Intent intent = new Intent(ChooseTeamActivity.this, GameActivity.class);
			startActivity(intent);
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.INVISIBLE);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//Dopisaæ obs³ugê przypisania weaponcode - obs³uga bluetootha
			sendTeamToServerAndGetWeaponCode();
			return null;
		}
	}
}
