package com.xwk.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jbvincey.xwk.R;
import com.xwk.database.CrossfitPerformanceDAO;
import com.xwk.database.CrossfitWorkoutDAO;
import com.xwk.database.ExerciseDAO;
import com.xwk.fragments.CustomAlertDialog;
import com.xwk.model.CrossfitPerformance;
import com.xwk.model.CrossfitWorkout;
import com.xwk.model.Ids;

public class CrossfitPerformanceActivity extends Activity {

	private CrossfitWorkoutDAO crossfitWorkoutDAO;
	private CrossfitPerformanceDAO crossfitPerformanceDAO;
	private ExerciseDAO exerciseDAO;

	private ListView listView;
	//private LinearLayout graphLayout;
	private LinearLayout numberLayout;
	private HorizontalScrollView scrollview;
	private TextView maxPerf;
	private FrameLayout frameLayout;
	private TextView avgText;
	private LinearLayout graphLayout;

	private CrossfitWorkout crossfitWorkout;
	private List<CrossfitPerformance> performances;
	private Activity activity;
	
	private float scale;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.crossfitperformance_activity);

		crossfitWorkoutDAO = CrossfitWorkoutDAO.getInstance(this);
		crossfitWorkoutDAO.open();

		crossfitPerformanceDAO = CrossfitPerformanceDAO.getInstance(this);
		crossfitPerformanceDAO.open();

		exerciseDAO = ExerciseDAO.getInstance(this);
		exerciseDAO.open();

		getActionBar().setDisplayHomeAsUpEnabled(true);

		listView = (ListView) findViewById(R.id.crossfitperformance_activity_listview);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		numberLayout = (LinearLayout) findViewById(R.id.crossfitperformance_activity_numberlayout);
		scrollview = (HorizontalScrollView) findViewById(R.id.crossfitperformance_activity_scrollview);
		maxPerf = (TextView) findViewById(R.id.crossfitperformance_max);
		frameLayout = (FrameLayout) findViewById(R.id.crossfitperformance_activity_framelayout);
		avgText = (TextView) findViewById(R.id.crossfitperformance_activity_averagetext);
		graphLayout = (LinearLayout) findViewById(R.id.crossfitperformance_activity_graphlayout);
		
		crossfitWorkout = CrossfitWorkout.recoverCrossfitWorkout(getIntent().getExtras(), crossfitWorkoutDAO);      

		setTitle("  " + crossfitWorkout.getName());

		activity = this;
		scale = this.getResources().getDisplayMetrics().density;
		
		setupView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		crossfitWorkoutDAO.open();
		crossfitPerformanceDAO.open();
		exerciseDAO.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		crossfitWorkoutDAO.close();
		crossfitPerformanceDAO.close();
		exerciseDAO.close();
	}

	private void setupView() {

		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		data.clear();
		int i = 0;
		performances = crossfitWorkout.getPerformances();
		int total = performances.size();

		if(performances.size()>0) {

			long max = 0;
			long min = 1000000;
			String maxString = "";
			String minString = "";
			long totalPerf = 0;
			long avg = 0;
			for(CrossfitPerformance performance: performances) {
				totalPerf += performance.getTime();
				if(performance.getTime() > max) {
					max = performance.getTime();
					maxString = performance.getTimeString();
				}
				if(performance.getTime() < min) {
					min = performance.getTime();
					minString = performance.getTimeString();
				}
			}
			//set max
			maxPerf.setText(maxString);
			avg = (totalPerf / performances.size());

			
			/*TextView avgText = new TextView(this);
			avgText.setTextColor(Color.parseColor("#33b5e5"));
			avgText.setText("AVG");
			FrameLayout.LayoutParams avgTextParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			avgTextParam.setMargins(0, (int) (270 + 2 - (avg*210/max)), 0, 0);
			frameLayout.addView(avgText, avgTextParam);
			
			TextView agvNumber = new TextView(this);
			agvNumber.setTextColor(Color.parseColor("#33b5e5"));
			int secs = (int) (avg / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			agvNumber.setText(mins + ":" + secs + "");
			FrameLayout.LayoutParams avgNumberParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			avgNumberParam.setMargins(0, (int) (270 - 30 - (avg*210/max)), 0, 0);
			frameLayout.addView(agvNumber, avgNumberParam);*/
			
			//Create graph
			/*LinearLayout graphLayout = new LinearLayout(this);
			LinearLayout.LayoutParams graphLayoutParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 210);
			graphLayoutParam.setMargins(0, 0, 0, 0);
			graphLayout.setOrientation(LinearLayout.HORIZONTAL);
			graphLayout.setGravity(Gravity.BOTTOM);
			graphContainer.addView(graphLayout, 0, graphLayoutParam);*/
			
			for(CrossfitPerformance performance: performances) {
				i++;
				FrameLayout column = new FrameLayout(this);
				column.setBackgroundResource(R.color.orange1);
				int dpsPerf = (int) (int) (performance.getTime()*140/max);
				int pixelsPerf = (int) (dpsPerf * scale + 0.5f);
				LinearLayout.LayoutParams graphParam = new LinearLayout.LayoutParams(20, pixelsPerf);
				graphParam.setMargins(10, 0, 10, 0);
				graphLayout.addView(column, graphParam);

				TextView number = new TextView(this);
				number.setText(String.valueOf(i));
				number.setTextSize(15);
				number.setWidth(40);
				number.setTextColor(Color.parseColor("#b8b8b8"));
				number.setGravity(Gravity.CENTER);
				numberLayout.addView(number);
			}

			//Set average line
			LinearLayout avgLine = new LinearLayout(this);
			avgLine.setBackgroundColor(Color.parseColor("#33b5e5"));
			FrameLayout.LayoutParams avgParam = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, 2);
			int dpsAvg = (int) (180 - (avg*140/max));
			int pixelsAvg = (int) (dpsAvg * scale + 0.5f);
			avgParam.setMargins(0, pixelsAvg, 0, 0);
			frameLayout.addView(avgLine, 0, avgParam);
			
			//Set average text
			int secs = (int) (avg / 1000);
			int mins = secs / 60;
			secs = secs % 60;
			avgText.setText("AVG: " + String.format("%02d", mins) + ":" + String.format("%02d", secs) + "");

			//Setup data for listview adapter		
			i = 0;
			Collections.reverse(performances);
			for(CrossfitPerformance performance: performances) {
				i++;
				Map<String, String> datum = new HashMap<String, String>(2);
				datum.put("number", String.valueOf(total - i + 1));
				datum.put("time", performance.getTimeString());
				datum.put("date", performance.getDateString());
				data.add(datum);
			}

			SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.rowlist_performance, new String[] {"number", "time", "date"}, new int[] {R.id.item1, R.id.item2, R.id.item3});
			listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					CrossfitPerformance performance = performances.get(position);

					CustomAlertDialog alertDialog = new CustomAlertDialog();
					Bundle args = new Bundle();
					args.putString(Ids.ALERTDIALOG_TITLE, performance.getDateString());
					String content = "Time: " + performance.getTimeString();
					String comment = performance.getComment();
					if(!comment.isEmpty()){
						content += "\nComment: " + comment;
					}
					args.putString(Ids.ALERTDIALOG_CONTENT, content);
					alertDialog.setArguments(args);
					alertDialog.show(getFragmentManager(), "alertdialog");

				}
			});

			new Handler().postDelayed(new Runnable() {
				public void run() {
					scrollview.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			}, 100L);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.crossfitworkout_performance, menu);
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
		}
		return super.onOptionsItemSelected(item);
	}
}
