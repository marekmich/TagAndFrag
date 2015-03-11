package com.pz.tagandfrag.restclient;

public class Player {

	private String name, attacker_name;
	private Integer id, healthPoints, ammunition, localization, team;
	
	
	public Player() {
		super();
	}

	public Player(String name, Integer healthPoints, Integer ammunition,
			Integer localization, Integer team, Integer id, String attacker_name) {
		super();
		this.name = name;
		this.id = id;
		this.healthPoints = healthPoints;
		this.ammunition = ammunition;
		this.localization = localization;
		this.team = team;
		this.attacker_name = attacker_name;
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

	public Integer getLocalization() {
		return localization;
	}

	public Integer getTeam() {
		return team;
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getAttacker_name() {
		return attacker_name;
	}
	
	@Override
	public String toString() {
		return "Player [name=" + name + ", healthPoints=" + healthPoints
				+ ", ammunition=" + ammunition + ", localization="
				+ localization + "]";
	}
}
