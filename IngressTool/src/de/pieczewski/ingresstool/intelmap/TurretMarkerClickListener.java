package de.pieczewski.ingresstool.intelmap;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import de.pieczewski.ingresstool.PortalDetail;
import de.pieczewski.ingresstool.R;
import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.util.PortalImages;

@SuppressLint("DefaultLocale")
public class TurretMarkerClickListener implements OnMarkerClickListener {

	@SuppressLint("DefaultLocale")
	@Override
	public boolean onMarkerClick(final Marker marker) {
		final MapManager mapManager = MapManager.getInstance();
		final Dialog dialog = new Dialog(mapManager.getManagedActivity());
		final Turret turret = IngressEntityCache.getInstance().getTurret(marker.getTitle());

		Resources resources = mapManager.getManagedActivity().getResources();

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.portal_info_dialog);

		String dis = String.format(Locale.getDefault(), "%.2f", MapManager.getInstance()
				.getDistanceTo(turret));

		TextView snippet = (TextView) dialog.findViewById(R.id.portal_info_dialog_snippet);
		snippet.setText(turret.getTitle() + "\n" + "Energy: " + turret.getTurretTotalEnergy()
				+ "\n\n" + turret.getAddress() + "\n" + resources.getString(R.string.portal_info_distance)+ " "+ dis + " km");

		ImageView level = (ImageView) dialog.findViewById(R.id.portal_info_dialog_level);
		Bitmap level_image = PortalImages.getPortalImageBitmap(resources, turret.getLevel(),
				turret.getTeam());
		level.setImageBitmap(level_image);

		ImageView nav = (ImageView) dialog.findViewById(R.id.portal_info_dialog_nav);
		nav.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String nav = String.format(Locale.ENGLISH, "geo:%f,%f?q=%f,%f(%s)",
						turret.getLatLng().latitude, turret.getLatLng().longitude,
						turret.getLatLng().latitude, turret.getLatLng().longitude,
						turret.getTitle());
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(nav));
				mapManager.getManagedActivity().startActivity(i);
			}
		});

		snippet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mapManager.getManagedActivity(), PortalDetail.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				intent.putExtra("turret_marker", marker.getTitle());
				mapManager.getManagedActivity().startActivity(intent);
			}
		});

		mapManager.moveTo(turret.getLatLng(), 0f);

		dialog.show();
		return true;
	}

}
