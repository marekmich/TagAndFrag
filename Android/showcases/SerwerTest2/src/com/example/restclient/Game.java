package com.example.restclient;

import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;

import org.json.JSONException;

public class Game {

	private Collection<Player> players;
	private RestClient<Player> restClient;
	
	public Game() {
		super();
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
	}
	
	public boolean addPlayer(Player object) throws IOException, JSONException{
		
		update();
		for (Player player : players) {
			if(player.getName().equals(object.getName())) return false;
		}
		players.add(object);
		restClient.POST(object);
		restClient.PUT(object);
		return true;
	}
	
	public void updatePlayer(Player object) throws IOException
	{
		restClient.PUT(object);
	}
	
	public void resetGame() throws IOException
	{
		players.clear();
		restClient.DELETE();
	}
	public void update() throws IOException, JSONException
	{
		players=restClient.GET();
	}
	public Collection<Player> getPlayers() throws IOException, JSONException
	{
		update();
		return players;
	}
	
}
