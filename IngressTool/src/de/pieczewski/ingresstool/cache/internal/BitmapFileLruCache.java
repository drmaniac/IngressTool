package de.pieczewski.ingresstool.cache.internal;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.util.Log;

public class BitmapFileLruCache extends LruCache<String, Bitmap> {
	
	private final static String TAG = BitmapFileLruCache.class.getName();

	protected final File cacheDirectory;

	public BitmapFileLruCache(int maxSize, Context context) {
		super(maxSize);
		cacheDirectory = context.getCacheDir(); // set the cache directory,
												// possibly with
												// Context.getCacheDir()
	}
	
	@Override
	protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
		super.entryRemoved(evicted, key, oldValue, newValue);
		File file = getCacheFile(key);
		if(file.exists() && !file.isDirectory()) {
			file.delete();
		}
	}

	public File getCacheFile(String key) {
		return new File(cacheDirectory, key);
	}

	@Override
	protected Bitmap create(String key) {
		File file = getCacheFile(key);
		Log.d(TAG, "cache file is "+file.getAbsolutePath());
		return (file.exists()) ? BitmapFactory.decodeFile(file.getAbsolutePath()) : null;
	}

	protected int sizeOf(String key, Bitmap value) {
		return value.getByteCount();
	}
}
