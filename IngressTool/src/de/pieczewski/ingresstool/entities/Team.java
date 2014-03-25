package de.pieczewski.ingresstool.entities;

public enum Team {

	ALIENS("ALIENS"),
	RESISTANCE("RESISTANCE"),
	NEUTRAL("NEUTRAL");

	private final String selected;
	
	private Team(String selection) {
		this.selected = selection;
	}
	
	@Override
	public String toString() {
		return this.selected;
	}
}
