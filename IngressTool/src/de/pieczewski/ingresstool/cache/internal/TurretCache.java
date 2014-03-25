package de.pieczewski.ingresstool.cache.internal;

import android.util.LruCache;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.intelmap.MapManager;
import de.pieczewski.ingresstool.intelmap.MapManagerEvents;

public class TurretCache extends LruCache<String, Turret> {

	public TurretCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected void entryRemoved(boolean evicted, String key, Turret oldValue, Turret newValue) {
		super.entryRemoved(evicted, key, oldValue, newValue);
		if (oldValue.isMarkerAvailabel()) { // If marke is available remove it
											// from the map
			MapManager.getInstance().sendAsynEventMsg(MapManagerEvents.REMOVE_TURRET, oldValue.getMarker());
		}
	}
}
