package de.pieczewski.ingresstool.intelmap;

public class PortalFilterManager {

	private static PortalFilterManager instance;

	private boolean[] levelEnabled = new boolean[9];
	private boolean fields;
	private boolean links;

	private PortalFilterManager() {
		enableAll();
		instance = this;
	}

	public static PortalFilterManager getInstance() {
		return instance == null ? new PortalFilterManager() : instance;
	}
	
	public boolean isLevelEnabled(int level) {
		if(level >= 0) {
			return levelEnabled[level];
		} else {
			return true;
		}
	}
	
	public void setLevelEnabled(int level, boolean value) {
		levelEnabled[level] = value;
	}
	
	public void enableAll() {
		for (int i = 0; i < levelEnabled.length; i++) {
			levelEnabled[i] = true;
		}
		fields = true;
		links = true;
	}
	
	public void setFields(boolean fields) {
		this.fields = fields;
	}
	
	public void setLinks(boolean links) {
		this.links = links;
	}
	
	public boolean isFields() {
		return fields;
	}
	
	public boolean isLinks() {
		return links;
	}
}
