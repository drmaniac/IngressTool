package de.pieczewski.ingresstool;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import de.pieczewski.ingresstool.intelmap.backgroundTasks.IngressToolRunningTask;

public class CoolDownTimerService extends Service {

	private static final String TAG = CoolDownTimerService.class.getName();
	
	private Timer ingressToolRunningTimer;
	private TimerTask ingressToolRunningTimerTask;

	public static int NOTIFICATION_ID = 01;
	private NotificationManager mNM;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.v(TAG, "create CoolDownTimerService");

		mNM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		if(ingressToolRunningTimerTask == null && ingressToolRunningTimer == null) {
			ingressToolRunningTimer = new Timer();
			ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
			ingressToolRunningTimerTask = new IngressToolRunningTask(this, activityManager, mNM, NOTIFICATION_ID);
			
			ingressToolRunningTimer.schedule(ingressToolRunningTimerTask, 1000, 5000);
		}
		
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Cool Down Timer").setContentText(getString(R.string.cool_down)).setOngoing(true).setAutoCancel(false);
		Notification notification = mBuilder.build();
		
		
		Intent notificationIntent = new Intent(this, HandleCoolDownClickService.class);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);
		
		notification.contentIntent = pendingIntent;
		
		// mId allows you to update the notification later on.
		mNM.notify(NOTIFICATION_ID, notification);
	}

	public void startTimer() {
		Log.v(TAG, "start Portal Cool Timer");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "stop cooldown timer Service");
		ingressToolRunningTimer.cancel();
		ingressToolRunningTimer = null;
		mNM.cancel(NOTIFICATION_ID);
	}
}
