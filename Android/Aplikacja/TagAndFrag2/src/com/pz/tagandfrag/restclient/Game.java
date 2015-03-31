package com.pz.tagandfrag.restclient;

import java.util.*;
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
		this.players = new ArrayList<Player>();  //lista wszystkich graczy
		players.clear();
		this.subHp = value;   //wartosc o jaka zmniejszane beda pkt zycia
	}
	
	public Integer addPlayer(Player object) throws IOException, JSONException{
		
		//dodawanie gracza do bazy, zwraca nadane mu id lub 0 jesli nie udalo dodac sie gracza (gracz istnial z innym id)
		Integer post;
		if((post = restClient.POST(object))==0) return 0;

		restClient.PUT(object);
		players.add(object);
		return post;
	}
	
	public void updatePlayer(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object);
	}
	
	public void shotPlayer(Player object, String attacker) throws IOException
	{
		//dodaje do bazy informacje o trafieniu gracza object przez gracza o nazwie attacker
		if(object.getHealthPoints()>0){
		object.reduceHealth(this.subHp); //jesli gracz mnial dodatnie pkt zycia to zostaja zmniejszone o subHp
		restClient.PUT(object, attacker); //wyslanie danych do serwera
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
		players = restClient.GET();
	}
	
	public Player getByName(String name) throws IOException, JSONException
	{
		//zwraca obiekt typu Player gracza, ktorego nazwa podana jest jako parametr
		update();
		return restClient.GET(name);
	}

	public Map<Integer,Integer> list() throws IOException, JSONException
	{
		//zwraca mapê zawieraj¹ca wszystkie dru¿yny jako klucze oraz liczbe czlonkow kazdej z nich
		return restClient.GET_T();
	}
	
	public Integer check(String name, Integer id) throws IOException, JSONException
	{
		
		Player object = new Player(name, 100, 100, "0#0", 0); //utworzenie obiektu gracza o podanej nazwie name
		object.setId(id); //przypisanie podanego id w obiekcie
		return restClient.POST(object); //metoda dodaje gracza do bazy (o ile nie istnial gracz o podanej nazwie) i zwraca
										//jego id lub 0 jesli gracz istnial ale mial inne id
	}
	
	public Collection<Player> getAll() throws IOException, JSONException
	{
		//aktualizuje i zwraca liste graczy
		update();
		return players;
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
