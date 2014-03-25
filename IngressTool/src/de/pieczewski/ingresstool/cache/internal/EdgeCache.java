package de.pieczewski.ingresstool.cache.internal;

import android.support.v4.util.LruCache;
import de.pieczewski.ingresstool.entities.Edge;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.MapManagerEvents;

public class EdgeCache extends LruCache<String, Edge> {
	

	public EdgeCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Edge oldValue, Edge newValue) {
		super.entryRemoved(evicted, key, oldValue, newValue);
		if(oldValue.isPolylineAvailable()) {
			MapManager.getInstance().sendAsynEventMsg(MapManagerEvents.REMOVE_EDGE, oldValue.getPolyline());
		}
	}
}
