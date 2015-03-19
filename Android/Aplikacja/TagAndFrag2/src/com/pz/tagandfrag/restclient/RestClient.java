package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET() throws IOException, JSONException;
	public Player        	 	GET(String parameter) throws IOException, JSONException;
	public void 				DELETE() throws IOException;
	public String 				POST(Type object) throws IOException;
	public void 				PUT(Type object, String attacker) throws IOException;
	public void 				PUT(Type object) throws IOException;
}
