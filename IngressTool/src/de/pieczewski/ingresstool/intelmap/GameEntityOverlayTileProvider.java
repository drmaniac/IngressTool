package de.pieczewski.ingresstool.intelmap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import de.pieczewski.ingresstool.cache.IngressEntityCache;
import de.pieczewski.ingresstool.entities.mapper.GameEntityMapper;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.IngressRPCUtil;
import de.pieczewski.ingresstool.util.QuadKeyHelper;

public class GameEntityOverlayTileProvider implements TileProvider {
	private static final String TAG = GameEntityOverlayTileProvider.class.getName();

	public static final String COMMAND_THINNED_ENTITIES_V2 = "https://www.ingress.com/rpc/dashboard.getThinnedEntitiesV2";
	public static final String DASHBOARD_METHOD = "dashboard.getThinnedEntitiesV2";

	private IngressEntityCache entityCache;
	private GameEntityTile emptyTile;
	private MapManagerEventHandler mapManagerEventHandler;

	public GameEntityOverlayTileProvider() {
		mapManagerEventHandler = new MapManagerEventHandler();
		entityCache = IngressEntityCache.getInstance();
		emptyTile = new GameEntityTile("0", null, null, 0, 3888000000l);
		emptyTile.setLoaded(true);
		entityCache.cacheTile(emptyTile);
	}

	@Override
	public Tile getTile(int x, int y, int zoom) {
		Tile tile = null;
		try {
			if (zoom > 10) {
				int defaultZoom = zoom;
				Log.v(TAG, "xyz[" + x + "," + y + "," + zoom + "]");
				// String key = QuadKeyHelper.quadKey(x, y, defaultZoom);
				String key = zoom + "_" + x + "_" + y;
				LatLng minLatLng = new LatLng(QuadKeyHelper.getLat(y, defaultZoom), QuadKeyHelper.getLong(x, defaultZoom));
				LatLng maxLatLng = new LatLng(QuadKeyHelper.getLat(y + 1, defaultZoom), QuadKeyHelper.getLong(x + 1, defaultZoom));

				GameEntityTile gameEntityTile = new GameEntityTile(key, minLatLng, maxLatLng, zoom, 100);
				gameEntityTile = loadTile(gameEntityTile, zoom);
				tile = new Tile(0, 0, null);
			} else {
				Log.v(TAG, String.format("zoom level %d > 10 ... SKIPED!", zoom));
				tile = new Tile(0, 0, null);
			}
		} catch (Exception e) {
			Log.e(TAG, "Strange things are happened", e);
		}
		return tile;
	}

	private GameEntityTile loadTile(GameEntityTile gameEntityTile, int zoom) {

		Message loadTileMsg = mapManagerEventHandler.obtainMessage(MapManagerEvents.LOAD_DATA);
		if (zoom >= 17) {
			loadTileMsg.arg1 = Integer.valueOf(0);
		} else if (zoom <= 16 && zoom >= 15) {
			loadTileMsg.arg1 = Integer.valueOf(1);
		} else if (zoom <= 14 && zoom >= 12) {
			loadTileMsg.arg1 = Integer.valueOf(2);
		} else if (zoom <= 12 && zoom >= 10) {
			loadTileMsg.arg1 = Integer.valueOf(3);
		}
		long loadTileMsgId = System.currentTimeMillis(); 
		loadTileMsg.obj = loadTileMsgId;
		mapManagerEventHandler.sendMessage(loadTileMsg);

		JSONObject requst;
		List<GameEntityTile> tilePackage = new ArrayList<GameEntityTile>();
		tilePackage.add(gameEntityTile);
		try {
			requst = createJSONRequest(tilePackage, zoom);

			DefaultHttpClient client = IngressRPCUtil.getClient();
			HttpPost method = IngressRPCUtil.getMethodPost(COMMAND_THINNED_ENTITIES_V2);
			Log.d(TAG, requst.toString());
			method.setEntity(new StringEntity(requst.toString()));
			HttpResponse res = client.execute(method);

			StatusLine statusLine = res.getStatusLine();
			if (statusLine.getStatusCode() == 200) {
				HttpEntity entity = res.getEntity();
				GameEntityMapper gameEntityMapper = new GameEntityMapper(entityCache);
				tilePackage = gameEntityMapper.parse(entity.getContent(), tilePackage);
			} else {
				Log.e(TAG, String.format("read thinned entities failed: StatusCode = %d", statusLine.getStatusCode()));
				Log.e(TAG, IOUtils.convertInputStream(res.getEntity().getContent()));
			}
		} catch (JSONException e) {
			Log.e(TAG, "failed to parse json request/response", e);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "failed to encode json request", e);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "failed to connect", e);
		} catch (IOException e) {
			Log.e(TAG, "failed to read response data", e);
		}
		
		Message stopLoadTileMsg = mapManagerEventHandler.obtainMessage(MapManagerEvents.STOP_LOAD_DATA);
		stopLoadTileMsg.obj = loadTileMsgId;
		mapManagerEventHandler.sendMessage(stopLoadTileMsg);
		
		return tilePackage.get(0);
	}

	private JSONObject createJSONRequest(List<GameEntityTile> tilePackage, int zoom) throws JSONException {
		JSONObject request = new JSONObject();

		request.put("minLevelOfDetail", -1);

		JSONArray boundsParamsList = new JSONArray();
		for (GameEntityTile tile : tilePackage) {
			boundsParamsList.put(tile.getJSONParam());
		}

		request.put("boundsParamsList", boundsParamsList);
		request.put("method", DASHBOARD_METHOD);
		request.put("zoom", zoom);
		Log.d(TAG, request.toString());
		return request;
	}
}
