package de.pieczewski.ingresstool;

import java.util.Timer;
import java.util.TimerTask;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.SparseArray;
import de.pieczewski.ingresstool.intelmap.backgroundTasks.CoolDownTimerTask;

public class HandleCoolDownClickService extends IntentService {

	private static final String TAG = HandleCoolDownClickService.class.getName();

	private NotificationManager mNM;

	private static int notification_counter = 1000;
	private static SparseArray<Timer> notifications = new SparseArray<Timer>();

	public HandleCoolDownClickService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.v(TAG, "HandleCoolDownClick!");
		mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Long start = System.currentTimeMillis();
		Log.v(TAG, "Currently running timers:"+notifications.size());
		if(notifications.size() < 4) {
			notification_counter++;
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext()).setSmallIcon(R.drawable.ic_launcher)
					.setContentTitle("Cool Down Timer").setContentText(getApplicationContext().getString(R.string.cool_down)).setAutoCancel(true)
					.setOngoing(false);
			mBuilder.setDeleteIntent(getDeleteIntent(notification_counter));
		    
			Timer cooldownTimer = new Timer();
			TimerTask cooldownTimerTask = new CoolDownTimerTask(mNM, mBuilder, this, start, notification_counter);
			cooldownTimer.schedule(cooldownTimerTask, 0, 1000);
			notifications.put(notification_counter, cooldownTimer);
		}
	}

	public static SparseArray<Timer> getNotifications() {
		return notifications;
	}

	protected PendingIntent getDeleteIntent(int id) {
		Intent intent = new Intent(getApplicationContext(), NotificationCoolDownCanceledBroadcastReceiver.class);
		intent.setAction("cooldown_notification_cancelled");
		intent.putExtra("notificationID", id);
		return PendingIntent.getBroadcast(getApplicationContext(), id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

}
