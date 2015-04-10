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
		return "Team [id=" + id + ", size=" + size + "]";
	}

}
