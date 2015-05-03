package com.example.pku_test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class MealMenuActivity extends ActionBarActivity implements
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
	private CharSequence mTitle;
	EditText inputSearch;
	SimpleAdapter sAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal_menu);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		Intent intent = getIntent();
		final String date = intent.getStringExtra("date");
		
		final myDBClass myDb = new myDBClass(this);
		final ArrayList<HashMap<String, String>> MealList = myDb.SelectAllMealsPHE(); 
     // listView1
        ListView lisView1 = (ListView)findViewById(R.id.mealList); 
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        
        sAdap = new SimpleAdapter(this, MealList, R.layout.meal_column,
                new String[] {"mealName", "mealPHE"}, new int[] { R.id.mealName,  R.id.mealPHE });      
        lisView1.setAdapter(sAdap);
        lisView1.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
		    	  	// Show on new activity
		    	  	InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
	            	Intent newActivity = new Intent(MealMenuActivity.this,AddLogMealActivity.class);
	            	HashMap<String,String> hash = (HashMap<String,String>)sAdap.getItem(position);
	            	newActivity.putExtra("mealName", hash.get("mealName").toString());
	            	newActivity.putExtra("date", date);
	            	startActivity(newActivity);
		      }       
        });
        
        lisView1.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //hide KB
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                   }

                   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
          });
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MealMenuActivity.this.sAdap.getFilter().filter(cs);   
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
			getMenuInflater().inflate(R.menu.meal_menu, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_meal_menu,
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
			((MealMenuActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
