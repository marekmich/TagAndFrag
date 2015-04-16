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
		String tittle = getString(R.string.show_new_id_dialog_title);
		String info = getString(R.string.show_new_id_dialog_info);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder	.setTitle("Tw�j unikatowy numer ID")
				.setMessage("Wygl�da na to �e jeszcze nigdy nie gra�e�.\n\n"
							+ "Tw�j nowy, unikatowy numer ID to: " + DataManager.preferences.getId() + "\n")
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						new ChooseWeaponDialog().show(getFragmentManager(), "DEV");
					}
				});
		return builder.create();
	}	
}
