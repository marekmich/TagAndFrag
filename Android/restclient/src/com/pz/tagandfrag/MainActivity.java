package com.pz.tagandfrag;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONException;

import android.app.Activity;
import android.content.res.AssetManager;
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
	
	public void onClickButton1(View view) {
		new ThreadedClick1().start();
	}
	
	private class ThreadedClick extends Thread {
		
		public void run() {

			
			try {
			
				game = new Game(10);
				Test t = new Test(game);
			      
			     t.script1();
				
				//Player player2= game.getByName("zdenek"); player2.setHealthPoints(100);
				//Player player3= game.getByName("mateo"); player3.setHealthPoints(100);
				
			//game.shotPlayer(player2, player3.getName());
				
				
				//File plik = new File("NOTICE.txt");
				//Scanner s = new Scanner(plik);
				//System.out.println(s.nextLine());
				//FileInputStream fstream = new FileInputStream("test.txt");
				///game.check("marek", 0);
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
			
			//ArrayList<Player> pl = new ArrayList<Player>(game.getAll());
			game.getAll();
			
				//Log.i("getttttt",pl.toString());
			
			
		//*ArrayList<Team> list = new ArrayList<Team>(game.list());
		//	game.list();
		//*	Log.i("list",list.toString());
			//game.addPlayer(player2);
			//game.addPlayer(player3);
			
			//game.shotPlayer(player, "GraczJakis2");
			//game.shotPlayer(player, "GraczJakis2");
			//game.shotPlayer(player2, "GraczJakis");
			//game.shotPlayer(player3, "GraczJakis");
			
			//Log.i("get", game.getByName("GraczJakis").toString());
			
			
			} catch (Exception e) {
				e.printStackTrace();
			}// catch (JSONException e) {
				//e.printStackTrace();
			//}
		}
	}
private class ThreadedClick1 extends Thread {
		
		public void run() {

			
			try {
			
				game = new Game(10);
				Test t = new Test(game);
			      
			     t.script2();
				
game.getAll();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
