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
	 * Zmiana kodu broni
	 * Uwaga: obsluzyc odpowiedz zgodnie z dokumentacja
	 * @param message
	 * */
	public void changeWeaponCode(int newCode)
	{
		// TODO Sprawdzanie dlugosci newCode?
		String correctNewCode = Integer.toString(newCode);
		switch(correctNewCode.length()) {
			case 1:
				correctNewCode = "00" + correctNewCode;
				break;
			case 2:
				correctNewCode = "0" + correctNewCode;
				break;
			default:
				break;
		}
		sendMessage("CWC\r\n");
		sendMessage(correctNewCode + "\r\n");
	}
	
	/**
	 * Zmiana pinu w module bluetooth broni
	 * Uwaga: obsluzyc odpowiedz zgodnie z dokumentacja
	 * @param message
	 * */
	public void changePIN(int newPin)
	{
		// TODO Sprawdzanie dlugosci newPin?
		String corretNewPIN = Integer.toString(newPin);
		switch(corretNewPIN.length()) {
			case 1:
				corretNewPIN = "000" + corretNewPIN;
				break;
			case 2:
				corretNewPIN = "00" + corretNewPIN;
				break;
			case 3:
				corretNewPIN = "0" + corretNewPIN;
				break;
			default:
				break;
		}
		sendMessage("PIN\r\n");
		sendMessage(corretNewPIN + "\r\n");
	}
	/**
	 * Zmiana nazwy broni
	 * Uwaga: obsluzyc odpowiedz zgodnie z dokumentacja
	 * @param message
	 * */
	public void changeWeaponName(String newWeaponName)
	{
		// TODO Sprawdzanie dlugosci newWeaponName?
		String correctNewWeaponName = Integer.toString(newWeaponName.length()) + newWeaponName;
		sendMessage("NME\r\n");
		sendMessage(correctNewWeaponName);		
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
