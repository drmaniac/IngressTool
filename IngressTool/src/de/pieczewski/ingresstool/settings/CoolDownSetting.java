package de.pieczewski.ingresstool.settings;

public class CoolDownSetting extends AbstractSetting {
	
	public final static String COOL_DOWN_ENABLED = "coolDownEnabled";
	
	private boolean cooldownEnabled;
	
	
	public boolean isCooldownEnabled() {
		return cooldownEnabled;
	}
	
	public void setCooldownEnabled(boolean cooldownEnabled) {
		setCooldownEnabled(cooldownEnabled, true);
	}
	
	public void setCooldownEnabled(boolean cooldownEnabled, boolean setChanged) {
		if(this.cooldownEnabled != cooldownEnabled) {
			this.cooldownEnabled = cooldownEnabled;
			isChanged = setChanged;
		}
	}
}
