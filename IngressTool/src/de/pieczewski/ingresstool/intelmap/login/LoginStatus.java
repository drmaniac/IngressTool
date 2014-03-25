package de.pieczewski.ingresstool.intelmap.login;

public class LoginStatus {

	private boolean authenticated;
	private boolean canceled;
	private boolean userActionNeeded;
	
	public LoginStatus() {
		authenticated = false;
		canceled = false;
		userActionNeeded = false;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	
	@Override
	public String toString() {
		return "authenticated="+authenticated+"|canceled="+canceled;
	}

	public void setUserActionNeeded(boolean b) {
		userActionNeeded = b;	
	}
	
	public boolean isUserActionNeeded() {
		return userActionNeeded;
	}
}
