package de.pieczewski.ingresstool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import de.pieczewski.ingresstool.settings.SettingsManager;

public class SettingsActivity extends Activity {

	private SettingsManager settingsManager;
	
	private CheckBox coolDownTimerEnabled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		loadManager();
		loadViews();
		loadClickEvents();
	}
	
	
	private void loadManager() {
		settingsManager = SettingsManager.getInstance();
	}
	
	private void loadViews() {
		coolDownTimerEnabled = (CheckBox) findViewById(R.id.coolDownTimerEnabled);
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		initialze();
	}
	
	
	private void initialze(){
		coolDownTimerEnabled.setChecked(settingsManager.isCoolDownTimerEnabled());
	}
	
	private void loadClickEvents () {
		coolDownTimerEnabled.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				settingsManager.setCoolDownTimerEnable(isChecked);
				settingsManager.saveCurrentSettings();
				if(!isChecked) {
					stopService(new Intent(CoolDownTimerService.class.getName()));
				} else {
					startService(new Intent(CoolDownTimerService.class.getName()));
				}
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.settings, menu);
//		return true;
//	}

}
