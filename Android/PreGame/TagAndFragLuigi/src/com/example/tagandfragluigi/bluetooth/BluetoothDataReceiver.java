package com.example.tagandfragluigi.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.bluetooth.BluetoothSocket;
/**
 * Odbiornik danych przychodzacych przez uprzednio zainicjalizowany Bluetooth Socket
 * @see BluetoothService
 * @author Mateusz
 *
 */
public class BluetoothDataReceiver {
	
	@SuppressWarnings("unused")
	private final BluetoothSocket bluetoothSocket;
	private InputStream inputStream;
	private Scanner inputScanner;
	
	/**
	 * Konstruktor inicjalizujacy gniazdo.
	 * @param bluetoothSocket
	 */
	public BluetoothDataReceiver(BluetoothSocket bluetoothSocket) {
		this.bluetoothSocket = bluetoothSocket;
		initializeStreamAndScanner(bluetoothSocket);
	}

	/**
	 * @param expectedMessage
	 * @return True, gdy oczekiwana wiadomosc rowna jest wiadomosci przychodzacej, w przeciwnym wypadku false
	 */
	public boolean readExpectedMessage(String expectedMessage) {
		String message = inputScanner.nextLine();
		if (message.equals(expectedMessage)) {
			return true;
		}
		return false;
	}

	/**
	 * @param prefix
	 * @param withPrefix
	 * @return Jezeli withPrefix == true zwraca cala wiadomosc, w przeciwnym wypadku bez prefix'u.
	 */
	public String readMessageWithPrefix(String prefix, boolean withPrefix) {
		String message = inputScanner.nextLine();
		if (message.startsWith(prefix)) {
			if (withPrefix) {
				return message;
			} else {
				String withoutPrefix = message.replace(prefix, "");
				return withoutPrefix;
			}
		}
		return null;
	}

	public boolean hasNextLine() {
		return inputScanner.hasNextLine();
	}
	
	/**
	 * Inicjalizacja strumienia przychodzacego i skanera.
	 * @param bluetoothSocket
	 */
	private void initializeStreamAndScanner(BluetoothSocket bluetoothSocket) {
		try {
			inputStream = bluetoothSocket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		inputScanner = new Scanner(inputStream);
	}
}
