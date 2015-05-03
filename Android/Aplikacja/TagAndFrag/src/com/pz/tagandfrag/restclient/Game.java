package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import com.pz.tagandfrag.managers.DataManager;


/**
 * Klasa zapewniająca interakcję pomiędzy poszczególnym graczem a danymi
 * znajdującymi się w zewnętrznej bazie danych.
 * @author Marek
 *
 */
public class Game {
	/**
	 * Kolekcja graczy biorących udział w rozgrywce;
	 */
	private Collection<Player> players;
	/**
	 * Klient REST;
	 */
	private RestClient<Player> restClient;
	/**
	 * Pole określające o ile punktów będzie redukowane życie gracza po pojedynczym trafieniu;
	 */
	private Integer subHp;
	
	/**
	 * Konstruktor domyślny; inicjalizuje klienta REST oraz kolekcję graczy jako ArrayList;
	 * ustawia pole subHp na 10.
	 */
	public Game() {
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();
		players.clear();
		this.subHp=10;
	}
	
	/**
	 * Konstruktor inicjalizujący klienta REST oraz kolekcję graczy jako ArrayList;
	 * ple subHp przyjmuje wartość parametru value.
	 */
	public Game(Integer value) {
		this.restClient = new TagAndFragRestClient();
		this.players = new ArrayList<Player>();  //lista wszystkich graczy
		players.clear();
		this.subHp = value;   //wartosc o jaka zmniejszane beda pkt zycia
	}
	
	/**
	 * Aktualizuje w bazie danych parametry gracza object(lokalizację, punkty życia i amunicję).
	 * @param object 
	 * @throws IOException
	 */
	public void updatePlayer(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object,"UPDATE");
	}
	
	public void updatePlayerPosition(Player object) throws IOException
	{
		//wyslanie do serwera aktualnych parametrow gracza w celu zapisania ich w bazie
		restClient.PUT(object, "UPDATE_MAP");
	}
	
	/**
	 * Wysłanie komendy zerującej CWC i TEAM na serwerze dla danego gracza
	 * @param object
	 * @throws IOException
	 */
	public void end(Player object) throws IOException
	{
		restClient.PUT(object,"END");
	}
	
	/**
	 * Metoda redukuje punkty zdrowia gracza object oraz wysyła do serwera
	 * informację o trafieniu gracza object przez gracza o nicku attacker.
	 * @param object
	 * @param attacker
	 * @throws IOException
	 */
	public void shootPlayer(Player object, int attackerWeaponCode) throws IOException
	{
		String attacker = "";
		for(Player player : (ArrayList<Player>) DataManager.oppositePlayers) {
			//TODO Sprawdzić czy działa
			if(player.getCWC() == attackerWeaponCode) {
				attacker = player.getName();
				break;
			}
		}
		
		//dodaje do bazy informacje o trafieniu gracza object przez gracza o nazwie attacker
		if(object.getHealthPoints() > 0) {
			object.reduceHealth(this.subHp); //jesli gracz mial dodatnie pkt zycia to zostaja zmniejszone o subHp
			restClient.PUT(object, "SHOT", attacker); //wyslanie danych do serwera
		}
	}
	
	/**
	 * Zwraca kolekcję zespołów.
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Team> list() throws IOException, JSONException
	{
		//Zwraca kolekcję obiektów typu Team
		return restClient.GET();
	}
	
	/**
	 * Dodaje gracza object do bazy danych o ile to mozliwe.
	 * @param object
	 * @return id gracza, jeśli gracz został poprawnie dodany lub jeśli w bazie znajdował się
	 * gracz o tym samym nicku i id; 0 jeśli gracz o takiej samej nazwie już znajdował się w bazie
	 * ale posiadał inne id.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Integer check(Player object) throws IOException, JSONException
	{
		
		return Integer.valueOf(restClient.POST(object)); //metoda dodaje gracza do bazy (o ile nie istnial gracz o podanej nazwie) i zwraca
										//jego id lub 0 jesli gracz istnial ale mial inne id
	}
	
	/**
	 * Zatwierdza przynależność gracza do danej drużyny i zapisuje jej numer w bazie.
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
	 * @return numer drużyny ,której członkiem jest gracz object
	 * @throws IOException
	 */
	public Integer team(Player object) throws IOException
	{
		return Integer.valueOf(restClient.PUT(object,"TEAM"));
	}
	
	/**
	 * Zwraca kolekcję graczy będących członkami drużyny o numerze teamId.
	 * @param teamId
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Player> getByTeam(Integer teamId) throws IOException, JSONException
	{
		//zwraca liste graczy z danej druzyny
		return restClient.GET(teamId);
	}
	
	Integer getSubHp() {
		//pobranie wartosci o jaka sa zmneijszane pkt zycia podczas trafienia przez innego gracza
		return subHp;
	}
}

