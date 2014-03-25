package de.pieczewski.ingresstool;

public class IngressToolManager {

	
	private static IngressToolManager instance;
	
	private MapActivity mapActivity;
	
	private IngressToolManager() {
		instance = this;
	}
	
	public static IngressToolManager getInstance() {
		return instance == null ? new IngressToolManager() : instance;
	}
	
	public MapActivity getMapActivity() {
		return mapActivity;
	}
	public void setMapActivity(MapActivity mapActivity) {
		this.mapActivity = mapActivity;
	}
}
