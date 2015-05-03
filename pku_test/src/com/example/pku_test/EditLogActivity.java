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
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is an EditLogActivity class.
 * This class is to edit the record of log tracking by user chooses in each list and date.
 * Also in this class, user can edit the amount of the food and the timepicker.
 */
public class EditLogActivity extends ActionBarActivity implements
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
	// date - is the date of the record of product/meal(food)
	private String date;
	// logId - is the id of the record of product/meal(food)
	private String logId;
	// foodName - is the name of product/meal(food)
	private String foodName;
	// type - is to specify the type of food
	private String type;
	// amount - is the amount of product
	private String amount;
	
	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class.
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_log);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Get the pass value from the listview
		Intent intent = getIntent();
		date = intent.getStringExtra("date");
		logId = intent.getStringExtra("logId");
		foodName = intent.getStringExtra("foodName");
		type = intent.getStringExtra("type");
		amount = intent.getStringExtra("amount");
		showData();
	}

	/*
	 * This is a showData method which shows a name, an amount, a time and a date.
	 */
	public void showData()
	{
		// Get all components from the layout
		final TextView tName = (TextView) findViewById(R.id.txtName);
		final TextView txtAmt = (TextView) findViewById(R.id.textView4);
		final EditText txtAmount = (EditText) findViewById(R.id.txtAmount);
		final TextView tUnit = (TextView) findViewById(R.id.txtUnit);
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		time.setIs24HourView(true);
		Calendar calendar = Calendar.getInstance();
		time.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String Date = df.format(new Date()).toString();
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		
		// Check the type of food
		if(type.equals("product")){
			// Show Data
			String arrData[] = myDb.SelectProduct(foodName);
			
			String prodId = arrData[0];
			if(arrData != null)
	    	{
	    		tName.setText(arrData[1]);
	    		tUnit.setText(arrData[2]);
	    		txtAmount.setText(amount);
	    	}
	    	else{
	    		tName.setText("");
	    	}
		}
		else if(type.equals("meal")){
			txtAmt.setVisibility(View.GONE);
			txtAmount.setVisibility(View.GONE);
			tUnit.setVisibility(View.GONE);
			tName.setText(foodName);
			
		}
		
	}
	
	
	/*
	 * This is a saveData method which saves(edits) the change of an amount and time in the records.
	 */
	public boolean SaveData()
	{
		final EditText tAmount = (EditText) findViewById(R.id.txtAmount);
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		String txtTime = time.getCurrentHour()+":"+time.getCurrentMinute();
		// Dialog
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		// Check if amount is not null and the type is a product
		if(tAmount.getText().length() == 0 && type.equals("product"))
		{
            ad.setMessage("Please input [Amount] ");
            ad.show();
            tAmount.requestFocus();
            return false;
		}	
		String amount ="";
		if(type.equals("product")){
			amount = tAmount.getText().toString();
		}else if(type.equals("meal")){
			amount = "0.0";
		}
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String Date = df.format(new Date()).toString();
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
    	
		// Save Data and Check Data
    	long saveStatus = myDb.updateLog(logId,txtTime,amount);
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
		//actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.edit_log, menu);
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
            	// Check to go back to the MainActivity if the date is today 
        		// else go back to HistoryActivity
        		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        		String today = dateformat.format(new Date()).toString();
        		Intent newActivity;
        		if(date.equals(today)){
        			newActivity = new Intent(this,MainActivity.class);
        		}else{
        			newActivity = new Intent(this,HistoryActivity.class);
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
			View rootView = inflater.inflate(R.layout.fragment_edit_log,
					container, false);
//			TextView textView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			textView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((EditLogActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
