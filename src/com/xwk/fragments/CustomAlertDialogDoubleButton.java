package com.xwk.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.model.Ids;

public class CustomAlertDialogDoubleButton extends DialogFragment {

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
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		final CustomAlertDialogDoubleButton alertdialog = this;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		String titleString = "";
		String contentString = "";
		
		Bundle args = getArguments();
		if(args.containsKey(Ids.ALERTDIALOG_TITLE)) {
			titleString = args.getString(Ids.ALERTDIALOG_TITLE);
		}
		if(args.containsKey(Ids.ALERTDIALOG_CONTENT)) {
			contentString = args.getString(Ids.ALERTDIALOG_CONTENT);
		}
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view=inflater.inflate(R.layout.alertdialog_doublebutton, null);
		TextView title = (TextView)view.findViewById(R.id.alertdialog_boublebutton_title);
		title.setText(titleString);
		TextView content = (TextView)view.findViewById(R.id.alertdialog_boublebutton_content);
		content.setText(contentString);
		Button button1 = (Button)view.findViewById(R.id.alertdialog_doublebutton_button1);
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listenerActivity.onConfirmOrCancel(true);
				alertdialog.dismiss();
			}
		});
		Button button2 = (Button)view.findViewById(R.id.alertdialog_doublebutton_button2);
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertdialog.dismiss();
			}
		});
		builder.setView(view);
		
		return builder.create();
	}
	
}
