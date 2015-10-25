package com.xwk.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.model.Exercise.ExerciseType;
import com.xwk.model.Ids;

public class CrossfitRawAdapter extends SimpleAdapter {


	private Context mContext;
	public LayoutInflater inflater=null;
	public CrossfitRawAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mContext = context;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.rowlist_workout, null);

		HashMap<String, String> data = (HashMap<String, String>) getItem(position);
		TextView text1 = (TextView)vi.findViewById(R.id.item1);
		String name = (String) data.get(Ids.CROSSFIT_WORKOUT_NAME);
		text1.setText(name);
		TextView text2 = (TextView)vi.findViewById(R.id.item2);
		String reps = (String) data.get(Ids.CROSSFIT_WORKOUT_DESCRIPTION);
		text2.setText(reps);

		LinearLayout imageLayouts=(LinearLayout)vi.findViewById(R.id.rowlist_workout_types);
		String typesString = (String) data.get(Ids.CROSSFIT_WORKOUT_TYPES);
		BitmapFactory.Options options = new BitmapFactory.Options();

		int childrenCount = imageLayouts.getChildCount();
		int typesCount = typesString.split(",").length;
		//if(childrenCount < typesCount) {
		imageLayouts.removeAllViews();
		Resources r = mContext.getResources();
		int screenSize = r.getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
		float picSize = 0;
		if(screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			picSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());
		} else if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			picSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
		} else {
			picSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics());
		}
		if(typesString.contains(ExerciseType.legs.toString())) {
			ImageView imageView = new ImageView(mContext);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.legsmedium5, options);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int)picSize, (int)picSize, true);
			imageView.setImageBitmap(scaledBitmap);

			imageLayouts.addView(imageView);
		}

		if(typesString.contains(ExerciseType.chest.toString())) {
			ImageView imageView = new ImageView(mContext);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.chestmedium5, options);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int)picSize, (int)picSize, true);
			imageView.setImageBitmap(scaledBitmap);

			imageLayouts.addView(imageView);
		}

		if(typesString.contains(ExerciseType.arms.toString())) {
			ImageView imageView = new ImageView(mContext);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.armsmedium5, options);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int)picSize, (int)picSize, true);
			imageView.setImageBitmap(scaledBitmap);

			imageLayouts.addView(imageView);
		}

		if(typesString.contains(ExerciseType.back.toString())) {
			ImageView imageView = new ImageView(mContext);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.backmedium5, options);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int)picSize, (int)picSize, true);
			imageView.setImageBitmap(scaledBitmap);

			imageLayouts.addView(imageView);
		}

		if(typesString.contains(ExerciseType.abs.toString())) {
			ImageView imageView = new ImageView(mContext);
			Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.absmedium5, options);
			Bitmap scaledBitmap = Bitmap.createScaledBitmap(bmp, (int)picSize, (int)picSize, true);
			imageView.setImageBitmap(scaledBitmap);

			imageLayouts.addView(imageView);
		}
		//}

		return vi;
	}
}