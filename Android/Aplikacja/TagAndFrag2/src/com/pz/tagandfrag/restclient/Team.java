package com.pz.tagandfrag.restclient;

public class Team implements Comparable<Team> {
	
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

	@Override
	public String toString() {
		return String.format("%d. dru¿yna - %d", id, size);
	}

	@Override
	public int compareTo(Team another) {
		if(this.id < another.getId()) return -1;
		else if(this.id == another.getId()) return 0;
		return 1;
	}

}
