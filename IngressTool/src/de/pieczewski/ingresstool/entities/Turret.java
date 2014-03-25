package de.pieczewski.ingresstool.entities;

import java.util.ArrayList;

import android.annotation.SuppressLint;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.pieczewski.ingresstool.util.PortalImages;

@SuppressLint("DefaultLocale")
public class Turret extends AbstractGameEntity implements PlayerGUIDToNick{

	private String address;
	private String title;
	private LatLng latLng;
	private ArrayList<Resonator> resonators;
	private ArrayList<Mod> mods;
	private String imageURL;
	private Long capturedTime;
	private String capturingPlayerId;
	private String capturingPlayer;

	private Marker marker;

	public Turret(Long id, String key, String title, String address, LatLng latLng, String team, String imageURL,
			Long capturedTime, String capturingPlayerId) {
		super(id, key, Team.valueOf(team.toUpperCase()));
		this.title = title;
		this.address = address;
		this.latLng = latLng;
		this.imageURL = imageURL;
		this.capturedTime = capturedTime;
		this.capturingPlayerId = capturingPlayerId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLatLng(LatLng latLng) {
		this.latLng = latLng;
	}

	public LatLng getLatLng() {
		return latLng;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public Long getCapturedTime() {
		return capturedTime;
	}

	public void setCapturedTime(Long capturedTime) {
		this.capturedTime = capturedTime;
	}

	public String getCapturingPlayerId() {
		return capturingPlayerId;
	}

	public void setCapturingPlayerId(String capturingPlayerId) {
		this.capturingPlayerId = capturingPlayerId;
	}

	public String getCapturingPlayer() {
		return capturingPlayer;
	}

	public void setCapturingPlayer(String capturingPlayer) {
		this.capturingPlayer = capturingPlayer;
	}

	public ArrayList<Resonator> getResonators() {
		if (resonators == null) {
			resonators = new ArrayList<Resonator>();
		}
		return resonators;
	}

	public void setResonators(ArrayList<Resonator> resonators) {
		this.resonators = resonators;
	}

	public void addResonator(Resonator resonator) {
		if (!this.resonators.contains(resonator)) {
			resonators.add(resonator);
		}
	}

	public ArrayList<Mod> getMods() {
		if (mods == null) {
			mods = new ArrayList<Mod>();
		}
		return mods;
	}

	public void setMods(ArrayList<Mod> mods) {
		this.mods = mods;
	}

	public int getLevel() {
		int sum_level = 0;
		for (Resonator r : resonators) {
			sum_level += r.getLevel();
		}
		int level = sum_level / 8;
		if(level == 0 && sum_level > 0) {
			level = 1;
		}
		return level;
	}

	public boolean isMarkerAvailabel() {
		return marker == null ? false : true;
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public MarkerOptions getMarkerOptions() {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(getLatLng());

		markerOptions.title(getKey());
		markerOptions.snippet(String.format("%f %f", getLatLng().latitude, getLatLng().longitude));

		switch (getTeam()) {
		case ALIENS:
			markerOptions.icon(PortalImages.getPortalImage(getLevel(), getTeam())).anchor(0.5f, 0.5f);
			break;
		case RESISTANCE:
			markerOptions.icon(PortalImages.getPortalImage(getLevel(), getTeam())).anchor(0.5f, 0.5f);
			break;

		default:
			markerOptions.icon(PortalImages.getPortalImage(getLevel(), Team.NEUTRAL)).anchor(0.5f, 0.5f);
			break;
		}

		return markerOptions;
	}

	public void removeMarker() {
		if (marker != null) {
			marker.remove();
			marker = null;
		}
	}

	public int getTurretTotalEnergy() {
		int total = 0;
		for (Resonator r : resonators) {
			total += r.getEnergyTotal();
		}
		return total;
	}
	
	@Override
	public void setPlayerNick(String nick) {
		capturingPlayer = nick;
	}
	
	@Override
	public String getGUID() {
		return capturingPlayerId;
	}
	
	@Override
	public String getNick() {
		return capturingPlayer;
	}

	public int getMitigation() {
		int mitigation = 0;
		for(Mod m : mods) {
			mitigation += m.mittigation;
		}
		return mitigation;
	}
}
