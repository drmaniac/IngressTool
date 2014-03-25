package de.pieczewski.ingresstool.entities;


public class Mod implements PlayerGUIDToNick{

	String installingUser;
	String type;
	int mittigation;
	String displayName;
	String rarity;

	public String getInstallingUser() {
		return installingUser;
	}

	public void setInstallingUser(String installingUser) {
		this.installingUser = installingUser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMittigation() {
		return mittigation;
	}

	public void setMittigation(int mittigation) {
		this.mittigation = mittigation;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRarity() {
		return rarity;
	}

	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	
	@Override
	public void setPlayerNick(String nick) {
		
	}
	
	@Override
	public String getGUID() {
		return installingUser;
	}
	
	@Override
	public String getNick() {
		return null;
	}

}
