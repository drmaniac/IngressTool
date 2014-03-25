package de.pieczewski.ingresstool.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import de.pieczewski.ingresstool.cache.BitmapCacheManager;

public class HTTPImageUtil extends AsyncTask<String, Void, Object> {

	private final static String TAG = "HTTPImageUtil";

	public final static int IMAGE_MODE_DRAWABLE = 0001;
	public final static int IMAGE_MODE_BITMAP = 0002;
	public final static int IMAGE_MODE_IMAGEVIEW = 0003;

	private Bitmap bitmap;
	@SuppressWarnings("unused")
	private Drawable drawable;
	private ImageView imageView;
	private int imageMode;

	public HTTPImageUtil(int imageMode, Object o) {
		this.imageMode = imageMode;
		switch (imageMode) {
		case IMAGE_MODE_BITMAP:
			this.bitmap = (Bitmap) o;
			break;

		case IMAGE_MODE_DRAWABLE:
			this.drawable = (Drawable) o;
			break;

		case IMAGE_MODE_IMAGEVIEW:
			this.imageView = (ImageView) o;
			break;

		default:
			break;
		}
	}

	@Override
	protected Object doInBackground(String... urls) {
		String url = urls[0];
		if(url == null) {
			return null;
		}
		return fetchImage(url);
	}

	public Object fetchImage(String address) {
		Object result = null;
		BitmapCacheManager imageCache = BitmapCacheManager.getInstance();
		try {
			bitmap = imageCache.getBitmap(address);
			if(bitmap == null) {
				URL url = new URL(address);
				URLConnection connection = url.openConnection();
				InputStream is = connection.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
				imageCache.putBitmap(address, bitmap);
			}
			
			switch (imageMode) {
			case IMAGE_MODE_BITMAP:
				Log.d(TAG, "URL: " + address + " Bytes" + bitmap.getByteCount());
				result = bitmap;
				break;

			case IMAGE_MODE_DRAWABLE:

				break;

			case IMAGE_MODE_IMAGEVIEW:
				Log.d(TAG, "set image to imageview");
				result = bitmap;
				break;

			default:
				break;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onPostExecute(Object result) {
		switch (imageMode) {
		case IMAGE_MODE_BITMAP:
			this.bitmap = (Bitmap) result;
			break;

		case IMAGE_MODE_DRAWABLE:
			this.drawable = (Drawable) result;
			break;

		case IMAGE_MODE_IMAGEVIEW:
			this.imageView.setImageBitmap((Bitmap) result);
			this.imageView.invalidate();
			break;

		default:
			break;
		}
	}
}
