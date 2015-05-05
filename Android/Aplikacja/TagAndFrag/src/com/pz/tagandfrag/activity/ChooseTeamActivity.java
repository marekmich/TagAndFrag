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
 * Aktywno�� odpowiedzialna za obs�ug� i wy�wietlenie ekranu wyboru dru�y�
 * @author �ukasz �urawski
 */
public class ChooseTeamActivity extends Activity {
	
	/**
	 * Pobiera z pami�ci ostatnio wybran� dru�yn� (je�li istnieje) wpp. przypisuje zero,
	 * a nast�pnie uruchamia zadanie odpowiedzialne za pobranie listy dost�pnych dru�yn.
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
	 * Przygotowuj� dane z kt�rych b�dzie korzysta� aplikacja
	 * */
	public void prepareActivity() {
		
		//Przygotowanie klas pomocniczych
		DataManager.preferences.loadTeamDataFromPreferences();
		
		//Pobranie listy dru�yn
		//BuG?
		DownloadTeamListTask task = new DownloadTeamListTask();
		task.execute();
	}
	/////////////////////////////////
	/* Zmiany w wygl�dzie */
	/**
	 * Tworzy list� dost�pnych dru�yn na bazie
	 * */
    public void addRadioButtons() {
    	//Doda� przycisk od�wie�ania
    	RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
    	radioGroupTeam.removeAllViews();
    	
        List<Team> teamList = new ArrayList<Team>(DataManager.teamList);
        
        //Je�li apka si� po�o�y�a lub gracz wyszed� z gry
        //To na g�rze jest pokazywana ostatnio wybrana dru�yna
        if(DataManager.preferences.getTeam() > 0) {
        	int myTeam = DataManager.preferences.getTeam();
        	int myVisibleTeam = (myTeam % 2 == 0) ? myTeam / 2 : (int) Math.ceil(((double) myTeam / 2));

        	RadioButton radioButtonTeam = new RadioButton(this);
        	radioButtonTeam.setId(DataManager.preferences.getTeam());
        	radioButtonTeam.setTextSize(23f);
        	radioButtonTeam.setTypeface(radioButtonTeam.getTypeface(), Typeface.BOLD);
        	radioButtonTeam.setText(String.format("%d. dru�yna (w trakcie gry)", myVisibleTeam));
            if (myTeam % 2 == 0) {
            	radioButtonTeam.setShadowLayer(30, 0, 0, Color.BLUE);
            } else { 
            	radioButtonTeam.setShadowLayer(30, 0, 0, Color.RED);
            }
            radioButtonTeam.setChecked(true);
            radioGroupTeam.addView(radioButtonTeam);
        }
        
        //Wrzucenie wszystkich team�w na list� wybieraln�
        for (Team team : teamList) {
        	if(DataManager.preferences.getTeam() != team.getId())
        	{
	            RadioButton radioButtonTeam = new RadioButton(this);
	            int visibleTeamId = (team.getId() % 2 == 0) ? team.getId() / 2 : (int) Math.ceil(((double) team.getId() / 2));
            	radioButtonTeam.setId(team.getId());
	            radioButtonTeam.setText(String.format("%d) Ilo�� graczy: %d", visibleTeamId, team.getSize()));
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
	 * Nas�uchuje  klikni�cia na przycisk odpowiedzialny za wys�anie do serwera informacji o wybranej dru�ynie
	 * poprzez uruchomienia zadania z klasy {@link TeamConnectProgressBarTask}
	 */
	public void onChooseTeamButtonClicked(View view) {
		
		//Pobranie, kt�ry team zosta� wybrany
		RadioGroup radioGroupTeam = (RadioGroup) findViewById(R.id.radiogroup);
		Integer teamNumber = radioGroupTeam.getCheckedRadioButtonId();
		//Zapisanie zespo�u do klas pomocniczych i pami�ci
		DataManager.player.setTeam(teamNumber);
		DataManager.preferences.setTeam(teamNumber);
		DataManager.preferences.saveTeamDataFromPreferences();
		//Rozpocz�cie nowej gry
		SendTeamProgressBarTask task = new SendTeamProgressBarTask();
		task.execute();
	}
	
	@Override
	public void onBackPressed() {
		Toast.makeText(this, getString(R.string.back_pressed_info), Toast.LENGTH_LONG).show();
	}
	
	/////////////////////////////////
	/* ��czno�� - REST Client */
	/**
	 * Wys�anie do serwera numeru wybranej dru�yny, odebranie kodu broni
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
	 * Pobiera z serwera list� dru�yn
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
	 * Klasa wykonuje zadanie wys�ania do serwera informacji o wybranej dru�ynie
	 * nast�pnie pobrania kodu broni
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
	 * Klasa wykonuje zadanie pobrania listy dru�yn z serwera, 
	 * nast�pnie dodaje elementy tej list do pola wyboru 
	 * oraz ods�ania elementy zwi�zane z wyborem dru�yny
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
