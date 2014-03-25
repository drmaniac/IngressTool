package de.pieczewski.ingresstool.intelmap.backgroundTasks;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;

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
import de.pieczewski.ingresstool.entities.GameScore;
import de.pieczewski.ingresstool.entities.Team;
import de.pieczewski.ingresstool.entities.mapper.GameScoreMapper;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.IngressColor;
import de.pieczewski.ingresstool.util.IngressRPCUtil;

public class GameScoreTask {

	private static final String TAG = GameScoreTask.class.getName();

	public static final String COMMAND_GET_GAME_SCORE = "https://www.ingress.com/rpc/dashboard.getGameScore";
	public static final String DASHBOARD_METHOD = "dashboard.getGameScore";

	public Spannable loadGameScore() {
		HashMap<String, GameScore> result = null;
		JSONObject requst;
		try {
			requst = createJSONRequest();

			DefaultHttpClient client = IngressRPCUtil.getClient();
			HttpPost method = IngressRPCUtil.getMethodPost(COMMAND_GET_GAME_SCORE);
			Log.d(TAG, requst.toString());
			method.setEntity(new StringEntity(requst.toString()));
			HttpResponse res = client.execute(method);

			StatusLine statusLine = res.getStatusLine();
			if (statusLine.getStatusCode() == 200) {
				HttpEntity entity = res.getEntity();
				GameScoreMapper gameScoreMapper = new GameScoreMapper();
				result = gameScoreMapper.parse(entity.getContent());
			} else {
				Log.e(TAG, String.format("read thinned entities failed: StatusCode = %d", statusLine.getStatusCode()));
				Log.e(TAG, IOUtils.convertInputStream(res.getEntity().getContent()));
			}
		} catch (JSONException e) {
			Log.e(TAG, "can't create JSON request", e);
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, "can't create JSON request", e);
		} catch (ClientProtocolException e) {
			Log.e(TAG, "can't send http request", e);
		} catch (IOException e) {
			Log.e(TAG, "could not convert response input stream to string", e);
		}

		return createSpannable(result);
	}

	private Spannable createSpannable(HashMap<String, GameScore> result) {
		if (result != null) {
			DecimalFormat df = new DecimalFormat(",##0.00");
			String resistanceScore = "RESISTANCE: " + df.format(result.get(Team.RESISTANCE.toString()).getScore()) + " MU's\n";
			String alienScore = "ENLIGHTENED: " + df.format(result.get(Team.ALIENS.toString()).getScore()) + " MU's";
			String scoreText = resistanceScore + alienScore;

			Spannable sp = new SpannableString(scoreText);
			sp.setSpan(new ForegroundColorSpan(IngressColor.RESISTANT_COLOR), 0, resistanceScore.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			sp.setSpan(new ForegroundColorSpan(IngressColor.ALIEN_COLOR), resistanceScore.length(), scoreText.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			return sp;
		}
		return null;
	}

	private JSONObject createJSONRequest() throws JSONException {
		JSONObject request = new JSONObject();
		/*
		 * {"method":"dashboard.getGameScore"}
		 */
		request.put("method", "dashboard.getGameScore");
		return request;
	}
}
