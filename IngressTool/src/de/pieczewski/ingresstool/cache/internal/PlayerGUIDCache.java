package de.pieczewski.ingresstool.cache.internal;

import android.util.LruCache;

public class PlayerGUIDCache extends LruCache<String, String> {

	private static PlayerGUIDCache instance;
	
	public PlayerGUIDCache(int maxSize) {
		super(maxSize);
		instance = this;
	}
	
	public static PlayerGUIDCache getInstance() {
		return instance == null ? new PlayerGUIDCache(100) : instance;
	}

}
