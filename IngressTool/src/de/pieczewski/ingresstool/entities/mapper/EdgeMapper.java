package de.pieczewski.ingresstool.entities.mapper;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.maps.model.LatLng;

import de.pieczewski.ingresstool.entities.Edge;

public class EdgeMapper {
	
	private static final String TAG="de.pieczewski.ingresslib.entities.mapper.EdgeMapper";
	
	public Edge mapEdge(Long id, String key, JsonNode edgeNode) {
		Edge result = null;
		try{
			JsonNode controllingTeam = edgeNode.findValue("controllingTeam");
			String team = controllingTeam.get("team").textValue();
	
			JsonNode originPortalLocation = edgeNode.findValue("originPortalLocation");
			JsonNode destinationPortalLocation = edgeNode.findValue("destinationPortalLocation");
	
			int origin_lat = originPortalLocation.get("latE6").intValue();
			int origin_lng = originPortalLocation.get("lngE6").intValue();
	
			int destination_lat = destinationPortalLocation.get("latE6").intValue();
			int destination_lng = destinationPortalLocation.get("lngE6").intValue();
	
			result = new Edge(id, key, team, new LatLng(origin_lat / 1E6, origin_lng / 1E6),
					new LatLng(destination_lat / 1E6, destination_lng / 1E6));
			Log.v(TAG,"Edge: "+key+" parsed");
		} catch (Exception e) {
			Log.e(TAG,"Failed to parse edge node: "+edgeNode.toString(),e);
		}
		return result;
	}
}
