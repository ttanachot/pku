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
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

public class CategoryActivity extends ActionBarActivity implements
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
	String[] Cmd = {"Delete"};
	final myDBClass myDb = new myDBClass(this);
	private ArrayList<HashMap<String, String>> CatList;
	EditText inputSearch;
	SimpleAdapter sAdap;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		CatList = myDb.SelectAll("Categories"); 
		
		// listView1
        ListView lisView1 = (ListView)findViewById(R.id.listView1); 
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        
        sAdap = new SimpleAdapter(CategoryActivity.this, CatList, R.layout.category_column,
                new String[] {"catName"}, new int[] {R.id.catName});      
        lisView1.setAdapter(sAdap);
        registerForContextMenu(lisView1);
        
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
                CategoryActivity.this.sAdap.getFilter().filter(cs);   
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
		String CatId = hash.get("catId").toString();
	    // Check Event Command
         
        if ("Delete".equals(CmdName)) {
	       	
	       	 myDb.deleteCategories(CatId);
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
			getMenuInflater().inflate(R.menu.category, menu);
			restoreActionBar();
			return true;
		}
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		return super.onCreateOptionsMenu(menu);
	}
	
	public void goToAdd(){
		Intent newActivity = new Intent(this,AddCategoryActivity.class);
    	startActivity(newActivity);  
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
			View rootView = inflater.inflate(R.layout.fragment_category,
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
			((CategoryActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
