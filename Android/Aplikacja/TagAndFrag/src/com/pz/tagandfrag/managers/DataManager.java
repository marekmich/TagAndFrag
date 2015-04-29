package com.pz.tagandfrag.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import android.util.Log;

import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

/**
 * Manager przechowuj�cy dane wykorzystywan� we wszystkich aktywno�ciach
 * @author �ukasz �urawski
 * */
public class DataManager {
	/**
	 * Statyczne pole z ustawieniami gry
	 * */
	public static Game game;
	/**
	 * Statyczne pole z informacjami o graczu
	 * */
	public static Player player;
	/**
	 * Statyczne pole z list� dru�yn
	 * */
	public static Collection<Team> teamList;
	/**
	 * Statyczne pole z list� graczy w dru�ynie gracza
	 * */
	public static Collection<Player> players;
	/**
	 * Statyczne pole z list� graczy w dru�ynie przeciwnej do gracza
	 * */
	public static Collection<Player> oppositePlayers;
	/**
	 * Statyczne pole z zarz�dc� ustawie�
	 * @see PreferencesManager
	 * */
	public static PreferencesManager preferences;
	/**
	 * Statyczne pole z adresem serwera wwww gry, wy�wietlaj�cego podsumowanie
	 * */
	public static String serverAddress = "http://158.75.2.62:8080/web/?team1=%d&team2=%d";
	/**
	 * Statyczne pole z zajmuj�ce si� ��czno�ci� przez bluetootha
	 * */
	public static BluetoothService bluetoothService;
	/**
	 * Statyczne pole z kodem broni
	 * */
	public static int weaponCode = 0;
	/**
	 * Statyczne, niezmienne pole z ilo�ci� obra�e� od pojedynczego strza�u
	 * */
	public static final int DAMAGE = 10;
	
	/**
	 * Pobieranie listy graczy z dru�yny gracza i listy graczy z dru�yny przeciwnej
	 * */
	static void getPlayers() {
		int team = player.getTeam();
		int oppositeTeam = team;
		if(team % 2 == 0) {
			oppositeTeam -= 1;
		}
		else {
			oppositeTeam += 1;
		}
		
		players.clear();
		oppositePlayers.clear();
		//Pobieranie listy graczy z dru�yny gracza
		try {
			players = new ArrayList<Player>(game.getByTeam(team));
		} catch (IOException e) {
			Log.e("Team.IOex", e.toString());
		} catch (JSONException e) {
			Log.e("Team.JSON", e.toString());
		}
		//Pobieranie listy graczy z dru�yny przeciwnej do gracza
		try {
			oppositePlayers = new ArrayList<Player>(game.getByTeam(oppositeTeam));
		} catch (IOException e) {
			Log.e("Team.IOex", e.toString());
		} catch (JSONException e) {
			Log.e("Team.JSON", e.toString());
		}
	}
}
