package com.example.serwertest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import com.example.restclient.Player;
import com.example.restclient.Game;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	protected Game game;
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

	public void onClickButton1(View view) {
		new ThreadedPOST().start();
	}
	
	
	public void onClickButton2(View view) {
		new ThreadedPUT().start();
	}

	public void onClickButton3(View view) {
		new ThreadedDELETE().start();
	}
	
	
	public void onClickButton4(View view) {
		new ThreadedGET().start();
	}
	
	public void onClickButton(View view) {
		new ThreadedSTART().start();
	}
	private class ThreadedDELETE extends Thread {
		
		public void run() {

			
			try {
			game.resetGame();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}	
	
	private class ThreadedSTART extends Thread {
		
		public void run() {

			
			game = new Game();
				
			
		}
	}	
	
	private class ThreadedPOST extends Thread {
		
		public void run() {

			EditText name  = (EditText)findViewById(R.id.editText1);
			EditText hea   = (EditText)findViewById(R.id.editText2);
			EditText amm   = (EditText)findViewById(R.id.editText3);
			EditText loc   = (EditText)findViewById(R.id.editText4);
			
			try {
				game.addPlayer(new Player(name.getText().toString(),
						Integer.parseInt(hea.getText().toString()),
						Integer.parseInt(amm.getText().toString()),
						Integer.parseInt(loc.getText().toString())));
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	}
	}
	
	private class ThreadedPUT extends Thread {
		
		public void run() {
			
			EditText name  = (EditText)findViewById(R.id.editText1);
			EditText hea   = (EditText)findViewById(R.id.editText2);
			EditText amm   = (EditText)findViewById(R.id.editText3);
			EditText loc   = (EditText)findViewById(R.id.editText4);
			
			try {
				game.updatePlayer(new Player(name.getText().toString(),
						Integer.parseInt(hea.getText().toString()),
						Integer.parseInt(amm.getText().toString()),
						Integer.parseInt(loc.getText().toString())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private class ThreadedGET extends Thread {
		
		public void run() {

		Collection<Player> players = new ArrayList<Player>();
			
			try {
				players=game.getPlayers();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			for (Player player : players) {
				Log.i("GET",player.toString());
			}
		}
	}
}
