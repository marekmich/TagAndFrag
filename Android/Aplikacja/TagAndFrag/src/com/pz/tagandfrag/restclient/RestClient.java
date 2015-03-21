package com.pz.tagandfrag.restclient;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET() throws IOException, JSONException;
	public void 				DELETE() throws IOException;
	public void 				POST(Type object) throws IOException;
	public void 				PUT(Type object) throws IOException;
}
