package com.pz.tagandfrag.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.utils.URLEncodedUtils;

public class TagAndFragRestClient implements RestClient<Player> {

	public static String URL = "http://158.75.2.62:8080";
	
	
	public TagAndFragRestClient() {
		super();
	
	}

	@Override
	public Collection<Player> GET() throws IOException, JSONException {
		HttpClient httpClient = new DefaultHttpClient();
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(1);
		param.add(new BasicNameValuePair("parameter", "ALL_PLAYERS"));
		
		HttpGet httpGet = new HttpGet(URL+"?"+URLEncodedUtils.format(param, "utf-8"));

		HttpResponse response = httpClient.execute(httpGet);
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String jsonGet = inputReader.readLine();
		System.out.println(jsonGet);
		JSONArray array = new JSONArray(jsonGet);
		return fromJsonArrayToCollection(array);
	}

	@Override
	public Collection<Team> GET_T() throws IOException ,JSONException{
		HttpClient httpClient = new DefaultHttpClient();
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(1);
		param.add(new BasicNameValuePair("parameter", "LIST"));
		
		HttpGet httpGet = new HttpGet(URL+"?"+URLEncodedUtils.format(param, "utf-8"));

		HttpResponse response = httpClient.execute(httpGet);
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String jsonGet = inputReader.readLine();;
				JSONObject jsonObject  = new JSONObject(jsonGet);
		
		Iterator<String> nameItr = jsonObject.keys();
		Collection<Team> teams = new ArrayList<Team>();
		
		
		while(nameItr.hasNext()) {
		    String id = nameItr.next();
		    String size = jsonObject.getString(id);
		    teams.add(new Team(Integer.valueOf(id), Integer.valueOf(size)));
		    }
		
		return teams;  
	}
	
	@Override
	public Player GET(String parameter) throws IOException, JSONException {
		
		HttpClient httpClient = new DefaultHttpClient();
		
		List<NameValuePair> param = new ArrayList<NameValuePair>(1);
		param.add(new BasicNameValuePair("parameter", parameter));
		
		HttpGet httpGet = new HttpGet(URL+"?"+URLEncodedUtils.format(param, "utf-8"));
		HttpResponse response = httpClient.execute(httpGet);
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String jsonGet = inputReader.readLine();
        
		
		JSONArray array = new JSONArray(jsonGet);
		
		ArrayList<Player> list = new ArrayList<Player>(fromJsonArrayToCollection(array));
		
		
		return list.get(0);
	    
	}
	
	@Override
	public void DELETE() throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(URL);
		httpClient.execute(httpDelete);
		}

	@Override
	public Integer POST(Player object) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(URL);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
		nameValuePair.add(new BasicNameValuePair("player_name", object.getName()));
		nameValuePair.add(new BasicNameValuePair("id", object.getId().toString()));
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
		HttpResponse response = httpClient.execute(httpPost);
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String line;
		line = inputReader.readLine();
		return Integer.valueOf(line);
	}

	@Override
	public void PUT(Player object, String attacker) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("player_name", object.getName()));
		nameValuePairs.add(new BasicNameValuePair("hp", object.getHealthPoints().toString()));
		nameValuePairs.add(new BasicNameValuePair("ammo", object.getAmmunition().toString()));
		nameValuePairs.add(new BasicNameValuePair("loc", object.getLocalization().toString()));
		nameValuePairs.add(new BasicNameValuePair("attacker_name", attacker));
		nameValuePairs.add(new BasicNameValuePair("id", object.getId().toString()));
		put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpClient.execute(put);
	}
	
	@Override
	public Integer PUT(Player object, Integer team) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("player_name", object.getName()));
		nameValuePairs.add(new BasicNameValuePair("hp", object.getHealthPoints().toString()));
		nameValuePairs.add(new BasicNameValuePair("ammo", object.getAmmunition().toString()));
		nameValuePairs.add(new BasicNameValuePair("loc", object.getLocalization().toString()));
		nameValuePairs.add(new BasicNameValuePair("team", team.toString()));
		nameValuePairs.add(new BasicNameValuePair("id", object.getId().toString()));
		put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		HttpResponse response = httpClient.execute(put);
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		String gunId = inputReader.readLine();
		return Integer.valueOf(gunId);
	}
	
	@Override
	public void PUT(Player object) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("player_name", object.getName()));
		nameValuePairs.add(new BasicNameValuePair("hp", object.getHealthPoints().toString()));
		nameValuePairs.add(new BasicNameValuePair("ammo", object.getAmmunition().toString()));
		nameValuePairs.add(new BasicNameValuePair("loc", object.getLocalization().toString()));
		nameValuePairs.add(new BasicNameValuePair("id", object.getId().toString()));
		put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		httpClient.execute(put);
	}
	
	@Override
	public void PUT_R(Player object, Integer ready) throws IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPut put = new HttpPut(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("player_name", object.getName()));
		nameValuePairs.add(new BasicNameValuePair("hp", object.getHealthPoints().toString()));
		nameValuePairs.add(new BasicNameValuePair("ammo", object.getAmmunition().toString()));
		nameValuePairs.add(new BasicNameValuePair("loc", object.getLocalization().toString()));
		nameValuePairs.add(new BasicNameValuePair("id", object.getId().toString()));
		nameValuePairs.add(new BasicNameValuePair("ready", ready.toString()));
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
			String localization 	= jsonObject.optString("localization");
			Integer team 	= Integer.valueOf(jsonObject.optString("team"));
			Integer id 	= Integer.valueOf(jsonObject.optString("id"));
			
			Player player = new Player(name, healthPoints, ammunition, localization, team);
			player.setId(id);
			
			players.add(player);
			
		}
		return players;
	}

	
}
