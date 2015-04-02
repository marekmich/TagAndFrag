package com.pz.tagandfrag.dialogs;

import java.util.ArrayList;
import java.util.Set;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pz.tagandfrag.ChooseTeamActivity;
import com.pz.tagandfrag.LoginActivity;
import com.pz.tagandfrag.R;

public class ChooseWeaponDialog extends DialogFragment {

	private Set<BluetoothDevice> devices;
	
	public ChooseWeaponDialog() {
		super();
		devices = LoginActivity.bluetoothService.getBluetoothAdapter().getBondedDevices();
	}
	
	public Dialog onCreateDialog(Bundle savedInstancState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle("Wybierz broñ")
				.setNegativeButton("Anuluj", null)
				.setItems(toListString(convertDevicesSetToArrayList(devices)), deviceClickedListener());
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
	
	private DialogInterface.OnClickListener deviceClickedListener() {
		return new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ArrayList<String> list = convertDevicesSetToArrayList(devices);

				LoginActivity.preferences.setMAC(findMacByName(list.get(which)));
				LoginActivity.preferences.saveMACDataFromPreferences();
				
				Intent intent = new Intent(getActivity(), ChooseTeamActivity.class);
		        startActivity(intent);
				Toast.makeText(getActivity(), R.string.nick_correct, Toast.LENGTH_LONG).show();
			}
		};
	}
}
