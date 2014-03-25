package de.pieczewski.ingresstool.intelmap.login;

public class LoginCodes {

	// CALLBACK REQUEST CODES
	public static final int REQUEST_LOGIN_STATUS = 1000;
	public static final int REQUEST_AUTH_SERVICE = 1001;

	// INTENT REQUEST CODES
	public static final int REQUEST_CODE_PICK_ACCOUNT = 2000;
	public static final int REQUEST_LOGIN_ACTIVITY = 2001;
	public static final int REQUEST_GOOGLE_AUTH_TOKEN = 2002;

	// LOGIN ACTIVITY RESULT CODES
	public static final int LOGIN_ACT_CANCELED = 3001;
	public static final int LOGIN_ACT_FAILED = 3002;
	public static final int LOGIN_ACT_SUCCESS = 3003;

	// LOGIN INTENT EXTRAS NAME
	public static final String LOGIN_ACTIVITY_AUTH_STATUS = "auth_status";
	public static final String LOGIN_ACTIVITY_AUTH_CANCELLED = "auth_cancelled";
	public static final String LOGIN_ACTIVITY_AUTH_ACCOUNT = "auth_account_name";
	public static final String LOGIN_ACTIVITY_AUTOLOGIN = "auth_autologin";
}
