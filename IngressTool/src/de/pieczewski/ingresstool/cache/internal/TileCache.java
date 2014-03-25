package de.pieczewski.ingresstool.cache.internal;

import android.util.LruCache;
import de.pieczewski.ingresstool.intelmap.GameEntityTile;

public class TileCache extends LruCache<String, GameEntityTile> {

	public TileCache(int maxSize) {
		super(maxSize);
	}
}
