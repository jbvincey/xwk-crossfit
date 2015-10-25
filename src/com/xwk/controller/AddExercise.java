package com.xwk.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jbvincey.xwk.R;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialog;
import com.xwk.fragments.ExerciseTypeFragment;
import com.xwk.misc.RoundedDrawable;
import com.xwk.model.Exercise;
import com.xwk.model.Exercise.ExerciseType;
import com.xwk.model.Ids;

public class AddExercise extends Activity implements OnClickListener {

	private final int SELECT_PHOTO = 1;

	private ExerciseDAO exerciseDAO;

	private EditText exerciseName;
	private EditText exerciseDescription;
	private LinearLayout exerciseType;
	private ImageView exerciseImage;
	private ImageView exerciseTypeImage;

	private Exercise exercise;

	private boolean modifyMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.add_exercise);

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		exerciseName = (EditText) findViewById(R.id.exercise_activity_name);
		exerciseDescription = (EditText) findViewById(R.id.exercise_activity_description);
		exerciseType = (LinearLayout) findViewById(R.id.exercise_activity_type);
		exerciseType.setOnClickListener(this);
		exerciseTypeImage = (ImageView) findViewById(R.id.exercise_activity_type_image);
		
		exerciseImage = (ImageView) findViewById(R.id.exercise_activity_exerciseimage);
		exerciseImage.setOnClickListener(this);

		exercise = new Exercise();
		exercise.setType(ExerciseType.legs);
		this.getActionBar().setTitle("  Add new exercise");

		Bundle bundle = getIntent().getExtras();
		if(bundle != null && 
				bundle.containsKey(Ids.FORM_MODE_MODIFY) && 
				bundle.getBoolean(Ids.FORM_MODE_MODIFY) &&
				bundle.containsKey(Ids.EXERCISE_ID)) {
			exercise = exerciseDAO.getExercise(bundle.getLong(Ids.EXERCISE_ID));
			modifyMode = true;
			this.getActionBar().setTitle("  Edit exercise");
			setupView();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		exerciseDAO.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		exerciseDAO.close();
	}

	private void setupView() {
		if(exercise!=null) {
			exerciseName.setText(exercise.getName());
			exerciseDescription.setText(exercise.getDescription());
			exerciseTypeImage.setImageBitmap(exercise.getTypeImageWhite(this));
			
			byte[] byteArray = exercise.getImage();
			Bitmap bmp = null;

			if(byteArray!=null && byteArray.length>0) {
				bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			}
			else {
				BitmapFactory.Options options = new BitmapFactory.Options();
				bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.nopicture, options);
			}
			RoundedDrawable picture = new RoundedDrawable(bmp);
			exerciseImage.setImageDrawable(picture);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_exercise, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
		} else if (id == R.id.check_add_exercise) {
			String name = exerciseName.getText().toString();
			String description = exerciseDescription.getText().toString();
			if(!name.isEmpty() && !description.isEmpty()) {
				exercise.setName(name);
				exercise.setDescription(description);
				if(modifyMode) {
					exerciseDAO.updateExercise(exercise);
				}
				else {
					exerciseDAO.addExercise(exercise);
				}
				finish();
			}
			else {
				CustomAlertDialog alertDialog = new CustomAlertDialog();
				Bundle args = new Bundle();
				args.putString(Ids.ALERTDIALOG_TITLE, "New exercise");
				args.putString(Ids.ALERTDIALOG_CONTENT, "You have to fill in all fields.");
				alertDialog.setArguments(args);
				alertDialog.show(getFragmentManager(), "alertdialog");

			}
		}
		return super.onOptionsItemSelected(item);
	}

	public void onExerciseTypeChosen(ExerciseType type) {
		exercise.setType(type);
		exerciseTypeImage.setImageBitmap(exercise.getTypeImageWhite(this));
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == R.id.exercise_activity_type) {
			ExerciseTypeFragment exerciseTypeFragment = new ExerciseTypeFragment();
			exerciseTypeFragment.show(getFragmentManager(), null);
		}

		if(id == R.id.exercise_activity_exerciseimage) {
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_PHOTO);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		switch(requestCode) { 
		case SELECT_PHOTO:
			if(resultCode == RESULT_OK){
				try {
					Uri imageUri = imageReturnedIntent.getData();
					InputStream imageStream = getContentResolver().openInputStream(imageUri);
					Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
					Bitmap resizedBitmap = getResizedBitmap(selectedImage, 384, 512);

					RoundedDrawable picture = new RoundedDrawable(resizedBitmap);
					exerciseImage.setImageDrawable(picture);

					ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
					resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream1);
					byte[] byteArray1 = stream1.toByteArray();
					try {
						stream1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					exercise.setImage(byteArray1);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

			}
		}
	}

	private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();

		int height = bm.getHeight();

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		// CREATE A MATRIX FOR THE MANIPULATION

		Matrix matrix = new Matrix();

		// RESIZE THE BIT MAP

		matrix.postScale(scaleWidth, scaleHeight);

		// RECREATE THE NEW BITMAP

		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}

}
