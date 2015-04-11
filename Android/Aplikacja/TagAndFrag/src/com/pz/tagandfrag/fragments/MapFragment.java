package com.pz.tagandfrag.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONException;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
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
	private CheckBox showLinesCheckBox;
	
	/* Mock's */
	private Collection<Player> mockPlayers;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.map_fragment, container, false);
		initalizeMockPlayers();
		initializeShowPlayersCheckBox();
		initializeShowLinesCheckBox();
		buildGoogleApiClient();
		mapView = (MapView) view.findViewById(R.id.mapView);
		mapView.onCreate(savedInstanceState);
		mapView.onResume();
		setUpMap();
		return view;
	}
	
	private void buildGoogleApiClient() {
		googleApiClient = new GoogleApiClient.Builder(getActivity())
		.addApi(LocationServices.API)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this)
		.build();
	}
	
	private void initalizeMockPlayers() {
		mockPlayers = new ArrayList<Player>();
		mockPlayers.add(new Player("Mock1", 100, 100, "53.010918#18.590522", -1));
		mockPlayers.add(new Player("Mock2", 100, 100, "53.010802#18.590371", -1));
		mockPlayers.add(new Player("Mock3", 100, 100, "53.011007#18.590663", -1));
		mockPlayers.add(new Player("Mock4", 100, 100, "53.009799#18.594387", -1));
		mockPlayers.add(new Player("Mock5", 100, 100, "53.010264#18.593690", -1));
		mockPlayers.add(new Player("Mock6", 100, 100, "53.010528#18.595149", -1));
	}
	
	private void initializeShowPlayersCheckBox() {
		showPlayersCheckBox = (CheckBox) view.findViewById(R.id.showPlayersCheckBox);
		showPlayersCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("Click", "Click");
				if (showPlayersCheckBox.isChecked()) {
					addAllMarkersToMap();
					showLinesCheckBox.setEnabled(true);
				} else {
					removeAllMarkersFromMap();
					removeAllLinesFromMap();
					showLinesCheckBox.setChecked(false);
					showLinesCheckBox.setEnabled(false);
				}
			}
			
		});
	}
	
	private void initializeShowLinesCheckBox() {
		showLinesCheckBox = (CheckBox) view.findViewById(R.id.showLinesCheckBox);
		showLinesCheckBox.setEnabled(false);
		showLinesCheckBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("CLICK", "Checkbox");
				if (showLinesCheckBox.isChecked()) {
					addAllLinesToMap();
				} else {
					removeAllLinesFromMap();
				}

			}
		});
	}

	private void addAllMarkersToMap() {
		for (Player player : DataManager.players) {
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
	
	private void removeAllMarkersFromMap() {
		for (Marker marker : markers) {
			marker.remove();
		}
	}
	
	private void addAllLinesToMap() {
		LatLng myLocation = new LatLng(	Double.valueOf(map.getMyLocation().getLatitude()), 
										Double.valueOf(map.getMyLocation().getLongitude())
										);
		for (Player player : DataManager.players) {
			LatLng mockLocation = getPlayerLocation(player);
			if (mockLocation != null) {
				PolylineOptions lineOptions = new PolylineOptions().add(myLocation).add(mockLocation).width(5);
				Polyline line = map.addPolyline(lineOptions);
				lines.add(line);					
			}
		}
	}
	
	private void removeAllLinesFromMap() {
		for (Polyline line : lines) { 
			line.remove();
		}
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

		if (showLinesCheckBox.isChecked()) {
			removeAllLinesFromMap();
			addAllLinesToMap();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Toast.makeText(getActivity(), "GoogleApiClient connection has failed", Toast.LENGTH_SHORT).show();		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        LocationServices.FusedLocationApi.requestLocationUpdates(
        		googleApiClient, locationRequest, this);
		Log.i("MAP", "Connected");
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
	}
	
	private LatLng getPlayerLocation(Player player) {
		String location = player.getLocalization();
		if (location.contains("#")) {
			String lat = location.split("#")[0];
			String lng = location.split("#")[1];
			return new LatLng(Double.valueOf(lat), Double.valueOf(lng));
		}
		return null;
	}
	
	private class UpdatePlayerLocationTask extends AsyncTask<Void, Void, Void> {

		private LatLng newLocation;
		
		public UpdatePlayerLocationTask(LatLng newLocation) {
			super();
			this.newLocation = newLocation;
		}

		@Override
		protected Void doInBackground(Void... params) {
			String location = newLocation.latitude + "#" + newLocation.longitude; 
			DataManager.player.setLocalization(location);
			try {
				// Aktualizacja (przeniesc do Handlera)
				DataManager.players = DataManager.game.getByTeam(DataManager.player.getTeam());
				DataManager.game.updatePlayer(DataManager.player);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
}
