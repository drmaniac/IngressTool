package de.pieczewski.ingresstool.util;

public class IngressColor {

	public static int LEVEL1 = 0xfffece5a;
	public static int LEVEL2 = 0xffffa630;
	public static int LEVEL3 = 0xffff7315;
	public static int LEVEL4 = 0xffe40000;
	public static int LEVEL5 = 0xfffd2992;
	public static int LEVEL6 = 0xffeb26cd;
	public static int LEVEL7 = 0xffc124e0;
	public static int LEVEL8 = 0xff9627f4;
	
	public static int PS_COMMON		= 0xff9dfd57;
	public static int PS_RARE	 	= 0xffde9ad2;
	public static int PS_VARY_RARE 	= 0xffea66d3;
	
	public static int ALIEN_COLOR = 0xff28f428;
	public static int RESISTANT_COLOR = 0xff00c2ff;

	public static int getLevelColor(int level) {
		int color = 0xff000000;
		switch (level) {
		case 1:
			color = LEVEL1;
			break;
		case 2:
			color = LEVEL2;
			break;
		case 3:
			color = LEVEL3;
			break;
		case 4:
			color = LEVEL4;
			break;
		case 5:
			color = LEVEL5;
			break;
		case 6:
			color = LEVEL6;
			break;
		case 7:
			color = LEVEL7;
			break;
		case 8:
			color = LEVEL8;
			break;
		default:
			break;
		}
		return color;
	}
}
