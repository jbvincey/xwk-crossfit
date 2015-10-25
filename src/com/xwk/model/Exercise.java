package com.xwk.model;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.jbvincey.xwk.R;
import com.xwk.database.ExerciseDAO;

public class Exercise {

	public enum ExerciseType {
		legs,
		chest,
		arms,
		back,
		abs
	}

	private long id;
	private String name;
	private String description;
	private byte[] image;
	private int reps;
	private ExerciseType type;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getReps() {
		return reps;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	public ExerciseType getType() {
		return type;
	}
	public void setType(ExerciseType type) {
		this.type = type;
	}
	public String getTypeStringLanguage() {
		return this.type.toString();
	}

	public static Exercise recoverExercise(Bundle bundle, ExerciseDAO exerciseDAO) {
		Exercise exercise = null;
		if(bundle!=null && bundle.containsKey(Ids.EXERCISE_ID)) {
			exercise = exerciseDAO.getExercise(bundle.getLong(Ids.EXERCISE_ID));
		}
		return exercise;
	}

	public byte[] getTypeBytearray(Context context) {
		Bitmap bmp = getTypeImage(context);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	public Bitmap getTypeImage(Context context) {
		ExerciseType type = this.getType();
		Bitmap bmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		switch (type) {
		case legs:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.legshdpi5, options);
			break;
		case chest:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.chesthdpi5, options);
			break;
		case arms:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.armshdpi5, options);
			break;
		case back:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.backhdpi5, options);
			break;
		case abs:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.abshdpi5, options);
			break;


		default:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.legsmedium5, options);
			break;
		}
		return bmp;
	}
	
	public byte[] getTypeBytearrayWhite(Context context) {
		Bitmap bmp = getTypeImageWhite(context);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}
	
	public Bitmap getTypeImageWhite(Context context) {
		ExerciseType type = this.getType();
		Bitmap bmp = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		switch (type) {
		case legs:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.legshdpi5white, options);
			break;
		case chest:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.chesthdpi5white, options);
			break;
		case arms:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.armshdpi5white, options);
			break;
		case back:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.backhdpi5white , options);
			break;
		case abs:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.abshdpi5white, options);
			break;


		default:
			bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.legsmedium5white, options);
			break;
		}
		return bmp;
	}

	public Exercise getCopy() {
		Exercise exercise = new Exercise();
		exercise.setId(this.id);
		exercise.setName(this.name);
		exercise.setDescription(this.description);
		exercise.setType(this.getType());
		exercise.setImage(this.image);
		return exercise;
	}

}
