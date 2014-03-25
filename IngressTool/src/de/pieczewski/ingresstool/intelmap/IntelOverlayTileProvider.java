package de.pieczewski.ingresstool.intelmap;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.util.Log;

import com.google.android.gms.maps.model.UrlTileProvider;

import de.pieczewski.ingresstool.util.Ingress;

public class IntelOverlayTileProvider extends UrlTileProvider {

	private final static String TAG = "de.pieczewski.ingresstool.map.IngressMapTileProvider";

	/** This returns ingress tiles. */

	public IntelOverlayTileProvider(int sizex, int sizey) {
		super(sizex, sizey);
	}

	@Override
	public URL getTileUrl(int x, int y, int zoom) {
		Log.d(TAG, String.format("MapTile: %d %d %d", x, y, zoom));
		URL url = null;
		try {
			String country = Locale.getDefault().getCountry();
			String language = Locale.getDefault().getLanguage();
			String s = String.format(Locale.US, Ingress.INTEL_MAP_OVERLAY_URL_FORMAT, language, country, x,
					y, zoom);
			url = new URL(s);
		} catch (MalformedURLException e) {
			Log.e(TAG, "somthing goes wrong", e);
		} catch (Exception e) {
			Log.e(TAG, "somthing goes totaly wrong", e);
		}
		return url;
	}

}
