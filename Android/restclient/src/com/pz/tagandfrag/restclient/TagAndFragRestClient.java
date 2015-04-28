
package com.pz.tagandfrag.restclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Klasa implementuj¹ca klienta REST.
 * @author Marek
 *
 */
public class TagAndFragRestClient implements RestClient<Player> {

	/**
	 * Pole statyczne zawieraj¹ce adres url serwera.
	 */
	public static String URL = "http://158.75.2.62:8080";
	
	@Override
	public Collection<Player> GET(String parameter, Integer teamId) throws IOException, JSONException
	{
		
	 URL url = new URL(URL+"?parameter="+parameter);

	 HttpURLConnection con=(HttpURLConnection) url.openConnection();

	con.setRequestMethod("GET");
	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
	con.setRequestProperty("charset", "utf-8");
	
	BufferedReader reader;

	int code = con.getResponseCode();
	if(code >=400) {
					reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					return null;
					}
	else 		   reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

	String jsonGet = reader.readLine();

	reader.close();		
	JSONArray array = new JSONArray(jsonGet);
	return fromJsonArrayToCollection(array, teamId);

	}
	
	@Override
	public Collection<Player> GET(String parameter) throws IOException, JSONException
	{
		return GET(parameter,0);
	}
	
	@Override
	public Collection<Team> GET() throws IOException, JSONException
	{
		
	 URL url = new URL(URL+"?parameter=LIST");

	 HttpURLConnection con=(HttpURLConnection) url.openConnection();

	con.setRequestMethod("GET");
	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
	con.setRequestProperty("charset", "utf-8");
	
	BufferedReader reader;

	int code = con.getResponseCode();
	if(code >=400) {
					reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					return null;
					}
	else 		   reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

	String jsonGet = reader.readLine();
	reader.close();		

	JSONObject jsonObject  = new JSONObject(jsonGet);
	
	Iterator<?> nameItr = jsonObject.keys();
	Collection<Team> teams = new ArrayList<Team>();
	
	
	while(nameItr.hasNext()) {
	    String id =(String) nameItr.next();
	    Integer size =(Integer) jsonObject.get(id);
	    teams.add(new Team(Integer.valueOf(id), size));
	    }
	
	return teams;  

	}	
	
	@Override
	public String POST(Player object) throws IOException
	{
		StringBuilder params = new StringBuilder();
		
		params.append("player_name=" + object.getName());
		params.append("&id=" + object.getId().toString());


	    URL url = new URL(URL);
	 
	HttpURLConnection con=(HttpURLConnection) url.openConnection();

	con.setDoOutput(true);
	con.setRequestMethod("POST");
	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
    con.setRequestProperty("charset", "utf-8");
	OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

	writer.write(params.toString());
	writer.flush();

	BufferedReader reader;

	int code = con.getResponseCode();
	if(code >=400) reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	else 		   reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

	StringBuilder response = new StringBuilder();
	String line;

	while((line=reader.readLine())!=null) response.append(line);

	reader.close();			
	writer.close();

	return response.toString();
	}
	
	
	@Override
	public String PUT(Player object, String arg1, String arg2) throws IOException
	{
		StringBuilder params = new StringBuilder();
		
								params.append("player_name=" + object.getName());
								params.append("&id=" + object.getId().toString());

	if(arg1.equals("UPDATE")) params.append("&hp=" + object.getHealthPoints().toString());
	if(arg1.equals("UPDATE")) params.append("&ammo=" + object.getAmmunition().toString());
	if(arg1.equals("UPDATE"))	params.append("&loc=" + object.getLocalization());

	if(arg1.equals("TEAM") ||
	   arg1.equals("END")) 	params.append("&team=" + object.getTeam().toString());
	if(arg1.equals("READY")) 	params.append("&ready=" + arg2);
	if(arg1.equals("SHOT")) 	params.append("&attacker_name=" + arg2);
	if(arg1.equals("END")) 	params.append("&end=" + "end");	
	
	URL url = new URL(URL);
	 
	HttpURLConnection con=(HttpURLConnection) url.openConnection();

	con.setDoOutput(true);
	con.setRequestMethod("PUT");
	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
    con.setRequestProperty("charset", "utf-8");
	OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

	writer.write(params.toString());
	writer.flush();

	BufferedReader reader;

	int code = con.getResponseCode();
	if(code >=400) reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	else 		   reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

	StringBuilder response = new StringBuilder();
	String line;

	while((line=reader.readLine())!=null) response.append(line);

	reader.close();			
	writer.close();

	return response.toString();
	}
	
	@Override
	public String PUT(Player object, String arg) throws IOException
	{
		return PUT(object,arg,null);
	}

	@Override
	public void DELETE() throws IOException {
		// TODO Auto-generated method stub
		
	}
	/**
	 * metoda prywatna konwertuj¹ca tablicê obiektów JSON na kolekcjê obiektów Player.
	 * @param array
	 * @param teamId numer dru¿yny (liczba naturalna);
	 * @return Kolekcja graczy nale¿acych do dru¿yny numer teamId, lub wszystkich gdy teamId==0.
	 * @throws JSONException
	 */
	private Collection<Player> fromJsonArrayToCollection(JSONArray array, Integer teamId) throws JSONException {
		Collection<Player> players = new ArrayList<Player>();
		for (int i = 0; i < array.length(); i++) {
			JSONObject jsonObject  = array.getJSONObject(i);
			Integer team 	= Integer.valueOf(jsonObject.optString("team"));
			
			if((team==teamId) || (teamId==0))
			{
			String name 			= jsonObject.optString("name");
			Integer healthPoints 	= Integer.valueOf(jsonObject.optString("health"));
			Integer ammunition 		= Integer.valueOf(jsonObject.optString("ammunition"));
			String localization 	= jsonObject.optString("localization");
			Integer id 	= Integer.valueOf(jsonObject.optString("id"));
			
			Player player = new Player(name, healthPoints, ammunition, localization, team);
			player.setId(id);
			
			players.add(player);
			}
			
		}
		return players;
	}





}
