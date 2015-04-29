package com.pz.tagandfrag.managers;

/**
 * Manager pozwalaj�cy wy��czy� dzia�anie niekt�rych element�w aplikacji w celu
 * u�atwienia testowania.
 * @author �ukasz �urawski
 * */
public class DebugManager {
	/**
	 * Statyczne pole pozwalaj�ce wy��czy� ��czno�ci z bluetoothem
	 * */
	static public boolean withoutBluetooth = false;
	/**
	 * Statyczne pole pozwalaj�ce wy��czy�  wysy�ania komendy ready na serwer
	 * */
	static public boolean withoutReady = true;
}
