package com.example.pku_test;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a CalendarActivity class.
 * This class is to control the calendar which user can choose to see his activity in the past.
 */

public class CalendarActivity extends ActionBarActivity implements
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

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of calendar
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Set up the calendar
		CalendarView cal = (CalendarView) findViewById(R.id.calendarView1);
        cal.setShowWeekNumber(false);
        
        // Set the Listener to get the date from user
        cal.setOnDateChangeListener(new OnDateChangeListener() {
		@Override
		public void onSelectedDayChange(CalendarView view, int year, int month,
				int dayOfMonth) {
			// Get the date from the calendar
			int mth = month+1;
			String tday = ""+dayOfMonth;
			String tmonth = ""+mth;
			String tyear = ""+year;
				if(tday.length() == 1){
					tday = "0"+tday;
				}
				if(tmonth.length() == 1){
					tmonth = "0"+tmonth;
				}
			String pickdate = tday+"/"+tmonth+"/"+tyear;
			
			// Set up the comparators to check in the condition below
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			Date today = c.getTime();
			
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			Date dateSpecified = c.getTime();
			
			// Check if the the date is before or today
			if (dateSpecified.before(today) || dateSpecified.equals(today)) {
				// Go to the HistoryActivity
				Intent newActivity = new Intent(CalendarActivity.this,HistoryActivity.class);
				// Pass the value of the pickdate to HistoryActivity
		    	newActivity.putExtra("date", pickdate);
		    	startActivity(newActivity);
			} 
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.calendar, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
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
			View rootView = inflater.inflate(R.layout.fragment_calendar,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((CalendarActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
