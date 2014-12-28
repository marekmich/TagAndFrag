package com.example.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothService {

	private BluetoothAdapter bluetoothAdapter;
	private BluetoothSocket bluetoothSocket;
	private BluetoothDevice device;
	
	private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	public BluetoothService() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	
	public BluetoothSocket getBluetoothSocket() {
		return bluetoothSocket;
	}
	
	public BluetoothDevice getDevice() {
		return device;
	}

	public void connectWithDeviceByAddress(String address) {
		device = bluetoothAdapter.getRemoteDevice(address);
		Log.i("DEV", "Connecting to... " + device);
		bluetoothAdapter.cancelDiscovery();
		initializeBluetoothSocket();
		Log.i("DEV", "CONNECTED");
	}

	private void initializeBluetoothSocket() {
		try {
			bluetoothSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
			bluetoothSocket.connect();
		} catch (IOException e) {
			closeBluetoothSocket(bluetoothSocket);
			e.printStackTrace();
		}
		
	}
	
	private void closeBluetoothSocket(BluetoothSocket bluetoothSocket) {
		try {
			bluetoothSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
