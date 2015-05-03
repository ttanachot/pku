package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;

/*
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is an UnitActivity class.
 * This class is to show all units and user can delete each unit by pressing long on the listview
 * 
 */
public class UnitActivity extends ActionBarActivity implements
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
	// Cmd - is the event of command
	private String[] Cmd = {"Delete"};
	// myDb - is the database class
	final myDBClass myDb = new myDBClass(this);
	// UnitList - is the ArrayList of units
	private ArrayList<HashMap<String, String>> UnitList;
	// inputSearch - is the edittext of search
	private EditText inputSearch;
	// sAdap - is the simpleadapter to manage the row and column of listview
	private SimpleAdapter sAdap;

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
   
        ListView lisView1 = (ListView)findViewById(R.id.listView1); 
        // Get the units
        UnitList = myDb.SelectAll("Unit");
        
        // Get the inputsearch and Open the soft keyboard
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        
        sAdap = new SimpleAdapter(UnitActivity.this, UnitList, R.layout.unit_column,
                new String[] {"unitName", "abberviation", "commonSize"}, new int[] {R.id.unitName, R.id.abb, R.id.size});      
        lisView1.setAdapter(sAdap);
        registerForContextMenu(lisView1);
        
        // Set the listener for listview when user scrolls down
        lisView1.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // hide the soft keyboard
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                   }

                   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
          });
        
        // Set the listener for inputsearch
        inputSearch.addTextChangedListener(new TextWatcher() {
        	
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // Set the sAdap to filter in search
                UnitActivity.this.sAdap.getFilter().filter(cs);   
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                          
            }

			
        });
	}
	
	/*
	 * This is an onCreateContextMenu method.
	 * This method is to show the long press command on the listview
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu, android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
		menu.setHeaderIcon(android.R.drawable.btn_star_big_on);	
		InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		menu.setHeaderTitle("Command");
		String[] menuItems = Cmd; 
		for (int i = 0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
	}
	
	/*
	 * This is an onContextItemSelected method.
	 * This method is to set the listener for the event by long press.
	 * If user presses on Edit it will go to EditLogActivity
	 * and then if user press on Delete it will delete the record and restart this activity.  
	 * @see android.app.Activity#onContextItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
		String[] menuItems = Cmd;
		String CmdName = menuItems[menuItemIndex];
		HashMap<String,String> hash = (HashMap<String,String>) sAdap.getItem(info.position);
		String UnitId = hash.get("unitId").toString();
		
	    // Check Event Command
        if ("Delete".equals(CmdName)) {
	       	 myDb.deleteUnit(UnitId);
	       	 finish();
	       	 startActivity(getIntent());
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
			getMenuInflater().inflate(R.menu.unit, menu);
			restoreActionBar();
			return true;
		}
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add:
	        	InputMethodManager imm = (InputMethodManager)getSystemService(
	  			      Context.INPUT_METHOD_SERVICE);
	  			imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
	            goToAdd();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	public void goToAdd() {
		Intent newActivity = new Intent(UnitActivity.this,AddUnitActivity.class);
    	startActivity(newActivity); 
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
			View rootView = inflater.inflate(R.layout.fragment_unit, container,
					false);
//			TextView textView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			textView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((UnitActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
