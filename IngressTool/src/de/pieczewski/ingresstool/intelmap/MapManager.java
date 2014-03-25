package de.pieczewski.ingresstool.intelmap;

import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;

import de.pieczewski.ingresstool.MapActivityI;
import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.AbstractGameEntity;
import de.pieczewski.ingresstool.entities.ControlField;
import de.pieczewski.ingresstool.entities.Edge;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.settings.SettingsManager;

public class MapManager {

	private static final String TAG = MapManager.class.getName();

	private static final int THIRTY_SECONDS = 1000 * 30;

	// SELF INSTANCE
	private static MapManager instance;

	// MANAGER
	private SettingsManager settingsManager;

	// MANAGED COMPONENTS
	private GoogleMap googleMap;
	private LocationManager locationManager;
	private PortalFilterManager filterManager;
	
	private MapActivityI mapActivity;

	private TileOverlay currentOverlay;
	private TileOverlay gameEntityOverlay;

	private CameraPosition currentCameraPosition;

	private MapManagerEventHandler eventHandler;

	private Location lastLocation;

	private IntelMapLocationListener intelMapGPSLocationListener;
	private IntelMapLocationListener intelMapNetworkLocationListener;

	private Timer backgroudServiceTimer;
	private TimerTask backgroudServiceTask;

	// OTHER
	private boolean setupDone = false;

	private boolean gpsLocationEnabled;
	private boolean networkLocationEnabled;

	private MapManager() {
		instance = this;
	}

	public static MapManager getInstance() {
		return instance == null ? new MapManager() : instance;
	}

	public void setupMapManager(GoogleMap googleMap, MapActivityI mapActivity) {
		if (setupDone) {
			return;
		}

		settingsManager = SettingsManager.getInstance();

		filterManager = PortalFilterManager.getInstance();
		eventHandler = new MapManagerEventHandler();

		if (locationManager == null) {

			locationManager = (LocationManager) mapActivity.getActivity().getSystemService(Context.LOCATION_SERVICE);

			gpsLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			networkLocationEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		}

		this.mapActivity = mapActivity;
		this.googleMap = googleMap;

		setupMap();
		setupDone = true;
	}

	public void setSetupDone(boolean setupDone) {
		this.setupDone = setupDone;
	}

	private void setupMap() {
		switchOverlay(settingsManager.getMapOverlay());

		moveToLastKnownPosition();
		googleMap.setMyLocationEnabled(true);

		TurretMarkerClickListener turretMarkerClickListener = new TurretMarkerClickListener();
		googleMap.setOnMarkerClickListener(turretMarkerClickListener);

		// Limiting the zoom level and update the current camera position
		googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition position) {
				currentCameraPosition = position;
				if (position.zoom <= 11) {
					CameraPosition newPosition = new CameraPosition.Builder(position).zoom(11f).build();
					googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition));
				}
		
//				boolean changed = false;
//				if (position.zoom >= 17) {
//					// ALL
//					mapActivity.setShowPortals(mapActivity.getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_all_portals));
//					
//					if(!filterManager.isLevelEnabled(0)) {
//						filterManager.setLevelEnabled(0, true);
//						changed = true;
//					}
//				} else if (position.zoom <= 16 && position.zoom >= 15) {
//					// L1 - L8
//					mapActivity.setShowPortals(mapActivity.getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L1_L8_portals));
//					if(filterManager.isLevelEnabled(0)) {
//						filterManager.setLevelEnabled(0, false);
//						changed = true;
//					}
//					if(!filterManager.isLevelEnabled(1)) {
//						filterManager.setLevelEnabled(1, true);
//						changed = true;
//					}
//				} else if (position.zoom <= 14 && position.zoom >= 12) {
//					// L2 - L8
//					mapActivity.setShowPortals(mapActivity.getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L2_L8_portals));
//					if(filterManager.isLevelEnabled(1)) {
//						filterManager.setLevelEnabled(1, false);
//						changed = true;
//					}
//					if(!filterManager.isLevelEnabled(2)) {
//						filterManager.setLevelEnabled(2, true);
//						changed = true;
//					}
//				} else if (position.zoom <= 12 && position.zoom >= 10) {
//					// L3 - L8
//					mapActivity.setShowPortals(mapActivity.getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L3_L8_portals));
//					if(filterManager.isLevelEnabled(2)) {
//						filterManager.setLevelEnabled(2, false);
//						changed = true;
//					}
//				}
//				
//				if(changed)
//					MapManager.getInstance().applyPortalFilter();
			}
		});
	}

	public void switchOverlay(MapOverlayEnum selectedOverlay) {
		Log.d(TAG, "change overlay to " + selectedOverlay.name());
		if (currentOverlay != null) {
			currentOverlay.remove();
		}
		if (gameEntityOverlay != null) {
			gameEntityOverlay.remove();
		}

		googleMap.clear();

		settingsManager.setMapOverlay(selectedOverlay);
		switch (selectedOverlay) {

		case MAPS:
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;

		case SATALITE:
			googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;

		case INGRESS:
			googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
			// TileOverlayOptions tileOptions = new
			// TileOverlayOptions().tileProvider(new
			// IntelOverlayTileProvider(256, 256));
			TileOverlayOptions tileOptions = new TileOverlayOptions().tileProvider(new IntelOverlayCachedTileProvider());
			tileOptions.zIndex(0);
			googleMap.addTileOverlay(tileOptions);

			break;
		default:
			break;
		}

		// // Add this to load ingress data tile based
		if (settingsManager.isSynEnabled()) {
			if (gameEntityOverlay != null) {
				gameEntityOverlay.remove();
			}
			TileOverlayOptions gameEntityOverlayOptions = new TileOverlayOptions()
					.tileProvider(new GameEntityOverlayTileProvider());
			gameEntityOverlayOptions.zIndex(10);
			gameEntityOverlay = googleMap.addTileOverlay(gameEntityOverlayOptions);
		}

		forceRedraw();
	}

	public Activity getManagedActivity() {
		return mapActivity.getActivity();
	}

	public MapActivityI getManagedMapActivity() {
		return mapActivity;
	}

	public void moveTo(LatLng latLng, float bearing) {
		CameraPosition position = new CameraPosition.Builder().target(latLng).bearing(bearing).zoom(currentCameraPosition.zoom)
				.build();
		moveTo(position);
	}

	public void moveTo(LatLng latLng, float bearing, Float zoom) {
		CameraPosition position = new CameraPosition.Builder().target(latLng).bearing(bearing).zoom(zoom).build();
		moveTo(position);
	}

	public void moveTo(CameraPosition position) {
		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
	}

	public void sendAsynEventMsg(int what, Object obj) {
		Message msg = eventHandler.obtainMessage(what, obj);
		eventHandler.sendMessage(msg);
	}

	public void moveToLastKnownPosition() {
		if (currentCameraPosition != null) {
			moveTo(currentCameraPosition);
		}

		LatLng latLngLocation = null;

		Log.d(TAG, "Try to get an location from GPS");
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		lastLocation = location;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setCostAllowed(false);
		// String provider = locationManager.getBestProvider(criteria, true);

		// If we don't get an location then we try it with the network provider
		if (location == null) {
			Log.d(TAG, "Try to get an location from NETWORK");
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		// Setup the initila location
		// TODO: ask to start the gps service
		// first we will start with an faked location if no location is
		// available
		// makes it also easy to debug it in an virtual device.
		if (location != null) {
			Log.d(TAG, "Got location fix");
			latLngLocation = new LatLng(location.getLatitude(), location.getLongitude());
		} else {
			Log.d(TAG, "Don't get an location... we will fake!");
			latLngLocation = new LatLng(51.90839, 8.38550);
		}

		moveTo(latLngLocation, 0f, 15f);
	}

	public void handleDrawTile(GameEntityTile gameEntityTile) {
		Log.v(TAG, "draw tile: " + gameEntityTile.getKey());
		IngressEntityCache ingressEntityCache = IngressEntityCache.getInstance();
		long start = System.currentTimeMillis();
		int count = 0;
		for (String key : gameEntityTile.getGameEntitiesKeys()) {
			AbstractGameEntity gameEntity = ingressEntityCache.findGameEntity(key);
			if (gameEntity instanceof Turret) {
				Turret turret = (Turret) gameEntity;
				count += drawTurret(turret);
			} else if (gameEntity instanceof Edge) {
				Edge edge = (Edge) gameEntity;
				count += drawEdge(edge);
			} else if (gameEntity instanceof ControlField) {
				ControlField controlField = (ControlField) gameEntity;
				count += drawControllField(controlField);
			}
		}
		long stop = System.currentTimeMillis();
		Log.v(TAG, String.format(" %d entities drawn in %dms ", count, stop - start));
	}

	private int drawTurret(Turret turret) {
		PortalFilterManager portalFilterManager = PortalFilterManager.getInstance();
		int count = 0;
		if (portalFilterManager.isLevelEnabled(turret.getLevel())) {
			if (!turret.isMarkerAvailabel()) {
				Log.v(TAG, "draw turret: " + turret.getKey());
				turret.setMarker(googleMap.addMarker(turret.getMarkerOptions()));
				count++;
			}
		}
		return count;
	}

	private int drawEdge(Edge edge) {
		PortalFilterManager portalFilterManager = PortalFilterManager.getInstance();
		int count = 0;
		if (portalFilterManager.isLinks()) {
			if (!edge.isPolylineAvailable()) {
				Log.v(TAG, "draw edge: " + edge.getKey());
				edge.setPolyline(googleMap.addPolyline(edge.getPolylineOptions()));
				count++;
			}
		}
		return count;
	}

	private int drawControllField(ControlField controlField) {
		PortalFilterManager portalFilterManager = PortalFilterManager.getInstance();
		int count = 0;
		if (portalFilterManager.isFields()) {
			if (!controlField.isPolygoneAvailabel()) {
				Log.v(TAG, "draw controll field: " + controlField.getKey());
				controlField.setPolygon(googleMap.addPolygon(controlField.getPolygonOptions()));
				count++;
			}
		}
		return count;
	}

	public void applyPortalFilter() {
		forceRedraw();
	}

	private void forceRedraw() {
		IngressEntityCache ingressEntityCache = IngressEntityCache.getInstance();
		Map<String, Turret> turrets = ingressEntityCache.getAllTurrets();
		Set<String> keys = turrets.keySet();
		for (String key : keys) {
			Turret turret = turrets.get(key);
			turret.removeMarker();
			drawTurret(turret);
		}

		Map<String, Edge> edges = ingressEntityCache.getAllEdges();
		keys = edges.keySet();
		for (String key : keys) {
			Edge edge = edges.get(key);
			edge.removePolyline();
			drawEdge(edge);
		}

		Map<String, ControlField> fields = ingressEntityCache.getAllControlFields();
		keys = fields.keySet();
		for (String key : keys) {
			ControlField controlField = fields.get(key);
			controlField.removePolygone();
			drawControllField(controlField);
		}
	}

	public void forceReload() {
		IngressEntityCache.getInstance().clear();
		gameEntityOverlay.clearTileCache();
		gameEntityOverlay.remove();
		switchOverlay(settingsManager.getMapOverlay());
	}

	public Location getLastLocation() {
		return lastLocation;
	}

	public void setLastLocation(Location lastLocation) {
		this.lastLocation = lastLocation;
	}

	public float getDistanceTo(Turret turret) {
		if (lastLocation != null) {
			Location turretLocation = new Location(lastLocation);
			turretLocation.setLatitude(turret.getLatLng().latitude);
			turretLocation.setLongitude(turret.getLatLng().longitude);
			return lastLocation.distanceTo(turretLocation) / 1000;
		}
		return 0;
	}

	public void startLocationUpdate() {
		if (gpsLocationEnabled && intelMapGPSLocationListener == null) {
			Log.v(TAG, "start gps location update");
			intelMapGPSLocationListener = new IntelMapLocationListener();
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, intelMapGPSLocationListener);
		}
		if (networkLocationEnabled && intelMapGPSLocationListener == null) {
			Log.v(TAG, "start network location update");
			intelMapNetworkLocationListener = new IntelMapLocationListener();
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, intelMapNetworkLocationListener);
		}
	}

	public void stopLocationUpdate() {
		if (locationManager != null) {
			if (intelMapGPSLocationListener != null) {
				Log.v(TAG, "stop gps location update");
				locationManager.removeUpdates(intelMapGPSLocationListener);
				intelMapGPSLocationListener = null;

			}
			if (intelMapNetworkLocationListener != null) {
				Log.v(TAG, "stop network location update");
				locationManager.removeUpdates(intelMapNetworkLocationListener);
				intelMapNetworkLocationListener = null;
			}
		}

	};

	public void startBackgroundService() {
		if (backgroudServiceTask == null && backgroudServiceTimer == null) {
			backgroudServiceTimer = new Timer();
			backgroudServiceTask = new BackgroundUpdateService();

			backgroudServiceTimer.schedule(backgroudServiceTask, 1000, THIRTY_SECONDS);
		}
	}

	public void stopBackgroundService() {
		if (backgroudServiceTimer != null) {
			backgroudServiceTimer.cancel();
			backgroudServiceTimer = null;
		}
		if (backgroudServiceTask != null) {
			backgroudServiceTask.cancel();
			backgroudServiceTask = null;
		}
	}
}
