package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a MealActivity class. It is a main class for meal.
 * It shows every meals that have created in the database by user.
 * This class consist of add button where user can go to add meal page.
 * User can either press on a meal for detail and edit or hold and press 
 * on a meal for delete.
 */
public class MealActivity extends ActionBarActivity implements
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
	
	// Cmd - a command name for deletion
	private String[] Cmd = {"Delete"};
	
	// myDb - use to access to the database class
	final myDBClass myDb = new myDBClass(this);
	
	// MealList - ArrayList of all existing meal in the database
	private ArrayList<HashMap<String, String>> MealList = new ArrayList<HashMap<String, String>>();
	
	// inputSearch - search text of a meal from user
	private EditText inputSearch;
	
	// sAdap - Adapter use to handle format in ListView
	private SimpleAdapter sAdap;
	private SimpleAdapter sAdap2;

	/**
	 * This is a main method in this class.
	 * It creates a meal page and shows all meals from database.
	 * 
	 * @para lisView1 a ListView that consist of MealList
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meal);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		long saveStatus = myDb.deleteAllProductTemp();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		MealList = myDb.SelectAllMealsPHE();
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, String> t: MealList){
			String t2;
			if(t.get("mealName").length() > 10){
				t2 = t.get("mealName").substring(0, 10) + "...";
			}else{
				t2 = t.get("mealName");
			}
			HashMap<String, String> t3 = new HashMap<String, String>();
			t3.put("mealName", t2);
			t3.put("mealPHE", t.get("mealPHE"));
			temp.add(t3);
		}

        ListView lisView1 = (ListView)findViewById(R.id.mealList); 
 
        //sAdap get its format from R.layout.meal_column
        sAdap = new SimpleAdapter(this, temp, R.layout.meal_column,
                new String[] {"mealName", "mealPHE"}, new int[] { R.id.mealName,  R.id.mealPHE });     
        sAdap2 = new SimpleAdapter(this, MealList, R.layout.meal_column,
                new String[] {"mealName", "mealPHE"}, new int[] { R.id.mealName,  R.id.mealPHE });   
        lisView1.setAdapter(sAdap);
        
        // register all value in lisView1 for hold and press command
        registerForContextMenu(lisView1);
        
        lisView1.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    //hide KB
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                   }

                   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
          });
        
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                MealActivity.this.sAdap.getFilter().filter(cs);
                MealActivity.this.sAdap2.getFilter().filter(cs);
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
        
        lisView1.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
		    	  	// Show on new activity
		    	    InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
	            	Intent newActivity = new Intent(MealActivity.this,DetailMealActivity.class);
					HashMap<String,String> hash = (HashMap<String,String>)sAdap2.getItem(position);
	            	newActivity.putExtra("mealName", hash.get("mealName").toString());
//	            	newActivity.putExtra("mealName", MealList.get(position).get("mealName").toString());
	            	startActivity(newActivity);
		      }       
    });
	}
	
	/**
	 * This method is to create a command dialog after user
	 * hold and press a meal in a ListView
	 */
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
		menu.setHeaderIcon(android.R.drawable.btn_star_big_on);	
		menu.setHeaderTitle("Command");
		String[] menuItems = Cmd; 
		for (int i = 0; i<menuItems.length; i++) {
			menu.add(Menu.NONE, i, i, menuItems[i]);
		}
    }
    
	/**
	 * This method is to command any action that has been click 
	 * in a command dialog. In this method, there is only one command 
	 * which is a delete command for a meal. 
	 * 
	 * @para menuItems - all commands in a dialog
	 * @para CmdName - selected command
	 * @para mealName - meal that has been selected
	 */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
		String[] menuItems = Cmd;
		String CmdName = menuItems[menuItemIndex];
		HashMap<String,String> hash = (HashMap<String,String>)sAdap2.getItem(info.position);
		String mealName = hash.get("mealName").toString();
	    
	    // Check Event Command
        if ("Delete".equals(CmdName)) {
	       	 myDb.deleteMeals(mealName);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.meal, menu);
			restoreActionBar();
			return true;
		}
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void goToAdd(){
		Intent newActivity = new Intent(this,AddMealActivity.class);
    	startActivity(newActivity);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 switch (item.getItemId()) {
	        case R.id.action_add:
	        	InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
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
			View rootView = inflater.inflate(R.layout.fragment_meal, container,
					false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MealActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

}
