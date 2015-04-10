package com.example.tagandfragluigi.bluetooth;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothService {

	private BluetoothAdapter bluetoothAdapter;
	private BluetoothSocket bluetoothSocket;
	private BluetoothDevice device;
	
	private static final UUID MY_UUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
	/**
	 * Konstruktor inicjalizujacy pole BluetoothAdapter domyslnym modulem z naszego urzadzenia.
	 */
	public BluetoothService() {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	
	/**
	 * 
	 * @return bluetoothAdapter
	 */
	public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	
	/**
	 * 
	 * @return bluetoothSocket
	 */
	public BluetoothSocket getBluetoothSocket() {
		return bluetoothSocket;
	}
	
	/**
	 * 
	 * @return nasze urzadzenie.
	 */
	public BluetoothDevice getDevice() {
		return device;
	}
	
	/**
	 * Zamyka gniazdo.
	 * @param bluetoothSocket
	 */
	public void closeBluetoothSocket(BluetoothSocket bluetoothSocket) {
		try {
			bluetoothSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Inicjalizacja gniazda. 
	 * 
	 */
	public void initializeBluetoothSocket() {
		try {
			bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
			bluetoothSocket.connect();
		} catch (IOException e) {
			closeBluetoothSocket(bluetoothSocket);
			e.printStackTrace();
		}
		
	}
	/**
	 * Polaczenie z urzadzeniem zdalnym za pomoca jego adresu MAC
	 * @param macAddress
	 */
	public void connectWithDeviceByMacAddress(String macAddress) {
		device = bluetoothAdapter.getRemoteDevice(macAddress);
		bluetoothAdapter.cancelDiscovery();
		initializeBluetoothSocket();
	}
	
	
}