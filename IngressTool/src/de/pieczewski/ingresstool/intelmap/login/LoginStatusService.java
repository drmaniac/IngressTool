package de.pieczewski.ingresstool.intelmap.login;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.List;
import java.util.regex.MatchResult;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import de.pieczewski.ingresstool.IngressToolCallbackI;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.Ingress;
import de.pieczewski.ingresstool.util.IngressRPCUtil;
import de.pieczewski.ingresstool.util.IngressToolInformations;
import de.pieczewski.ingresstool.util.StringUtils;

public class LoginStatusService extends AsyncTask<Void, Void, LoginStatus> {

	private static final String TAG = LoginStatusService.class.getName();

	IngressToolCallbackI callback;
	LoginStatus loginStatus;

	public LoginStatusService(IngressToolCallbackI callback, LoginStatus loginStatus) {
		this.callback = callback;
		this.loginStatus = loginStatus;
	}

	@Override
	protected LoginStatus doInBackground(Void... arg0) {
		LoginStatus loginStatus = new LoginStatus();
		loginStatus.setAuthenticated(false);

		DefaultHttpClient client = IngressRPCUtil.getClient();
		HttpPost method = IngressRPCUtil.getMethodPost(Ingress.INTEL_MAP_URL_SSL);
		try {
			HttpResponse res = client.execute(method);
			String httpResult = IOUtils.convertInputStream(res.getEntity().getContent());
			Log.v(TAG, httpResult);

			List<MatchResult> playerStatsResults = StringUtils.findAll(httpResult, "PLAYER(.*?);");
			if(playerStatsResults.size() > 0) {
				Log.i(TAG,"authentication sucessufully");
				loginStatus.setAuthenticated(true);
			}	
		} catch (ClientProtocolException e) {
			Log.e(TAG, "get login status failed", e);
		} catch (IOException e) {
			Log.e(TAG, "get login status failed", e);
		} catch (Exception e) {
			Log.e(TAG, "get login status failed", e);
		}
		
		return loginStatus;
	}

	@Override
	protected void onPostExecute(LoginStatus result) {
		super.onPostExecute(result);
		this.loginStatus = result;
		callback.onCallback(LoginCodes.REQUEST_LOGIN_STATUS, result);
	}

}
