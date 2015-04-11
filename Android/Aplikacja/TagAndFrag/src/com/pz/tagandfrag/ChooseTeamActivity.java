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
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	public void prepareActivity() {
		
		//Przygotowanie klas pomocniczych
		TagAndFragContainer.preferences.loadTeamDataFromPreferences();
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	/**
	 * Zmienia wygl�d na podstawie pobranych ustawie� z pami�ci
	 * */
	public void initializeViewComponents() {
		addRadioButtons();
	}
	/**
	 * Tworzy list� dost�pnych dru�yn na bazie
	 * */
    public void addRadioButtons() {
    	//Doda� przycisk od�wie�ania
    	RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
    	radioGroupTeam.removeAllViews();
    	
        List<Team> teamList = new ArrayList<Team>(TagAndFragContainer.teamList);
        
        //Je�li apka si� po�o�y�a lub gracz wyszed� z gry
        //To na g�rze jest pokazywana ostatnio wybrana dru�yna
        if(TagAndFragContainer.preferences.getTeam() > 0) {
        	RadioButton radioButtonTeam = new RadioButton(this);
            radioButtonTeam.setId(TagAndFragContainer.preferences.getTeam());
            radioButtonTeam.setText(String.format("%d. dru�yna (w trakcie gry)", 
            		TagAndFragContainer.preferences.getTeam()));
            radioButtonTeam.setChecked(true);
            radioGroupTeam.addView(radioButtonTeam);
        }
        
        //Wrzucenie wszystkich team�w na list� wybieraln�
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
	 * Nas�uchuje ( ;) ) klikni�cia na przycisk odpowiedzialny za wys�anie do serwera informacji o wybranej dru�ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		
		//Pobranie, kt�ry team zosta� wybrany
		RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
		Integer teamNumber = radioGroupTeam.getCheckedRadioButtonId();
		//Zapisanie zespo�u do klas pomocniczych i pami�ci
		TagAndFragContainer.player.setTeam(teamNumber);
		TagAndFragContainer.preferences.setTeam(teamNumber);
		TagAndFragContainer.preferences.saveTeamDataFromPreferences();
		//Rozpocz�cie nowej gry
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	/////////////////////////////////
	/* ��czno�� - REST Client */
	/**
	 * Wys�anie do serwera numeru wybranej dru�yny, odebranie kodu broni
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
	 * Klasa wykonuje zadanie rozpocz�cia nowej gry, 
	 * najpierw pobiera nowy kod strza�u, utawia na broni nowy kod strza�u
	 * a nast�pnie pobiera list� wszystkich graczy z dru�yny danego gracza
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
			//Dopisa� obs�ug� przypisania weaponcode - obs�uga bluetootha
			sendTeamToServerAndGetWeaponCode();
			return null;
		}
	}
}
