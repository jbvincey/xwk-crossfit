package com.xwk.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.model.Ids;

public class ExerciseRawAdapter extends SimpleAdapter {

	
	private Context mContext;
	public LayoutInflater inflater=null;
	public ExerciseRawAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
		super(context, data, resource, from, to);
		mContext = context;
		inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi=convertView;
		if(convertView==null)
			vi = inflater.inflate(R.layout.rowlist_exercise, null);

		HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
		TextView text1 = (TextView)vi.findViewById(R.id.item1);
		String name = (String) data.get(Ids.EXERCISE_NAME);
		text1.setText(name);
		TextView text2 = (TextView)vi.findViewById(R.id.item2);
		String reps = (String) data.get(Ids.EXERCISE_REPS);
		text2.setText(reps);
		
		ImageView image=(ImageView)vi.findViewById(R.id.exercise_row_image);
		Bitmap bmp = (Bitmap) data.get(Ids.EXERCISE_PIC);
		image.setImageBitmap(bmp);
		
		return vi;
	}
}