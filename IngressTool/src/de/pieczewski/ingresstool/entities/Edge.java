package de.pieczewski.ingresstool.entities;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

@SuppressLint("DefaultLocale")
public class Edge extends AbstractGameEntity{

	private LatLng originLatLng;
	private LatLng destinationLatLng;
	private Polyline polyline;

	public Edge(Long id, String key, String team, LatLng origin, LatLng destination) {
		super(id, key, Team.valueOf(team.toUpperCase()));
		this.originLatLng = origin;
		this.destinationLatLng = destination;
	}
	public LatLng getDestinationLatLng() {
		return destinationLatLng;
	}

	public void setDestinationLatLng(LatLng destinationLatLng) {
		this.destinationLatLng = destinationLatLng;
	}

	public LatLng getOriginLatLng() {
		return originLatLng;
	}

	public void setOriginLatLng(LatLng originLatLng) {
		this.originLatLng = originLatLng;
	}
	
	public Polyline getPolyline() {
		return polyline;
	}
	
	public void setPolyline(Polyline polyline) {
		this.polyline = polyline;
	}
	
	public boolean isPolylineAvailable() {
		return polyline == null ? false : true;
	}
	
	public PolylineOptions getPolylineOptions() {
		PolylineOptions polylineOptions = new PolylineOptions();
		polylineOptions.add(getOriginLatLng());
		polylineOptions.add(getDestinationLatLng());
		polylineOptions.width(2);
		polylineOptions.zIndex(10);
		switch (getTeam()) {
		case ALIENS:
			polylineOptions.color(0xff02be02);
			break;
		case RESISTANCE:
			polylineOptions.color(0xff0692d0);
			break;

		default:
			break;
		}
		
		return polylineOptions;
	}
	public void removePolyline() {
		if(polyline != null) {
			polyline.remove();
			polyline = null;
		}
	}
}
