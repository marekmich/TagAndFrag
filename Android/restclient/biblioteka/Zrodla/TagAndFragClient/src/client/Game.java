package client;


import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;


public class Game{
	
	private String url;
	private Map<String, Integer> players;
	
	
	public Game()

	{
		this.url = "http://158.75.2.62:8080";
		this.players = new HashMap<String, Integer>();
	}


	
	public Game(String url)
	{
		this.url = url;
		this.players = new HashMap<String, Integer>();
	}
	

		private String POST(String player) throws Exception
	{

		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPost post = new HttpPost(url);

		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);

		nameValuePairs.add(new BasicNameValuePair("player_name", player));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		HttpResponse response = client.execute(post);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

		String line = "", message = "";

		while ((line = rd.readLine()) != null) message += line + "\n";

		return message;

	}

	private void PUT(String name, Integer hp, Integer ammo, Integer loc) throws Exception
	{

		HttpClient client = HttpClientBuilder.create().build();
		
		HttpPut put = new HttpPut(url);

		List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>(1);

		nameValuePairs.add(new BasicNameValuePair("player_name", name));

		if (hp >= 0) nameValuePairs.add(new BasicNameValuePair("hp", hp.toString()));

		if (ammo >= 0) nameValuePairs.add(new BasicNameValuePair("ammo", ammo.toString()));

		if (loc >= 0) nameValuePairs.add(new BasicNameValuePair("loc", loc.toString()));

		put.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		client.execute(put);

	}

	
	private void DELETE() throws Exception

	{
		HttpClient client = HttpClientBuilder.create().build();
		
		HttpDelete delete = new HttpDelete(url);

		client.execute(delete);
	}

	private Player[] GET() throws Exception
	{

		HttpClient client = HttpClientBuilder.create().build();

		HttpGet request = new HttpGet(url);

		HttpResponse response = client.execute(request);

		BufferedReader rd = new BufferedReader(new InputStreamReader(response

		.getEntity().getContent()));

		ObjectMapper mapper = new ObjectMapper();

		Player[] players  = mapper.readValue(rd, Player[].class);

		return players;

	}
	
	public void newGame() throws Exception
	{
		DELETE();
	}
	
	private void update() throws Exception
	{
		players.clear();
		Player[] Gracze = GET();
		for(int i =0; i<Gracze.length; i++)
		players.put(Gracze[i].getName(), Gracze[i].getLocalization());
	}
	
	public boolean addPlayer(String name) throws Exception
	{
		update();
		
		if(players.containsKey(name)) return false;
		
		players.put(name, 123);
		POST(name);
		return true;		
	}
	
	public Map<String, Integer> getLocalizations() throws Exception
	{
		update();
		return players;
	}

	public void updateHp(String name, Integer hp) throws Exception
	{
		PUT(name, hp, -1, -1);
	}
	public void updateAmmo(String name, Integer ammo) throws Exception
	{
		PUT(name, -1, ammo, -1);
	}
	public void updateLoc(String name, Integer loc) throws Exception
	{
		PUT(name, -1, -1, loc);
	}

}
