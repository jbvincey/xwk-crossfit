package com.xwk.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.misc.ResizableImageView;
import com.xwk.model.Ids;

public class ExerciseDetailsFragment extends DialogFragment {

	private ExerciseDetailsFragment dialog;
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
					+ " must implement DialogActivity");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){

		Bundle args = getArguments();
		dialog = this;
		String name = "name";
		byte[] typeBytes = null;
		String description = "description";
		byte[] picBytes = null;
		boolean picker = false;

		if(args.containsKey(Ids.EXERCISE_NAME)) {
			name = args.getString(Ids.EXERCISE_NAME);
		}

		if(args.containsKey(Ids.EXERCISE_DESCRIPTION)) {
			description = args.getString(Ids.EXERCISE_DESCRIPTION);
		}
		if(args.containsKey(Ids.ALERTDIALOG_PICKER)) {
			picker = args.getBoolean(Ids.ALERTDIALOG_PICKER);
		}
		Bitmap bmpType = null;
		if(args.containsKey(Ids.EXERCISE_TYPE)) {
			typeBytes = args.getByteArray(Ids.EXERCISE_TYPE);
			if(typeBytes!=null && typeBytes.length>0) {
				bmpType = BitmapFactory.decodeByteArray(typeBytes, 0, typeBytes.length);
			}
			else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				bmpType = BitmapFactory.decodeResource(this.getResources(), R.drawable.legsmedium5white, options);
			}
		}

		Bitmap bmpPic = null;
		if(args.containsKey(Ids.EXERCISE_PIC)) {
			picBytes = args.getByteArray(Ids.EXERCISE_PIC);
			if(picBytes!=null && picBytes.length>0) {
				bmpPic = BitmapFactory.decodeByteArray(picBytes, 0, picBytes.length);
			}
			else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				bmpPic = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
			}
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		View dialogView = null;
		if(picker) {
			dialogView = inflater.inflate(R.layout.exercise_details_fragment_picker, null);
		}
		else {
			dialogView = inflater.inflate(R.layout.exercise_details_fragment, null);
		}
		TextView titleView = (TextView) dialogView.findViewById(R.id.exercise_details_fragment_title);
		titleView.setText(name);
		TextView descriptionView = (TextView) dialogView.findViewById(R.id.exercise_details_fragment_description);
		descriptionView.setText(description);
		ImageView typePic = (ImageView) dialogView.findViewById(R.id.exercise_details_fragment_image);
		typePic.setImageBitmap(bmpType);
		//ResizableImageView pic = (ResizableImageView) dialogView.findViewById(R.id.exercise_details_fragment_pic);
		ResizableImageView pic = (ResizableImageView) dialogView.findViewById(R.id.exercise_details_fragment_pic);
		pic.setImageBitmap(bmpPic);

		Button button = (Button)dialogView.findViewById(R.id.exercise_details_fragment_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				listenerActivity.onExerciseChosen();
				dialog.dismiss();
			}
		});
		if(picker) {
			Button button2 = (Button)dialogView.findViewById(R.id.exercise_details_fragment_button_cancel);
			button2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					dialog.dismiss();
				}
			});
		}
		builder.setView(dialogView);
		return builder.create();
	}
}
