package de.pieczewski.ingresstool;

import java.util.Timer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationCoolDownCanceledBroadcastReceiver extends BroadcastReceiver {
	
	private static final String TAG = NotificationCoolDownCanceledBroadcastReceiver.class.getName();

	@Override
	public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("cooldown_notification_cancelled"))
        {
        	int notificationId = intent.getIntExtra("notificationID", 0);
        	Timer timer = HandleCoolDownClickService.getNotifications().get(notificationId);
        	if(timer != null) {
	        	timer.cancel();
	        	HandleCoolDownClickService.getNotifications().remove(notificationId);
        	}
        	Log.v(TAG,"notification canceled: "+notificationId);
        }
	}
}
