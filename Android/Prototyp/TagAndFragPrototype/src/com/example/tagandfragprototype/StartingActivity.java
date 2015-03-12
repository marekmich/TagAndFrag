package com.example.tagandfragprototype;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.tagandfragprototype.bluetooth.BluetoothService;

public class StartingActivity extends Activity {

	private EditText nickEditText;
	private TextView deviceTextView;
	private Button connectButton;
	private ProgressBar connectProgressBar;

	public static BluetoothService bluetoothService;
	private static final int REQUEST_ENABLE_BT = 0;
	public final static String EXTRA_MESSAGE = "com.example.tagandfragprototype.MSG";
	private static String MAC = null; //"30:14:08:28:05:74";
	
	
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
		if (nickEditText.length() > 0) {
			new DeviceChooser(this).buildWindow().show();
		} else {
			Toast.makeText(this, "Wpisz swój nick", Toast.LENGTH_LONG).show();;
		}
		
	}
	
	public void onConnectButtonClicked(View view) {
		ConnectProgressBarTask task = new ConnectProgressBarTask(this, nickEditText.getText().toString());
		task.execute();
		connectButton.setEnabled(false);
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
	
	private class DeviceChooser extends AlertDialog {
		private Context context;
		public DeviceChooser(Context context) {
			super(context);
			this.context = context;
		}

		public AlertDialog buildWindow() {
			final Set<BluetoothDevice> devices = bluetoothService.getBluetoothAdapter().getBondedDevices();
			final ArrayList<String> devicesList = new ArrayList<String>(devices.size());
			
			for (BluetoothDevice device : devices) {
				devicesList.add(device.getName());
			}
			
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder	.setTitle("Tytul")
					.setSingleChoiceItems(toListString(devicesList), 0, null)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Integer position = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
							deviceTextView.setVisibility(TextView.VISIBLE);
							connectButton.setVisibility(Button.VISIBLE);
							
							for (BluetoothDevice device : devices) {
								if (devicesList.get(position).equals(device.getName())) {
									MAC = device.getAddress();
								}
							}
							deviceTextView.setText("");
							deviceTextView.append("Twoje urzadzenie: " + devicesList.get(position) + "\n");
							deviceTextView.append("MAC: " + MAC + "\n");
						}
					})
					.setNegativeButton("Cancel", null);
				
			return builder.create();
		}
		
		private String[] toListString(ArrayList<String> list) {
			String[] listString = new String[list.size()];
			listString = list.toArray(listString);
			return listString;
		}
	}
}
