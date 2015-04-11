package com.pz.tagandfrag.managers;

import com.pz.tagandfrag.R;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager 
{
	
	
	int id;
	String nick;
	String MAC;
	int team;
	
	Context context;
	
	
	public PreferencesManager(Context applicationContext) {
		this.context = applicationContext;
		id = 0;
		nick = "";
	}
	
	/**
	 * Pobranie wszystkich danych z preferencji
	 * */
	public void loadAllDataFromPreferences() {
		loadLoginDataFromPreferences();
		loadMACDataFromPreferences();
		loadTeamDataFromPreferences();
	}
	/**
	 * Zapisanie wszystkich danych do preferencji
	 * */
	public void saveAllDataToPreferences() {
		saveLoginDataToPreferences();
		saveMACDataFromPreferences();
		saveTeamDataFromPreferences();
	}
	/** 
	 * Pobiera dane na temat logowania z preferencji
	 * */
	public void loadLoginDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Pobranie id gracza
		int defaultId = 0;
		id = preferences.getInt(context.getResources().getString(R.string.preferences_id), defaultId);
		//Pobranie nicku gracza
		String defaultName = context.getResources().getString(R.string.preferences_name_default);
		nick = preferences.getString(context.getResources().getString(R.string.preferences_name), defaultName);
	}
	/** 
	 * Pobiera dane na temat MAC adresu z preferencji
	 * */
	public void loadMACDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Pobranie MAC adresu
		String defaultMAC = "";
		MAC = preferences.getString(context.getResources().getString(R.string.preferences_mac), defaultMAC);
	}
	/** 
	 * Pobiera dane na temat teamu z preferencji
	 * */
	public void loadTeamDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Pobranie teamu gracza
		int defaultTeam = 0;
		team = preferences.getInt(context.getResources().getString(R.string.preferences_team), defaultTeam);
	}
	
	/**
	 * Zapisuje dane na temat logowania do preferencji
	 * */
	public void saveLoginDataToPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Odpalenie (?) edytora
		SharedPreferences.Editor editor = preferences.edit();
		//Zapisanie id i nicku gracza do preferencji
		editor.putInt(context.getResources().getString(R.string.preferences_id), id);
		editor.putString(context.getResources().getString(R.string.preferences_name), nick);
		editor.commit();
	}
	/** 
	 * Zapisuje dane na temat MAC adresu do preferencji
	 * */
	public void saveMACDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Odpalenie (?) edytora
		SharedPreferences.Editor editor = preferences.edit();
		//Zapisanie MAC adresu
		editor.putString(context.getResources().getString(R.string.preferences_mac), MAC);
		editor.commit();
	}
	/** 
	 * Zapisuje dane na temat teamu do preferencji
	 * */
	public void saveTeamDataFromPreferences() {
		//Ustawienie uchwytu na preferencje
		SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_file), Context.MODE_PRIVATE);
		//Odpalenie (?) edytora
		SharedPreferences.Editor editor = preferences.edit();
		//Pobranie teamu gracza
		editor.putInt(context.getResources().getString(R.string.preferences_team), team);
		editor.commit();
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		this.MAC = mAC;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
}
