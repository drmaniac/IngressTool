package de.pieczewski.ingresstool.intelmap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import de.pieczewski.ingresstool.cache.BitmapCacheManager;
import de.pieczewski.ingresstool.util.Ingress;

public class IntelOverlayCachedTileProvider implements TileProvider {

	private final static String TAG = IntelOverlayCachedTileProvider.class.getName();

	// @Override
	// public URL getTileUrl(int x, int y, int zoom) {
	// Log.d(TAG, String.format("MapTile: %d %d %d", x, y, zoom));
	// URL url = null;
	// try {
	// String country = Locale.getDefault().getCountry();
	// String language = Locale.getDefault().getLanguage();
	// String s = String.format(Locale.US, Ingress.INTEL_MAP_OVERLAY_URL_FORMAT,
	// language, country, x,
	// y, zoom);
	// url = new URL(s);
	// } catch (MalformedURLException e) {
	// Log.e(TAG, "somthing goes wrong", e);
	// } catch (Exception e) {
	// Log.e(TAG, "somthing goes totaly wrong", e);
	// }
	// return url;
	// }

	@Override
	public Tile getTile(int x, int y, int zoom) {
		Log.d(TAG, String.format("MapTile: %d %d %d", x, y, zoom));
		URL url = null;
		Tile tile = null;
		Bitmap bitmap = null;
		byte[] data = null;
		String country = Locale.getDefault().getCountry();
		String language = Locale.getDefault().getLanguage();
		String key = language+"_"+country+"_"+x+"_"+y+"_"+zoom;
		BitmapCacheManager bitmapCacheManager = BitmapCacheManager.getInstance();
		
		bitmap = bitmapCacheManager.getBitmap(key);
		if(bitmap == null) {
			Log.d(TAG, "get new tile overlay");
			try {
				String s = String.format(Locale.US, Ingress.INTEL_MAP_OVERLAY_URL_FORMAT, language, country, x, y, zoom);
				url = new URL(s);
				InputStream is = (InputStream) url.getContent();
		        bitmap = BitmapFactory.decodeStream(is);
		        bitmapCacheManager.putBitmap(key, bitmap);
		        
			} catch (MalformedURLException e) {
				Log.e(TAG, "somthing goes wrong", e);
			} catch (Exception e) {
				Log.e(TAG, "somthing goes totaly wrong", e);
			}
		} else {
			Log.d(TAG, "found cached tile overlay");
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 0, bos);
        data = bos.toByteArray();
		tile = new Tile(bitmap.getHeight(), bitmap.getWidth(), data);
		return tile;
	}

}
