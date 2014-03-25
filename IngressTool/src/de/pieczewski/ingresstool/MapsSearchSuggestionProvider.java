package de.pieczewski.ingresstool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.Log;

public class MapsSearchSuggestionProvider extends ContentProvider {
	
	private static final String TAG = MapsSearchSuggestionProvider.class.getName();

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		Log.d(TAG, "getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d(TAG, "inster");
		return null;
	}

	@Override
	public boolean onCreate() {
		Log.d(TAG, "create");
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Log.d(TAG, "query: "+uri);
		Log.d(TAG, "projection: "+Arrays.toString(projection));
		Log.d(TAG, "selection: "+selection);
		Log.d(TAG, "selectionArgs: "+Arrays.toString(selectionArgs));
		Log.d(TAG, "sortOrder: "+sortOrder);
		
//		String[] columns = ;
		MatrixCursor cursor = new MatrixCursor(new String[]{"_id", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_INTENT_DATA}); 
		Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocationName(selectionArgs[0], 5);
			long counter = 0;
			for(Address a : addresses) { 
				Uri.Builder uriBuilder = new Uri.Builder();
				uriBuilder.scheme("content");
				uriBuilder.appendPath("search");
				uriBuilder.appendPath("intel");
				uriBuilder.appendQueryParameter("ll", a.getLatitude()+","+a.getLongitude());
				uriBuilder.appendQueryParameter("pll", a.getLatitude()+","+a.getLongitude());
				uriBuilder.appendQueryParameter("z", "15");
//				Uri intent_data = "intel?ll="+a.getLatitude()+","+a.getLongitude()+"&pll="+a.getLatitude()+","+a.getLongitude()+"&z="+13;
				Uri intent_data = uriBuilder.build();
				List<Object> ad = new ArrayList<Object>();
				ad.add(counter);	// _ID
				ad.add(a.getAddressLine(0));	// SUGGEST_COLUMN_TEXT_1
				String address = "";
				for(int i = 1; i < a.getMaxAddressLineIndex(); i++) {
					address += a.getAddressLine(i)+" ";
				}
				ad.add(address);	// SUGGEST_COLUMN_TEXT_2
				ad.add(intent_data); // SUGGEST_COLUMN_INTENT_DATA_ID
				cursor.addRow(ad);
				counter++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		Log.d(TAG, "update");
		return 0;
	}

}

