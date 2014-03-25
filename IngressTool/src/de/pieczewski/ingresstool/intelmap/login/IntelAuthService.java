package de.pieczewski.ingresstool.intelmap.login;

import java.io.IOException;
import java.util.List;
import java.util.regex.MatchResult;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import de.pieczewski.ingresstool.IngressToolCallbackI;
import de.pieczewski.ingresstool.exceptions.IngressLoginException;
import de.pieczewski.ingresstool.util.ErrorToastHandler;
import de.pieczewski.ingresstool.util.IOUtils;
import de.pieczewski.ingresstool.util.Ingress;
import de.pieczewski.ingresstool.util.IngressAuthStore;
import de.pieczewski.ingresstool.util.IngressAuthStore.AUTH_STATUS;
import de.pieczewski.ingresstool.util.StringUtils;

public class IntelAuthService extends AsyncTask<String, Void, LoginStatus> {

	private static final String TAG = IntelAuthService.class.getName();

	private IngressToolCallbackI callback;
	private Activity activity;
	private ErrorToastHandler errorToastHandler;

	public IntelAuthService(IngressToolCallbackI callback, Activity activity) {
		this.callback = callback;
		this.activity = activity;
		errorToastHandler = new ErrorToastHandler(activity);
	}

	@Override
	protected LoginStatus doInBackground(String... params) {
		LoginStatus loginStatus = new LoginStatus();
		try {
			String account_name = params[0];
			Log.v(TAG, "account_name " + account_name);

			String googletoken = "&auth=" + getGoogleAuthToken(account_name);
			
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpProtocolParams
					.setUserAgent(
							httpClient.getParams(),
							"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
			httpClient.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
			String uri = Ingress.GOOGLE_APP_ENGINE_LOGIN_URL + googletoken;
			
			Log.v(TAG,String.format("auth URI: %s",uri));
			HttpGet method = new HttpGet(uri);
			
			HttpResponse res = httpClient.execute(method);
			StatusLine statusLine = res.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			FollowMovePagesResult movePagesResult = followMovedPages(res, statusCode);

			for (Cookie cookie : movePagesResult.httpClient.getCookieStore().getCookies()) {
				if (IngressAuthStore.AUTH_COOKIE_NAME.equals(cookie.getName())) {
					Log.d(TAG, String.format("joined the dark side with cookie %s=%s)",
							IngressAuthStore.AUTH_COOKIE_NAME, cookie.getValue()));
					setIntelCSRFCookie(cookie);
					IngressAuthStore.setStatus(AUTH_STATUS.AUTHENTICATED);
					loginStatus.setAuthenticated(true);
					return loginStatus;
				} else {
					Log.v(TAG, "cookie "+cookie.getName());
					IngressAuthStore.setStatus(AUTH_STATUS.USER_ACTION_NEEDED);
					IngressAuthStore.setUserActionURL(movePagesResult.uri);
					loginStatus.setAuthenticated(false);
					loginStatus.setUserActionNeeded(true);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			errorToastHandler.sendEmptyMessage(ErrorToastHandler.NO_ACCOUNT_NAME_SET);
			Log.e(TAG, "paramter is missing", e);
		} catch (IngressLoginException e) {
			errorToastHandler.sendEmptyMessage(ErrorToastHandler.LOGIN_FAILED);
			Log.e(TAG, "ingress login exception", e);
			loginStatus.setAuthenticated(false);
		} catch (ClientProtocolException e) {
			errorToastHandler.sendEmptyMessage(ErrorToastHandler.NETWORK_FAILED);
			Log.e(TAG, "client protocol exception", e);
			loginStatus.setAuthenticated(false);
		} catch (IOException e) {
			errorToastHandler.sendEmptyMessage(ErrorToastHandler.NETWORK_FAILED);
			Log.e(TAG, "ingress login exception", e);
			loginStatus.setAuthenticated(false);
		}
		return loginStatus;
	}
	
	
	private void setIntelCSRFCookie(Cookie intelCookie) throws IOException {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams
				.setUserAgent(
						httpClient.getParams(),
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
		httpClient.getCookieStore().addCookie(intelCookie);
		HttpGet method = new HttpGet(Ingress.INTEL_MAP_URL_SSL);
		HttpResponse res = httpClient.execute(method);

		for (Cookie cookie : httpClient.getCookieStore().getCookies()) {
			if (cookie.getName().equals("csrftoken")) {
				IngressAuthStore.setCSFRCookie(cookie);
				Log.v(TAG, "xcsfr_toke:"+cookie.getValue());
				IngressAuthStore.setXCSRFToken(cookie.getValue());
			}
		}

		String site = IOUtils.convertInputStream(res.getEntity().getContent());
		Log.v(TAG, "Intel Site: "+site);

		List<MatchResult> matches = StringUtils.findAll(site,
				"name='csrfmiddlewaretoken'.*?value='(.*?)'");
		for (MatchResult r : matches) {
			String XCSRFToken = r.group().substring(34, 66);
			Log.v(TAG, "XCSRF: "+XCSRFToken);
//			authStore.setXCSRFToken(XCSRFToken);
		}
		IngressAuthStore.setCookieStore(httpClient.getCookieStore());
	}
	
	private class FollowMovePagesResult {
		public DefaultHttpClient httpClient;
		public String uri;
	}
	
	private FollowMovePagesResult followMovedPages(HttpResponse res, int statusCode) throws IllegalStateException, IOException {
		int tries= 0;
		String uri = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams
				.setUserAgent(
						httpClient.getParams(),
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");
		Header[] headers = res.getHeaders("Set-Cookie");
		
		while(statusCode == 302 && tries < 5 && headers.length == 0) {
			String site1 = IOUtils.convertInputStream(res.getEntity().getContent());
			List<MatchResult> results1 = StringUtils.findAll(site1, "\"https://(.*?)\"");
			Log.v(TAG,"SITE_1:"+site1);
			for (MatchResult r : results1) {
				uri = r.group().replaceAll("\"", "");
			}
			HttpGet method = new HttpGet(uri);
			res = httpClient.execute(method);
			StatusLine statusLine = res.getStatusLine();
			statusCode = statusLine.getStatusCode();
			Log.v(TAG,"folow symlink "+statusCode+" "+uri);
			headers = res.getHeaders("Set-Cookie");
			tries++;
		}
		FollowMovePagesResult followMovePagesResult = new FollowMovePagesResult();
		followMovePagesResult.httpClient = httpClient;
		followMovePagesResult.uri = uri;
		return followMovePagesResult;
	}

	private String getGoogleAuthToken(String account_name) throws IngressLoginException {
		Log.v(TAG, "try to get the google auth token for " + account_name);
		String gooleToken = null;
		try {

			gooleToken = GoogleAuthUtil.getToken(activity, account_name, Ingress.GOOGLE_AUTH_TOKEN_SCOPE);
			GoogleAuthUtil.invalidateToken(activity, gooleToken);
			gooleToken = GoogleAuthUtil.getToken(activity, account_name, Ingress.GOOGLE_AUTH_TOKEN_SCOPE);

		} catch (final GooglePlayServicesAvailabilityException googlePlayServicesAvailabilityException) {
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Dialog alert = GooglePlayServicesUtil.getErrorDialog(
							googlePlayServicesAvailabilityException.getConnectionStatusCode(), activity,
							LoginCodes.REQUEST_GOOGLE_AUTH_TOKEN);
					alert.show();					
				}
			});
		} catch (UserRecoverableAuthException userRecoverableAuthException) {
			Log.v(TAG, "interaction needed to login");
			activity.startActivityForResult(userRecoverableAuthException.getIntent(), LoginCodes.REQUEST_GOOGLE_AUTH_TOKEN);
		} catch (IOException ioException) {
			errorToastHandler.sendEmptyMessage(ErrorToastHandler.NETWORK_FAILED);
			Log.e(TAG, ioException.getMessage(), ioException);
			throw new IngressLoginException(ioException);
		} catch (GoogleAuthException googleAuthException) {
			Log.e(TAG, googleAuthException.getMessage(), googleAuthException);
			throw new IngressLoginException(googleAuthException);
		}
		return gooleToken;
	}

	@Override
	protected void onPostExecute(LoginStatus result) {
		super.onPostExecute(result);
		callback.onCallback(LoginCodes.REQUEST_AUTH_SERVICE, result);
	}
}
