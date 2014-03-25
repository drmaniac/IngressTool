package de.pieczewski.ingresstool.cache;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import de.pieczewski.ingresstool.cache.internal.BitmapFileLruCache;

public class BitmapCacheManager {

	private static BitmapCacheManager instance;
	private Context context;
	
	private BitmapFileLruCache bitmapFileLruCache;
	

	private BitmapCacheManager(Context contex) {
		this.context = contex;
		bitmapFileLruCache = new BitmapFileLruCache(40 * 1024 * 1024, context);
	}
	
	public static BitmapCacheManager factory(Context contex) {
		instance = new BitmapCacheManager(contex);
		return instance;
	}
	
	public static BitmapCacheManager getInstance() {
		return instance;
	}
	
	
	public Bitmap getBitmap(String key) {
		return bitmapFileLruCache.get(key);
	}
	
	public void putBitmap(String key, Bitmap bitmap) {
		try {
			File file = bitmapFileLruCache.getCacheFile(key);
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
			bitmapFileLruCache.put(key, bitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
	}
}
