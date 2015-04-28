package com.pz.tagandfrag.restclient;

public class Player implements Comparable<Player> {

	private String name, localization;
	private Integer id, healthPoints, ammunition, team;
	
	
	public Player() {
		super();
	}
/**
 * Konstruktor ustawia wartoœæ punktów ¿ycia i amunicjê na 100, pole team na 0 a lokalizacjê na "".
 * @param name nazwa gracza
 * @param id id gracza
 */
	public Player(String name, Integer id) {
		super();
		this.name = name;
		this.healthPoints = 100;
		this.ammunition = 100;
		this.localization = "";
		this.team = 0;
		this.id = id;
	}

	/**
	 * Konstruktor przypisuje wartoœæ 0 do id gracza.
	 * @param name nazwa gracza;
	 * @param healthPoints punkty ¿ycia;
	 * @param ammunition iloœæ amunicji;
	 * @param localization pocz¹tkowa lokalizacja;
	 * @param team numer dru¿yny;
	 */
	public Player(String name, Integer healthPoints, Integer ammunition, String localization, Integer team) {
		super();
		this.name = name;
		this.healthPoints = healthPoints;
		this.ammunition = ammunition;
		this.localization = localization;
		this.team = team;
		this.id = 0;
	}
/**
 * Zmniejsza punkty zdrowia gracza o wartoœæ parametru value.
 * @param value
 */
	public void reduceHealth(int value) {
	
		this.healthPoints = this.healthPoints - value;
		if(this.healthPoints < 0) this.healthPoints = 0;
		
	}
	
	public String getName() {
		return name;
	}

	public Integer getHealthPoints() {
		return healthPoints;
	}

	public Integer getAmmunition() {
		return ammunition;
	}

	public String getLocalization() {
		return localization;
	}

	public Integer getTeam() {
		return team;
	}
	
	public Integer getId() {
		return id;
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

	public  void setId(Integer id) {
		this.id = id;
	}

	public void setHealthPoints(Integer healthPoints) {
		this.healthPoints = healthPoints;
	}

	public void setAmmunition(Integer ammunition) {
		this.ammunition = ammunition;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", healthPoints=" + healthPoints
				+ ", ammunition=" + ammunition + ", localization="
				+ localization + ",team " + team + ", id " + id + "]";
	}

	@Override
	public int compareTo(Player another) {
		if(this.healthPoints > another.getHealthPoints()) return -1;
		else if(this.healthPoints == another.getHealthPoints()) return 0;
		return 1;
	}
}
