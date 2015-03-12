package com.example.tagandfragprototype;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tagandfragprototype.bluetooth.BluetoothService;

public class StartingActivity extends Activity {

	private EditText nickEditText;
	private TextView deviceTextView;
	private Button connectButton;
	private ProgressBar connectProgressBar;

	public static BluetoothService bluetoothService;
	private static final int REQUEST_ENABLE_BT = 0;
	public final static String EXTRA_MESSAGE = "com.example.tagandfragprototype.MSG";
	private static String MAC = "30:14:08:28:05:74";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starting);
		initializeViewComponents();
		bluetoothService = new BluetoothService();
		turnOnBluetoothIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.starting, menu);
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
	
	public void onChooseDeviceButtonClicked(View view) {
		deviceTextView.setVisibility(TextView.VISIBLE);
		connectButton.setVisibility(Button.VISIBLE);
		
		deviceTextView.append("Twoje urzadzenie: TU BEDZIE NAZWA");
		deviceTextView.append("MAC: TU BEDZIE MAC");
	}
	
	public void onConnectButtonClicked(View view) {
		ConnectProgressBarTask task = new ConnectProgressBarTask(this, nickEditText.getText().toString());
		task.execute();
	}
	
	private void initializeViewComponents() {
		nickEditText = (EditText) findViewById(R.id.nickEditText);
		deviceTextView = (TextView) findViewById(R.id.device_text_view);
		connectButton = (Button) findViewById(R.id.connect_button);
		connectProgressBar = (ProgressBar) findViewById(R.id.connect_progress_bar);
		setComponentsVisibility(false);
	}
	
	private void setComponentsVisibility(boolean areVisible) {
		if (areVisible) {
			deviceTextView.setVisibility(TextView.VISIBLE);
			connectButton.setVisibility(Button.VISIBLE);
			connectProgressBar.setVisibility(ProgressBar.VISIBLE);
		} else {
			deviceTextView.setVisibility(TextView.INVISIBLE);
			connectButton.setVisibility(Button.INVISIBLE);
			connectProgressBar.setVisibility(ProgressBar.INVISIBLE);
		}
	}
	
	private void turnOnBluetoothIfNeeded() {
		if (!bluetoothService.getBluetoothAdapter().isEnabled()) {
			Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
		}
	}
	
	private class ConnectProgressBarTask extends AsyncTask<Void, Void, Void> {

		private Activity activity;
		private String message;
		
		public ConnectProgressBarTask(Activity activity, String message) {
			super();
			this.activity = activity;
			this.message = message;
		}

		@Override
		protected void onPostExecute(Void result) {
			connectProgressBar.setVisibility(ProgressBar.INVISIBLE);
			Intent intent = new Intent(activity, GameActivity.class);
			intent.putExtra(StartingActivity.EXTRA_MESSAGE, message);
			startActivity(intent);
		}

		@Override
		protected void onPreExecute() {
			connectProgressBar.setVisibility(ProgressBar.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			bluetoothService.connectWithDeviceByMacAddress(MAC);
			return null;
		}
		
	}
}
