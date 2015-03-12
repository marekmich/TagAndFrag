package com.example.tagandfragprototype;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LogFragment extends Fragment {

	private View view;
	public static TextView logTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.log_fragment, container, false);
		logTextView = (TextView) view.findViewById(R.id.logTextView);
		return view;
	}
	
	
}
