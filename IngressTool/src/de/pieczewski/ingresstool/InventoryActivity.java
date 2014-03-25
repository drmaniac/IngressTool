package de.pieczewski.ingresstool;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import de.pieczewski.ingresstool.settings.InventorySetting;
import de.pieczewski.ingresstool.settings.SettingsManager;
import de.pieczewski.ingresstool.util.BursterTextWatcher;

public class InventoryActivity extends Activity {

	protected static final int INV_REQUEST = 1001;
	private EditText bursterL1;
	private EditText bursterL2;
	private EditText bursterL3;
	private EditText bursterL4;
	private EditText bursterL5;
	private EditText bursterL6;
	private EditText bursterL7;
	private EditText bursterL8;
	
	private SettingsManager settingsManager;
	private InventorySetting inventorySetting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inventory);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		settingsManager = SettingsManager.getInstance();
		inventorySetting = settingsManager.getInventorySetting();
		
		bursterL1 = (EditText) findViewById(R.id.inv_burster_level_01);
		bursterL2 = (EditText) findViewById(R.id.inv_burster_level_02);
		bursterL3 = (EditText) findViewById(R.id.inv_burster_level_03);
		bursterL4 = (EditText) findViewById(R.id.inv_burster_level_04);
		bursterL5 = (EditText) findViewById(R.id.inv_burster_level_05);
		bursterL6 = (EditText) findViewById(R.id.inv_burster_level_06);
		bursterL7 = (EditText) findViewById(R.id.inv_burster_level_07);
		bursterL8 = (EditText) findViewById(R.id.inv_burster_level_08);
		
		bursterL1.setText(""+inventorySetting.getBursterL1());
		bursterL2.setText(""+inventorySetting.getBursterL2());
		bursterL3.setText(""+inventorySetting.getBursterL3());
		bursterL4.setText(""+inventorySetting.getBursterL4());
		bursterL5.setText(""+inventorySetting.getBursterL5());
		bursterL6.setText(""+inventorySetting.getBursterL6());
		bursterL7.setText(""+inventorySetting.getBursterL7());
		bursterL8.setText(""+inventorySetting.getBursterL8());
		
		bursterL1.addTextChangedListener(new BursterTextWatcher(bursterL1));
		bursterL2.addTextChangedListener(new BursterTextWatcher(bursterL2));
		bursterL3.addTextChangedListener(new BursterTextWatcher(bursterL3));
		bursterL4.addTextChangedListener(new BursterTextWatcher(bursterL4));
		bursterL5.addTextChangedListener(new BursterTextWatcher(bursterL5));
		bursterL6.addTextChangedListener(new BursterTextWatcher(bursterL6));
		bursterL7.addTextChangedListener(new BursterTextWatcher(bursterL7));
		bursterL8.addTextChangedListener(new BursterTextWatcher(bursterL8));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_inventory, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
//			NavUtils.navigateUpFromSameTask(this);
			SettingsManager.getInstance().saveCurrentSettings();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
