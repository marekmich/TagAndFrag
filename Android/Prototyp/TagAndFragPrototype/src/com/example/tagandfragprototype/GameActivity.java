package com.example.tagandfragprototype;

import java.io.IOException;

import org.json.JSONException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.tagandfragprototype.bluetooth.BluetoothDataReceiver;
import com.example.tagandfragprototype.restclient.Game;
import com.example.tagandfragprototype.restclient.Player;

public class GameActivity extends FragmentActivity {

	private static Game game;
	private static Player player;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game);
		
		ViewPager viewPager = (ViewPager) findViewById(R.id.game_activity);
		PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pageAdapter);
		
		Intent currentIntent = getIntent();
		String nickname = currentIntent.getStringExtra(StartingActivity.EXTRA_MESSAGE);
		
		game = new Game();
		player = new Player(nickname, 100, 100, 100);
		
		new AddNewPlayerTask().execute();
	}
	
	private class AddNewPlayerTask extends AsyncTask<Void, Void, Void> {

		public AddNewPlayerTask() {
			super();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				game.resetGame();
				game.addPlayer(player);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			LogFragment.logTextView.append("Dodano gracza " + player.getName() + "\n");
			new ThreadedBluetoothDataReader().start();
		}
	}
	
	private class UpdatePlayerTask extends AsyncTask<Void, Void, Void> {
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				game.updatePlayer(player);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
	private class ThreadedBluetoothDataReader extends Thread {
		private BluetoothDataReceiver receiver;
		private Handler handler;
		
		public ThreadedBluetoothDataReader() {
			super();
			receiver = new BluetoothDataReceiver(StartingActivity.bluetoothService.getBluetoothSocket());
			handler = new Handler();
		}
		
		public void run() {
			while (receiver.hasNextLine()) {
				final String message = receiver.readMessageWithPrefix("SHT", false);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						player.reduceHealth(10);
						LogFragment.logTextView.append(String.format("Otrzymalem strza³ od (%s): mam (%s) pkt zycia\n", message, player.getHealthPoints()));
						new UpdatePlayerTask().execute();
					}
				});
			}
			
		}
	}
}
