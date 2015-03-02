package com.example.restclient;

import java.io.IOException;
import java.util.Collection;

import org.json.JSONException;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET() throws IOException, JSONException;
	public void 				DELETE();
	public void 				POST(Type object) throws IOException;
	public void 				PUT(Type object);
}
