package com.example.tagandfragprototype;

import java.io.IOException;

import org.json.JSONException;

import com.example.tagandfragprototype.restclient.Game;
import com.example.tagandfragprototype.restclient.Player;
import com.example.tagandfragprototype.restclient.TagAndFragRestClient;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class GameActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game);
		ViewPager viewPager = (ViewPager) findViewById(R.id.game_activity);
		PageAdapter pageAdapter = new PageAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pageAdapter);
		
		Intent currentIntent = getIntent();
		String nickname = currentIntent.getStringExtra(StartingActivity.EXTRA_MESSAGE);
		new RestTask(nickname).execute();
	}
	
	private class RestTask extends AsyncTask<Void, Void, Void> {

		private String nickname;
		
		public RestTask(String nickname) {
			super();
			this.nickname = nickname;
		}

		@Override
		protected Void doInBackground(Void... params) {
			Game game = new Game();
			try {
				game.resetGame();
				game.addPlayer(new Player(nickname
						, 100, 100, 100));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
}
