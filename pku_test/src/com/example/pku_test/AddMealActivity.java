package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;


/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is AddMealActivity class.
 * This class will allow user to create a meal by 
 * naming a meal and add the existing products into a meal.
 * User can also delete an unwanted product from a meal in this activity.
 * 
 */
public class AddMealActivity extends ActionBarActivity implements
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
	
	// context - context of this activity
	final Context context = this;
	
	// productList - list of added products in a meal
	private ArrayList<HashMap<String, String>> productList;

	// Cmd - a command name for deletion
	private String[] Cmd = {"Delete"};
	
	// myDb - use to access to the database class
	final myDBClass myDb = new myDBClass(this);
	
	// lisView1 - ListView of all product in the database
	private ListView lisView1;
	
	/**
	 * This is a main method of this activity.
	 * This method will shows all the functions necessary for adding a meal.
	 * 
	 * ProductList - list of all products in the database
	 * productList - list of new added product in a meal
	 * sAdap - Adapter use to handle format in ListView
	 * lisView1 - list of added product in a meal
	 * spin - Spinner uses to select products to a meal
	 * alertDialogBuilder - a dialog for user to input amount of unit for particular product
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_meal);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		final ArrayList<HashMap<String, String>> ProductList = myDb.SelectAllProd(); 
		
		// Show list of added products
		SimpleAdapter sAdap;
		productList = myDb.SelectAll("ProductTemp");
        lisView1 = (ListView)findViewById(R.id.prodMealList);
        sAdap = new SimpleAdapter(AddMealActivity.this, productList, R.layout.meal_product_column,
                new String[] {"prodName", "amount", "abberviation", "phePerUnit2"}, new int[] { R.id.prodName, R.id.amount, R.id.abb, R.id.prodPHE });      
        lisView1.setAdapter(sAdap);
        
        // register all value in lisView1 for hold and press command
        registerForContextMenu(lisView1);
        
		HashMap<String,String> hash = new HashMap<String,String>();
		hash.put("prodName", "Select Product");
		hash.put("commonSize", "amount");
		ProductList.add(0, hash);
		
        // Show list of products for user to select
        sAdap = new SimpleAdapter(this, ProductList, R.layout.product_column,
                new String[] {"prodName", "commonSize", "abberviation", "phePerUnit"}, new int[] { R.id.prodName, R.id.commonSize, R.id.abberviation, R.id.prodPHE });      
        Spinner spin = (Spinner) findViewById(R.id.spinProd);
        spin.setAdapter(sAdap);
	    spin.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {

    	    	if(pos > 0){
    	    	
	    	    	LayoutInflater layoutInflater = LayoutInflater.from(context);
	    	    	View promptView = layoutInflater.inflate(R.layout.meal_dialog, null);
	
	    	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    	    	alertDialogBuilder.setView(promptView);
	    	    	final EditText input = (EditText) promptView.findViewById(R.id.amount);
	    	    	
	    	    	alertDialogBuilder
	    	        	.setCancelable(false)
	    	        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    	        		public void onClick(DialogInterface dialog, int id) {
		    	        		exeDialog(input, ProductList);
	    	        		}
	    	        	})
	    	        	.setNegativeButton("Cancel",
	    	        			new DialogInterface.OnClickListener() {
	    	        				public void onClick(DialogInterface dialog, int id) {
	    	        					dialog.cancel();
	    	        				}
	    	        	});
	    	    	AlertDialog alertD = alertDialogBuilder.create();
	    	    	alertD.show();

    	    	}
    	    	
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parentView) {
    	        // your code here
    	    }
    	});
	   
	}
	
	/**
	 * This method will be execute when user enter the amount in a dialog and
	 * press OK button. This method will calculate a number of PHE of that product 
	 * and save it into a temporary product's database ready to be added to a meal. 
	 * 
	 * @param input - amount of unit user input for particular product
	 * @param ProductList - all products in the database
	 */
	public void exeDialog(EditText input, ArrayList<HashMap<String,String>> ProductList){
		// Select item from spinner
		final Spinner spinner1 = (Spinner) findViewById(R.id.spinProd);
		HashMap<String, String> hash = (HashMap) spinner1.getSelectedItem();
		
		// Calculate number of PHE
		double pheCal = Double.parseDouble((String)hash.get("phePerUnit")) / Double.parseDouble((String)hash.get("commonSize"));
		pheCal = pheCal * Double.parseDouble(input.getText().toString());
		hash.put("phePerUnit2", String.format("%.2f", pheCal));
		hash.put("amount" , input.getText().toString());

		long saveStatus = myDb.insertProductTemp((String)hash.get("prodName"),
				(String)hash.get("amount"), (String)hash.get("abberviation"), (String)hash.get("phePerUnit2"));
		
		// Add to temporary product's database
		productList = myDb.SelectAll("ProductTemp");
		lisView1 = (ListView)findViewById(R.id.prodMealList); 
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(AddMealActivity.this, productList, R.layout.meal_product_column,
                new String[] {"prodName", "amount", "abberviation", "phePerUnit2"}, new int[] { R.id.prodName, R.id.amount, R.id.abb, R.id.prodPHE });      
        lisView1.setAdapter(sAdap);
        
     // register all value in lisView1 for hold and press command
        registerForContextMenu(lisView1);
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
	 * which is a delete product command in a meal. 
	 * 
	 * menuItems - all commands in a dialog
	 * CmdName - selected command
	 * prodName - name of product that user want to delete
	 * amount - amount of unit of that particular product
	 */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
		String[] menuItems = Cmd;
		String CmdName = menuItems[menuItemIndex];
		if ("Delete".equals(CmdName)) {
			
			String prodName = productList.get(info.position).get("prodName").toString();
			String amount = productList.get(info.position).get("amount");

  	       	myDb.deleteProductTemp( prodName, amount);
  	       	finish();
  	       	startActivity(getIntent());
		}
		
    	return true;
    }
	
    /**
     * This method is to save selected products into a meal and create a meal.
     * After user have added a product and its amount, and named a meal, then user can
     * press save for create a meal.
     * 
     * @return true if save successfully
     * @return false if not saved
     */
	private boolean SaveData() {
		final EditText mealNameTxt = (EditText) findViewById(R.id.mealName);
		String mealName = mealNameTxt.getText().toString();
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		
		// save products to a meal and calculate a total PHE
		long saveStatus = 0;
		double phe = 0;
		productList = myDb.SelectAll("ProductTemp");
		for(int i = 0; i < productList.size() ; i++){
			HashMap<String, String> temp = productList.get(i);
			saveStatus = myDb.insertMeals(mealName, 
					(String)temp.get("prodName"), 
					Double.parseDouble((String)temp.get("amount")));
			ad.setMessage(mealName+(String)temp.get("prodName")+Double.parseDouble((String)temp.get("phePerUnit2")));
            ad.show();
			
			phe += Double.parseDouble((String)temp.get("phePerUnit2"));
			
			if(saveStatus <=  0)
	    	{
	            ad.setMessage("Unable to create a meal, Please try again. ");
	            ad.show();
	            return false;
	    	}
		} 
		saveStatus = myDb.insertMealsPHE(mealName, phe); 
		
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
//		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.add_meal, menu);
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
            	Intent newActivity = new Intent(this,MealActivity.class);
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
			View rootView = inflater.inflate(R.layout.fragment_add_meal,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((AddMealActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
