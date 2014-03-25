package de.pieczewski.ingresstool.util;

import de.pieczewski.ingresstool.R;
import de.pieczewski.ingresstool.intelmap.MapManager;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ErrorToastHandler extends Handler{
	
	private Activity activity;
	
	public static final int NETWORK_FAILED = 9901;
	public static final int NO_ACCOUNT_NAME_SET = 9902;
	public static final int LOGIN_FAILED = 9903;

	public ErrorToastHandler(Activity activity) {
		this.activity = activity;
	}
	
	public void handleMessage(Message msg) {
		switch (msg.what) {
			case NETWORK_FAILED:
				Toast.makeText(activity, R.string.network_failed, Toast.LENGTH_LONG).show();
				break;
			case NO_ACCOUNT_NAME_SET:
				Toast.makeText(activity, R.string.no_account_name_set, Toast.LENGTH_LONG).show();
				break;
			case LOGIN_FAILED:
				Toast.makeText(activity, R.string.ingress_login_failed, Toast.LENGTH_LONG).show();
				break;
		}
	}
}
