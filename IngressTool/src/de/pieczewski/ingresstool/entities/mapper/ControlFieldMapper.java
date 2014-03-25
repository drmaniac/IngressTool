package de.pieczewski.ingresstool.entities.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.maps.model.LatLng;

import de.pieczewski.ingresstool.entities.ControlField;

public class ControlFieldMapper {
	public ControlField mapControllField(Long id, String key, JsonNode fieldNode) {

		JsonNode controllingTeam = fieldNode.findValue("controllingTeam");
		String team = controllingTeam.get("team").textValue();

		JsonNode vertexA = fieldNode.findValue("vertexA").findValue("location");
		JsonNode vertexB = fieldNode.findValue("vertexB").findValue("location");
		JsonNode vertexC = fieldNode.findValue("vertexC").findValue("location");

		int lat = vertexA.get("latE6").intValue();
		int lng = vertexA.get("lngE6").intValue();
		LatLng vA = new LatLng(lat / 1E6, lng / 1E6);

		lat = vertexB.get("latE6").intValue();
		lng = vertexB.get("lngE6").intValue();
		LatLng vB = new LatLng(lat / 1E6, lng / 1E6);

		lat = vertexC.get("latE6").intValue();
		lng = vertexC.get("lngE6").intValue();
		LatLng vC = new LatLng(lat / 1E6, lng / 1E6);

		ControlField field = new ControlField(id, key, team, vA, vB, vC);
		return field;
	}
}
