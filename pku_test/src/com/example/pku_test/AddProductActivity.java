package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a AddProductActivity class.
 * This class is to add the product into user's database
 * It has the textfields to provide a name and an amount that an user would add a product into the database.
 * Also it has a dropdown(spinner) to make user easy to choose his/her own unit and category of a product.
 * and an accept button which user can finish adding the product to the database.
 */

public class AddProductActivity extends ActionBarActivity implements
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
	// spinUnit - is the group of the units that shows in the dropdown
	private Spinner spinUnit;
	// spinCat - is the group of the categories that shows in the dropdown
	private Spinner spinCat;
	// cat -  is the category which the product belongs to.
	private String cat;
	
	
	/*
	 * This is the onCreate method which initiates by Android
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
        
        // Initiate the database
        final myDBClass myDb = new myDBClass(this);
        // Get all units from database and save them in to ArrayList
		final ArrayList<HashMap<String, String>> unitList = myDb.SelectAll("Unit");
	
		SimpleAdapter sAdap;
		// Spinner for Unit by using the SimpleAdapter to manage the row and column
		sAdap = new SimpleAdapter(this, unitList, R.layout.unit_spinner,
                new String[] {"unitName"}, new int[] {R.id.unitName}); 
        spinUnit = (Spinner) findViewById(R.id.spinUnit);
	    spinUnit.setAdapter(sAdap);
	    
	    // Spinner for Category	by using the SimpleAdapter to manage the row and column
		final ArrayList<HashMap<String, String>> catList = myDb.SelectAll("Categories");
        sAdap = new SimpleAdapter(this, catList, R.layout.category_spinner,
                new String[] { "catName"}, new int[] {R.id.catName});
        spinCat = (Spinner) findViewById(R.id.spinCat);
	    spinCat.setAdapter(sAdap);
	    final EditText tPhe = (EditText) findViewById(R.id.txtPhe);
	    tPhe.setOnKeyListener(new OnKeyListener()
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
		                		// Go back to the ProductActivity which in the category has been added.
		                    	Intent newActivity = new Intent(AddProductActivity.this, ProductActivity.class);
		                    	// Pass the value of the cat(category) to ProductActivity
		                    	newActivity.putExtra("catName", cat);
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
	

	/**
	 * This is a SaveData method which records a name, an amount, an unit and a category of the product
	 * and then adding it to the database.
	 */
	public boolean SaveData()
	{
		final EditText tName = (EditText) findViewById(R.id.txtName);
		final Spinner spinner1 = (Spinner) findViewById(R.id.spinUnit);
		final Spinner spinner2 = (Spinner) findViewById(R.id.spinCat);
		final EditText tPhe = (EditText) findViewById(R.id.txtPhe);
		HashMap hash = (HashMap) spinner1.getSelectedItem();
		// Get the unitName from the spinner of Unit
		String unit = (String) hash.get("unitName");
		
		hash = (HashMap) spinner2.getSelectedItem();
		// Get the catName from the spinner of Category
		cat = (String) hash.get("catName");
				
		// Dialog
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		// Check tName is not null.
		if(tName.getText().length() == 0)
		{
            ad.setMessage("Please input [ProductName] or the [ProductName] is too long");
            ad.show();
            tName.requestFocus();
            return false;
		}	
			
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		
		// Check Data (If product exists)
		String arrData[] = myDb.SelectProduct(tName.getText().toString());
    	if(arrData != null)
    	{
    		ad.setMessage("Product already exists!  ");
    		ad.show();
    		tName.requestFocus();
   		 	return false; 
    	}
    		
    	// Save Data to the database
    	long saveStatus = myDb.insertProduct(tName.getText().toString()
    			,unit ,cat 
    			,Double.parseDouble(tPhe.getText().toString()));
    	
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
			getMenuInflater().inflate(R.menu.add_product, menu);
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
            	// Go back to the ProductActivity which in the category has been added.
            	Intent newActivity = new Intent(AddProductActivity.this, ProductActivity.class);
            	// Pass the value of the cat(category) to ProductActivity
            	newActivity.putExtra("catName", cat);
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
			View rootView = inflater.inflate(R.layout.fragment_add_product,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AddProductActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
