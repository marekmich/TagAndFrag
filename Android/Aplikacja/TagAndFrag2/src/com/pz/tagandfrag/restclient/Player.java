package com.pz.tagandfrag.restclient;

public class Player {

	private String name, localization;
	private Integer id, healthPoints, ammunition, team;
	
	
	public Player() {
		super();
	}

	public Player(String name, Integer healthPoints, Integer ammunition,
			String localization, Integer team, Integer id) {
		super();
		this.name = name;
		this.id = id;
		this.healthPoints = healthPoints;
		this.ammunition = ammunition;
		this.localization = localization;
		this.team = team;
	}

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
	
	
	void setName(String name) {
		this.name = name;
	}

	void setId(Integer id) {
		this.id = id;
	}

	void setHealthPoints(Integer healthPoints) {
		this.healthPoints = healthPoints;
	}

	void setAmmunition(Integer ammunition) {
		this.ammunition = ammunition;
	}

	void setLocalization(String localization) {
		this.localization = localization;
	}

	void setTeam(Integer team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", healthPoints=" + healthPoints
				+ ", ammunition=" + ammunition + ", localization="
				+ localization + "]";
	}
}
