package com.example.pku_test;


import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.TimePicker;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is AlarmActivity class.
 * This class shows a setting of an alarm for user to set.
 * User selects a time to alarm and click set alarm.
 * User can click Alarm off to turn off an alarm
 * 
 */
public class AlarmActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	
	// mTitle - title of this activity 
	private CharSequence mTitle;
	
	// tpTime - to select time
	private TimePicker tpTime;

	/*
	 * This is a main method of this activity.
	 * This method is to handle a time a user set.
	 * It handle a button for set alarm and turn off alarm.
	 * 
	 * (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		Intent intent = getIntent();
		String time = intent.getStringExtra("time");
		
		// Initialize TimePicker		
		tpTime = (TimePicker) findViewById(R.id.tp_time);
        tpTime.setIs24HourView(true);
        
        if(time.equals("Off")){
        	Calendar calendar = Calendar.getInstance();
        	tpTime.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        	
        }else{
        	String[] times = time.split(" ");
        	String[] temp = times[1].split(":");
			tpTime.setCurrentHour(Integer.parseInt(temp[0]));
			tpTime.setCurrentMinute(Integer.parseInt(temp[1]));
        }
		
		// Button to set an alarm		
        final Button button = (Button) findViewById(R.id.btn_set_alarm);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	/** This intent invokes the activity DemoActivity, which in turn opens the AlertDialog window */
		        Intent i = new Intent("com.example.pku_test.demoactivity");
		
		        /** Creating a Pending Intent */
		        PendingIntent operation = PendingIntent.getActivity(getBaseContext(), 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		        /** Getting a reference to the System Service ALARM_SERVICE */
		        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
		        
		        /** Set hour and minute to alarm */
		        Calendar calendar = Calendar.getInstance();	
		        calendar.set(Calendar.HOUR_OF_DAY, tpTime.getCurrentHour());
		        calendar.set(Calendar.MINUTE, tpTime.getCurrentMinute());
		        long alarm_time = calendar.getTimeInMillis();
		        
		        /** Setting an alarm, which invokes the operation at alart_time */
		        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
		        	    alarm_time,
		        	    AlarmManager.INTERVAL_DAY, operation);
		
		       
		        SharedPreferences.Editor editor = getSharedPreferences("com.example.pku_test", MODE_PRIVATE).edit();
		        editor.putBoolean("NameOfThingToSave", true);
		        editor.commit();
		        
		        Intent newActivity = new Intent(AlarmActivity.this, SettingActivity.class);
		        newActivity.putExtra("alarm", "On "+ tpTime.getCurrentHour() + ":"+ tpTime.getCurrentMinute());
            	startActivity(newActivity);
            }
        });
        
        // Button to turn off an alarm
        final Button button2 = (Button) findViewById(R.id.btn_off_alarm);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	/** This intent invokes the activity DemoActivity, which in turn opens the AlertDialog window */
		        Intent i = new Intent("com.example.pku_test.demoactivity");
		
		        /** Creating a Pending Intent */
		        PendingIntent operation = PendingIntent.getActivity(getBaseContext(), 0, i, Intent.FLAG_ACTIVITY_NEW_TASK);
		
		        /** Getting a reference to the System Service ALARM_SERVICE */
		        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
            	
		        /** Cancel all the alarm operation **/
            	alarmManager.cancel(operation);
            	Intent newActivity = new Intent(AlarmActivity.this, SettingActivity.class);
            	newActivity.putExtra("alarm", "Off");
            	startActivity(newActivity); 
            }
        });        
		
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
//			getMenuInflater().inflate(R.menu.alarm, menu);
//			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_alarm,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AlarmActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
