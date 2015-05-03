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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a AddLogProductActivity class.
 * It shows the detail of product which user would like to make a record.
 * This class consist of an amount field and a timepicker 
 * and an accept button which user can finish adding the product.
 * User can change the time in the timepicker. 
 */

public class AddLogProductActivity extends ActionBarActivity implements
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
	// prodId - is the id of the product
	private String prodId;
	// date -  is the date of the record
	private String date;

	/*
	 * This is the onCreate method which initiates by Android
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_log_product);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Get the pass values from the ProductMenuActivity
		Intent intent = getIntent();
		String prodName = intent.getStringExtra("prodName");
		date = intent.getStringExtra("date");
		
		// Initiate the view by ProductName
		showData(prodName);
		
		// Set to close the soft keyboard when user touches the view
		FrameLayout layout = (FrameLayout) findViewById(R.id.container);
		layout.setOnTouchListener(new OnTouchListener()
		{
		    @Override
		    public boolean onTouch(View view, MotionEvent ev)
		    {
		        hideKeyboard(view);
		        return false;
		    }
		});
	}
	
	/**
	 * This is a hideKeyboard method which force the soft keyboard to hide.
	 * @param view - is the view to make the touch to hide the soft keyboard.
	 */
	protected void hideKeyboard(View view)
	{
	    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	
	/**
	 * This is a showData method which will create an UI for this class.
	 * @param prodName - is the name of product which user chooses for preparing the record.
	 */
	public void showData(String prodName)
	{
		final EditText tAmount = (EditText) findViewById(R.id.txtAmount);
		
		// Set the listener to provide the user press the done/enter button
		tAmount.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if (event.getAction() == KeyEvent.ACTION_DOWN)
		        {
		            switch (keyCode)
		            {
		                case KeyEvent.KEYCODE_DPAD_CENTER:
		                case KeyEvent.KEYCODE_ENTER:
		                	// if Save Complete
		                	if(SaveData())
		                	{
		                		// check the date if it is not today redirect to the HistoryActivity
		                		// else goes to MainActivity
		                		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		                		String today = dateformat.format(new Date()).toString();
		                		Intent newActivity;
		                		if(date.equals(today)){
		                			newActivity = new Intent(AddLogProductActivity.this,MainActivity.class);
		                		}else{
		                			newActivity = new Intent(AddLogProductActivity.this,HistoryActivity.class);
		                			// Pass the value of the date to HistoryActivity
		                			newActivity.putExtra("date", date);
		                		}
		                    	startActivity(newActivity);            		
		                	}   
		                    return true;
		                default:
		                    break;
		            }
		        }
		        return false;
		    }
		});
		final TextView tName = (TextView) findViewById(R.id.txtName);
		final TextView tUnit = (TextView) findViewById(R.id.txtUnit);
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		// Set the timepicker to be 24 hours
		time.setIs24HourView(true);
		Calendar calendar = Calendar.getInstance();
		// Set the accurate hour in the timepicker
		time.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		
		// Show Data
		String arrData[] = myDb.SelectProduct(prodName);
		
		prodId = arrData[0];
		// Check product is not null
		if(arrData != null)
    	{
    		tName.setText(arrData[1]);
    		tUnit.setText(arrData[2]);
    	}
   
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        return true;
    }
	
	/**
	 * This is a SaveData method which records the date, time, and mealId into the Log table
	 */
	public boolean SaveData()
	{
		final EditText tAmount = (EditText) findViewById(R.id.txtAmount);
		final TimePicker time = (TimePicker) findViewById(R.id.timePicker1);
		String txtTime = time.getCurrentHour()+":"+time.getCurrentMinute();
		// Dialog
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		// Check tAmount is not null
		if(tAmount.getText().length() == 0)
		{
            ad.setMessage("Please input [Amount] ");
            ad.show();
            tAmount.requestFocus();
            return false;
		}	
		String amount = tAmount.getText().toString();
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
    	// Save Data
    	long saveStatus = myDb.insertLog(date,txtTime,Integer.parseInt(prodId),"product",Double.parseDouble(amount));
    	
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
		//actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.add_log_product, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_add_log_product,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AddLogProductActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
