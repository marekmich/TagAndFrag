package com.example.serwertest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import com.example.restclient.Player;
import com.example.restclient.RestClient;
import com.example.restclient.TagAndFragRestClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClickButton(View view) {
		new ThreadedRestClient().start();
	}
	
	private class ThreadedRestClient extends Thread {
		
		public void run() {
//			RestClient<Player> restClient = new TagAndFragRestClient();
//			Player matt = new Player("Matt", 100, 100, 100);
//			try {
//				restClient.POST(matt);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		
			Collection<Player> players = new ArrayList<Player>();
			RestClient<Player> restClient = new TagAndFragRestClient();
			try {
				players = restClient.GET();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			for (Player player : players) {
				Log.i("GET", "Gracz " + player.getName() + " ma " + player.getAmmunition() + " amunicji");
			}
		}
	}
}
