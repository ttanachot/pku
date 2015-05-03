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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.AdapterView.OnItemClickListener;


/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a CatalogActivity class.
 * This class is to show the categories to make the user chooses to view the product in each of them.
 */

public class CatalogActivity extends ActionBarActivity implements
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
	// myDb - is the database class
	final myDBClass myDb = new myDBClass(this);
	// CatList - is the ArrayList of categories
	private ArrayList<HashMap<String, String>> CatList;
	// inputSearch - is the edittext for searching 
	private EditText inputSearch;
	// sAdap - is the adapter to manage the row and column of the listview
	private SimpleAdapter sAdap;

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class.
	 * This method has set up the listener of listview and inputsearch.
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_catalog);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Get the ArrayList of categories
		CatList = myDb.SelectAll("Categories"); 
		
		// Get the listview
        ListView lisView1 = (ListView)findViewById(R.id.listView1); 
        
        // Get the inputsearch 
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        // Force open the soft keyboard and focus on the inputsearch
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        
        // Set up the listview to use the adapter to manage the row and column
        sAdap = new SimpleAdapter(CatalogActivity.this, CatList, R.layout.catalog_column,
                new String[] {"catName"}, new int[] {R.id.catName});      
        lisView1.setAdapter(sAdap);
		        // Set the listener for onClick in each view
		        lisView1.setOnItemClickListener(new OnItemClickListener() {
				      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
				    	  	// Hide the soft keyboard
				    	  	InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		                    // Open the ProductActivity in each of category
		                    Intent newActivity = new Intent(CatalogActivity.this,ProductActivity.class);
			            	HashMap<String,String> hash = (HashMap<String,String>) sAdap.getItem(position);
			            	newActivity.putExtra("catName", hash.get("catName").toString());
			            	startActivity(newActivity);
			            	startActivity(newActivity);
				      }       
		        });
		        
		        // Set the listener when user scrolls down the listview
		        lisView1.setOnScrollListener(new OnScrollListener() {
		            public void onScrollStateChanged(AbsListView view, int scrollState) {
		                    // Hide the soft keybroad
		                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		                   }

		                   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
		          });
		        
		        // Set the listener of inputSearch to filter of the listview
		        inputSearch.addTextChangedListener(new TextWatcher() {
		            
		            @Override
		            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		                // Set the adapter can be searched.
		                CatalogActivity.this.sAdap.getFilter().filter(cs);   
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
	
	/*
	 * This is a goToAdd method which is to go to AddProductActivity.
	 */
	public void goToAdd(){
		InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		Intent newActivity = new Intent(CatalogActivity.this,AddProductActivity.class);
    	startActivity(newActivity);      
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
			getMenuInflater().inflate(R.menu.catalog, menu);
			restoreActionBar();
			return true;
		}
		// Hide the soft keyboard
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add:
	            goToAdd();
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
			View rootView = inflater.inflate(R.layout.fragment_catalog,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((CatalogActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
