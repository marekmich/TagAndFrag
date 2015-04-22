package com.pz.tagandfrag.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

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
				.setPositiveButton(getString(R.string.go_to_settings), onPositiveButtonClicked())
				.setOnKeyListener(onBackPressed());
		
		return builder.create();
	}
	
	/**
	 * 
	 * @return nowy listener obs³uguj¹cy klikniêcie na button przejœcia do ustawieñ.
	 */
	private DialogInterface.OnClickListener onPositiveButtonClicked() {
		return new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (!LoginActivity.WIFI_ENABLED) {
					startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
					dialog.dismiss();
				} else {
					startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					dialog.dismiss();
				}
			}
		};
	}
	
	/**
	 * 
	 * @return nowy listener obs³uguj¹cy klikniêcie "wstecz".
	 */
	private DialogInterface.OnKeyListener onBackPressed() {
		return new DialogInterface.OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					Toast.makeText(getActivity(), getString(R.string.connections_error_back_pressed), Toast.LENGTH_LONG).show();
					return true;
				}
				return false;
			}
		};
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
