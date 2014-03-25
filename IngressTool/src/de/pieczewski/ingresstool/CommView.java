package de.pieczewski.ingresstool;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

public class CommView extends Fragment{

	public static final String ARG_COMM_LAYER = "comm_layer";
	public static final String COMM_LAYER_FULL = "comm_layer_full";
	public static final String COMM_LAYER_ALL_CHAT = "comm_layer_all_chat";
	public static final String COMM_LAYER_FACTION_CHAT = "comm_layer_faction_chat";
	public static final String COMM_LAYER_ACTION = "comm_layer_action";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.comm_view, null);
		
		MultiAutoCompleteTextView replyText = (MultiAutoCompleteTextView) view.findViewById(R.id.replyText);
		
		return view;
	}
}
