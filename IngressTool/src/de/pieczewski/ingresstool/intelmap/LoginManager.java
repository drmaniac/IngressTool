package de.pieczewski.ingresstool.intelmap;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import de.pieczewski.ingresstool.IngressToolCallbackI;
import de.pieczewski.ingresstool.LoginActivity;
import de.pieczewski.ingresstool.intelmap.login.IntelAuthService;
import de.pieczewski.ingresstool.intelmap.login.LoginCodes;
import de.pieczewski.ingresstool.intelmap.login.LoginStatus;
import de.pieczewski.ingresstool.intelmap.login.LoginStatusService;
import de.pieczewski.ingresstool.settings.SettingsManager;
import de.pieczewski.ingresstool.util.IngressAuthStore;

public class LoginManager {

	private static final String TAG = LoginManager.class.getName();

	private static LoginManager instance;

	private SettingsManager settingsManager;

	private LoginStatus loginStatus;

	private boolean firstTry = true;

	private LoginManager() {
		instance = this;
	}

	public static LoginManager factory(SettingsManager settingsManager) {
		if (instance == null) {
			instance = new LoginManager();
			instance.setSettingsManager(settingsManager);
		} else if (instance.settingsManager == null) {
			instance.setSettingsManager(settingsManager);
		}
		return instance;
	}

	public static LoginManager getInstance() {
		return instance;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public void requestLoginStatus(IngressToolCallbackI callback, boolean forceLoad) {
		if ( (loginStatus == null || !loginStatus.isAuthenticated()) || forceLoad) {
			Log.v(TAG, "request loginstatus get new status "+loginStatus);
			LoginStatusService loginStatusService = new LoginStatusService(callback, loginStatus);
			loginStatusService.execute();
		} else {
			Log.v(TAG, "request loginstatus take old status");
			callback.onCallback(LoginCodes.REQUEST_LOGIN_STATUS, loginStatus);
		}
	}
	
	public void logout() {
		loginStatus = new LoginStatus();
		settingsManager.setAccountName(null);
		IngressAuthStore.setAuthStatusMsg(null);
		IngressAuthStore.setCookieStore(null);
		IngressAuthStore.setCSFRCookie(null);
		IngressAuthStore.setStatus(null);
		IngressAuthStore.setUserActionURL(null);
		IngressAuthStore.setXCSRFToken(null);
	}

	public void startLoginIntent(Activity activity, int requestCode) {
		Intent loginIntent = new Intent(activity, LoginActivity.class);
		if (firstTry) {
			Log.v(TAG, "first try with autologin");
			firstTry = false;
			loginIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_ACCOUNT, settingsManager.getAccountName());
			loginIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTOLOGIN, true);
		} else {
			Log.v(TAG, "first try failed or logout even try without autologin");
			loginIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_ACCOUNT, settingsManager.getAccountName());
			loginIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTOLOGIN, false);
		}
		Log.v(TAG, "start activity for result");
		activity.startActivityForResult(loginIntent, requestCode);
	}

	public void handleLoginRequestResult(IngressToolCallbackI callback, int resultCode, Intent data) {
		loginStatus = new LoginStatus();
		Log.v(TAG, "handle login request result :" + resultCode);
		switch (resultCode) {

		case Activity.RESULT_OK:
			loginStatus.setAuthenticated(data.getBooleanExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_STATUS, false));
			loginStatus.setCanceled(data.getBooleanExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_CANCELLED, false));
			Log.v(TAG, "authenticated = " + loginStatus.isAuthenticated() + " cancelled = " + loginStatus.isCanceled());
			break;

		case Activity.RESULT_CANCELED:
			Log.i(TAG,"Login canceled");
		default:
			loginStatus.setAuthenticated(false);
			loginStatus.setCanceled(true);
			break;
		}
		callback.onCallback(LoginCodes.REQUEST_LOGIN_STATUS, loginStatus);
	}

	public void executeIntelAuthService(IngressToolCallbackI callback, Activity activity, String account_name) {
		IntelAuthService intelAuthService = new IntelAuthService(callback, activity);
		intelAuthService.execute(account_name);
	}
}
