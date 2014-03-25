package de.pieczewski.ingresstool;

import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConfigurationException;
import org.acra.ReportingInteractionMode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Switch;
import android.widget.TextView;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.MapOverlayEnum;
import de.pieczewski.ingresstool.intelmap.PortalFilterManager;
import de.pieczewski.ingresstool.settings.SettingsManager;

@SuppressLint("ValidFragment") public class MapActivitySlidingMenu extends Fragment {

	private static final String TAG = MapActivitySlidingMenu.class.getName();

//	private static final int REQUEST_CODE_RESOLVE_GOOGLE_PLUS_ERROR = 1;
//	private static final String TAG_ERROR_DIALOG_FRAGMENT = "errorDialog";

	MapActivityI mapActivity;

	SettingsManager settingsManager;

	Button logoutButton;
	Button overlayButton;
	Button shareButton;
	Button settingButton;
	Button forceReloadButton;
	Switch tileloaderSwitch;
	Button inventoryButton;
	Button ingressButton;
	Button infoButton;
	Button portalFilterButton;
	Button commButton;

	public MapActivitySlidingMenu() {
		this.mapActivity = IngressToolManager.getInstance().getMapActivity();
		settingsManager = SettingsManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_map_sliding_menu, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		settingsManager = SettingsManager.getInstance();
		super.onActivityCreated(savedInstanceState);
		overlayButton = (Button) getActivity().findViewById(R.id.smenu_map_overlay);
		shareButton = (Button) getActivity().findViewById(R.id.smenu_share);
		forceReloadButton = (Button) getActivity().findViewById(R.id.smenu_reload);
		settingButton = (Button) getActivity().findViewById(R.id.smenu_manage);
		logoutButton = (Button) getActivity().findViewById(R.id.smenu_logout);
		tileloaderSwitch = (Switch) getActivity().findViewById(R.id.smenu_tileloader);
		inventoryButton = (Button) getActivity().findViewById(R.id.smenu_inventory);
		ingressButton = (Button) getActivity().findViewById(R.id.smenu_launch_ingress);
		infoButton = (Button) getActivity().findViewById(R.id.smenu_about);
		portalFilterButton = (Button)getActivity().findViewById(R.id.smenu_portal_filter);
		commButton = (Button)getActivity().findViewById(R.id.smenu_intel_comm);

		tileloaderSwitch.setChecked(settingsManager.isSynEnabled());

		ingressButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.setComponent(ComponentName.unflattenFromString("com.nianticproject.ingress/.NemesisActivity"));
				intent.addCategory("android.intent.category.LAUNCHER");
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(getString(R.string.ingress_not_installed)).setTitle(
							getString(R.string.ingress_not_installed_title));
					builder.create();
				}
			}
		});
		
		commButton.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				Intent commIntent = new Intent(getActivity(), CommActivity.class);
				startActivity(commIntent);
				
			}
		});

		infoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					String versionName = mapActivity.getPackageManager().getPackageInfo(mapActivity.getPackageName(), 0).versionName;
					SpannableString info_msg = new SpannableString(String.format(
							getResources().getString(R.string.ingress_tool_info_msg), versionName));
					Linkify.addLinks(info_msg, Linkify.WEB_URLS);

					final Dialog dialog = new Dialog(mapActivity.getContext());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.info_dialog);

					TextView infoMsg = (TextView) dialog.findViewById(R.id.ingress_tool_info_msg);
					infoMsg.setText(info_msg);
					infoMsg.setMovementMethod(LinkMovementMethod.getInstance());
					
					Button dbg_msg_button = (Button) dialog.findViewById(R.id.info_dlg_send_debug_msg);
					
					dbg_msg_button.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ACRAConfiguration defaultConfiguration = ACRA.getConfig();
							// UPDATE CONFIG
							try {
								defaultConfiguration.setMode(ReportingInteractionMode.SILENT);
								defaultConfiguration.setResToastText(0);
							} catch (ACRAConfigurationException e) {
								e.printStackTrace();
							}
							ACRA.setConfig(defaultConfiguration);
							// CREATE SEND LOG NOTIFICATION
							ACRA.getErrorReporter().handleException(null);
							// RESET CONFIGURATION
							try {
								defaultConfiguration.setMode(ReportingInteractionMode.DIALOG);
								defaultConfiguration.setResToastText(R.string.crash_toast_text);
							} catch (ACRAConfigurationException e) {
								e.printStackTrace();
							}
							ACRA.setConfig(defaultConfiguration);
						}
					});

					ImageView donate = (ImageView) dialog.findViewById(R.id.donate);
					donate.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							startActivity(new Intent(Intent.ACTION_VIEW, Uri
									.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=MNME3CESKLGXL")));
						}
					});

					dialog.show();
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}

			}
		});

		logoutButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mapActivity.getSlidingMenu().toggle(false);
				mapActivity.logout();
			}
		});

		inventoryButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), InventoryActivity.class);
				mapActivity.getSlidingMenu().toggle(false);
				startActivityForResult(intent, InventoryActivity.INV_REQUEST);
			}
		});

		overlayButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				showOverlaySelector(view);
			}
		});

		forceReloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				intelMapHandler.sendEmptyMessage(IntelMapHandler.FORCE_RELOAD);
				MapManager.getInstance().forceReload();
				mapActivity.getSlidingMenu().toggle();
			}
		});

		tileloaderSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					Log.v(TAG, "Sync is on");
					settingsManager.setSyncEnabled(true);
				} else {
					Log.v(TAG, "Sync is off");
					settingsManager.setSyncEnabled(false);
				}
				MapManager.getInstance().switchOverlay(settingsManager.getMapOverlay());
			}
		});
		
		portalFilterButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPortalLevelFilter(v);
			}
		});

		shareButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.smenu_share:
//					final int errorCode = GooglePlusUtil.checkGooglePlusApp(getActivity());
//					if (errorCode == GooglePlusUtil.SUCCESS) {
						// Create an ACTION_SEND intent to share to Google+ with
						// attribution.

						// Include a deep link in the intent to a resource in
						// your app.
						// When the user clicks on the deep link,
						// ParseDeepLinkActivity will
						// immediately route to that resource.
						// View content =
						// getActivity().findViewById(R.id.activity_map);
						// content.setDrawingCacheEnabled(true);
						// String thumbnail_path = getScreen(content);
//						String thumbnail_path = getScreen();
//						Uri thumbnail = Uri.parse(thumbnail_path);
//						Intent shareIntent = PlusShare.Builder;
//								.from(getActivity())
//								.setType("text/plain")
//								.setContent(TAG, getString(R.string.plus_deep_link_metadata_title),
//										getString(R.string.plus_deep_link_metadata_description), thumbnail).getIntent();
//
//						startActivity(shareIntent);
//					} else {
//						// Prompt the user to install the Google+ app.
//						DialogFragment fragment = new GooglePlusErrorDialogFragment();
//						Bundle args = new Bundle();
//						args.putInt(GooglePlusErrorDialogFragment.ARG_ERROR_CODE, errorCode);
//						args.putInt(GooglePlusErrorDialogFragment.ARG_REQUEST_CODE, REQUEST_CODE_RESOLVE_GOOGLE_PLUS_ERROR);
//						fragment.setArguments(args);
//						fragment.show(getActivity().getFragmentManager(), TAG_ERROR_DIALOG_FRAGMENT);
//					}
//					break;
				}
			}
		});
	}

	public void showOverlaySelector(View view) {
		PopupMenu popup = new PopupMenu(getActivity(), view);
		popup.inflate(R.menu.overlay_map);
		popup.show();
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				MapOverlayEnum selectedOverlay = null;
				switch (item.getItemId()) {
				case R.id.overly_ingress: // INGRESS
					selectedOverlay = MapOverlayEnum.INGRESS;
					break;
				case R.id.overlay_maps: // MAPS
					selectedOverlay = MapOverlayEnum.MAPS;
					break;
				case R.id.overlay_satellite: // SATALITE
					selectedOverlay = MapOverlayEnum.SATALITE;
					break;
				default:// INGRESS overlay is the default overlay
					selectedOverlay = MapOverlayEnum.INGRESS;
					break;
				}
				MapManager.getInstance().switchOverlay(selectedOverlay);
				mapActivity.getSlidingMenu().toggle();
				return false;
			}
		});
	}
	
	
	public void showPortalLevelFilter(View view) {
		final Dialog dialog = new Dialog(mapActivity.getContext());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.portal_filter_dialog);
		
		final PortalFilterManager filterManager = PortalFilterManager.getInstance();
		
		Button ok = (Button) dialog.findViewById(R.id.portal_filter_dlg_ok);
		Button cancel = (Button) dialog.findViewById(R.id.portal_filter_dlg_cancel);
		Button selectAll = (Button) dialog.findViewById(R.id.portal_filter_dlg_select_all_button);
		
		final CheckBox level0 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel0);
		final CheckBox level1 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel1);
		final CheckBox level2 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel2);
		final CheckBox level3 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel3);
		final CheckBox level4 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel4);
		final CheckBox level5 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel5);
		final CheckBox level6 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel6);
		final CheckBox level7 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel7);
		final CheckBox level8 = (CheckBox) dialog.findViewById(R.id.checkBoxLevel8);
		final CheckBox fields = (CheckBox) dialog.findViewById(R.id.checkBoxFields);
		final CheckBox links = (CheckBox) dialog.findViewById(R.id.checkBoxLinks);
		
		level1.setChecked(filterManager.isLevelEnabled(0));
		level2.setChecked(filterManager.isLevelEnabled(1));
		level3.setChecked(filterManager.isLevelEnabled(2));
		level4.setChecked(filterManager.isLevelEnabled(3));
		level5.setChecked(filterManager.isLevelEnabled(4));
		level6.setChecked(filterManager.isLevelEnabled(5));
		level7.setChecked(filterManager.isLevelEnabled(6));
		level8.setChecked(filterManager.isLevelEnabled(7));
		level8.setChecked(filterManager.isLevelEnabled(8));
		fields.setChecked(filterManager.isFields());
		links.setChecked(filterManager.isLinks());
		
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				filterManager.setLevelEnabled(0, level0.isChecked());
				filterManager.setLevelEnabled(1, level1.isChecked());
				filterManager.setLevelEnabled(2, level2.isChecked());
				filterManager.setLevelEnabled(3, level3.isChecked());
				filterManager.setLevelEnabled(4, level4.isChecked());
				filterManager.setLevelEnabled(5, level5.isChecked());
				filterManager.setLevelEnabled(6, level6.isChecked());
				filterManager.setLevelEnabled(7, level7.isChecked());
				filterManager.setLevelEnabled(8, level8.isChecked());
				filterManager.setFields(fields.isChecked());
				filterManager.setLinks(links.isChecked());
				dialog.dismiss();
				MapManager.getInstance().applyPortalFilter();
			}
		});
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		
		selectAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				filterManager.enableAll();
				level0.setChecked(filterManager.isLevelEnabled(0));
				level1.setChecked(filterManager.isLevelEnabled(1));
				level2.setChecked(filterManager.isLevelEnabled(2));
				level3.setChecked(filterManager.isLevelEnabled(3));
				level4.setChecked(filterManager.isLevelEnabled(4));
				level5.setChecked(filterManager.isLevelEnabled(5));
				level6.setChecked(filterManager.isLevelEnabled(6));
				level7.setChecked(filterManager.isLevelEnabled(7));
				level8.setChecked(filterManager.isLevelEnabled(8));
				fields.setChecked(filterManager.isFields());
				links.setChecked(filterManager.isLinks());
			}
		});
		
		dialog.show();
	}

	// private String getScreen(View content)
	// {
	// Bitmap bitmap = content.getDrawingCache();
	// Calendar cal = Calendar.getInstance();
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmssS");
	// String dateStr = sdf.format(cal.getTime());
	//
	// File file = new
	// File(Environment.getExternalStorageDirectory()+"/IngressTool/"+dateStr+".png");
	// try
	// {
	// file.createNewFile();
	// FileOutputStream ostream = new FileOutputStream(file);
	// bitmap.compress(CompressFormat.PNG, 100, ostream);
	// ostream.close();
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// return file.getAbsoluteFile().toString();
	// }

//	private String getScreen() {
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HHmmssS");
//		String dateStr = sdf.format(cal.getTime());
//		String filepath = Environment.getExternalStorageDirectory() + "/IngressTool/" + dateStr + ".png";
//		try {
//			Process sh = Runtime.getRuntime().exec("sh", null, null);
//			OutputStream os = sh.getOutputStream();
//			os.write(("/system/bin/screencap " + filepath).getBytes("ASCII"));
//			os.flush();
//			os.close();
//			sh.waitFor();
//		} catch (IOException e) {
//
//		} catch (InterruptedException e) {
//
//		}
//		return filepath;
//	}
}
