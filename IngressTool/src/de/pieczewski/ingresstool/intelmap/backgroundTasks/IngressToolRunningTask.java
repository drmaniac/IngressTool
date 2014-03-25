package de.pieczewski.ingresstool.intelmap.backgroundTasks;

import java.util.List;
import java.util.TimerTask;

import de.pieczewski.ingresstool.CoolDownTimerService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.NotificationManager;
import android.util.Log;

public class IngressToolRunningTask extends TimerTask {
	
	private static final String TAG = IngressToolRunningTask.class.getName();
	
	private ActivityManager activityManager;
	private NotificationManager notificationManager;
	private int mId;
	private CoolDownTimerService coolDownTimerService;

	public IngressToolRunningTask(CoolDownTimerService coolDownTimerService, ActivityManager activityManager, NotificationManager notificationManager, int mId) {
		this.activityManager = activityManager;
		this.notificationManager = notificationManager;
		this.mId = mId;
		this.coolDownTimerService = coolDownTimerService;
	}

	@Override
	public void run() {
		boolean isRunning = false;
	    List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
	    for(RunningAppProcessInfo info : procInfos){
	    	if(info.processName.startsWith("de.pieczewski.ingresstool") && !info.processName.endsWith(":remote")) {
//	    		Log.v(TAG, "IngressTool is running ["+info.processName+"]");
	    		isRunning = true;
	    	}
	    }
	    
	    if(!isRunning){
	    	Log.v(TAG, "IngressTool exit");
	    	notificationManager.cancelAll();
	    	coolDownTimerService.stopSelf();
	    	cancel();
	    }
	}
	
	@Override
	protected void finalize() throws Throwable {
		Log.v(TAG, "IngressTool finalize");
		notificationManager.cancelAll();
		coolDownTimerService.stopSelf();
		super.finalize();
	}
}
