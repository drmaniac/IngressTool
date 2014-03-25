package de.pieczewski.ingresstool;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;

import de.pieczewski.ingresstool.cache.BitmapCacheManager;
import de.pieczewski.ingresstool.cache.internal.ImageCache;
import de.pieczewski.ingresstool.intelmap.LoginManager;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.backgroundTasks.UpdateStatusService;
import de.pieczewski.ingresstool.intelmap.login.LoginCodes;
import de.pieczewski.ingresstool.intelmap.login.LoginStatus;
import de.pieczewski.ingresstool.settings.SettingsManager;
import de.pieczewski.ingresstool.util.IngressToolInformations;

public class MapActivity extends SlidingActivity implements MapActivityI, IngressToolCallbackI {
	private static final String TAG = MapActivity.class.getName();

	// Manager
	private SettingsManager settingsManager;
	private LoginManager loginManager;
	private MapManager mapManager;
	private BitmapCacheManager bitmapCacheManager;

	// Views
	private MapActivitySlidingMenu slidingMenu;
	private MapFragment mapFragment;
	private GoogleMap googleMap;
	private TextView playerStats;
	private TextView gameScore;
	private TextView initializeText;
	private TextView showPortals;
	private View mRootView;

	// Other
	private LoginStatus loginStatus;

	private boolean firstRun = true;

	private boolean isLandscape = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(TAG, "create Ingress Tool MapActivity");
		super.onCreate(savedInstanceState);

		try {
			IngressToolInformations.currentVersion = Integer.toString(getPackageManager()
					.getPackageInfo(this.getPackageName(), 0).versionCode);
			IngressToolInformations.currentVersionInt = Integer.parseInt(IngressToolInformations.currentVersion);
		} catch (NameNotFoundException e) {
			Log.e(TAG, "package not found");
		}

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(true);

		IngressToolManager ingressToolManager = IngressToolManager.getInstance();
		ingressToolManager.setMapActivity(this);

		setContentView(R.layout.activity_map);
		loadViews();

		loadManager();

		// This code fragment must be in this function before we could setUp the
		// Sliding Menu
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getFragmentManager().beginTransaction();
		slidingMenu = new MapActivitySlidingMenu();
		t.replace(R.id.menu_frame, slidingMenu);
		t.commit();
		setUpSlidingMenu();

		// Services
		if (settingsManager.isCoolDownTimerEnabled()) {
			startService(new Intent(CoolDownTimerService.class.getName()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_map, menu);

		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		// searchView.setIconifiedByDefault(false); // Do not iconify the
		// widget; expand it by default

		return true;
	}

	private void loadViews() {
		mRootView = getWindow().getDecorView();
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		googleMap = mapFragment.getMap();

		playerStats = (TextView) findViewById(R.id.player_stats);
		gameScore = (TextView) findViewById(R.id.gameScore);
		initializeText = (TextView) findViewById(R.id.initalize_map);
		showPortals = (TextView) findViewById(R.id.show_portals);

		mRootView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {

			@Override
			public void onSystemUiVisibilityChange(int visibility) {
				if (isLandscape) {
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							MapActivity.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									setLandscapeMode();
								}
							});
						}
					}, 5000);
				}
			}
		});
	}

	private void preInitalization() {
		if (firstRun) {
			UpdateStatusService updateStatusService = new UpdateStatusService(this);
			updateStatusService.execute();
			Log.v(TAG, "intent is not running restart all");
			mapFragment.getView().setVisibility(View.INVISIBLE);
			playerStats.setVisibility(View.INVISIBLE);
			gameScore.setVisibility(View.INVISIBLE);
			showPortals.setVisibility(View.INVISIBLE);
			initializeText.setVisibility(View.VISIBLE);
			loginManager.requestLoginStatus(this, true);
			ImageCache.factory(this.getCacheDir());
			firstRun = false;
		} else {
			Log.v(TAG, "intent is running!");
			loginManager.requestLoginStatus(this, false);
		}
	}

	private void postInitialization() {
		Log.v(TAG, "post initialization loginStatus=" + loginStatus);
		mapManager.setupMapManager(googleMap, this);

		if (!checkForURIIntent()) {
			// TODO: check if last location from mapmanafer is available
			// if last location is not available then goto gps last known.
		}

		// At least make everything visible
		mapFragment.getView().setVisibility(View.VISIBLE);
		playerStats.setVisibility(View.VISIBLE);
		gameScore.setVisibility(View.VISIBLE);
		showPortals.setVisibility(View.VISIBLE);
		initializeText.setVisibility(View.INVISIBLE);
		mapManager.startLocationUpdate();
		mapManager.startBackgroundService();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			isLandscape = true;
			setLandscapeMode();
		} else {
			isLandscape = false;
			setPortraitMode();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// getIntent() should always return the most recent
		setIntent(intent);
	}

	private boolean checkForURIIntent() {
		Log.v(TAG, "Check for URI intent ");
		Uri data = getIntent().getData();
		if (data != null) {
			Log.v(TAG, "Split URI :"+data);
			String scheme = data.getScheme();
			String host = data.getHost();
			String path = data.getPath();
			Log.v(TAG, "Start from URI intent :" + scheme + " " + host + " " + path);

			try {
				String latlng = data.getQueryParameter("ll");
				String[] latlngarray = latlng.split(",");
				NumberFormat format = NumberFormat.getInstance(Locale.US);
				Float latE6 = format.parse(latlngarray[0]).floatValue();
				Float lngE6 = format.parse(latlngarray[1]).floatValue();
				float zoom = Float.parseFloat(data.getQueryParameter("z"));

				if (latE6 != null && lngE6 != null) {
					LatLng moveTo = new LatLng(latE6, lngE6);
					mapManager.moveTo(moveTo, 0f, zoom);
					getIntent().setData(null); // This marks the data as
												// consumed!
					return true;
				}
			} catch (NullPointerException e) {
				Log.e(TAG, "map handler is not available", e);
			} catch (NumberFormatException e) {
				Log.e(TAG, "lat or lng was not provided for the intent",e);
			} catch (ParseException e) {
				Log.e(TAG, "lat or lng was not provided for the intent",e);
			}
		} else {
			Log.v(TAG, "No data found!");
		}
		return false;
	}

	private void setUpSlidingMenu() {
		Point displaySize = new Point();
		getWindowManager().getDefaultDisplay().getSize(displaySize);

		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		// sm.setMode(SlidingMenu.LEFT_RIGHT);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setShadowWidth(15);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffset(displaySize.x * 30 / 100);
		sm.setFadeDegree(0.35f);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		setSlidingActionBarEnabled(true);
	}

	private void loadManager() {
		SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		settingsManager = SettingsManager.factory(sharedPref);
		bitmapCacheManager = BitmapCacheManager.factory(this);
		loginManager = LoginManager.factory(settingsManager);
		mapManager = MapManager.getInstance();
	}

	private void setLandscapeMode() {
		if (isLandscape) {
			mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
			mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
			getActionBar().hide();
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			googleMap.setMyLocationEnabled(false);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			playerStats.setVisibility(View.INVISIBLE);
			gameScore.setVisibility(View.INVISIBLE);
			showPortals.setVisibility(View.INVISIBLE);
		}
	}

	private void setPortraitMode() {
		getActionBar().show();
		mRootView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		playerStats.setVisibility(View.VISIBLE);
		gameScore.setVisibility(View.VISIBLE);
		showPortals.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "handle activity result " + requestCode);
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case LoginCodes.REQUEST_LOGIN_ACTIVITY:
			loginManager.handleLoginRequestResult(this, resultCode, data);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onStart() {
		Log.v(TAG, "start IngressTool");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.v(TAG, "resume IngressTool");
		super.onResume();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			isLandscape = true;
			setLandscapeMode();
		}
		preInitalization();

		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		Log.d(TAG, "Intent Action is :"+intent.getAction());
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			String query = intent.getStringExtra(SearchManager.QUERY);
			Log.d(TAG, "got query intent '" + query + "'");
			Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
			List<Address> addresses;
			try {
				addresses = geocoder.getFromLocationName(query, 1);
				if(addresses.size() > 0) {
					LatLng moveTo = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
					mapManager.moveTo(moveTo, 0f, 15f);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
		    // Handle a suggestions click (because the suggestions all use ACTION_VIEW)
		    Uri data = intent.getData();
		    Log.d(TAG, "suggestion: "+data);
		    checkForURIIntent();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.v(TAG, "pause IngressTool");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.v(TAG, "stop IngressTool");
		settingsManager.saveCurrentSettings();
		mapManager.stopLocationUpdate();
		mapManager.stopBackgroundService();
	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean result = false;
		switch (item.getItemId()) {
		case android.R.id.home:
			getSlidingMenu().toggle();
			break;

		case R.id.menu_settings:
			Intent intent = new Intent(getActivity(), SettingsActivity.class);
			startActivityForResult(intent, 0);
			break;

		default:
			result = super.onOptionsItemSelected(item);
			break;
		}
		return result;
	}

	@Override
	public void onCallback(int caller, Object msg) {
		switch (caller) {
		case LoginCodes.REQUEST_LOGIN_STATUS:
			LoginStatus loginStatus = (LoginStatus) msg;
			Log.v(TAG, "current loginstatus is " + loginStatus);
			if (loginStatus.isAuthenticated() && !loginStatus.isCanceled()) {
				this.loginStatus = loginStatus;
				postInitialization();
			} else if (!loginStatus.isCanceled()) {
				Log.v(TAG, "is not authenticated try to login ");
				loginManager.startLoginIntent(this, LoginCodes.REQUEST_LOGIN_ACTIVITY);
			} else {
				Log.i(TAG, "Login canceld");
				finish();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	@Override
	public void logout() {
		loginManager.logout();
		firstRun = true;
		mapManager.setSetupDone(false);
		preInitalization();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	public void updateGameScore(Spannable spannable) {
		gameScore.setText(spannable);
	}

	public void updatePlayerStat(Spannable spannable) {
		playerStats.setText(spannable);
	}
	
	public void setShowPortals(String text) {
		Animation slideUpIn = AnimationUtils.loadAnimation(this, R.anim.slide_in);
		showPortals.setVisibility(View.INVISIBLE);
		showPortals.setText(text);
		showPortals.setVisibility(View.VISIBLE);
		showPortals.startAnimation(slideUpIn);
	}
	
	@Override
	public void hideShowPortals() {
		Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out);
		showPortals.startAnimation(slideOut);
		showPortals.setVisibility(View.INVISIBLE);
	}

}
