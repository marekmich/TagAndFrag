package com.example.tagandfragprototype.restclient;

public class Player {

	private String name;
	private Integer healthPoints, ammunition, localization;
	
	public Player() {
		super();
	}

	public Player(String name, Integer healthPoints, Integer ammunition,
			Integer localization) {
		super();
		this.name = name;
		this.healthPoints = healthPoints;
		this.ammunition = ammunition;
		this.localization = localization;
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
	
	public void reduceHealth(int value) {
		if (this.healthPoints > 0) {
			this.healthPoints = this.healthPoints - value;
		}
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", healthPoints=" + healthPoints
				+ ", ammunition=" + ammunition + ", localization="
				+ localization + "]";
	}
}
