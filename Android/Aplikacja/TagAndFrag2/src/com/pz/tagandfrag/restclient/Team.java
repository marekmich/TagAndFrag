package com.pz.tagandfrag.restclient;

public class Team {
	
	private Integer id;
	private Integer size;
	
	
	public Team() {
	}

	public Team(Integer id, Integer size) {
		this.id=id;
		this.size=size;
	}

	Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	Integer getSize() {
		return size;
	}

	void setSize(Integer size) {
		this.size = size;
	}

}
