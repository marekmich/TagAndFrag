package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;


/**
 * Klasa zapewniaj¹ca interakcjê pomiêdzy poszczególnym graczem a danymi
 * znajduj¹cymi siê w zewnêtrznej bazie danych.
 * @author Marek
 *
 */
public class Game {
	/**
	 * Kolekcja graczy bior¹cych udzia³ w rozgrywce;
	 */
	private Collection<Player> players;
	/**
	 * Klient REST;
	 */
	private RestClient<Player> restClient;
	/**
	 * Pole okreœlaj¹ce o ile punktów bêdzie redukowane ¿ycie gracza po pojedynczym trafieniu;
	 */
	private Integer subHp;
	
	/**
	 * Konstruktor domyœlny; inicjalizuje klienta REST oraz kolekcjê graczy jako ArrayList;
	 * ustawia pole subHp na 10.
	 */
	public Game() {
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
		this.subHp=10;
	}
	
	/**
	 * Konstruktor inicjalizuj¹cy klienta REST oraz kolekcjê graczy jako ArrayList;
	 * ple subHp przyjmuje wartoœæ parametru value.
	 */
	public Game(Integer value) {
		super();
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();  //lista wszystkich graczy
		players.clear();
		this.subHp = value;   //wartosc o jaka zmniejszane beda pkt zycia
	}
	
	/**
	 * Dodaje gracza object do bazy danych o ile to mozliwe.
	 * @param object
	 * @throws IOException
	 * @throws JSONException
	 */
	public void addPlayer(Player object) throws IOException, JSONException{
		
		//dodawanie gracza do bazy
		restClient.POST(object);
	}
	
	/**
	 * Aktualizuje w bazie danych parametry gracza object(lokalizacjê, punkty ¿ycia i amunicjê).
	 * @param object 
	 * @throws IOException
	 */
	public void updatePlayer(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object,"UPDATE");
	}
	
	/**
	 * ..........................................................
	 * @param object
	 * @throws IOException
	 */
	public void end(Player object) throws IOException
	{
		restClient.PUT(object,"END");
	}
	
	/**
	 * Metoda redukuje punkty zdrowia gracza object oraz wysy³a do serwera
	 * informacjê o trafieniu gracza object przez gracza o nicku attacker.
	 * @param object
	 * @param attacker
	 * @throws IOException
	 */
	public void shotPlayer(Player object, String attacker) throws IOException
	{
		//dodaje do bazy informacje o trafieniu gracza object przez gracza o nazwie attacker
		if(object.getHealthPoints()>0){
		object.reduceHealth(this.subHp); //jesli gracz mial dodatnie pkt zycia to zostaja zmniejszone o subHp
		restClient.PUT(object, "SHOT", attacker); //wyslanie danych do serwera
		}
		
	}
	
	
//	public void resetGame() throws IOException
//	{
//		//czysci cala zawartosc bazy i usuwa graczy
//		players.clear();
//		restClient.DELETE();
//	}
	
	/**
	 * Aktualizuje listê graczy players danymi z bazy danych.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void update() throws IOException, JSONException
	{
		//uaktualnienie parametrow graczy z listy danymi z serwera
		players = restClient.GET("ALL_PLAYERS");
	}
	
	/**
	 * Pobiera z bazy danych gracza o nazwie name i zwraca w postaci obiektu typu Player
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Player getByName(String name) throws IOException, JSONException
	{
		//zwraca obiekt typu Player gracza, ktorego nazwa podana jest jako parametr
		return restClient.GET(name).iterator().next();
	}

	/**
	 * Zwraca kolekcjê zespo³ów.
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Team> list() throws IOException, JSONException
	{
		//Zwraca kolekcjê obiektów typu Team
		return restClient.GET();
	}
	
	/**
	 * Dodaje gracza object do bazy danych o ile to mozliwe.
	 * @param object
	 * @return id gracza, jeœli gracz zosta³ poprawnie dodany lub jeœli w bazie znajdowa³ siê
	 * gracz o tym samym nicku i id; 0 jeœli gracz o takiej samej nazwie ju¿ znajdowa³ siê w bazie
	 * ale posiada³ inne id.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Integer check(Player object) throws IOException, JSONException
	{
		
		return Integer.valueOf(restClient.POST(object)); //metoda dodaje gracza do bazy (o ile nie istnial gracz o podanej nazwie) i zwraca
										//jego id lub 0 jesli gracz istnial ale mial inne id
	}
	
	/**
	 * Zatwierdza przynale¿noœæ gracza do danej dru¿yny i zapisuje jej numer w bazie.
	 * @param object
	 * @param ready
	 * @throws IOException
	 */
	public void ready(Player object, Integer ready) throws IOException
	{
		restClient.PUT(object, "READY", ready.toString());
	}

	/**
	 * 
	 * @param object
	 * @return numer dru¿yny ,której cz³onkiem jest gracz object
	 * @throws IOException
	 */
	public Integer team(Player object) throws IOException
	{
		return Integer.valueOf(restClient.PUT(object,"TEAM"));
	}
	
	/**
	 * Zwraca kolekcjê wszystkich graczy znajduj¹cych siê w bazie danych.
	 * @return 
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Player> getAll() throws IOException, JSONException
	{
		//aktualizuje i zwraca liste graczy
		return restClient.GET("ALL_PLAYERS");
	}
	
	/**
	 * Zwraca kolekcjê graczy bêd¹cych cz³onkami dru¿yny o numerze teamId.
	 * @param teamId
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
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
