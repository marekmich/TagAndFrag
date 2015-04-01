package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.*;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET() throws IOException, JSONException;
	public Collection<Team>	 	GET_T() throws IOException, JSONException;
	public Player        	 	GET(String parameter) throws IOException, JSONException;
	public void 				DELETE() throws IOException;
	public Integer 				POST(Type object) throws IOException;
	public void 				PUT(Type object, String attacker) throws IOException;
	public Integer 				PUT(Type object, Integer team) throws IOException;
	public void 				PUT(Type object) throws IOException;
	public void 				PUT_R(Type object, Integer ready) throws IOException;
}
