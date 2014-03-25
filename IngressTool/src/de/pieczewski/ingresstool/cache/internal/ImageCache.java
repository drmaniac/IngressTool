package de.pieczewski.ingresstool.cache.internal;

import java.io.File;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


public class ImageCache extends LruCache<String, Bitmap> {

	static ImageCache instance;
	
	File cacheDir;
	File imageCacheDir;
	
	private ImageCache(int maxSize, File cacheDir) {
		super(maxSize);
		this.cacheDir = cacheDir;
		imageCacheDir = new File (cacheDir.getAbsolutePath()+File.pathSeparator+"images");
		initCache();
		instance = this;
	}
	
	public static ImageCache factory(File cacheDir) {
		instance  = new ImageCache(100, cacheDir);
		return instance;
	}
	
	public static ImageCache getInstance() {
		return instance;
	}
	
	private void initCache() {
		if(!imageCacheDir.exists()) {
			imageCacheDir.mkdir();
		}
	}
}
