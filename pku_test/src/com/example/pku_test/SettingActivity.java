package com.example.pku_test;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is SettingActivity class.
 * This class handle all setting in the PKU app.
 * User can access Unit, Category, Info, Calendar, Alarm Activities through
 * this class.
 *
 */
public class SettingActivity extends ActionBarActivity implements
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
	
	// Context of this class
	final Context context = this;
	
	// alarm - String set on and off
	private String alarm;
	
	// temp - temporary to show on and off
	private String temp;

	/**
	 * This is a main method of this class.
	 * It contains all link to Unit, Category, Info, Calendar, Alarm Activities
	 * It checks whether an alarm is turning on or off.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));

        // Set whether alarm is on or off
        Intent intent = getIntent();
        alarm = intent.getStringExtra("alarm");
        if(alarm == null || alarm.equals("Off")){
        	temp = "Off";
        	alarm = temp;
		}
        else
        	temp = "On";
        
        // ListView button of setting
        String[] values = new String[] { "Units","Catagories","PHE", "Calendar", "Alarm "+ temp };
        ListView lisView1 = (ListView)findViewById(R.id.view1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, values);
	    lisView1.setAdapter(adapter);
	    lisView1.setOnItemClickListener(new OnItemClickListener() {
	      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
	    	  	//Units
	    	  	if(position==0){
	    	  		Intent newActivity = new Intent(SettingActivity.this,UnitActivity.class);
                	startActivity(newActivity); 
	    	  	}
	    	  	//Categories
	    	  	else if(position==1){
	    	  		Intent newActivity = new Intent(SettingActivity.this,CategoryActivity.class);
                	startActivity(newActivity); 
	    	  	}
	    	  	//PHE
	    	  	else if(position==2){
	    	  		Intent newActivity = new Intent(SettingActivity.this,InfoActivity.class);
                	startActivity(newActivity); 
	    	  	}
	    	  	//Calendar
	    	  	else if(position==3){
	    	  		Intent newActivity = new Intent(SettingActivity.this,CalendarActivity.class);
                	startActivity(newActivity); 
	    	  	}
	    	  	//Alarm
	    	  	else if(position==4){
	    	  		Intent newActivity = new Intent(SettingActivity.this,AlarmActivity.class);
	    	  		newActivity.putExtra("time", alarm);
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
		//actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.setting, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_setting,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((SettingActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
