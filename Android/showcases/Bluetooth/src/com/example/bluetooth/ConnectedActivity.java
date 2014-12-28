package com.example.bluetooth;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConnectedActivity extends Activity {

	public final static int SHOOT = 0;
	private String MAC;
	public static TextView recivedDataTextView;
	
	public final static Handler updateHandler = new Handler() {
		public void handleMessage(Message message) {
			final int what = message.what;
			switch (what) {
			case SHOOT: updateTextViewWithShoot(); break;
			}
		}

		private void updateTextViewWithShoot() {
			recivedDataTextView.append("Shoot!\n");
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		MAC = intent.getStringExtra(DevicesDialogFragment.EXTRA_MESSAGE);
		setContentView(R.layout.activity_connected);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connected, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_connected,
					container, false);
			return rootView;
		}
	}
	
	public void connectButtonClicked(View view) {
		BluetoothService bluetoothService = new BluetoothService();
		bluetoothService.connectWithDeviceByAddress(MAC);
		recivedDataTextView = (TextView) findViewById(R.id.incomingMessages);
		recivedDataTextView.append("Connected to: " + bluetoothService.getDevice().getName() + "\n");
		recivedDataTextView.append(MAC + "\n");
		new ThreadedDataReader(bluetoothService.getBluetoothSocket()).start();
	}

}


