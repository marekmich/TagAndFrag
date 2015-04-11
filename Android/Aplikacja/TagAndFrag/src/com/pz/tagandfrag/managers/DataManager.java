package com.pz.tagandfrag.managers;

import java.util.Collection;

import com.pz.tagandfrag.restclient.Game;
import com.pz.tagandfrag.restclient.Player;
import com.pz.tagandfrag.restclient.Team;

public class DataManager {
	public static Game game;
	public static Player player;
	public static Collection<Team> teamList;
	public static Collection<Player> players;
	public static PreferencesManager preferences;
	public static String serverAddress = "http://www.mat.umk.pl/~luigi";
}
