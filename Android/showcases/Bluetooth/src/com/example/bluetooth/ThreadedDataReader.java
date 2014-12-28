package com.example.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class ThreadedDataReader extends Thread {
	
	/**
	 * Bluetooth socket is used only to initialize input stream.
	 */
	@SuppressWarnings("unused")
	private final BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private Scanner inputScanner;
    
	public ThreadedDataReader(BluetoothSocket bluetoothSocket) {
		this.bluetoothSocket = bluetoothSocket;
		initializeStreamAndScanner(bluetoothSocket);
	}
	
	public void run() {
		while(inputScanner.hasNextLine()) {
			String message = inputScanner.nextLine();
			
			if (message.equals("SHT")) {
				Log.i("MSG", message);
				ConnectedActivity.updateHandler.sendEmptyMessage(ConnectedActivity.SHOOT);
			}
		}
	}
	
	private void initializeStreamAndScanner(BluetoothSocket bluetoothSocket) {
		try {
			inputStream = bluetoothSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputScanner = new Scanner(inputStream);
	}
}
		

