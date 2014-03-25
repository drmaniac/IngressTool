package de.pieczewski.ingresstool.entities;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

@SuppressLint("DefaultLocale")
public class ControlField extends AbstractGameEntity {
	private LatLng vertexALatLng;
	private LatLng vertexBLatLng;
	private LatLng vertexCLatLng;

	private Polygon polygon;

	public ControlField(Long id, String key, String team, LatLng vA, LatLng vB, LatLng vC) {
		super(id, key, Team.valueOf(team.toUpperCase()));
		this.vertexALatLng = vA;
		this.vertexBLatLng = vB;
		this.vertexCLatLng = vC;
	}

	public LatLng getVertexALatLng() {
		return vertexALatLng;
	}

	public void setVertexALatLng(LatLng vertexALatLng) {
		this.vertexALatLng = vertexALatLng;
	}

	public LatLng getVertexBLatLng() {
		return vertexBLatLng;
	}

	public void setVertexBLatLng(LatLng vertexBLatLng) {
		this.vertexBLatLng = vertexBLatLng;
	}

	public LatLng getVertexCLatLng() {
		return vertexCLatLng;
	}

	public void setVertexCLatLng(LatLng vertexCLatLng) {
		this.vertexCLatLng = vertexCLatLng;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public boolean isPolygoneAvailabel() {
		return polygon == null ? false : true;
	}
	
	public PolygonOptions getPolygonOptions () {
		PolygonOptions polygonOptions = new PolygonOptions();
		List<LatLng> region = new ArrayList<LatLng>();
		region.add(getVertexALatLng());
		region.add(getVertexBLatLng());
		region.add(getVertexCLatLng());
		polygonOptions.addAll(region);
		
		switch (getTeam()) {
		case ALIENS:
			polygonOptions.strokeColor(0xff02be02);
			polygonOptions.fillColor(0x6402be02);
			break;
		case RESISTANCE:
			polygonOptions.strokeColor(0xff0692d0);
			polygonOptions.fillColor(0x640692d0);
			break;

		default:
			break;
		}

		polygonOptions.strokeWidth(2);
		polygonOptions.zIndex(20);
		
		return polygonOptions;
	}

	public void removePolygone() {
		if(polygon != null) {
			polygon.remove();
			polygon = null;
		}
	}

}
