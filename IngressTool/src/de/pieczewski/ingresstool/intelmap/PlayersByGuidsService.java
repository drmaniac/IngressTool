package de.pieczewski.ingresstool.intelmap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import de.pieczewski.ingresstool.cache.internal.PlayerGUIDCache;
import de.pieczewski.ingresstool.entities.PlayerGUIDToNick;
import de.pieczewski.ingresstool.entities.mapper.PlayerByGuidMapper;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.IngressRPCUtil;

public class PlayersByGuidsService extends AsyncTask<Object, Void, String> {

	private static final String TAG = PlayersByGuidsService.class.getName();
	
	public static final String COMMAND_GET_GAME_SCORE = "https://www.ingress.com/rpc/dashboard.getPlayersByGuids";
	public static final String DASHBOARD_METHOD = "dashboard.getPlayersByGuids";
	
	private TextView playerText;
	private String stringFormat;
	private PlayerGUIDToNick guidToNick;
	private PlayerGUIDCache playerGUIDCache;
	private String additionalText = null;
	
	public PlayersByGuidsService() {
		playerGUIDCache = PlayerGUIDCache.getInstance();
	}
	
	@Override
	protected String doInBackground(Object... params) {
		
		try {
			guidToNick = (PlayerGUIDToNick)params[0];
			playerText = (TextView) params[1];
			stringFormat = (String)params[2];
			if(params.length > 3)
				additionalText = (String)params[3];
			
			if(guidToNick.getNick() == null) {
				String nick = playerGUIDCache.get(guidToNick.getGUID());
				if(nick == null) {
					JSONObject requst = createJSONRequest(guidToNick.getGUID());
					DefaultHttpClient client = IngressRPCUtil.getClient();
					HttpPost method = IngressRPCUtil.getMethodPost(COMMAND_GET_GAME_SCORE);
					Log.d(TAG, requst.toString());
					method.setEntity(new StringEntity(requst.toString()));
					HttpResponse res = client.execute(method);
		
					StatusLine statusLine = res.getStatusLine();
					if (statusLine.getStatusCode() == 200) {
						HttpEntity entity = res.getEntity();
						PlayerByGuidMapper mapper = new PlayerByGuidMapper();
						nick = mapper.parse(entity.getContent());
					} else {
						Log.e(TAG, String.format("read thinned entities failed: StatusCode = %d", statusLine.getStatusCode()));
						Log.e(TAG, IOUtils.convertInputStream(res.getEntity().getContent()));
					}
				}
				playerGUIDCache.put(guidToNick.getGUID(), nick);
				Log.v(TAG, "GUID("+guidToNick.getGUID()+") = Nick("+nick+")");
				return nick;
			} else {
				return guidToNick.getNick();
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	
	private JSONObject createJSONRequest(String guid) throws JSONException {
		JSONObject request = new JSONObject();
		/*
		 * {"guids":["GUID"],"method":"dashboard.getPlayersByGuids"}
		 */
		request.put("method", DASHBOARD_METHOD);		
		JSONArray guids = new JSONArray();
		guids.put(guid);
		request.put("guids", guids);
		
		return request;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.d(TAG, stringFormat);
		if(additionalText != null) {
			playerText.setText(String.format(stringFormat, result, additionalText));
		} else {
			playerText.setText(String.format(stringFormat, result));
		}
		guidToNick.setPlayerNick(result);
	}

}
