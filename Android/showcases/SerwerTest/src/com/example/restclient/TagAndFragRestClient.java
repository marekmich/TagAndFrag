package com.example.restclient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class TagAndFragRestClient implements RestClient<Player> {

	public static String URL = "http://158.75.2.62:8080";
	private HttpClient httpClient;
	
	public TagAndFragRestClient() {
		super();
		this.httpClient = new DefaultHttpClient();
	}

	@Override
	public Collection<Player> GET() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void DELETE() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void POST(Player object) throws IOException {
		HttpPost httpPost = new HttpPost(URL);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		nameValuePair.add(new BasicNameValuePair("player_name", object.getName()));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		HttpResponse response = httpClient.execute(httpPost);
		System.out.println(response.toString());
	}

	@Override
	public void PUT(Player object) {
		// TODO Auto-generated method stub
		
	}

}
