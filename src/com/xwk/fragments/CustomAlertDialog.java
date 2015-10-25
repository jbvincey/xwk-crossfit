package com.xwk.fragments;

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

public class CustomAlertDialog extends DialogFragment {

	private CustomAlertDialog alertdialog;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		alertdialog = this;
		
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
		View view=inflater.inflate(R.layout.alertdialog, null);
		TextView title = (TextView)view.findViewById(R.id.alertdialog_title);
		title.setText(titleString);
		TextView content = (TextView)view.findViewById(R.id.alertdialog_content);
		content.setText(contentString);
		Button button = (Button)view.findViewById(R.id.alertdialog_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				alertdialog.dismiss();
			}
		});
		builder.setView(view);
		
		return builder.create();
	}
	
}
