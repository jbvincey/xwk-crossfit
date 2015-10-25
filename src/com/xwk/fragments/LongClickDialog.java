package com.xwk.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.model.Ids;

public class LongClickDialog extends DialogFragment {

	public final static String SET_TITLE = "set_title";
	public final static String SET_TITLE_CONFIRM = "set_title_confirm";
	public final static String SET_CONTENT_CONFIRM = "set_content_confirm";
	
	public enum decision {
		MODIFY,
		DELETE
	}
	
	private String titleConfirm = "";
	private String contentConfirm = "";
	private LongClickDialog dialog;

	private DialogActivity listenerActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			listenerActivity = (DialogActivity) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement RestActivity");
		}
	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		String titleString = "";
		
		Bundle args = getArguments();
		if(args!=null && args.containsKey(SET_TITLE)) {
			titleString = args.getString(SET_TITLE);
		}
		if(args!=null && args.containsKey(SET_TITLE_CONFIRM)) {
			titleConfirm = args.getString(SET_TITLE_CONFIRM);
		}
		if(args!=null && args.containsKey(SET_CONTENT_CONFIRM)) {
			contentConfirm = args.getString(SET_CONTENT_CONFIRM);
		}
		
		dialog = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(listenerActivity);
		LayoutInflater inflater = listenerActivity.getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout

		View dialogView = inflater.inflate(R.layout.longclickdialog_fragment, null);
		TextView title  = (TextView) dialogView.findViewById(R.id.longclickdialog_fragment_title);
		title.setText(titleString);
		
		TextView modify = (TextView) dialogView.findViewById(R.id.longclickdialog_modify);
		modify.setClickable(true);
		modify.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listenerActivity.onDecisionTaken(decision.MODIFY);
				dialog.dismiss();
			}
		});

		TextView delete = (TextView) dialogView.findViewById(R.id.longclickdialog_delete);
		delete.setClickable(true);
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CustomAlertDialogDoubleButton alertDialog = new CustomAlertDialogDoubleButton();
				Bundle args = new Bundle();
				args.putString(Ids.ALERTDIALOG_TITLE, titleConfirm);
				args.putString(Ids.ALERTDIALOG_CONTENT, contentConfirm);
				alertDialog.setArguments(args);
				alertDialog.show(getActivity().getFragmentManager(), "alertdialog");
				dialog.dismiss();
				
			}
		});		

		builder.setView(dialogView);
		return builder.create();
		/*final CharSequence[] items = {"Modifier", "Supprimer"};

		Bundle args = getArguments();
		if(args!=null && args.containsKey(SET_TITLE)) {
			builder.setTitle(args.getString(SET_TITLE));
		}
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {

				Bundle args = getArguments();
				if(args!=null && args.containsKey(SET_TITLE_CONFIRM)) {
					titleConfirm = args.getString(SET_TITLE_CONFIRM);
				}
				if(args!=null && args.containsKey(SET_CONTENT_CONFIRM)) {
					contentConfirm = args.getString(SET_CONTENT_CONFIRM);
				}


				switch(item){
				case 1: 
					new AlertDialog.Builder(getActivity())
					.setTitle(titleConfirm)
					.setMessage(contentConfirm)
					.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setPositiveButton("ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							listenerActivity.onDecisionTaken(decision.DELETE);
							dialog.dismiss();
						}
					})
					.show();
					break;

				case 0: 
					listenerActivity.onDecisionTaken(decision.MODIFY);
					break;

				}
			}
		});
		AlertDialog alert = builder.create();
		return alert;*/
	}
}