package de.pieczewski.ingresstool.settings;

public class InventorySetting extends AbstractSetting {

	
	public final static String BURSTER_L1 = "BURSTER_L1";
	public final static String BURSTER_L2 = "BURSTER_L2";
	public final static String BURSTER_L3 = "BURSTER_L3";
	public final static String BURSTER_L4 = "BURSTER_L4";
	public final static String BURSTER_L5 = "BURSTER_L5";
	public final static String BURSTER_L6 = "BURSTER_L6";
	public final static String BURSTER_L7 = "BURSTER_L7";
	public final static String BURSTER_L8 = "BURSTER_L8";
	
	private int bursterL1;
	private int bursterL2;
	private int bursterL3;
	private int bursterL4;
	private int bursterL5;
	private int bursterL6;
	private int bursterL7;
	private int bursterL8;

	public int getBursterL1() {
		return bursterL1;
	}

	public void setBursterL1(int bursterL1) {
		this.bursterL1 = bursterL1;
	}

	public int getBursterL2() {
		return bursterL2;
	}

	public void setBursterL2(int bursterL2) {
		this.bursterL2 = bursterL2;
	}

	public int getBursterL3() {
		return bursterL3;
	}

	public void setBursterL3(int bursterL3) {
		this.bursterL3 = bursterL3;
	}

	public int getBursterL4() {
		return bursterL4;
	}

	public void setBursterL4(int bursterL4) {
		this.bursterL4 = bursterL4;
	}

	public int getBursterL5() {
		return bursterL5;
	}

	public void setBursterL5(int bursterL5) {
		this.bursterL5 = bursterL5;
	}

	public int getBursterL6() {
		return bursterL6;
	}

	public void setBursterL6(int bursterL6) {
		this.bursterL6 = bursterL6;
	}

	public int getBursterL7() {
		return bursterL7;
	}

	public void setBursterL7(int bursterL7) {
		this.bursterL7 = bursterL7;
	}

	public int getBursterL8() {
		return bursterL8;
	}

	public void setBursterL8(int bursterL8) {
		this.bursterL8 = bursterL8;
	}

	public void setBurster(int level, int count) {
		switch (level) {
		case 1:
			setBursterL1(count);
			break;
		case 2:
			setBursterL2(count);
			break;
		case 3:
			setBursterL3(count);
			break;
		case 4:
			setBursterL4(count);
			break;
		case 5:
			setBursterL5(count);
			break;
		case 6:
			setBursterL6(count);
			break;
		case 7:
			setBursterL7(count);
			break;
		case 8:
			setBursterL8(count);
			break;
		default:
			break;
		}
		isChanged = true;
	}
}
