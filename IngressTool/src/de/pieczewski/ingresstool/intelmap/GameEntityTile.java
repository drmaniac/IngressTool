package de.pieczewski.ingresstool.intelmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class GameEntityTile {
	private final static String TAG = GameEntityTile.class.getName();
	
	private String key;
	private LatLng minLatLng;
	private LatLng maxLatLng;
	private long createdAt;
	private int zoom;
	
	private Set<String> gameEntities;
	
	private boolean loaded = false;
	
	private long expire;
	
	public GameEntityTile(String key, LatLng minLatLng, LatLng maxLatLng, int zoom, long lifetime) {
		this.key = key;
		this.minLatLng = minLatLng;
		this.maxLatLng = maxLatLng;
		this.createdAt = System.currentTimeMillis();
		this.expire = System.currentTimeMillis()+lifetime;
		this.zoom = zoom;
	}
	
	public String getKey() {
		return key;
	}
	
	public long getCreatedAt() {
		return createdAt;
	}
	
	public JSONObject getJSONParam() throws JSONException {
		ArrayList<Integer> lat = new ArrayList<Integer>();
		lat.add((int) (minLatLng.latitude * 1E6));
		lat.add((int) (maxLatLng.latitude * 1E6));
		
		ArrayList<Integer> lng = new ArrayList<Integer>();
		lng.add((int) (minLatLng.longitude * 1E6));
		lng.add((int) (maxLatLng.longitude * 1E6));
		
		Collections.sort(lat);
		Collections.sort(lng);
		
		JSONObject param = new JSONObject();

		param.put("id", key);
		param.put("minLatE6", lat.get(0));
		param.put("minLngE6", lng.get(0));
		param.put("maxLatE6", lat.get(1));
		param.put("maxLngE6", lng.get(1));
		param.put("qk", key);
		
		return param;
	}
	
	public int getZoom() {
		return zoom;
	}
	
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	
	public void addGameEntity (String key) {
		if(gameEntities == null) {
			gameEntities = new CopyOnWriteArraySet<String>();
		}
		gameEntities.add(key);
	}
	
	public Set<String> getGameEntitiesKeys() {
		if(gameEntities == null) {
			gameEntities = new CopyOnWriteArraySet<String>();
		}
		return gameEntities;
	}
	
	public void removeGameEntity (String key) {
		if(gameEntities == null) {
			gameEntities = new CopyOnWriteArraySet<String>();
		}
		gameEntities.remove(key);
	}
	
	public boolean containsGameEntity(String key) {
		if(gameEntities == null) {
			gameEntities = new CopyOnWriteArraySet<String>();
		}
		return gameEntities.contains(key);
	}
	
	@Override
	public boolean equals(Object o) {
		GameEntityTile gameEntityTile = (GameEntityTile) o;
		return this.getKey().equals(gameEntityTile.getKey());
	}
	
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}
	public boolean isLoaded() {
		return loaded;
	}
	
	public void setLifeTime(long lifeTime) {
		this.expire = System.currentTimeMillis()+lifeTime;
	}
	
	public boolean isExpired() {
		Log.v(TAG, String.format("tile will die in %dms", expire-System.currentTimeMillis()));
		return System.currentTimeMillis() > expire ? true : false; 
	}
}
