package de.pieczewski.ingresstool.settings;

import org.apache.commons.lang3.StringUtils;

public class AccountSetting extends AbstractSetting{

	public final static String ACCOUNT_NAME = "account_name";


	public static final String INTEL_TOKEN = "intel_token";
	
	
	private String accountName;
	
	private String intelAuthToken;
	
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		setAccountName(accountName, true);
	}
	
	public void setAccountName(String accountName, boolean setChanged) {
		if(StringUtils.difference(this.accountName, accountName) != null) {
			this.accountName = accountName;
			isChanged = setChanged;
		}
	}
	

	public String getIntelAuthToken() {
		return intelAuthToken;
	}
	
	public void setIntelAuthToken(String intelAuthToken, boolean setChanged){
		if(StringUtils.difference(this.intelAuthToken, intelAuthToken) != null) {
			this.intelAuthToken = intelAuthToken;
			isChanged = setChanged;
		}
	}
	public void setIntelAuthToken(String intelAuthToken2) {
		setIntelAuthToken(intelAuthToken2, true);
	}
	
}
