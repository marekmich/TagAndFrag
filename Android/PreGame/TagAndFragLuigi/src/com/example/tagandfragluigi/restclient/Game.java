package com.example.tagandfragluigi.restclient;


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
	
	public Integer addPlayer(Player object) throws IOException, JSONException{
		
		Integer post;
		if((post = restClient.POST(object))==0) return 0;

		restClient.PUT(object);
		players.add(object);
		return post;
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

	public Integer check(String name, Integer id) throws IOException, JSONException
	{
		Player object = new Player(name, 100, 100, "0#0", 0);
		object.setId(id);
		return restClient.POST(object);
				
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
