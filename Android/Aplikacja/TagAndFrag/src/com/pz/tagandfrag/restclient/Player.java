package com.pz.tagandfrag.restclient;

public class Player {

	private String name, localization;
	private Integer id, healthPoints, ammunition, team;
	
	
	public Player() {
		super();
	}

	public Player(String name, Integer id) {
		super();
		this.name = name;
		this.healthPoints = 100;
		this.ammunition = 100;
		this.localization = "";
		this.team = 0;
		this.id = id;
	}

	public Player(String name, Integer healthPoints, Integer ammunition, String localization, Integer team) {
		super();
		this.name = name;
		this.healthPoints = healthPoints;
		this.ammunition = ammunition;
		this.localization = localization;
		this.team = team;
		this.id = 0;
	}

	public void reduceHealth(int value) {
	
		this.healthPoints = this.healthPoints - value;
		if(this.healthPoints < 0) this.healthPoints = 0;
		
	}
	


	@Override
	public String toString() {
		return "Player [name=" + name + ", healthPoints=" + healthPoints
				+ ", ammunition=" + ammunition + ", localization="
				+ localization + ",team " + team + ", id " + id + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getHealthPoints() {
		return healthPoints;
	}

	public void setHealthPoints(Integer healthPoints) {
		this.healthPoints = healthPoints;
	}

	public Integer getAmmunition() {
		return ammunition;
	}

	public void setAmmunition(Integer ammunition) {
		this.ammunition = ammunition;
	}

	public Integer getTeam() {
		return team;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}
}

