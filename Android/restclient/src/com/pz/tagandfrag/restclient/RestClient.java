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
	 * Metoda zwraca Kolekcjê graczy nalê¿acych do dru¿yny o numerze teamId
	 * @param parameter domyœlnie "ALL_PLAYERS"; okreœla spoœród graczy o jakim imieniu
	 * wyszukiwaæ.
	 * @param teamId numer dru¿yny.
	 * @return Kolekcja obiektów typu Type.
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Type> 	GET(String parameter, Integer teamId) throws IOException, JSONException;
	
	/**
	 * Zwraca jednoelementow¹ kolekcjê zawieraj¹c¹ gracza o nazwie podanej jako parametr.
	 * @param parameter
	 * @return 
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Type> 	GET(String parameter) throws IOException, JSONException;
	
	/**
	 * Zwraca kolekcjê dru¿yn.
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 */
	public Collection<Team>	 	GET() throws IOException, JSONException;
	
	/**
	 * Dodaje do bazy danych gracza podanego jako parametr object.
	 * @param object
	 * @return numer id dodanego gracza b¹dŸ 0 w razie niepowodzenia.
	 * @throws IOException
	 */
	public String 				POST(Type object) throws IOException;

	/**
	 * Metoda realizuj¹ca metodê POST serwera.
	 * @param object obiekt typu Player;
	 * @param arg1 pierwszy argument; mo¿e przyjmowaæ wartoœæi "UPDATE", "TEAM",
	 * "SHOT", "END", "READY"
	 * @param arg2 istotny tylko gdy parametr arg1 równy jest "SHOT" lub "READY" 
	 * @return
	 * @throws IOException
	 */
	public String 				PUT(Type object, String arg1, String arg2) throws IOException;
	
	/**
	 * 
	 * @param object
	 * @param arg mo¿e przyjmowaæ wartoœæi "UPDATE", "TEAM", "END".
	 * @return
	 * @throws IOException
	 */
	public String 				PUT(Type object, String arg) throws IOException;

	public void 				DELETE() throws IOException;
}
