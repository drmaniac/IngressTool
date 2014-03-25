package de.pieczewski.ingresstool.intelmap.backgroundTasks;

import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import de.pieczewski.ingresstool.HandleCoolDownClickService;
import de.pieczewski.ingresstool.R;

public class CoolDownTimerTask extends TimerTask {

	private static final String TAG = CoolDownTimerTask.class.getName();

	private NotificationManager mNM;
	private Context context;
	private Notification notification;
	private NotificationCompat.Builder mBuilder;
	private Long start;
	private Long end;
	private Long five_min = (long) (1000 * 60 * 5);

	private int id;

	public CoolDownTimerTask(NotificationManager mNM, NotificationCompat.Builder mBuilder, Context context, Long start, int id) {
		this.mNM = mNM;
		this.context = context;
		this.start = start;
		this.end = (long) this.start + five_min; // 5min
		
		this.mBuilder = mBuilder;
		this.id = id;
	}

	@Override
	public void run() {
		Log.v(TAG, "isRunning "+id);
		Long current = System.currentTimeMillis();
		if (current < end) {
			Long time = end - current;
			mBuilder.setContentTitle(context.getString(R.string.cool_down_time_left, Long.toString(time / 1000)));
			mBuilder.setContentText(context.getString(R.string.cool_down_in_progress));
			mBuilder.setProgress(five_min.intValue(), time.intValue(), false);
			notification = mBuilder.build();
			mNM.notify(id, notification);

		} else {
			mNM.cancel(id);
			mBuilder.setDefaults(Notification.DEFAULT_ALL);
			mBuilder.setProgress(0, 0, false);
			mBuilder.setContentTitle(context.getString(R.string.cool_down_time_left, 0));
			mBuilder.setContentText(context.getString(R.string.cool_down_complete));
			mBuilder.setContentIntent(getContentIntent());
			notification = mBuilder.build();
			mNM.notify(id, notification);
			HandleCoolDownClickService.getNotifications().remove(id);
			this.cancel();
		}
	}
	
	
	protected PendingIntent getContentIntent() {
		Intent ingressIntent = new Intent(Intent.ACTION_MAIN);
		ingressIntent.setComponent(ComponentName.unflattenFromString("com.nianticproject.ingress/.NemesisActivity"));
		ingressIntent.addCategory("android.intent.category.LAUNCHER");
		ingressIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, ingressIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
	    return pendingIntent;
	}
}
