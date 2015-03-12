package com.example.tagandfragprototype.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TagAndFragRestClient implements RestClient<Player> {

	public static String URL = "http://158.75.2.62:8080";
	private HttpClient httpClient;
	
	public TagAndFragRestClient() {
		super();
		this.httpClient = new DefaultHttpClient();
	}

	@Override
	public Collection<Player> GET() throws IOException, JSONException {
		HttpGet httpGet = new HttpGet(URL);
		HttpResponse response = httpClient.execute(httpGet);
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String jsonGet = inputReader.readLine();

		JSONArray array = new JSONArray(jsonGet);
		return fromJsonArrayToCollection(array);
	}

	@Override
	public void DELETE() throws IOException {
		HttpDelete httpDelete = new HttpDelete(URL);
		httpClient.execute(httpDelete);
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
	public void PUT(Player object) throws IOException {
		
		HttpPut put = new HttpPut(URL);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

		nameValuePairs.add(new BasicNameValuePair("player_name", object.getName()));

		nameValuePairs.add(new BasicNameValuePair("hp", object.getHealthPoints().toString()));

		nameValuePairs.add(new BasicNameValuePair("ammo", object.getAmmunition().toString()));

		nameValuePairs.add(new BasicNameValuePair("loc", object.getLocalization().toString()));

		put.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		httpClient.execute(put);
		
	}
	
	private Collection<Player> fromJsonArrayToCollection(JSONArray array) throws JSONException {
		Collection<Player> players = new ArrayList<Player>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject  = array.getJSONObject(i);
			
			String name 			= jsonObject.optString("name");
			Integer healthPoints 	= Integer.valueOf(jsonObject.optString("health"));
			Integer ammunition 		= Integer.valueOf(jsonObject.optString("ammunition"));
			Integer localization 	= Integer.valueOf(jsonObject.optString("localization"));
			
			Player player = new Player(name, healthPoints, ammunition, localization);
			players.add(player);
		}
		return players;
	}

}
