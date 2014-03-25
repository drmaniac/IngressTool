package de.pieczewski.ingresstool;

import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.settings.InventorySetting;
import de.pieczewski.ingresstool.settings.SettingsManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PortalTakeDown extends Fragment{
	
	private static final String TAG = PortalTakeDown.class.getName();

	TextView bursterL1;
	TextView bursterL2;
	TextView bursterL3;
	TextView bursterL4;
	TextView bursterL5;
	TextView bursterL6;
	TextView bursterL7;
	TextView bursterL8;
	
	int totalEnergy;
	int mitigation;
	
	public PortalTakeDown() {
		Log.v(TAG, "reintent PortalTakeDown");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.portal_take_down, null);
		
		Bundle bundle = getArguments();
		String turretKey = bundle.getString("turret_key");
		
		IngressEntityCache entityCache = IngressEntityCache.getInstance();
		Turret turret = entityCache.getTurret(turretKey);
		this.totalEnergy = turret.getTurretTotalEnergy();
		this.mitigation = turret.getMitigation();
		
		bursterL1 = (TextView) view.findViewById(R.id.portal_take_down_l1);
		bursterL2 = (TextView) view.findViewById(R.id.portal_take_down_l2);
		bursterL3 = (TextView) view.findViewById(R.id.portal_take_down_l3);
		bursterL4 = (TextView) view.findViewById(R.id.portal_take_down_l4);
		bursterL5 = (TextView) view.findViewById(R.id.portal_take_down_l5);
		bursterL6 = (TextView) view.findViewById(R.id.portal_take_down_l6);
		bursterL7 = (TextView) view.findViewById(R.id.portal_take_down_l7);
		bursterL8 = (TextView) view.findViewById(R.id.portal_take_down_l8);
		
		String dummy = getResources().getString(R.string.portal_take_down_dummy);
		InventorySetting inventorySetting = SettingsManager.getInstance().getInventorySetting();
		bursterL1.setText(String.format(dummy, 1, calcTakedown(totalEnergy, 150, this.mitigation), inventorySetting.getBursterL1()));
		bursterL2.setText(String.format(dummy, 2, calcTakedown(totalEnergy, 300, this.mitigation), inventorySetting.getBursterL2()));
		bursterL3.setText(String.format(dummy, 3, calcTakedown(totalEnergy, 500, this.mitigation), inventorySetting.getBursterL3()));
		bursterL4.setText(String.format(dummy, 4, calcTakedown(totalEnergy, 900, this.mitigation), inventorySetting.getBursterL4()));
		bursterL5.setText(String.format(dummy, 5, calcTakedown(totalEnergy, 1200, this.mitigation), inventorySetting.getBursterL5()));
		bursterL6.setText(String.format(dummy, 6, calcTakedown(totalEnergy, 1500, this.mitigation), inventorySetting.getBursterL6()));
		bursterL7.setText(String.format(dummy, 7, calcTakedown(totalEnergy, 1800, this.mitigation), inventorySetting.getBursterL7()));
		bursterL8.setText(String.format(dummy, 8, calcTakedown(totalEnergy, 2700, this.mitigation), inventorySetting.getBursterL8()));
		
		return view;
	}
	
	private int calcTakedown(int totalEnergy, int bursterEnergy, int mitigation) {
		return totalEnergy/ (bursterEnergy - (bursterEnergy/100*mitigation));
	}

}
