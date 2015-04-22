package com.pz.tagandfrag.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.activity.LoginActivity;

/**
 * Okienko obslugujace braki w polaczaniach niezbednych do dzialania.
 * @author Mateusz Wrzos
 *
 */
public class ConnectionsErrorDialog extends DialogFragment {

	/**
	 * Tworzy dialog z informacja co jest wylaczone.
	 * Przekierowuje do ustawieñ.
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getString(R.string.connections_error_title);
		String info = String.format(getString(R.string.connections_error_info), whatIsDiasabled());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle(title)
				.setMessage(info)
				.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!LoginActivity.WIFI_ENABLED) {
							startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
						} else {
							startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
						
					}
				});
		return builder.create();
	}

	/**
	 * 
	 * @return napis z informacjami o wylaczonych modulach.
	 */
	private String whatIsDiasabled() {
		String whatIsDisabled = "";
		if (!LoginActivity.WIFI_ENABLED) {
			whatIsDisabled += "Wi-Fi\n";
		}
		
		if (!LoginActivity.GPS_ENABLED) {
			whatIsDisabled += "GPS\n";
		}
		return whatIsDisabled;
	}
}
