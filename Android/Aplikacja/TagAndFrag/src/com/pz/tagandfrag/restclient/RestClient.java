package com.pz.tagandfrag.restclient;

import java.io.IOException;


import java.util.Collection;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET(String parameter, Integer teamId) throws IOException, JSONException;
	public Collection<Type> 	GET(String parameter) throws IOException, JSONException;
	public Collection<Team>	 	GET() throws IOException, JSONException;
	
	public String 				POST(Type object) throws IOException;

	public String 				PUT(Type object, String arg1, String arg2) throws IOException;
	public String 				PUT(Type object, String arg) throws IOException;

	public void 				DELETE() throws IOException;
}
