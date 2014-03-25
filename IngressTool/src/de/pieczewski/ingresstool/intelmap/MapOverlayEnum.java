package de.pieczewski.ingresstool.intelmap;

public enum MapOverlayEnum {

	INGRESS("INGRESS"),
	MAPS("INGRESS"),
	SATALITE("INGRESS");

	private final String selected;
	
	private MapOverlayEnum(String selection) {
		this.selected = selection;
	}
	
	@Override
	public String toString() {
		return this.selected;
	}
}
