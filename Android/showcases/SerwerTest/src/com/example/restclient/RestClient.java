package com.example.restclient;

import java.io.IOException;
import java.util.Collection;

public interface RestClient<Type> {
	
	public Collection<Type> 	GET();
	public void 				DELETE();
	public void 				POST(Type object) throws IOException;
	public void 				PUT(Type object);
}
