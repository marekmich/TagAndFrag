package com.example.tagandfragprototype.bluetooth;

import java.io.IOException;
import java.io.BufferedOutputStream;
import java.util.Scanner;

import android.bluetooth.BluetoothSocket;
/**
 * Nadajnik danych wysylanych przez uprzednio zainicjalizowany Bluetooth Socket
 * @see BluetoothService
 * @author Lukasz
 *
 */
@SuppressWarnings("unused")
public class BluetoothDataSender 
{
	private final BluetoothSocket bluetoothSocket;
	private BufferedOutputStream outputStream;
	/**
	 * Konstruktor inicjalizujacy gniazdo.
	 * @param bluetoothSocket
	 */
	public BluetoothDataSender(BluetoothSocket bluetoothSocket) {
		this.bluetoothSocket = bluetoothSocket;
		initializeStreamAndScanner(bluetoothSocket);
	}
	/**
	 * Blokwanie broni i wylaczanie diody led
	 * @param message
	 * */
	public void blockWeaponAndTurnLedOff()
	{
		sendMessage("WLN\r\n");
	}
	/**
	 * Odblokowanie broni i wlaczanie diody led
	 * @param message
	 * */
	public void unlockWeaponAndTurnLedOn()
	{
		sendMessage("WLF\r\n");
	}
	
	/**
	 * Wysla wiadomosc przez Bluetooth Socket
	 * @param message
	 * */
	private void sendMessage(String message)
	{
		/*Wsadzenie wiadomosci do buffora*/
		for(int i : message.toCharArray()) {
			try {
				outputStream.write(i);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*Wlasciwe wyslanie*/
		try {
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Inicjalizacja strumienia wychodzacego.
	 * @param bluetoothSocket
	 */
	private void initializeStreamAndScanner(BluetoothSocket bluetoothSocket) {
		try {
			outputStream = new BufferedOutputStream(bluetoothSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
