package de.pieczewski.ingresstool.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import de.pieczewski.ingresstool.R;
import de.pieczewski.ingresstool.settings.InventorySetting;
import de.pieczewski.ingresstool.settings.SettingsManager;

public class BursterTextWatcher implements TextWatcher {
	private View view;

	public BursterTextWatcher(View view) {
		this.view = view;
	}

	public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
	}

	public void afterTextChanged(Editable editable) {
		String text = editable.toString();
		InventorySetting inventorySetting = SettingsManager.getInstance().getInventorySetting();
		int count = 0;
		try {
			count = Integer.parseInt(text);
		} catch (NumberFormatException e) {

		}
		switch (view.getId()) {
		case R.id.inv_burster_level_01:
			inventorySetting.setBurster(1, count);
			break;
		case R.id.inv_burster_level_02:
			inventorySetting.setBurster(2, count);
			break;
		case R.id.inv_burster_level_03:
			inventorySetting.setBurster(3, count);
			break;
		case R.id.inv_burster_level_04:
			inventorySetting.setBurster(4, count);
			break;
		case R.id.inv_burster_level_05:
			inventorySetting.setBurster(5, count);
			break;
		case R.id.inv_burster_level_06:
			inventorySetting.setBurster(6, count);
			break;
		case R.id.inv_burster_level_07:
			inventorySetting.setBurster(7, count);
			break;
		case R.id.inv_burster_level_08:
			inventorySetting.setBurster(8, count);
			break;
		}
	}
}
