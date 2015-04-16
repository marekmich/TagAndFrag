package com.pz.tagandfrag.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

public class ShowNewIdDialog extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getString(R.string.show_new_id_dialog_title);
		String info = String.format(getString(R.string.show_new_id_dialog_info),
									DataManager.preferences.getId());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle(title)
				.setMessage(info)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
					}
				});
		return builder.create();
	}	
}
