package com.pz.tagandfrag.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.pz.tagandfrag.R;
import com.pz.tagandfrag.managers.DataManager;

/**
 * Okienko informuj¹ce nowego uzytkonika o nadaniu nowego numeru ID.
 * @author Mateusz Wrzos
 *
 */
public class ShowNewIdDialog extends DialogFragment {

	/**
	 * Tworzy dialog z nowym ID pobranym z DataManagera.
	 * Po zaakceptowaniu, pokazuje okienko z wyborem broni (modu³u bluetooth).
	 * @see DataManager
	 * @see ChooseWeaponDialog
	 */
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
