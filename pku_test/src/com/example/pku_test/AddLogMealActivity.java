package com.example.pku_test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a AddLogMealActivity class.
 * It shows the detail of meal which user would like to make a record.
 * This class consist of the timepicker and accept button which user can finish adding the meal.
 * User can change the time in the timepicker. 
 */
public class AddLogMealActivity extends ActionBarActivity implements
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
	// mealId - is the id of the meal
	private String mealId;
	// mealName - is the name of meal
	private String mealName;
	// date - is the date of the record
	private String date;
	
	/*
	 * This is the onCreate method which initiates by Android
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_log_meal);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Get the pass values from the MealMenuActivity
		Intent intent = getIntent();
		mealName = intent.getStringExtra("mealName");
		date = intent.getStringExtra("date");
		// Initiate the view by mealName
		showData(mealName);
	}
	
	/**
	 * This is a showData method which will create an UI for this class.
	 * @param mealName - is the name of meal which user chooses for preparing the record.
	 */
	public void showData(String mealName)
	{
		final TextView tName = (TextView) findViewById(R.id.txtName);
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		// Set the timepicker to be 24 hours
		time.setIs24HourView(true);
		Calendar calendar = Calendar.getInstance();
		// Set the accurate hour in the timepicker
		time.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		
		// Show Data
		String arrData[] = myDb.SelectIdMeal(mealName);
		mealId = arrData[0];
		
		// Check mealId is exist or not
		if(arrData != null)
    	{
    		tName.setText(mealName);
    	}
    	
	}
	
	
	/**
	 * This is a SaveData method which records the date, time, and mealId into the Log table
	 */
	public boolean SaveData()
	{
		
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		// Get the time from timepicker
		String txtTime = time.getCurrentHour()+":"+time.getCurrentMinute();
		
		// Dialog
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		// Initiate the database class
		final myDBClass myDb = new myDBClass(this);
    	// Save Data
    	long saveStatus = myDb.insertLog(date,txtTime,Integer.parseInt(mealId),"meal",0.0);
    	
    	// Show the error message if saveStatus is less or equal than zero
    	if(saveStatus <=  0)
    	{
            ad.setMessage("Error!! ");
            ad.show();
            return false;
    	}
		return true;
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
			getMenuInflater().inflate(R.menu.add_log_meal, menu);
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
		switch (item.getItemId()) {
        case R.id.action_accept:
        	// If Save Complete
        	if(SaveData())
        	{
            	// Check the date if it is not today redirect to the HistoryActivity
        		// else goes to MainActivity
        		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        		String today = dateformat.format(new Date()).toString();
        		Intent newActivity;
        		if(date.equals(today)){
        			newActivity = new Intent(this,MainActivity.class);
        		}else{
        			newActivity = new Intent(this,HistoryActivity.class);
        			// Pass the value of the date to HistoryActivity
        			newActivity.putExtra("date", date);
        		}
            	startActivity(newActivity);         		
        	}
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
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
			View rootView = inflater.inflate(R.layout.fragment_add_log_meal,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AddLogMealActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
