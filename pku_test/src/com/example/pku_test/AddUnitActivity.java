package com.example.pku_test;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.support.v4.widget.DrawerLayout;
import android.widget.EditText;


/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a AddUnitActivity class.
 * This class is to add the unit into user's database
 * It has the textfields to provide a name, an abbreviation, and a size 
 * that an user would add an unit into the database.
 * and an accept button which user can finish adding the product to the database.
 */

public class AddUnitActivity extends ActionBarActivity implements
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
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_unit);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		final EditText tSize = (EditText) findViewById(R.id.txtSize);
		tSize.setOnKeyListener(new OnKeyListener()
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
		                		// Go back to the UnitActivity
		                    	Intent newActivity = new Intent(AddUnitActivity.this,UnitActivity.class);
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
			getMenuInflater().inflate(R.menu.add_unit, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_accept:
        	// If Save Complete
        	if(SaveData())
        	{
            	// Go back to the UnitActivity
            	Intent newActivity = new Intent(AddUnitActivity.this,UnitActivity.class);
            	startActivity(newActivity);            		
        	}
            return true;
        default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * This is a SaveData method which records a name, an abbreviation and a size of the unit
	 * and then adding it to the database.
	 */
	public boolean SaveData()
	{
		// Get the EditTexts from layout
		final EditText tName = (EditText) findViewById(R.id.txtName);
		final EditText tAbb = (EditText) findViewById(R.id.txtAbb);
		final EditText tSize = (EditText) findViewById(R.id.txtSize);
		
		// Dialog
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		
		// Check tName is not null.
		if(tName.getText().length() == 0)
		{
            ad.setMessage("Please input [UnitName] ");
            ad.show();
            tName.requestFocus();
            return false;
		}	
		
		// Check Abbreviation is not null
		if(tAbb.getText().length() == 0)
		{
            ad.setMessage("Please input [Abbreviation] ");
            ad.show();
            tAbb.requestFocus();
            return false;
		}		
		
		// Check Size
		if(tSize.getText().length() == 0)
		{
			ad.setMessage("Please input [Common Size] ");
			ad.show();
	        tSize.requestFocus();
	        return false;
		}		
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		
		// Check Data (If an unit exists)
		String arrData[] = myDb.SelectProduct(tName.getText().toString());
    	if(arrData != null)
    	{
    		ad.setMessage("Unit already exists!  ");
    		ad.show();
    		tName.requestFocus();
   		 	return false; 
    	}
    		
    	// Save Data to the database
    	long saveStatus = myDb.insertUnit(tName.getText().toString(),
    			tAbb.getText().toString(),
    			Double.parseDouble(tSize.getText().toString()));
    	
    	// Show the error message if saveStatus is less or equal than zero
    	if(saveStatus <=  0)
    	{
            ad.setMessage("Error!! ");
            ad.show();
            return false;
    	}
		
		return true;
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
			View rootView = inflater.inflate(R.layout.fragment_add_unit,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AddUnitActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
