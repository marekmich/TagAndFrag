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

import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

public class ChooseTeamActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_team);
		prepareActivity();
		initializeViewComponents();
	}
	
	public void prepareActivity() {
		
		LoginActivity.preferences.loadAllDataFromPreferences();
	}
	public void initializeViewComponents() {
		addRadioButtons();
	}
    public void addRadioButtons() {


    	RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
        List<Team> teamList = new ArrayList<Team>(LoginActivity.teamList);
        
        for (Team team : teamList) {
            RadioButton radioButtonTeam = new RadioButton(this);
            radioButtonTeam.setId(team.getId());
            radioButtonTeam.setText(team.toString());
            radioGroupTeam.addView(radioButtonTeam);
        }
        
    }
    
	/**
	 * Nas³uchuje ( ;) ) klikniêcia na przycisk odpowiedzialny za wys³anie do serwera informacji o wybranej dru¿ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		
		RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
		Integer teamNumber = radioGroupTeam.getCheckedRadioButtonId();
		
		LoginActivity.player.setTeam(teamNumber);
		StartGameProgressBarTask task = new StartGameProgressBarTask();
		task.execute();
	}
	// TODO Zrobic 
	private int sendTeamToServerAndGetWeaponCode() {
		LoginActivity.player.setTeam(LoginActivity.player.getTeam());
		int weaponCode = 0;
		try {
			weaponCode = LoginActivity.game.team(LoginActivity.player);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weaponCode;
	}
	private void getMyTeamFromServer()
	{
		try {
			LoginActivity.players = LoginActivity.game.getByTeam(LoginActivity.player.getTeam());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private class StartGameProgressBarTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPostExecute(Void result) {
			// TODO przejœcie do GameActivity poprzez Intent
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.INVISIBLE);
			
			Intent intent = new Intent(ChooseTeamActivity.this, GameActivity.class);
			startActivity(intent);
		}

		@Override
		protected void onPreExecute() {
			findViewById(R.id.progress_bar_team).setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//Dopisaæ obs³ugê przypisania weaponcode - obs³uga bluetootha
			sendTeamToServerAndGetWeaponCode();
			getMyTeamFromServer();
			return null;
		}
	}
}
