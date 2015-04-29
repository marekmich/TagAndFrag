
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
		String aditionalURLParameter = "?parameter=" + parameter;
		
		HttpURLConnection con = createConnection("GET", false, aditionalURLParameter);
		BufferedReader reader = createBufferedReader(con);
		
		String jsonGet = reader.readLine();
	
		reader.close();		
		JSONArray array = new JSONArray(jsonGet);
		con.disconnect();
		return fromJsonArrayToCollection(array, teamId);
	}
	
	@Override
	public Collection<Team> GET() throws IOException, JSONException
	{
		
		String aditionalURLParameter = "?parameter=LIST";
		
		HttpURLConnection con = createConnection("GET", false, aditionalURLParameter);
		BufferedReader reader = createBufferedReader(con);

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
		con.disconnect();
		return teams;
	}	
	
	@Override
	public String POST(Player object) throws IOException
	{
		StringBuilder params = new StringBuilder();
		
		params.append("player_name=" + object.getName());
		params.append("&id=" + object.getId().toString());

	    HttpURLConnection con = createConnection("POST", true);
	    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

	    writer.write(params.toString());
	    writer.flush();

	    BufferedReader reader = createBufferedReader(con);

		StringBuilder response = new StringBuilder();
		String line;
	
		while((line=reader.readLine())!=null) {
			response.append(line);
		}
	
		reader.close();			
		writer.close();
		con.disconnect();
		return response.toString();
	}
	
	@Override
	public String PUT(Player object, String arg) throws IOException
	{
		return PUT(object, arg, "");
	}
	
	@Override
	public String PUT(Player object, String arg1, String arg2) throws IOException
	{
		StringBuilder params = new StringBuilder();
		
		params.append("player_name=" + object.getName());
		params.append("&id=" + object.getId().toString());

		if(arg1.equals("UPDATE")) {
			params.append("&hp=" + object.getHealthPoints().toString());
			params.append("&ammo=" + object.getAmmunition().toString());
		}
		else if(arg1.equals("UPDATE_MAP"))	{
			params.append("&loc=" + object.getLocalization());
		}
		else if(arg1.equals("TEAM") || arg1.equals("END")) {
			params.append("&team=" + object.getTeam().toString());
		}
		else if(arg1.equals("READY")) {
			params.append("&ready=" + arg2);
		}
		else if(arg1.equals("SHOT")) {
			params.append("&attacker_name=" + arg2);
			params.append("&hp=" + object.getHealthPoints());
		}
		else if(arg1.equals("END")) {
			params.append("&end=" + "end");	
		}
		
		HttpURLConnection con = createConnection("PUT" ,true);
		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
	
		writer.write(params.toString());
		writer.flush();
		
		BufferedReader reader = createBufferedReader(con);
		
		StringBuilder response = new StringBuilder();
		String line;
	
		while((line=reader.readLine())!=null) {
			response.append(line);
		}

		reader.close();			
		writer.close();
		con.disconnect();
		return response.toString();
	}
	
	/**
	 * Tworzy i ustawia parametry po³¹czenia
	 * @param requestCommandType typ komendy wysy³any do serwera HTTP
	 * @param allowingOutputFlag flaga wskazuj¹ca, czy po³¹czenie dopuszcza wyjœcie
	 * @throws IOException 
	 * */
	private HttpURLConnection createConnection(String requestCommandType, boolean allowingOutputFlag) throws IOException {
		return createConnection(requestCommandType, allowingOutputFlag, "");
	}
	/**
	 * Tworzy i ustawia parametry po³¹czenia
	 * @param requestCommandType typ komendy wysy³any do serwera HTTP
	 * @param allowingOutputFlag flaga wskazuj¹ca, czy po³¹czenie dopuszcza wyjœcie
	 * @param additionalParameter dodatkowe parametry do adresu www
	 * @throws IOException 
	 * */
	private HttpURLConnection createConnection(String requestCommandType, boolean allowingOutputFlag, String additionalParameter) throws IOException {
		URL url = new URL(URL + additionalParameter);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(allowingOutputFlag);
		connection.setRequestMethod(requestCommandType);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" );
		connection.setRequestProperty("charset", "utf-8");
		
		return connection;
	}
	/**
	 * Tworzy {@link BufferedReader} z którego bêdzie zbierany wynik
	 * @param connection po³¹czenie z serwerem HTTP
	 * @throws IOException 
	 * */
	private BufferedReader createBufferedReader(HttpURLConnection connection) throws IOException {
		int code = connection.getResponseCode();
		if(code >=400) {
			return new BufferedReader(new InputStreamReader(connection.getErrorStream()));
		}
		else {
			return new BufferedReader(new InputStreamReader(connection.getInputStream()));
		}
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
				//TODO Sprawdziæ czy nie k³adzie siê tutaj - mo¿e siê bardzo piêknie wy³o¿yæ
				Integer cwc = Integer.valueOf(jsonObject.optString("cwc"));
				Player player = new Player(name, healthPoints, ammunition, localization, team);
				player.setId(id);
				player.setCWC(cwc);
				players.add(player);
			}
		}
		return players;
	}
}
