package com.example.bluetooth;

import java.util.ArrayList;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Builder for Dialog which contains list of paried devices.
 * It is also a starting point for new Activity
 * @author Mateusz
 *
 */
public class DevicesDialogFragment extends DialogFragment {

	public final static String EXTRA_MESSAGE = "com.example.bluetooth.CONNECTED_DEVICE";
	private Set<BluetoothDevice> devices;
	
	public DevicesDialogFragment(Set<BluetoothDevice> devices) {
		super();
		this.devices = devices;
	}

	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle("Paried devices")
				.setNegativeButton("Cancel", null)
				.setItems(toListString(convertDevicesSetToArrayList(devices)), listenerForListClicked());
				
		return builder.create();
	}
	
	private String[] toListString(ArrayList<String> list) {
		String[] listString = new String[list.size()];
		listString = list.toArray(listString);
		return listString;
	}
	
	private ArrayList<String> convertDevicesSetToArrayList(Set<BluetoothDevice> devices) {
		ArrayList<String> devicesList = new ArrayList<String>();
		for (BluetoothDevice device : devices) {
			devicesList.add(device.getName());
		}
		
		return devicesList;
	}
	
	private String findMacByName(String deviceName) {
		for (BluetoothDevice device : devices) {
			if (device.getName().equals(deviceName)) {
				return device.getAddress();
			}
		}
		return null;
	}
	
	private DialogInterface.OnClickListener listenerForListClicked() {
		return new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				ArrayList<String> list = convertDevicesSetToArrayList(devices);
				Toast.makeText(getActivity(), "MAC: " + findMacByName(list.get(which)), Toast.LENGTH_SHORT).show();
				
				Intent connectedIntent = new Intent(getActivity(), ConnectedActivity.class);
				connectedIntent.putExtra(EXTRA_MESSAGE, findMacByName(list.get(which)));
				startActivity(connectedIntent);
			}
		};
	}
}
