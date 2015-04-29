package com.pz.tagandfrag.managers;

/**
 * Manager pozwalaj¹cy wy³¹czyæ dzia³anie niektórych elementów aplikacji w celu
 * u³atwienia testowania.
 * @author £ukasz ¯urawski
 * */
public class DebugManager {
	/**
	 * Statyczne pole pozwalaj¹ce wy³¹czyæ ³¹cznoœci z bluetoothem
	 * */
	static public boolean withoutBluetooth = false;
	/**
	 * Statyczne pole pozwalaj¹ce wy³¹czyæ  wysy³ania komendy ready na serwer
	 * */
	static public boolean withoutReady = true;
}
