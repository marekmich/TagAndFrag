package com.pz.tagandfrag.managers;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;

import android.util.Log;

import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

public class DataManager {
	public static Game game;
	public static Player player;
	public static Collection<Team> teamList;
	public static Collection<Player> players;
	public static Collection<Player> oppositePlayers;
	public static PreferencesManager preferences;
	public static String serverAddress = "http://www.mat.umk.pl/~luigi";
	public static BluetoothService bluetoothService;
	public static int weaponCode = 0;
	public static int DAMAGE = 10;
	
	static void getPlayers() {
		int team = player.getTeam();
		int oppositeTeam = team;
		if(team % 2 == 0) {
			oppositeTeam -= 1;
		}
		else {
			oppositeTeam += 1;
		}
		
		try {
			players = game.getByTeam(team);
		} catch (IOException e) {
			Log.e("Team.IOex", e.toString());
		} catch (JSONException e) {
			Log.e("Team.JSON", e.toString());
		}
		
		try {
			oppositePlayers = game.getByTeam(oppositeTeam);
		} catch (IOException e) {
			Log.e("Team.IOex", e.toString());
		} catch (JSONException e) {
			Log.e("Team.JSON", e.toString());
		}
	}
}
