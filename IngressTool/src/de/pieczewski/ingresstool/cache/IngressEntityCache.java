package de.pieczewski.ingresstool.cache;

import java.util.Map;

import de.pieczewski.ingresstool.cache.internal.ControlFieldCache;
import de.pieczewski.ingresstool.cache.internal.EdgeCache;
import de.pieczewski.ingresstool.cache.internal.TileCache;
import de.pieczewski.ingresstool.cache.internal.TurretCache;
import de.pieczewski.ingresstool.entities.AbstractGameEntity;
import de.pieczewski.ingresstool.entities.ControlField;
import de.pieczewski.ingresstool.entities.Edge;
import de.pieczewski.ingresstool.entities.Turret;
import de.pieczewski.ingresstool.intelmap.GameEntityTile;

public class IngressEntityCache {

	@SuppressWarnings("unused")
	private static final String TAG = IngressEntityCache.class.getName();

	private TurretCache turretCache;
	private EdgeCache edgeCache;
	private ControlFieldCache controlFieldCache;
	private TileCache tileCache;
	
	private static IngressEntityCache ingressEntityCache;

	private IngressEntityCache() {
		turretCache = new TurretCache(1000);
		edgeCache = new EdgeCache(5000);
		controlFieldCache = new ControlFieldCache(5000);
		tileCache = new TileCache(200);
		ingressEntityCache = this;
	}
	
	public static IngressEntityCache getInstance() {
		return ingressEntityCache == null ? new IngressEntityCache() : ingressEntityCache;
	}

	public void cacheTurret(Turret turret) {
		if (turretCache.get(turret.getKey()) == null)
			turretCache.put(turret.getKey(), turret);
	}

	public void cacheEdge(Edge edge) {
		if (edgeCache.get(edge.getKey()) == null)
			edgeCache.put(edge.getKey(), edge);
	}

	public void cacheControlField(ControlField controlField) {
		if (controlFieldCache.get(controlField.getKey()) == null)
			controlFieldCache.put(controlField.getKey(), controlField);
	}

	public void cacheTile(GameEntityTile gameEntityTile) {
//		if (tileCache.get(gameEntityTile.getKey()) == null) {
			tileCache.put(gameEntityTile.getKey(), gameEntityTile);
//		}
	}

	public boolean isGameTileCached(String key) {
		boolean result = false;
		if(tileCache.get(key) != null && tileCache.get(key).isLoaded()) {
			result = true;
		}
		return result;
	}
	
	public boolean isGameTileCachedOrPreparedToCache(String key) {
		boolean result = false;
		GameEntityTile entityTile = tileCache.get(key);
		if(entityTile != null && !entityTile.isExpired()) {
			result = true;
		}
		return result;
	}

	public GameEntityTile getGameEntityTile(String key) {
		return tileCache.get(key);
	}

	public void removeTurret(Turret turret) {
		removeTurret(turret.getKey());
	}

	public void removeTurret(String key) {
		turretCache.remove(key);
	}

	public void removeEdge(Edge edge) {
		removeEdge(edge.getKey());
	}

	public void removeEdge(String key) {
		edgeCache.remove(key);
	}

	public AbstractGameEntity findGameEntity(String key) {
		AbstractGameEntity result = turretCache.get(key);
		if (result != null)
			return result;
		result = edgeCache.get(key);
		if (result != null)
			return result;
		result = controlFieldCache.get(key);
		return result;
	}

	public void removeControlField(ControlField controlField) {
		removeEdge(controlField.getKey());
	}

	public void removeControlField(String key) {
		controlFieldCache.remove(key);
	}

	public Map<String, Turret> getAllTurrets() {
		return turretCache.snapshot();
	}

	public Map<String, Edge> getAllEdges() {
		return edgeCache.snapshot();
	}

	public Map<String, ControlField> getAllControlFields() {
		return controlFieldCache.snapshot();
	}
	
	public void clear() {
		turretCache.evictAll();
		edgeCache.evictAll();
		controlFieldCache.evictAll();
		tileCache.evictAll();
	}

	public Turret getTurret(String turretKey) {
		return turretCache.get(turretKey);
	}

}
