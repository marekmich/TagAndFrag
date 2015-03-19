package com.pz.tagandfrag.restclient;

import java.util.ArrayList;
import java.util.Collection;
import java.io.IOException;

import org.json.JSONException;


public class Game {

	private Collection<Player> players;
	private RestClient<Player> restClient;
	private Integer subHp;
	
	public Game() {
		super();
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
		this.subHp=0;
	}

	public Game(Integer value) {
		super();
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
		this.subHp = value;
	}
	
	public boolean addPlayer(Player object) throws IOException, JSONException{
		update();
		for (Player player : players) {
			if(player.getName().equals(object.getName())) return false;
		}
		players.add(object);
		object.setId(Integer.valueOf(restClient.POST(object)));
		restClient.PUT(object);
		return true;
	}
	
	public void updatePlayer(Player object) throws IOException
	{
		restClient.PUT(object);
	}
	
	public void shotPlayer(Player object, String attacker) throws IOException
	{
		if(object.getHealthPoints()>0){
		object.reduceHealth(this.subHp);
		restClient.PUT(object, attacker);
		}
		
	}
	
	public void resetGame() throws IOException
	{
		players.clear();
		restClient.DELETE();
	}
	public void update() throws IOException, JSONException
	{
		players = restClient.GET();
	}
	
	public Player getByName(String name) throws IOException, JSONException
	{
		update();
		return restClient.GET(name);
	}
	
	public Collection<Player> getAll() throws IOException, JSONException
	{
		update();
		return players;
	}

	Integer getSubHp() {
		return subHp;
	}

	void setSubHp(Integer valueHp) {
		this.subHp = valueHp;
	}
	
	
	
}
