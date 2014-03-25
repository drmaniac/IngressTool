package de.pieczewski.ingresstool.settings;

import android.content.SharedPreferences;
import android.util.Log;
import de.pieczewski.ingresstool.intelmap.MapOverlayEnum;

public class SettingsManager {
	public final static String TAG = "de.pieczewski.ingresslib.settings.SettingsManager";

	private static SettingsManager instance;

	private SharedPreferences sharedPreferences;

	private AccountSetting accountSetting;
	private MapSettings mapSettings;
	private InventorySetting inventorySetting;
	private CoolDownSetting coolDownSetting;

	private SettingsManager(SharedPreferences sharedPreferences) {
		Log.v(TAG,"initialize settings manager");
		this.sharedPreferences = sharedPreferences;
		loadSettings();
	}

	public static SettingsManager factory(SharedPreferences sp) {
		if (instance == null) {
			instance = new SettingsManager(sp);
		}
		return instance;
	}

	public static boolean isAvailable() {
		return instance == null ? false : true;
	}

	public static SettingsManager getInstance() {
		return instance;
	}

	private void loadSettings() {
		Log.v(TAG, "load settings");
		accountSetting = new AccountSetting();
		accountSetting.setAccountName(
				sharedPreferences.getString(AccountSetting.ACCOUNT_NAME, null), false);
		accountSetting.setIntelAuthToken(sharedPreferences.getString(AccountSetting.INTEL_TOKEN, null), false);
		loadSettingDebug(AccountSetting.ACCOUNT_NAME, accountSetting.getAccountName());

		mapSettings = new MapSettings();
		mapSettings.setMapOverlay(MapOverlayEnum.valueOf(sharedPreferences.getString(
				MapSettings.MAP_OVERLAY, MapOverlayEnum.INGRESS.name())), false);
		mapSettings.setSyncEnabled(sharedPreferences.getBoolean(MapSettings.SYNC_ENABLED, true), false);
		loadSettingDebug(MapSettings.MAP_OVERLAY, mapSettings.getMapOverlay().name());
		
		inventorySetting = new InventorySetting();
		inventorySetting.setBurster(1, sharedPreferences.getInt(InventorySetting.BURSTER_L1, 0));
		inventorySetting.setBurster(2, sharedPreferences.getInt(InventorySetting.BURSTER_L2, 0));
		inventorySetting.setBurster(3, sharedPreferences.getInt(InventorySetting.BURSTER_L3, 0));
		inventorySetting.setBurster(4, sharedPreferences.getInt(InventorySetting.BURSTER_L4, 0));
		inventorySetting.setBurster(5, sharedPreferences.getInt(InventorySetting.BURSTER_L5, 0));
		inventorySetting.setBurster(6, sharedPreferences.getInt(InventorySetting.BURSTER_L6, 0));
		inventorySetting.setBurster(7, sharedPreferences.getInt(InventorySetting.BURSTER_L7, 0));
		inventorySetting.setBurster(8, sharedPreferences.getInt(InventorySetting.BURSTER_L8, 0));
		
		coolDownSetting = new CoolDownSetting();
		coolDownSetting.setCooldownEnabled(sharedPreferences.getBoolean(CoolDownSetting.COOL_DOWN_ENABLED, true));
	}

	public void saveCurrentSettings() {
		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (accountSetting.isChanged()) {
			editor.putString(AccountSetting.ACCOUNT_NAME, accountSetting.getAccountName());
			saveSettingDebug(AccountSetting.ACCOUNT_NAME, accountSetting.getAccountName());
			
			editor.putString(AccountSetting.INTEL_TOKEN, accountSetting.getIntelAuthToken());
		}

		if (mapSettings.isChanged()) {
			editor.putString(MapSettings.MAP_OVERLAY, mapSettings.getMapOverlay().name());
			editor.putBoolean(MapSettings.SYNC_ENABLED, mapSettings.isSyncEnabled());
			saveSettingDebug(MapSettings.MAP_OVERLAY, mapSettings.getMapOverlay().name());
		}
		
		if(inventorySetting.isChanged()) {
			editor.putInt(InventorySetting.BURSTER_L1, inventorySetting.getBursterL1());
			editor.putInt(InventorySetting.BURSTER_L2, inventorySetting.getBursterL2());
			editor.putInt(InventorySetting.BURSTER_L3, inventorySetting.getBursterL3());
			editor.putInt(InventorySetting.BURSTER_L4, inventorySetting.getBursterL4());
			editor.putInt(InventorySetting.BURSTER_L5, inventorySetting.getBursterL5());
			editor.putInt(InventorySetting.BURSTER_L6, inventorySetting.getBursterL6());
			editor.putInt(InventorySetting.BURSTER_L7, inventorySetting.getBursterL7());
			editor.putInt(InventorySetting.BURSTER_L8, inventorySetting.getBursterL8());
		}
		
		if(coolDownSetting.isChanged) {
			editor.putBoolean(CoolDownSetting.COOL_DOWN_ENABLED, coolDownSetting.isCooldownEnabled());
		}
		
		editor.commit();
	}

	private void loadSettingDebug(String key, String value) {
		Log.d(TAG, String.format("Loaded %s setting %s", key, value));
	}

	private void saveSettingDebug(String key, String value) {
		Log.d(TAG, String.format("Save %s setting %s", key, value));
	}

	public String getAccountName() {
		return accountSetting.getAccountName();
	}

	public void setAccountName(String accountName) {
		accountSetting.setAccountName(accountName);
	}
	
	public String getIntelAuthToken() {
		return accountSetting.getIntelAuthToken();
	}
	
	public void setIntelAuthToken(String intelAuthToken) {
		accountSetting.setIntelAuthToken(intelAuthToken);
	}

	public MapOverlayEnum getMapOverlay() {
		return mapSettings.getMapOverlay();
	}

	public void setMapOverlay(MapOverlayEnum mapOverlay) {
		mapSettings.setMapOverlay(mapOverlay);
	}
	
	public InventorySetting getInventorySetting() {
		return inventorySetting;
	}
	
	public boolean isSynEnabled() {
		return mapSettings.isSyncEnabled();
	}
	
	public void setSyncEnabled(boolean syncEnabled) {
		mapSettings.setSyncEnabled(syncEnabled);
	}

	public boolean isCoolDownTimerEnabled() {
		return coolDownSetting.isCooldownEnabled();
	}
	
	public void setCoolDownTimerEnable(boolean cooldownEnabled) {
		coolDownSetting.setCooldownEnabled(cooldownEnabled);
	}
}
