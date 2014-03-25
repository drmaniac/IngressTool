package de.pieczewski.ingresstool.util;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;

import android.util.Log;

public class IngressRPCUtil {
	
	public static final String INGRESS_INTEL_MAP_URL = "https://www.ingress.com/intel";

	public static DefaultHttpClient getClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpProtocolParams
				.setUserAgent(
						httpClient.getParams(),
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1312.57 Safari/537.17");

		httpClient.setCookieStore(IngressAuthStore.getCookieStore());

		return httpClient;
	}
	
	public static HttpPost getMethodPost(String url) {
		HttpPost method = new HttpPost(url);
		Log.d("", "XCFR: "+IngressAuthStore.getXCSRFToken());
		Log.d("", "Referer: "+INGRESS_INTEL_MAP_URL);
		method.addHeader("x-csrftoken",IngressAuthStore.getXCSRFToken());
		method.addHeader("Referer",INGRESS_INTEL_MAP_URL);
		method.addHeader("origin", "https://www.ingress.com");
		method.addHeader("Connection","keep-alive");
		method.addHeader("x-requested-with","XMLHttpRequest");
		return method;
	}
}
