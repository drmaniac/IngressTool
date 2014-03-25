package de.pieczewski.ingresstool.entities.mapper;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.android.gms.maps.model.LatLng;

import de.pieczewski.ingresstool.entities.Team;
import de.pieczewski.ingresstool.entities.Turret;

public class TurretMapper {
	
	private static final String TAG = TurretMapper.class.getName();
	
	public Turret mapTurret(Long id, String key, JsonNode turretNode) throws NullPointerException {
		// Load turret description

		JsonNode description = turretNode.findValue("descriptiveText");
		
		String address = ""; 
		try {
			address = description.get("ADDRESS").textValue();
		} catch ( NullPointerException e) {
			Log.e(TAG, "address not available");
		}
		String title = description.get("TITLE").textValue();

		JsonNode imageNode = turretNode.findValue("imageByUrl");
		String imageURL = imageNode.get("imageUrl").textValue();

		// Load turret location
		JsonNode location = turretNode.findValue("locationE6");
		int lat = location.get("latE6").intValue();
		int lng = location.get("lngE6").intValue();

		// Convert to LatLng
		LatLng latLng = new LatLng(lat / 1E6, lng / 1E6);

		// Load turret controlling team
		JsonNode controllingTeam = turretNode.findValue("controllingTeam");
		String team = controllingTeam.get("team").textValue();

		String capturingPlayerId = "";
		Long capturedTime = null;
		if(!team.equals(Team.NEUTRAL.toString())) { 
			JsonNode capturedNode = turretNode.findValue("captured");
			capturingPlayerId = capturedNode.get("capturingPlayerId").asText();
			capturedTime = capturedNode.get("capturedTime").asLong();
		}

		// Create turret
		Turret turret = new Turret(id, key, title, address, latLng, team, imageURL, capturedTime,
				capturingPlayerId);

		// Load resonators
		JsonNode resonatorsNodes = turretNode.findValue("resonatorArray");
		turret.setResonators(new ResonatorMapper().mapResonators(resonatorsNodes));

		JsonNode modNodes = turretNode.findValue("portalV2");
		turret.setMods(new ModMapper().mapMods(modNodes));

		return turret;
	}
}
