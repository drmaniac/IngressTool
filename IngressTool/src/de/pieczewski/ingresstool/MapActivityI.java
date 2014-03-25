package de.pieczewski.ingresstool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.Spannable;

import com.slidingmenu.lib.SlidingMenu;

import de.pieczewski.ingresstool.settings.SettingsManager;

public interface MapActivityI {

	PackageManager getPackageManager();

	Context getContext();

	String getPackageName();

	SlidingMenu getSlidingMenu();

	SettingsManager getSettingsManager();
	
	Activity getActivity();

	void logout();
	
	public void updateGameScore(Spannable spannable);
	public void updatePlayerStat(Spannable spannable);
	public void setShowPortals(String text);

	void hideShowPortals();
}
