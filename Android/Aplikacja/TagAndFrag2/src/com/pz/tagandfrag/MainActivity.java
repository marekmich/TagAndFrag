package com.pz.tagandfrag;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pz.tagandfrag.restclient.*;


public class MainActivity extends Activity {
	
	private Game game;

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
		new ThreadedClick().start();
	}
	private class ThreadedClick extends Thread {
		
		public void run() {

			
			try {
			
				game = new Game(10);
				//game.check("marek", 0);
			//Log.i ("checkkkkkkk",game.check("gracz", 111222).toString());
			//Log.i ("checkkkkkkk",game.check("gracz", 111222).toString());
			//Log.i ("checkkkkkkk",game.check("gracz", game.getByName("gracz").getId()).toString());
			//Player player = new Player("GraczJakis",99,33,"12345",1);
			
			//Player player2 = new Player("GraczJakis2",66,22,"14345",1);
			//Player player3 = new Player("GraczJakis3",100,100,"15",2);
			
			//game.resetGame();
			//Integer id, id2, id3;
			//id = game.check(player.getName(), player.getId());
			//id=1;
			//Log.i("check1", id.toString());
			//id2 = game.check(player.getName(), 3333);
			//id2=1;
			//Log.i("check2", id2.toString());
			//id3 = game.check(player.getName(), id);
			//id3=1;
			//Log.i("check3", id3.toString());
			
			ArrayList<Player> pl = new ArrayList<Player>(game.getAll());
			
			for(Player p: pl){
				Log.i("getttttt",p.toString());
			}
			
			Log.i("list",game.list().toString());
			//game.addPlayer(player2);
			//game.addPlayer(player3);
			
			//game.shotPlayer(player, "GraczJakis2");
			//game.shotPlayer(player, "GraczJakis2");
			//game.shotPlayer(player2, "GraczJakis");
			//game.shotPlayer(player3, "GraczJakis");
			
			//Log.i("get", game.getByName("GraczJakis").toString());
			
			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
}
