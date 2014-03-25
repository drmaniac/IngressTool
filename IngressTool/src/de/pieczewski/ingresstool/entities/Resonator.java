package de.pieczewski.ingresstool.entities;

public class Resonator implements PlayerGUIDToNick{

	private int slot;
	private int level;
	private int energyTotal;
	private int distanceToPortal;
	private String id;
	private String ownerGuid;
	private String owner;
	
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getEnergyTotal() {
		return energyTotal;
	}
	public void setEnergyTotal(int energyTotal) {
		this.energyTotal = energyTotal;
	}
	
	public int getEnergyInPercent() {
		int percent = 0;
		switch(getLevel()) {
		case 1:
			percent = energyTotal/(1000/100);
			break;
		case 2:
			percent = energyTotal/(1500/100);
			break;
		case 3:
			percent = energyTotal/(2000/100);
			break;
		case 4:
			percent = energyTotal/(2500/100);
			break;
		case 5:
			percent = energyTotal/(3000/100);
			break;
		case 6:
			percent = energyTotal/(4000/100);
			break;
		case 7:
			percent = energyTotal/(5000/100);
			break;
		case 8:
			percent = energyTotal/(6000/100);
			break;
		}
		
		return percent;
	}
	
	public int getDistanceToPortal() {
		return distanceToPortal;
	}
	public void setDistanceToPortal(int distanceToPortal) {
		this.distanceToPortal = distanceToPortal;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOwnerGuid() {
		return ownerGuid;
	}
	public void setOwnerGuid(String ownerGuid) {
		this.ownerGuid = ownerGuid;
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	@Override
	public void setPlayerNick(String nick) {
		owner = nick;
	}
	
	@Override
	public String getGUID() {
		return ownerGuid;
	}
	
	@Override
	public String getNick() {
		return owner;
	}
}
