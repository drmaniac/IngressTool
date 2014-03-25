package de.pieczewski.ingresstool.util;

public class QuadKeyHelper {
	public static double getLong(int inX, int zoom) {
		double x = Math.floor(inX * 256);
		double longitude = ((x * 360) / (256 * Math.pow(2, zoom))) - 180;
		while (longitude > 180.0) {
			longitude -= 360.0;
		}
		while (longitude < -180.0) {
			longitude += 360.0;
		}
		return longitude;

	}

	public static double getLat(int inY, int zoom) {
		double y = Math.floor(inY * 256);
		double efactor = Math.exp((0.5 - y / 256 / Math.pow(2, zoom)) * 4 * Math.PI);
		double latitude = Math.asin((efactor - 1) / (efactor + 1)) * 180 / Math.PI;
		if (latitude < -90.0) {
			latitude = -90.0;
		} else if (latitude > 90.0) {
			latitude = 90.0;
		}
		return latitude;
	}
	
	public static String quadKey(int x, int y, final int zoom) {
		final StringBuilder quadKey = new StringBuilder();
		for (int i = zoom; i > 0; i--) {
			char digit = '0';
			final int mask = 1 << (i - 1);
			if ((x & mask) != 0) {
				digit++;
			}
			if ((y & mask) != 0) {
				digit++;
				digit++;
			}
			quadKey.append(digit);
		}
		if (quadKey.length() > 1) {
			quadKey.insert(0, "0");
		}
		return quadKey.toString();
	}
}
