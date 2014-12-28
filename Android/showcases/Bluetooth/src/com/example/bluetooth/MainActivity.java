package com.example.bluetooth;

import java.util.Set;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final int REQUEST_ENABLE_BT = 0;
	private BluetoothService bluetoothService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bluetoothService = new BluetoothService();
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void onTurnOnBluetoothButtonClicked(View view) {
		if (bluetoothService.getBluetoothAdapter().isEnabled()) {
			Toast.makeText(getApplicationContext(), "Bluetooth already turned on", Toast.LENGTH_SHORT).show();
  		} else {
  			Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
  			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
  		}
		
	}
	
	public void onEnableDiscoverAbilityButtonClicked(View view) {
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		startActivity(discoverableIntent);
	}
	
	public void onShowDevicesButtonClicked(View view) { 
		Set<BluetoothDevice> devices = bluetoothService.getBluetoothAdapter().getBondedDevices();
		
		if (!bluetoothService.getBluetoothAdapter().isEnabled()) {
			Toast.makeText(getApplicationContext(), "Turn on Bluetooth first!", Toast.LENGTH_SHORT).show();
		} else {
			if (devices.isEmpty()) {
				Toast.makeText(getApplicationContext(), "No paried devices", Toast.LENGTH_SHORT).show();
			} else {
				DialogFragment newDialogFragment = new DevicesDialogFragment(devices);
				newDialogFragment.show(getFragmentManager(), "DEV");
			}
		}
	}
	
}
