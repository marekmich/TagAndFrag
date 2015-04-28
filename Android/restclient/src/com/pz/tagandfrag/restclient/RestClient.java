package com.pz.tagandfrag.restclient;

import java.io.IOException;


import java.util.Collection;

import org.json.JSONException;
/**
 * Interfejs klienta REST
 * @author Marek
 *
 * @param <Type>
 */
public interface RestClient<Type> {
	/**
	 * Metoda zwraca Kolekcj� graczy nal�acych do dru�yny o numerze teamId
	 * @param parameter domy�lnie "ALL_PLAYERS"; okre�la spo�r�d graczy o jakim imieniu
	 * wyszukiwa�.
	 * @param teamId numer dru�yny.
	 * @return Kolekcja obiekt�w typu Type.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Type> 	GET(String parameter, Integer teamId) throws IOException, JSONException;
	
	/**
	 * Zwraca jednoelementow� kolekcj� zawieraj�c� gracza o nazwie podanej jako parametr.
	 * @param parameter
	 * @return 
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Type> 	GET(String parameter) throws IOException, JSONException;
	
	/**
	 * Zwraca kolekcj� dru�yn.
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Team>	 	GET() throws IOException, JSONException;
	
	/**
	 * Dodaje do bazy danych gracza podanego jako parametr object.
	 * @param object
	 * @return numer id dodanego gracza b�d� 0 w razie niepowodzenia.
	 * @throws IOException
	 */
	public String 				POST(Type object) throws IOException;

	/**
	 * Metoda realizuj�ca metod� POST serwera.
	 * @param object obiekt typu Player;
	 * @param arg1 pierwszy argument; mo�e przyjmowa� warto��i "UPDATE", "TEAM",
	 * "SHOT", "END", "READY"
	 * @param arg2 istotny tylko gdy parametr arg1 r�wny jest "SHOT" lub "READY" 
	 * @return
	 * @throws IOException
	 */
	public String 				PUT(Type object, String arg1, String arg2) throws IOException;
	
	/**
	 * 
	 * @param object
	 * @param arg mo�e przyjmowa� warto��i "UPDATE", "TEAM", "END".
	 * @return
	 * @throws IOException
	 */
	public String 				PUT(Type object, String arg) throws IOException;

	public void 				DELETE() throws IOException;
}
