package de.pieczewski.ingresstool;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

@ReportsCrashes(
formKey = "", 
//mailTo = "ingressTool@pieczewski.de",
mailTo = "somevalidemail@test.com",
additionalSharedPreferences = { "de.pieczewski.IngressTool.ACRA" }, mode = ReportingInteractionMode.DIALOG, resToastText = R.string.crash_toast_text, resDialogText = R.string.crash_dialog_text,
resDialogTitle = R.string.crash_dialog_title, resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, resDialogOkToast = R.string.crash_dialog_ok_toast, excludeMatchingSharedPreferencesKeys = {
		"^user.private", "password" }, customReportContent = { ReportField.USER_COMMENT, ReportField.ANDROID_VERSION,
		ReportField.APP_VERSION_NAME, ReportField.BRAND, ReportField.PHONE_MODEL, ReportField.STACK_TRACE, ReportField.LOGCAT }, logcatArguments = {
		"-t", "1000", "-v", "time" })
public class IngressTool extends Application {

	private final static String TAG = IngressTool.class.getName();

	@Override
	public void onCreate() {
		Log.v(TAG, "Ingress Tool");
		ACRA.init(this);
		
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
			
			@Override
			public void onActivityStopped(Activity activity) {
				Log.v(TAG, "onActivityStopped");
			}
			
			@Override
			public void onActivityStarted(Activity activity) {
				Log.v(TAG, "onActivityStarted");
			}
			
			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				Log.v(TAG, "onActivitySaveInstanceState "+outState);
			}
			
			@Override
			public void onActivityResumed(Activity activity) {
				Log.v(TAG, "onActivityResumed");
			}
			
			@Override
			public void onActivityPaused(Activity activity) {
				Log.v(TAG, "onActivityPaused");
			}
			
			@Override
			public void onActivityDestroyed(Activity activity) {
				Log.v(TAG, "onActivityDestroyed");
			}
			
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				Log.v(TAG, "onActivityCreated");
			}
		});
		
		super.onCreate();
	}
}
