package de.pieczewski.ingresstool.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import de.pieczewski.ingresstool.R;
import de.pieczewski.ingresstool.entities.Team;

public class PortalImages {

	public static BitmapDescriptor getPortalImage(int level, Team team ) {
		BitmapDescriptor bitmap = null;
		switch (team) {
		case ALIENS:
			switch (level) {
			case 0:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l0);
				break;
			case 1:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l1);
				break;
			case 2:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l2);
				break;
			case 3:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l3);
				break;
			case 4:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l4);
				break;
			case 5:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l5);
				break;
			case 6:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l6);
				break;
			case 7:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l7);
				break;
			case 8:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.e_portal_l8);
				break;
			}
			break;
			
		case RESISTANCE:
			switch (level) {
			case 0:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l0);
				break;
			case 1:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l1);
				break;
			case 2:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l2);
				break;
			case 3:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l3);
				break;
			case 4:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l4);
				break;
			case 5:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l5);
				break;
			case 6:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l6);
				break;
			case 7:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l7);
				break;
			case 8:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.r_portal_l8);
				break;
			}
			break;
			
			case NEUTRAL:
				bitmap = BitmapDescriptorFactory.fromResource(R.drawable.n_portal_l0);
		}
		
		
		return bitmap;
	}
	
	public static Bitmap getPortalImageBitmap(Resources res, int level, Team team ) {
		Bitmap bitmap = null;
		switch (team) {
		case ALIENS:
			switch (level) {
			case 0:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l0);
				break;
			case 1:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l1);
				break;
			case 2:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l2);
				break;
			case 3:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l3);
				break;
			case 4:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l4);
				break;
			case 5:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l5);
				break;
			case 6:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l6);
				break;
			case 7:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l7);
				break;
			case 8:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l8);
				break;
			default:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.e_portal_l0);
				break;
			}
			break;
			
		case RESISTANCE:
			switch (level) {
			case 0:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l0);
				break;
			case 1:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l1);
				break;
			case 2:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l2);
				break;
			case 3:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l3);
				break;
			case 4:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l4);
				break;
			case 5:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l5);
				break;
			case 6:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l6);
				break;
			case 7:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l7);
				break;
			case 8:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l8);
				break;
			default:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.r_portal_l0);
				break;
			}
			break;
			default:
				bitmap = BitmapFactory.decodeResource(res, R.drawable.ic_launcher);
				break;
		}
		
		return bitmap;
	}
}
