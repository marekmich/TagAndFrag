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
import android.view.KeyEvent;
import android.widget.Toast;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.activity.ChooseTeamActivity;
import com.pz.tagandfrag.bluetoothservice.BluetoothService;
import com.pz.tagandfrag.managers.DataManager;

/**
 * Okienko pozwalaj�ce wybra� bro� (modu� bluetooth).
 * @author Mateusz Wrzos
 * @see BluetoothService
 * 
 */
public class ChooseWeaponDialog extends DialogFragment {

	/**
	 * Zbi�r sprawowanych urz�dze�.
	 */
	private Set<BluetoothDevice> devices;
	
	/**
	 * Konstruktor pobieraj�cy zbi�r sparowanych urz�dze� z domy�lnego adaptera.
	 * @see BluetoothService
	 * @see DataManager
	 */
	public ChooseWeaponDialog() {
		super();
		devices = DataManager.bluetoothService.getBluetoothAdapter().getBondedDevices();
	}
	
	/**
	 * Buduje dialog. Ustawia na nim list� sparowanych urz�dze�.
	 * 
	 */
	public Dialog onCreateDialog(Bundle savedInstancState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle(getString(R.string.choose_weapon_title))
				.setPositiveButton(getString(R.string.pair_devices_button), onPairButtoniClicked())
				.setOnKeyListener(onBackPressed());

		ArrayList<String> devicesNames = convertDevicesSetToArrayList(devices);
		if (devicesNames.isEmpty()) {
			builder.setMessage(getString(R.string.no_paired_devices_message));
		} else {
			builder.setItems(toArray(devicesNames), deviceClickedListener());
		}
		return builder.create();
	}
	
	/**
	 * Konwertuje list� do tablicy
	 * @param lista 
	 * @return tablica stworzona z tej listy
	 */
	private String[] toArray(ArrayList<String> list) {
		String[] listString = new String[list.size()];
		listString = list.toArray(listString);
		return listString;
	}

	/**
	 * Konwertuje zbi�r do ArrayList<>
	 * @param zbi�r urzadzen 
	 * @return lista urzadzen
	 */
	private ArrayList<String> convertDevicesSetToArrayList(Set<BluetoothDevice> devices) {
		ArrayList<String> devicesList = new ArrayList<String>();
		for (BluetoothDevice device : devices) {
			devicesList.add(device.getName());
		}
		
		return devicesList;
	}
	
	/**
	 * Znajduje adres MAC na podstawie nazwy urz�dzenia.
	 * @param nazwa urzadzenia
	 * @return adres MAC, gdy nie znajdzie takiego urzadzenia to null
	 */
	private String findMacByName(String deviceName) {
		for (BluetoothDevice device : devices) {
			if (device.getName().equals(deviceName)) {
				return device.getAddress();
			}
		}
		return null;
	}
	
	/**
	 * Listener obs�ugujacy klikniecia na okienko. Ustawia MAC do preferencji. 
	 * Uruchamia Intent do nastepnej aktywnosci
	 * @return nowy OnClickListener na dialogu.
	 * @see DataManager
	 * @see ChooseTeamActivity
	 */
	private DialogInterface.OnClickListener deviceClickedListener() {
		return new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try
				{
					ArrayList<String> list = convertDevicesSetToArrayList(devices);

					DataManager.preferences.setMAC(findMacByName(list.get(which)));
					DataManager.preferences.saveMACDataFromPreferences();
					
					Intent intent = new Intent(getActivity(), ChooseTeamActivity.class);
			        startActivity(intent);
					Toast.makeText(getActivity(), R.string.nick_correct, Toast.LENGTH_LONG).show();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
	}
	
	/**
	 * Listener obs�uguj�cy klikni�cia na button otwieraj�cy ustawienia Bluetooth.
	 * @return
	 */
	private DialogInterface.OnClickListener onPairButtoniClicked() {
		return new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
				dialog.dismiss();
			}
		};
	}
	
	/**
	 * 
	 * @return nowy listener obs�uguj�cy klikni�cie "wstecz".
	 */
	private DialogInterface.OnKeyListener onBackPressed() {
		return new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Toast.makeText(getActivity(), getString(R.string.choose_weapon_back_pressed), Toast.LENGTH_SHORT).show();
					return true;
				}
				return false;
			}
		};
	}
}
