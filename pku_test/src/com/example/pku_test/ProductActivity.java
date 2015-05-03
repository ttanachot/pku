package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/*
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a ProductActivity class.
 * This class is to show the products.
 * User can add more products by pressing on the add button.
 * Also user can edit or delete product by long pressing on the listview of product
 * 
 */
public class ProductActivity extends ActionBarActivity implements
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
	private String[] Cmd = {"Edit","Delete"};
	// myDb - is the database class
	final myDBClass myDb = new myDBClass(this);
	// ProductList - is the ArrayList of products
	private ArrayList<HashMap<String, String>> ProductList;
	// inputSearch - is the edittext of search
	private EditText inputSearch;
	// sAdap - is the simpleadapter to manage the row and column of listview
	private SimpleAdapter sAdap;
	
	private SimpleAdapter sAdap2;

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		// Get the pass by value
		Intent intent = getIntent();
		String catName = intent.getStringExtra("catName");
		mTitle = catName;
		
		// Get the products from database
		
		
		ProductList = myDb.SelectCatalogProd(catName);
		
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, String> t: ProductList){
			String t2;
			if(t.get("prodName").length() > 10){
				t2 = t.get("prodName").substring(0, 10) + "...";
			}else{
				t2 = t.get("prodName");
			}
			HashMap<String, String> t3 = new HashMap<String, String>();
			t3.put("prodName", t2);
			t3.put("commonSize", t.get("commonSize"));
			t3.put("abberviation", t.get("abberviation"));
			t3.put("phePerUnit", t.get("phePerUnit"));
			temp.add(t3);
		}
        final ListView lisView1 = (ListView)findViewById(R.id.listView1); 
        
        // Set up the inputSearch and open the soft keybroad
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        
        // Set the sAdap to use with listview
        
        sAdap2 = new SimpleAdapter(ProductActivity.this, ProductList, R.layout.product_column,
                new String[] {"prodName","commonSize","abberviation", "phePerUnit"}, new int[] { R.id.prodName, R.id.commonSize, R.id.abberviation, R.id.prodPHE });      
        
        sAdap = new SimpleAdapter(ProductActivity.this, temp, R.layout.product_column,
                new String[] {"prodName","commonSize","abberviation", "phePerUnit"}, new int[] { R.id.prodName, R.id.commonSize, R.id.abberviation, R.id.prodPHE });      
        lisView1.setAdapter(sAdap);
        
        lisView1.setTextFilterEnabled(true);
        // Set the listener to the listview when user scrolls down
        lisView1.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // Hide the soft keyboard
                    InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                   }

                   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) { }
          });
        
        // Set the listener of the inputsearch
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // Set the search
                ProductActivity.this.sAdap.getFilter().filter(cs);
                ProductActivity.this.sAdap2.getFilter().filter(cs);
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
        
     // Set the listener of the listview
        lisView1.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
		    	  	// Hide the soft keyboard and go to DetailProductActivity
		    	    InputMethodManager imm = (InputMethodManager)getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
	            	Intent newActivity = new Intent(ProductActivity.this,DetailProductActivity.class);
	            	
	            	HashMap<String,String> hash = (HashMap<String,String>)sAdap2.getItem(position);
	            	newActivity.putExtra("prodName", hash.get("prodName").toString());
	            	startActivity(newActivity);
		      }       
        });
        registerForContextMenu(lisView1);
		
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
		HashMap<String,String> hash = (HashMap<String,String>)sAdap2.getItem(info.position);
		String ProdId = hash.get("prodId").toString();
	    
	    // Check Event Command
        if ("Edit".equals(CmdName)) {
          	Intent newActivity = new Intent(ProductActivity.this,DetailProductActivity.class);
        	newActivity.putExtra("prodName", hash.get("prodName").toString());
        	startActivity(newActivity);
        } else if ("Delete".equals(CmdName)) {
	       	 myDb.deleteProduct(ProdId);
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

		}
	}
	
	/*
     * This is a goToAdd method which is to go to Activity.
     */
	public void goToAdd(){
		InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
		Intent newActivity = new Intent(ProductActivity.this,AddProductActivity.class);
    	startActivity(newActivity);      
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.product, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_product,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((ProductActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
