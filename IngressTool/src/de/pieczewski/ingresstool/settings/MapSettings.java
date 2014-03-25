package de.pieczewski.ingresstool.settings;

import de.pieczewski.ingresstool.intelmap.MapOverlayEnum;

public class MapSettings extends AbstractSetting{
	
	public final static String MAP_OVERLAY = "map_overlay";
	public final static String SYNC_ENABLED = "syncEnabled";

	private MapOverlayEnum mapOverlay;
	
	private boolean syncEnabled = true;
	
	public MapOverlayEnum getMapOverlay() {
		return mapOverlay;
	}
	public void setMapOverlay(MapOverlayEnum mapOverlay) {
		setMapOverlay(mapOverlay, true);
	}
	
	public void setMapOverlay(MapOverlayEnum mapOverlay, boolean setChanged) {
		if(this.mapOverlay == null || !this.mapOverlay.equals(mapOverlay)) {
			this.mapOverlay = mapOverlay;
			isChanged = setChanged;
		}
	}
	
	public boolean isSyncEnabled() {
		return syncEnabled;
	}
	
	public void setSyncEnabled(boolean syncEnabled, boolean setChanged) {
		if(this.syncEnabled != syncEnabled) {
			this.syncEnabled = syncEnabled;
			isChanged = setChanged;
		}
	}
	
	public void setSyncEnabled(boolean syncEnabled) {
		setSyncEnabled(syncEnabled, true);
	}
}
