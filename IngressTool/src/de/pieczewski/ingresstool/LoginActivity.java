package de.pieczewski.ingresstool;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;

import de.pieczewski.ingresstool.intelmap.LoginManager;
import de.pieczewski.ingresstool.intelmap.login.LoginCodes;
import de.pieczewski.ingresstool.intelmap.login.LoginStatus;
import de.pieczewski.ingresstool.settings.SettingsManager;
import de.pieczewski.ingresstool.util.IngressAuthStore;

public class LoginActivity extends Activity implements IngressToolCallbackI {

	private static final String TAG = LoginActivity.class.getName();

	// MANAGER
	private LoginManager loginManager;
	private SettingsManager settingsManager;

	// VIEWS
	private Button loginButton;
	private TextView loginStatusText;
	private ProgressBar loginProgress;
	private ImageView loginLogoImage;
	private WebView loginWebView;

	// OTHER
	private String account_name;

	private boolean autoLogin;

	private boolean userAcctionDone = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	private void loadViews() {
		loginButton = (Button) findViewById(R.id.login_button);
		loginStatusText = (TextView) findViewById(R.id.login_status_text);
		loginProgress = (ProgressBar) findViewById(R.id.login_progress);
		loginLogoImage = (ImageView) findViewById(R.id.login_ingress_tool_logo);
		loginWebView = (WebView) findViewById(R.id.login_webview);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(TAG, "start login activity");
		preInitalization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.v(TAG, "resume login activity");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "stop login activity");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "pause login activity");
	}

	// Load Setting Views etc.
	private void preInitalization() {
		loginManager = LoginManager.getInstance();
		settingsManager = SettingsManager.getInstance();
		loadViews();
		loginButton.setVisibility(View.INVISIBLE);
		setupButtons();

		autoLogin = getIntent().getBooleanExtra(LoginCodes.LOGIN_ACTIVITY_AUTOLOGIN, false);
		account_name = getIntent().getStringExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_ACCOUNT);

		postInitalization();
	}

	private void setupButtons() {
		loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				account_name = null;
				pickAccount();
			}
		});
	}

	// Start work here
	private void postInitalization() {
		Log.v(TAG, "autologin=" + autoLogin + " account=" + account_name);
		if (account_name == null && autoLogin) {
			pickAccount();
		} else if (account_name != null && autoLogin) {
			executeIntelAuthService();
		} else {
			loginButton.setVisibility(View.INVISIBLE);
			loginStatusText.setVisibility(View.INVISIBLE);
			loginProgress.setVisibility(View.INVISIBLE);
			loginLogoImage.setVisibility(View.VISIBLE);
			loginButton.setVisibility(View.VISIBLE);
		}
	}

	private void pickAccount() {
		loginButton.setVisibility(View.INVISIBLE);
		loginStatusText.setVisibility(View.INVISIBLE);
		loginProgress.setVisibility(View.INVISIBLE);
		loginLogoImage.setVisibility(View.VISIBLE);
		Intent intent = AccountPicker.newChooseAccountIntent(null, null,
				new String[] { "com.google" }, false, null, null, null, null);
		startActivityForResult(intent, LoginCodes.REQUEST_CODE_PICK_ACCOUNT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.v(TAG, "handle activity result " + requestCode);
		switch (requestCode) {
		case LoginCodes.REQUEST_CODE_PICK_ACCOUNT:
			handlePickAccountResult(resultCode, data);
			break;

		case LoginCodes.REQUEST_GOOGLE_AUTH_TOKEN:
			if (resultCode == RESULT_OK) {
				pickAccount();
			} else {
				autoLogin = false;
				postInitalization();
			}
			break;

		default:
			Log.w(TAG, String.format("requestCode %d has no handler", requestCode));
			break;
		}
	}

	private void handlePickAccountResult(int resultCode, Intent data) {
		Log.v(TAG, "handle PickAccount result " + resultCode);
		switch (resultCode) {
		case Activity.RESULT_OK:
			account_name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
			Log.v(TAG, "no account picked or user interaction needed: result = " + account_name);
			settingsManager.setAccountName(account_name);
			executeIntelAuthService();
			break;
		case Activity.RESULT_CANCELED:
		default:
			// sendResult(LoginCodes.LOGIN_ACT_CANCELED);
			loginReset();
			break;
		}
	}

	private void executeIntelAuthService() {
		loginButton.setVisibility(View.INVISIBLE);
		loginStatusText.setVisibility(View.VISIBLE);
		loginProgress.setVisibility(View.VISIBLE);
		loginLogoImage.setVisibility(View.INVISIBLE);

		loginStatusText.setText(account_name);
		loginManager.executeIntelAuthService(this, this, account_name);
	}

	private void loginReset() {
		account_name = null;
		autoLogin = false;
		postInitalization();
	}

	private void sendResult(int resultCode) {
		Intent resultIntent = getIntent();
		switch (resultCode) {
		case LoginCodes.LOGIN_ACT_SUCCESS:
			Log.v(TAG,
					"set intent result to RESULT_OK|LOGIN_ACTIVITY_AUTH_STATUS=true|LOGIN_ACTIVITY_AUTH_CANCELLED=false");
			resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_STATUS, true);
			resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_CANCELLED, false);
			setResult(RESULT_OK, resultIntent);
			finish();
			break;

		case LoginCodes.LOGIN_ACT_FAILED:
			Log.v(TAG,
					"set intent result to RESULT_OK|LOGIN_ACTIVITY_AUTH_STATUS=false|LOGIN_ACTIVITY_AUTH_CANCELLED=false");
			// resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_STATUS,
			// false);
			// resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_CANCELLED,
			// false);
			// setResult(RESULT_OK, resultIntent);
			loginReset();
			break;

		case LoginCodes.LOGIN_ACT_CANCELED:
		default:
			Log.v(TAG,
					"set intent result to RESULT_OK|LOGIN_ACTIVITY_AUTH_STATUS=false|LOGIN_ACTIVITY_AUTH_CANCELLED=true");
			resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_STATUS, false);
			resultIntent.putExtra(LoginCodes.LOGIN_ACTIVITY_AUTH_CANCELLED, true);
			setResult(RESULT_CANCELED, resultIntent);
			finish();
			break;
		}
		Log.v(TAG, "send login activity result");
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void handleUserAction() {
		WebSettings webSettings = loginWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAppCacheEnabled(false);
		webSettings.setSaveFormData(false);
		loginWebView.setVisibility(View.VISIBLE);
		;
		loginWebView.loadUrl(IngressAuthStore.getUserActionURL());

		loginWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				Log.v(TAG, "URL: " + url);
				if (url.endsWith("www.ingress.com/intel")) {
					Log.v(TAG, "retry with " + account_name);
					view.setVisibility(View.GONE);
					userAcctionDone = true;
					executeIntelAuthService();
					return true;
				}
				if (url.endsWith("www.google.com/") || url.endsWith("www.google.de/")) {
					view.setVisibility(View.GONE);
					loginReset();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onBackPressed() {
		sendResult(LoginCodes.LOGIN_ACT_CANCELED);
	}

	@Override
	public void onCallback(int caller, Object msg) {
		if (caller == LoginCodes.REQUEST_AUTH_SERVICE) {
			LoginStatus loginStatus = (LoginStatus) msg;
			Log.v(TAG, "loginstatus authenticated=" + loginStatus.isAuthenticated());
			if (loginStatus.isAuthenticated()) {
				sendResult(LoginCodes.LOGIN_ACT_SUCCESS);
			} else if (loginStatus.isCanceled()) {
				sendResult(LoginCodes.LOGIN_ACT_CANCELED);
			} else if (loginStatus.isUserActionNeeded() && !userAcctionDone) {
				handleUserAction();
			} else {
				if (userAcctionDone) {
					Toast.makeText(this, getString(R.string.login_failed_after_user_action_msg), Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(this, getString(R.string.login_failed_msg), Toast.LENGTH_SHORT)
							.show();
				}
				userAcctionDone = false;
				loginReset();
			}
		}
	}
}
