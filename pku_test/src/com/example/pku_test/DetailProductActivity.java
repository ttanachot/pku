package com.example.pku_test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a DetailProductActivity class.
 * This class is to show the detail of product which user choose in the listview from the ProductActivity.
 * Also in this class, user can edit the detail of the product.
 */
public class DetailProductActivity extends ActionBarActivity implements
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
	// prodId - is the id of product
	private String prodId;

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class.
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_product);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		Intent intent = getIntent();
		String prodName = intent.getStringExtra("prodName");
		showData(prodName);
    	
	}
	
	/*
	 * This is a showData method which shows a name, a phe, a unit 
	 * and a category of the product on the layout.
	 */
	public void showData(String prodName)
	{
		// Get the edittexts
		final EditText tName = (EditText) findViewById(R.id.txtName);
		final EditText tPhe = (EditText) findViewById(R.id.txtPhe);	
		
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
		// Get the ArrayList of units from database 
		final ArrayList<HashMap<String, String>> unitList = myDb.SelectAll("Unit");
	
		// Use the SimpleAdapter to manage listview row and column
		SimpleAdapter sAdap;
		sAdap = new SimpleAdapter(this, unitList, R.layout.unit_spinner,
                new String[] {"unitName"}, new int[] {R.id.unitName}); 
		// Spinner for Unit
        Spinner spinUnit = (Spinner) findViewById(R.id.spinUnit);
	    spinUnit.setAdapter(sAdap);
	    // Spinner for Category	   
		final ArrayList<HashMap<String, String>> catList = myDb.SelectAll("Categories");
        sAdap = new SimpleAdapter(this, catList, R.layout.category_spinner,
                new String[] {"catName"}, new int[] {R.id.catName});
        Spinner spinCat = (Spinner) findViewById(R.id.spinCat);
	    spinCat.setAdapter(sAdap);
		
		String arrData[] = myDb.SelectProduct(prodName);
		// Check if product is not null
    	if(arrData != null)
    	{
    		prodId = arrData[0];
    		tName.setText(arrData[1]);
    		tPhe.setText(arrData[4]);
    	}
    	else{
    		tName.setText("");
    		tPhe.setText("");
    	}
    	
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
		                	if(UpdateData())
		                	{
		                		//Go back to the CatalogActivity
		                    	Intent newActivity = new Intent(DetailProductActivity.this,CatalogActivity.class);
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
		//actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.detail_product, menu);
			restoreActionBar();
			return true;
		}
		
		return super.onCreateOptionsMenu(menu);
	}

	
	/**
	 * This is a UpdateData method which would update 
	 * the change of product after user presses the accept button.
	 */
	public boolean UpdateData()
	{
		// Get all components in the view
		final EditText tName = (EditText) findViewById(R.id.txtName);
		final Spinner spinner1 = (Spinner) findViewById(R.id.spinUnit);
		final Spinner spinner2 = (Spinner) findViewById(R.id.spinCat);
		final EditText tPhe = (EditText) findViewById(R.id.txtPhe);
		HashMap hash = (HashMap) spinner1.getSelectedItem();
		String unit = (String) hash.get("unitName");
		hash = (HashMap) spinner2.getSelectedItem();
		String cat = (String) hash.get("catName");
		
		// Dialog
				final AlertDialog.Builder adb = new AlertDialog.Builder(this);
				AlertDialog ad = adb.create();
		// Initiate the database
		final myDBClass myDb = new myDBClass(this);
    		
    	 // Save Data and Check Data
    	long saveStatus = myDb.updateProduct(prodId,tName.getText().toString(),unit ,cat ,tPhe.getText().toString());
    	if(saveStatus <=  0)
    	{
            ad.setMessage("Error!! ");
            ad.show();
            return false;
    	}	
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
        case R.id.action_accept:
        	// If Save Complete
        	if(UpdateData())
        	{
        		//Go back to the CatalogActivity
            	Intent newActivity = new Intent(DetailProductActivity.this,CatalogActivity.class);
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
			View rootView = inflater.inflate(R.layout.fragment_detail_product,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DetailProductActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
