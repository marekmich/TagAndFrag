package com.pz.tagandfrag.restclient;

public class Team implements Comparable<Team> {
	
	private Integer id;
	private Integer size;
	
	
	public Team() {
	}

	public Team(Integer id, Integer size) {
		/**
		 * identyfikator dru¿yny >0;
		 */
		this.id=id;
		/**
		 * rozmiar dru¿yny - liczba cz³onków dru¿yny
		 */
		this.size=size;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
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
