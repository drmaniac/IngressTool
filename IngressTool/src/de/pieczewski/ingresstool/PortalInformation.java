package de.pieczewski.ingresstool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.Mod;
import de.pieczewski.ingresstool.entities.Resonator;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.intelmap.PlayersByGuidsService;
import de.pieczewski.ingresstool.util.HTTPImageUtil;
import de.pieczewski.ingresstool.util.IngressColor;
import de.pieczewski.ingresstool.util.PortalImages;

public class PortalInformation extends Fragment {

	private static final String TAG = PortalInformation.class.getName();
	
	private String turretKey;
	private Turret turret;
	private Drawable emptyMod;

	public PortalInformation() {
		Log.v(TAG, "reintent PortalInformation");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.portal_information, null);
		
		Bundle bundle = getArguments();
		this.turretKey = bundle.getString("turret_key");
		this.turret = IngressEntityCache.getInstance().getTurret(turretKey);
		Log.v(TAG, "load turret["+turretKey+"]");
		Log.v(TAG, "load title["+turret.getTitle()+"]");
		Log.v(TAG, "load energy["+turret.getTurretTotalEnergy()+"]");
		Log.v(TAG, "load address["+turret.getAddress()+"]");

		TextView title = (TextView) view.findViewById(R.id.portal_detail_title);
		title.setText(turret.getTitle() + "\n" + "Energy: " + turret.getTurretTotalEnergy() + "\n\n" + turret.getAddress());

		TextView capturingPlayer = (TextView) view.findViewById(R.id.portal_capturing_player);
		PlayersByGuidsService playersByGuidsService = new PlayersByGuidsService();
		String captureByTxt = getResources().getString(R.string.capturing_player);
		capturingPlayer.setText(String.format(captureByTxt, "Loading...", ""));
		
		String capturetTimeString = "";
		if(turret.getCapturedTime() != null) {
			Date capturedDate = new Date(turret.getCapturedTime());
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault());
			capturetTimeString = "@"+dateFormat.format(capturedDate);	
		}
		playersByGuidsService.execute(turret, capturingPlayer, captureByTxt, capturetTimeString);

		ImageView portal_level_img = (ImageView) view.findViewById(R.id.portal_detail_level_img);
		Bitmap level_image = PortalImages.getPortalImageBitmap(getResources(), turret.getLevel(), turret.getTeam());
		portal_level_img.setImageBitmap(level_image);

		ImageView portalImage = (ImageView) view.findViewById(R.id.portal_image);
		HTTPImageUtil imageUtil = new HTTPImageUtil(HTTPImageUtil.IMAGE_MODE_IMAGEVIEW, portalImage);
		imageUtil.execute(turret.getImageURL());

		emptyMod = view.getResources().getDrawable(android.R.drawable.dialog_frame);

		loadMods(view);
		loadResonators(view);

		setUpButtons(view);

		return view;
	}

	private void loadMods(View view) {
		TextView mod1Type = (TextView) view.findViewById(R.id.portal_mod_01);
		mod1Type.setVisibility(View.VISIBLE);

		TextView mod2Type = (TextView) view.findViewById(R.id.portal_mod_02);
		mod2Type.setVisibility(View.VISIBLE);

		TextView mod3Type = (TextView) view.findViewById(R.id.portal_mod_03);
		mod3Type.setVisibility(View.VISIBLE);

		TextView mod4Type = (TextView) view.findViewById(R.id.portal_mod_04);
		mod4Type.setVisibility(View.VISIBLE);

		int count = 0;

		for (int i = 0; i < turret.getMods().size(); i++) {
			count++;
			StringBuilder modString = new StringBuilder();
			Mod mod = turret.getMods().get(i);
			int color = 0xffffffff;
			if (mod.getRarity().equals("COMMON")) {
				color = IngressColor.PS_COMMON;
			}
			if (mod.getRarity().equals("RARE")) {
				color = IngressColor.PS_RARE;
			}
			if (mod.getRarity().equals("VERY_RARE")) {
				color = IngressColor.PS_VARY_RARE;
			}

			switch (i) {
			case 0:
				modString.append(mod.getRarity());
				mod1Type.setText(modString.toString());
				mod1Type.setTextColor(color);
				mod1Type.setVisibility(View.VISIBLE);
				break;
			case 1:
				modString.append(mod.getRarity());
				mod2Type.setText(modString.toString());
				mod2Type.setTextColor(color);
				mod2Type.setVisibility(View.VISIBLE);
				break;
			case 2:
				modString.append(mod.getRarity());
				mod3Type.setText(modString.toString());
				mod3Type.setTextColor(color);
				mod3Type.setVisibility(View.VISIBLE);
				break;
			case 3:
				modString.append(mod.getRarity());
				mod4Type.setText(modString.toString());
				mod4Type.setTextColor(color);
				mod4Type.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}

		String empty = "Empty";

		for (int i = 0; i < 4 - count; i++) {
			switch (i) {
			case 0:
				mod4Type.setText(empty);
				mod4Type.setCompoundDrawablesWithIntrinsicBounds(null, emptyMod, null, null);
				mod4Type.setVisibility(View.VISIBLE);
				break;
			case 1:
				mod3Type.setText(empty);
				mod3Type.setCompoundDrawablesWithIntrinsicBounds(null, emptyMod, null, null);
				mod3Type.setVisibility(View.VISIBLE);
				break;
			case 2:
				mod2Type.setText(empty);
				mod2Type.setCompoundDrawablesWithIntrinsicBounds(null, emptyMod, null, null);
				mod2Type.setVisibility(View.VISIBLE);
				break;
			case 3:
				mod1Type.setText(empty);
				mod1Type.setCompoundDrawablesWithIntrinsicBounds(null, emptyMod, null, null);
				mod1Type.setVisibility(View.VISIBLE);
				break;

			default:
				break;
			}
		}
	}

	private void loadResonators(View view) {
		TextView resonatorSlot1Level = (TextView) view.findViewById(R.id.portal_slot_01);
		resonatorSlot1Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot2Level = (TextView) view.findViewById(R.id.portal_solt_02);
		resonatorSlot2Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot3Level = (TextView) view.findViewById(R.id.portal_slot_03);
		resonatorSlot3Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot4Level = (TextView) view.findViewById(R.id.portal_slot_04);
		resonatorSlot4Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot5Level = (TextView) view.findViewById(R.id.portal_slot_05);
		resonatorSlot5Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot6Level = (TextView) view.findViewById(R.id.portal_slot_06);
		resonatorSlot6Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot7Level = (TextView) view.findViewById(R.id.portal_slot_07);
		resonatorSlot7Level.setVisibility(View.INVISIBLE);
		TextView resonatorSlot8Level = (TextView) view.findViewById(R.id.portal_slot_08);
		resonatorSlot8Level.setVisibility(View.INVISIBLE);

		for (Resonator r : turret.getResonators()) {

			switch (r.getSlot()) {
			case 0:
				showResonator(r, resonatorSlot1Level);
				break;
			case 1:
				showResonator(r, resonatorSlot2Level);
				break;
			case 2:
				showResonator(r, resonatorSlot3Level);
				break;
			case 3:
				showResonator(r, resonatorSlot4Level);
				break;
			case 4:
				showResonator(r, resonatorSlot5Level);
				break;
			case 5:
				showResonator(r, resonatorSlot6Level);
				break;
			case 6:
				showResonator(r, resonatorSlot7Level);
				break;
			case 7:
				showResonator(r, resonatorSlot8Level);
				break;
			default:
				break;
			}
		}
	}
	
	private void showResonator(Resonator r, TextView rText) {
		String resonatorText_Loading = "L" + r.getLevel() + " " + r.getEnergyInPercent() + "%" + "\n" + "Loading..";
		String resonatorText = "L" + r.getLevel() + " " + r.getEnergyInPercent() + "%%" + "\n" + "%s";
		
		PlayersByGuidsService playersByGuidsService = new PlayersByGuidsService();
		playersByGuidsService.execute(r, rText, resonatorText);
		
		rText.setText(resonatorText_Loading);
		rText.setTextColor(IngressColor.getLevelColor(r.getLevel()));
		rText.setVisibility(View.VISIBLE);
	}

	private void setUpButtons(View view) {
		ImageButton nav = (ImageButton) view.findViewById(R.id.portal_detail_nav);

		nav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nav = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)", turret.getLatLng().latitude,
						turret.getLatLng().longitude, turret.getLatLng().latitude, turret.getLatLng().longitude,
						turret.getTitle());
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(nav));
				getActivity().startActivity(i);
			}
		});
	}
}
