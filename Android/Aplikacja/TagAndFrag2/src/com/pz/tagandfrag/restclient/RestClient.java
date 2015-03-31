package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.*;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET() throws IOException, JSONException;
	public Map<Integer,Integer> GET_T() throws IOException, JSONException;
	public Player        	 	GET(String parameter) throws IOException, JSONException;
	public void 				DELETE() throws IOException;
	public Integer 				POST(Type object) throws IOException;
	public void 				PUT(Type object, String attacker) throws IOException;
	public void 				PUT(Type object) throws IOException;
}
