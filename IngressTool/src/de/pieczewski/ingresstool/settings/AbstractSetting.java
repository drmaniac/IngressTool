package de.pieczewski.ingresstool.settings;

public abstract class AbstractSetting {
	protected boolean isChanged = false;
	
	public boolean isChanged() {
		return isChanged;
	}
}
