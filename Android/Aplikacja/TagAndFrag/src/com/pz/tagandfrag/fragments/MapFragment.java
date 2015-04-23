package com.pz.tagandfrag.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;
import com.pz.tagandfrag.restclient.Player;

/**
 * Fragment odpowiedzialny za wyœwietlenie i inicjalizacjê mapy oraz jej widoku.
 * @author Mateusz Wrzos
 *
 */
public class MapFragment extends Fragment 
implements 	GoogleApiClient.ConnectionCallbacks,
			GoogleApiClient.OnConnectionFailedListener,
			LocationListener {

	private static boolean firstLocationEstablishing = true;
	/**
	 * Interwa³ aktualizacji w milisekundach.
	 */
	public static int UPDATE_INTERVAL = 3000;
	private Handler updateTeamOnMapHandler;
	
	/* Map elements */
	private GoogleMap map;
	private GoogleApiClient googleApiClient;
	private LocationRequest locationRequest;
	private Collection<Polyline> lines;
	private Collection<Marker> markers;
	
	/* UI elements */
	private View view;
	private MapView mapView;
	private CheckBox showPlayersCheckBox;
	
	/* Mock's */
	private Collection<Player> mockPlayers;
	
	/**
	 * Tworzy widok fragmentu. Inicjalizuje elementy widoku, buduje klienta Google API.
	 * Ustawia pole mapView, inicjalizuje mapê, tworzy nowy Handler aktualizuj¹cy pozycjê graczy (cyklicznie).
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_fragment, container, false);
		initializeShowPlayersCheckBox();
		//initializeShowLinesCheckBox();
		buildGoogleApiClient();
		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		setUpMap();
		updateTeamOnMapHandler = new Handler();
		updateTeamOnMapHandler.removeCallbacks(updateTeamOnMapTask());
		updateTeamOnMapHandler.postDelayed(updateTeamOnMapTask(), TeamFragment.UPDATE_PERIOD);
		return view;
	}
	
	/**
	 * Buduje klienta Google API.
	 */
	private void buildGoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(getActivity())
		.addApi(LocationServices.API)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.build();
	}
	
	@SuppressWarnings("unused")
	private void initalizeMockPlayers() {
		mockPlayers = new ArrayList<Player>();
		mockPlayers.add(new Player("Mock1", 100, 100, "53.010918#18.590522", -1));
		mockPlayers.add(new Player("Mock2", 100, 100, "53.010802#18.590371", -1));
		mockPlayers.add(new Player("Mock3", 100, 100, "53.011007#18.590663", -1));
		mockPlayers.add(new Player("Mock4", 100, 100, "53.009799#18.594387", -1));
		mockPlayers.add(new Player("Mock5", 100, 100, "53.010264#18.593690", -1));
		mockPlayers.add(new Player("Mock6", 100, 100, "53.010528#18.595149", -1));
	}
	
	/**
	 * Inicjalizuje CheckBox pokazuj¹cy graczy na mapie.
	 */
	private void initializeShowPlayersCheckBox() {
		showPlayersCheckBox = (CheckBox) view.findViewById(R.id.showPlayersCheckBox);
		showPlayersCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Click", "Click");
				if (showPlayersCheckBox.isChecked()) {
					addAllMarkersToMap();
//					showLinesCheckBox.setEnabled(true);
				} else {
					removeAllMarkersFromMap();
//					removeAllLinesFromMap();
//					showLinesCheckBox.setChecked(false);
//					showLinesCheckBox.setEnabled(false);
				}
			}
			
		});
	}
	

//	/**
//	 * Inicjalizuje CheckBox pokazuj¹cy linie na mapie (³¹cz¹ce gracza z jego druzyna).
//	 */
//	private void initializeShowLinesCheckBox() {
//		showLinesCheckBox = (CheckBox) view.findViewById(R.id.showLinesCheckBox);
//		showLinesCheckBox.setEnabled(false);
//		showLinesCheckBox.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Log.i("CLICK", "Checkbox");
//				if (showLinesCheckBox.isChecked()) {
//					addAllLinesToMap();
//				} else {
//					removeAllLinesFromMap();
//				}
//
//			}
//		});
//	}


	/**
	 * Dodaje wszystkie Markery z na podstawie pozycji graczy z mojej druzyny.
	 */
	private void addAllMarkersToMap() {
		for (Player player : DataManager.players) {
			if (!player.getId().equals(DataManager.player.getId())) {
				Log.i("MARKER", "Dodaje " + player.getId());
				LatLng location = getPlayerLocation(player);
				if (location != null) {
					MarkerOptions options = new MarkerOptions().title(player.getName())
							.position(location)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.player));
					Marker marker = map.addMarker(options);
					markers.add(marker);		
				}
			}
		}
	}
	
	/**
	 * Usuwa wszystkie markery z mapy.
	 */
	private void removeAllMarkersFromMap() {
		for (Marker marker : markers) {
			marker.remove();
		}
		markers.clear();
	}
	
	/**
	 * Dodaje wszystkie linie ³¹cz¹ce gracza z jego dru¿yn¹. 
	 */
	private void addAllLinesToMap() {
		LatLng myLocation = new LatLng(	Double.valueOf(map.getMyLocation().getLatitude()), 
										Double.valueOf(map.getMyLocation().getLongitude())
										);
		for (Player player : DataManager.players) {
			LatLng location = getPlayerLocation(player);
			if (location != null) {
				PolylineOptions lineOptions = new PolylineOptions().add(myLocation).add(location).width(5);
				Polyline line = map.addPolyline(lineOptions);
				lines.add(line);					
			}
		}
	}
	
	/**
	 * Usuwa wszystkie linie z mapy.
	 */
	private void removeAllLinesFromMap() {
		for (Polyline line : lines) { 
			line.remove();
		}
		lines.clear();
	}
	
	/**
	 * Przerysowywuje obiekty na mapie.
	 */
	private void redrawMapObjects() {
		removeAllMarkersFromMap();
		if (showPlayersCheckBox.isChecked()) {
			addAllMarkersToMap();
		}
	}
	
	/**
	 * Zadanie aktualizujace pozycje druzyny na mapie.
	 * @return obiekt Runnable, argument cyklicznego Handlera 
	 */
	private Runnable updateTeamOnMapTask() {
		return new Runnable() {
			
			@Override
			public void run() {
				redrawMapObjects();
				updateTeamOnMapHandler.postDelayed(updateTeamOnMapTask(), TeamFragment.UPDATE_PERIOD);
				Log.i("MAP_OBJ", "Updated markers");
			}
		};
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}

	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		googleApiClient.connect();
	}

	@Override
	public void onStop() {
		googleApiClient.disconnect();
		super.onStop();
	}

	/**
	 * Inicjalizuje mapê, liste markerów i linii, wy³¹cza gesty na mapie.
	 */
	private void setUpMap() {
		// Radzyn
//		double lat = 53.385034;
//		double lng = 18.937130;
		
		MapsInitializer.initialize(getActivity());
		lines = new ArrayList<Polyline>();
		markers = new ArrayList<Marker>();
		map = mapView.getMap();
		map.getUiSettings().setAllGesturesEnabled(false);
		map.getUiSettings().setMyLocationButtonEnabled(false);
		map.getUiSettings().setZoomControlsEnabled(true);
		map.setMyLocationEnabled(true);
	}

	
	/**
	 * Gdy lokalizacja sie zmieni po raz pierwszy to przybliza kamerê i j¹ centruje na lokalizacji.
	 * Centruje kamere za kazdym innym razem.
	 * Wykonuje zadanie aktualizujace pozycje gracza na serwerze.
	 * @see UpdatePlayerLocationTask
	 */
	@Override
	public void onLocationChanged(Location location) {
		LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
		if (firstLocationEstablishing) {
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(newLocation, 17);
			map.animateCamera(cameraUpdate);
			firstLocationEstablishing = false;
		} else {
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(newLocation);
			map.moveCamera(cameraUpdate);
		}
		new UpdatePlayerLocationTask(newLocation).execute();
		Log.i("MAP", location.toString());
	}

	/**
	 * Gdy polaczenie zawiedzie, wypisuje Toast
	 */
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(getActivity(), "GoogleApiClient connection has failed", Toast.LENGTH_SHORT).show();		
	}

	/**
	 * Gdy sie polaczy, ustawia wysoki priorytet, interwa³ aktualizacji lokalizacji oraz klienta Google API.
	 */
	@Override
	public void onConnected(Bundle connectionHint) {
		locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        LocationServices.FusedLocationApi.requestLocationUpdates(
        		googleApiClient, locationRequest, this);
		Log.i("MAP", "Connected");
	}

	@Override
	public void onConnectionSuspended(int cause) {
	}
	
	/**
	 * Pobiera lokalizacjê gracza.
	 * @param player
	 * @return Gdy gracz posiada lokalizacje w bazie, zwraca LatLng gotowy do wstawienia na mape. W p.p. null.
	 */
	private LatLng getPlayerLocation(Player player) {
		String location = player.getLocalization();
		if (location.contains("#")) {
			String lat = location.split("#")[0];
			String lng = location.split("#")[1];
			return new LatLng(Double.valueOf(lat), Double.valueOf(lng));
		}
		return null;
	}
	
	/**
	 * Zadanie aktualizujace pozycjê gracza na serwerze.
	 * @author Mateusz Wrzos
	 *
	 */
	private class UpdatePlayerLocationTask extends AsyncTask<Void, Void, Void> {

		private LatLng newLocation;
		
		public UpdatePlayerLocationTask(LatLng newLocation) {
			super();
			this.newLocation = newLocation;
		}

		/**
		 * W tle aktualizuje pozycje gracza.
		 */
		@Override
		protected Void doInBackground(Void... params) {
			String location = newLocation.latitude + "#" + newLocation.longitude; 
			DataManager.player.setLocalization(location);
			try {
				DataManager.game.updatePlayer(DataManager.player);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
