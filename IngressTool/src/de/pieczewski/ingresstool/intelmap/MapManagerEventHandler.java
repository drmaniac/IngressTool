package de.pieczewski.ingresstool.intelmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import android.os.Handler;
import android.os.Message;
import android.text.Spannable;

public class MapManagerEventHandler extends Handler {

	// CALLBACK
	private MapManager mapManager;
	private static long load_data_counter = 0;

	public MapManagerEventHandler() {
		mapManager = MapManager.getInstance();
	}

	public void handleMessage(Message msg) {
		switch (msg.what) {
		case MapManagerEvents.MOVE_TO_LAST_KNOWN_POSITION:
			mapManager.moveToLastKnownPosition();
			break;

		case MapManagerEvents.MOVE_TO_LOCATION:
			LatLng location = (LatLng) msg.obj;
			mapManager.moveTo(location, 0f);
			break;

		case MapManagerEvents.DRAW_TILE:
			GameEntityTile gameEntityTile = (GameEntityTile) msg.obj;
			mapManager.handleDrawTile(gameEntityTile);
			break;

		case MapManagerEvents.REMOVE_CONTROLLFIELD:
			Polygon polygon = (Polygon) msg.obj;
			polygon.remove();
			break;
		case MapManagerEvents.REMOVE_EDGE:
			Polyline polyline = (Polyline) msg.obj;
			polyline.remove();
			break;
		case MapManagerEvents.REMOVE_TURRET:
			Marker marker = (Marker) msg.obj;
			marker.remove();
			break;
			
		case MapManagerEvents.UPDATE_GAME_SCORE:
			Spannable gameScoreSpannable = (Spannable) msg.obj;
			mapManager.getManagedMapActivity().updateGameScore(gameScoreSpannable);
			break;
			
		case MapManagerEvents.UPDATE_PLAYER_STAT:
			Spannable playerStatSpannable = (Spannable) msg.obj;
			mapManager.getManagedMapActivity().updatePlayerStat(playerStatSpannable);
			break;
			
		case MapManagerEvents.LOAD_DATA:
			int level = msg.arg1;
			load_data_counter = (Long) msg.obj;
			String msg_text = "";
			switch (level) {
			case 0:
				msg_text = mapManager.getManagedMapActivity().getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_all_portals);
				break;
				
			case 1:
				msg_text = mapManager.getManagedMapActivity().getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L1_L8_portals);
				break;
			
			case 2:
				msg_text = mapManager.getManagedMapActivity().getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L2_L8_portals);
				break;
				
			case 3:
				msg_text = mapManager.getManagedMapActivity().getContext().getResources().getString(de.pieczewski.ingresstool.R.string.show_L3_L8_portals);
				break;

			default:
				break;
			}
			mapManager.getManagedMapActivity().setShowPortals(msg_text);
			break;
		
		case MapManagerEvents.STOP_LOAD_DATA:
			long stop_data_counter = (Long) msg.obj;
			if(stop_data_counter >= load_data_counter) {
				mapManager.getManagedMapActivity().hideShowPortals();
			}
			break;
			
		default:
			break;
		}
	};
}
