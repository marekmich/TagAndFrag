package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;



public class Game {

	private Collection<Player> players;
	private RestClient<Player> restClient;
	private Integer subHp;
	
	public Game() {
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
		this.subHp=10;
	}

	public Game(Integer value) {
		super();
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();  //lista wszystkich graczy
		players.clear();
		this.subHp = value;   //wartosc o jaka zmniejszane beda pkt zycia
	}
	
	public void addPlayer(Player object) throws IOException, JSONException{
		
		//dodawanie gracza do bazy
		restClient.POST(object);
	}
	
	public void updatePlayer(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object,"UPDATE");
	}
	public void updatePlayerPosition(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object,"UPDATE_MAP");
	}
	public void end(Player object) throws IOException
	{
		
		restClient.PUT(object,"END");
	}
	
	public void shotPlayer(Player object, String attacker) throws IOException
	{
		//dodaje do bazy informacje o trafieniu gracza object przez gracza o nazwie attacker
		if(object.getHealthPoints()>0){
		object.reduceHealth(this.subHp); //jesli gracz mial dodatnie pkt zycia to zostaja zmniejszone o subHp
		restClient.PUT(object, "SHOT", attacker); //wyslanie danych do serwera
		}
		
	}
	
	public void resetGame() throws IOException
	{
		//czysci cala zawartosc bazy i usuwa graczy
		players.clear();
		restClient.DELETE();
	}
	public void update() throws IOException, JSONException
	{
		//uaktualnienie parametrow graczy z listy danymi z serwera
		players = restClient.GET("ALL_PLAYERS");
	}
	
	public Player getByName(String name) throws IOException, JSONException
	{
		//zwraca obiekt typu Player gracza, ktorego nazwa podana jest jako parametr
		return restClient.GET(name).iterator().next();
	}

	public Collection<Team> list() throws IOException, JSONException
	{
		//Zwraca kolekcjê obiektów typu Team
		return restClient.GET();
	}
	
	public Integer check(Player object) throws IOException, JSONException
	{
		
		return Integer.valueOf(restClient.POST(object)); //metoda dodaje gracza do bazy (o ile nie istnial gracz o podanej nazwie) i zwraca
										//jego id lub 0 jesli gracz istnial ale mial inne id
	}
	
	public void ready(Player object, Integer ready) throws IOException
	{
		restClient.PUT(object, "READY", ready.toString());
	}

	public Integer team(Player object) throws IOException
	{
		return Integer.valueOf(restClient.PUT(object,"TEAM"));
	}
	
	public Collection<Player> getAll() throws IOException, JSONException
	{
		//aktualizuje i zwraca liste graczy
		return restClient.GET("ALL_PLAYERS");
	}
	


	public Collection<Player> getByTeam(Integer teamId) throws IOException, JSONException
	{
		//zwraca liste graczy z danej druzyny
		return restClient.GET("ALL_PLAYERS", teamId);
	}
	
	Integer getSubHp() {
		//pobranie wartosci o jaka sa zmneijszane pkt zycia podczas trafienia przez innego gracza
		return subHp;
	}

	void setSubHp(Integer valueHp) {
		//ustawienie wartosci o jaka beda zmneijszane pkt zycia podczas trafienia przez innego gracza
		this.subHp = valueHp;
	}
	

	
}
