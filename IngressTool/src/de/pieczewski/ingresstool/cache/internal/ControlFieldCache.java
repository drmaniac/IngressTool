package de.pieczewski.ingresstool.cache.internal;

import android.support.v4.util.LruCache;
import de.pieczewski.ingresstool.entities.ControlField;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.MapManagerEvents;

public class ControlFieldCache extends LruCache<String, ControlField> {


	public ControlFieldCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, ControlField oldValue,
			ControlField newValue) {
		super.entryRemoved(evicted, key, oldValue, newValue);
		if (oldValue.isPolygoneAvailabel()) {
			MapManager.getInstance().sendAsynEventMsg(MapManagerEvents.REMOVE_CONTROLLFIELD, oldValue.getPolygon());
		}
	}
}
