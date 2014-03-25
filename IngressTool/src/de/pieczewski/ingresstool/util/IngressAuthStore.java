package de.pieczewski.ingresstool.util;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;

public class IngressAuthStore {

	// private static Cookie intelCookie;
	private static Cookie CSFRCookie;
	private static String XCSRFToken;
	private static CookieStore cookieStore;
	private static String userActionURL;
	
	public final static String AUTH_COOKIE_NAME = "SACSID";

	public enum AUTH_STATUS {
		AUTHENTICATED, CANCELLED, FAILED, USER_ACTION_NEEDED
	}

	private static AUTH_STATUS status;

	private static String authStatusMsg = "";
	
	private static IngressAuthStore ingressAuthStore;

	private IngressAuthStore() {
		status = AUTH_STATUS.CANCELLED;
		ingressAuthStore = this;
	}
	
	public static IngressAuthStore getInstance() {
		return ingressAuthStore == null ? new IngressAuthStore() : ingressAuthStore;
	}

	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(CookieStore cookieStore) {
		IngressAuthStore.cookieStore = cookieStore;
	}

	public static Cookie getCSFRCookie() {
		return CSFRCookie;
	}

	public static void setCSFRCookie(Cookie cSFRCookie) {
		IngressAuthStore.CSFRCookie = cSFRCookie;
	}

//	public static Cookie getIntelCookie() {
//		return intelCookie;
//	}

//	public static void setIntelCookie(Cookie intelCookie) {
//		IngressAuthStore.intelCookie = intelCookie;
//	}

	public static String getXCSRFToken() {
		return XCSRFToken;
	}

	public static void setXCSRFToken(String xCSRFToken) {
		IngressAuthStore.XCSRFToken = xCSRFToken;
	}

	public static boolean isAuthenticated() {
		return IngressAuthStore.status.equals(AUTH_STATUS.AUTHENTICATED) ? true : false;
	}

	public static void setStatus(AUTH_STATUS status) {
		IngressAuthStore.status = status;
	}

	public static AUTH_STATUS getStatus() {
		return status;
	}

	public static void setAuthStatusMsg(String authStatusMsg) {
		IngressAuthStore.authStatusMsg = authStatusMsg;
	}

	public static  String getAuthStatusMsg() {
		return authStatusMsg;
	}
	
	public static String getUserActionURL() {
		return userActionURL;
	}
	public static void setUserActionURL(String userActionURL) {
		IngressAuthStore.userActionURL = userActionURL;
	}
}
