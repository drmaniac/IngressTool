package de.pieczewski.ingresstool.intelmap.backgroundTasks;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.MatchResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.IngressColor;
import de.pieczewski.ingresstool.util.IngressRPCUtil;
import de.pieczewski.ingresstool.util.StringUtils;

public class PlayerStatTask {

	private static final String TAG = PlayerStatTask.class.getName();

	private static final String COMMAND_GET_PLAYER_STAT = "https://www.ingress.com/intel";
	
	public static final String COMMAND_GET_GAME_SCORE = "https://www.ingress.com/rpc/dashboard.getGameScore";
	public static final String DASHBOARD_METHOD = "dashboard.getGameScore";
	
	private static final String GUID = "9b368f61b8f949efb305961313057af6.c";
	
	public Spannable loadPlayerStat(){
		HashMap<String, String> results = new HashMap<String, String>();
		try {
			DefaultHttpClient client = IngressRPCUtil.getClient();
			HttpPost method = IngressRPCUtil.getMethodPost(COMMAND_GET_PLAYER_STAT);
			HttpResponse res = client.execute(method);
			
			HttpEntity entity = res.getEntity();
			String httpResult = IOUtils.convertInputStream(entity.getContent());
			
			List<MatchResult> playerStatsResults = StringUtils.findAll(httpResult,
					"PLAYER(.*?);");
			if(playerStatsResults.size() > 0) {
				String player_stats_string = playerStatsResults.get(0).group();
				int start = player_stats_string.indexOf("{")+1;
				int end = player_stats_string.indexOf("}");
				
				String player_stats= player_stats_string.substring(start, end);
				for(String value : player_stats.split(",")) {
					Log.v(TAG,getKey(value)+" "+ getValue(value) );
					results.put(getKey(value), getValue(value));
				}
			}
			
			loadGameScoreTest();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createSpannable(results);
	}
	
	private Spannable createSpannable(HashMap<String, String> result) {
		Spannable sp = null;
		try {
			DecimalFormat df = new DecimalFormat(",##0.00");
			
			String string = result.get("nickname")+" "+"AP:"+ df.format(Long.parseLong(result.get("ap")))+" "+"XM:"+df.format(Long.parseLong(result.get("energy")));
			sp = new SpannableString(string);
			
			if(result.get("team").equals("ALIENS")) {
				sp.setSpan(new ForegroundColorSpan(IngressColor.ALIEN_COLOR), 0, string.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				sp.setSpan(new ForegroundColorSpan(IngressColor.RESISTANT_COLOR), 0, string.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		} catch (Exception e) {
			Log.e(TAG, "something happend", e);
			return null;
		}
		return sp;
	}
	
	private String getValue(String keyvalueString) {
		String value = keyvalueString.split(":")[1];
		value = value.replace("\"", "");
		return value.trim();
	}
	
	private String getKey(String keyvalueString) {
		String key = keyvalueString.split(":")[0];
		key = key.replaceAll("\"", "");
		return key.trim();
	}
	
	private void loadGameScoreTest() throws JSONException, ClientProtocolException, IOException {
		JSONObject requst = createJSONRequest();
		DefaultHttpClient jsonClient = IngressRPCUtil.getClient();
		HttpPost jsonMethod = IngressRPCUtil.getMethodPost(COMMAND_GET_GAME_SCORE);
		Log.d(TAG, requst.toString());
		jsonMethod.setEntity(new StringEntity(requst.toString()));
		HttpResponse jsonRes = jsonClient.execute(jsonMethod);

		StatusLine statusLine = jsonRes.getStatusLine();
		if (statusLine.getStatusCode() == 200) {
			HttpEntity jsonEntity = jsonRes.getEntity();
			Log.v(TAG, IOUtils.convertInputStream(jsonEntity.getContent()));
		} else {
			Log.e(TAG, String.format("read thinned entities failed: StatusCode = %d", statusLine.getStatusCode()));
			Log.e(TAG, IOUtils.convertInputStream(jsonRes.getEntity().getContent()));
		}
	}
	
	private JSONObject createJSONRequest() throws JSONException {
		JSONObject request = new JSONObject();
		/*
		 * {"method":"dashboard.getGameScore"}
		 */
		request.put("method", "dashboard.getGameScore");
		request.put("guid", GUID);
		return request;
	}
}
