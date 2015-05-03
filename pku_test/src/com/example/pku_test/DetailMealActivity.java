package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is DetailMealActivity class.
 * This class shows user a detail of that particular meal that he or she select.
 * User can delete or add products into that meal. 
 * User can delete that meal in a "Delete All" button
 */
public class DetailMealActivity extends ActionBarActivity implements
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
	
	// productList - List of exist products in a meal
	private ArrayList<HashMap<String, String>> productList;
	
	// productList2 - List of new added product in a meal
	private ArrayList<HashMap<String, String>> productList2;
	
	// prodListT - Temporary List of new added product in a meal
	private ArrayList<HashMap<String,String>> prodListT;
	
	// phe - total number of PHE
	private double phe;
	
	//listView1 - ListView of the products in a meal
	private ListView listView1;
	
	// Cmd - a command name for deletion
	private String[] Cmd = {"Delete"};
	
	// myDb - use to access to the database class
	final myDBClass myDb = new myDBClass(this);
	
	// mealName - name of a meal
	private String mealName;

	
	/*
	 * This is a main method of this activity.
	 * This method create a detail of meal page and its products and amounts.
	 * 
	 * (non-Javadoc)
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_meal);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		Intent intent = getIntent();
		mealName = intent.getStringExtra("mealName");
		List<String> prodName = myDb.SelectMeal(mealName);
		TextView view = (TextView)findViewById(R.id.mealName);
		view.setText("  "+ mealName);
		
		
		// AddMealActivity	
		productList = new ArrayList<HashMap<String,String>>();
		productList2 = new ArrayList<HashMap<String,String>>();
		prodListT = new ArrayList<HashMap<String,String>>();
		final ArrayList<HashMap<String, String>> ProductList = myDb.SelectAllProd(); 
		
		
		for(int i = 0; i< prodName.size(); i+=2){
			// Add exist product to productList
			for(int j = 0; j<ProductList.size(); j++){
				if( ProductList.get(j).get("prodName").equals(prodName.get(i)) ){
					HashMap<String,String> has = new HashMap<String,String>();
					double pheCal = Double.parseDouble((String)ProductList.get(j).get("phePerUnit")) / Double.parseDouble((String)ProductList.get(j).get("commonSize"));
	        		pheCal = pheCal * Double.parseDouble(prodName.get(i+1));
	        		has.put("prodName", ProductList.get(j).get("prodName"));
	        		has.put("abberviation", ProductList.get(j).get("abberviation"));
	        		has.put("amount",prodName.get(i+1) );
	        		has.put("phePerUnit2", String.format("%.2f", pheCal) );
	        		phe += Double.parseDouble(has.get("phePerUnit2"));
	        		productList.add(has);
				}
			}
			
			// Add both exist products and new added product to prodListT
			productList2 = myDb.SelectAll("ProductTemp");
    		prodListT = productList2;
    		for(int j =productList.size()-1; j>=0 ; j--){
    			HashMap<String,String> temp = productList.get(j);
    			prodListT.add(0,temp);
    		}
		}
		
		// Show products of a meal
		listView1 = (ListView)findViewById(R.id.prodMealList); 
		SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(DetailMealActivity.this, prodListT, R.layout.meal_product_column,
                new String[] {"prodName", "amount", "abberviation", "phePerUnit2"}, new int[] { R.id.prodName, R.id.amount, R.id.abb, R.id.prodPHE });      
        listView1.setAdapter(sAdap);
		
        HashMap<String,String> hash = new HashMap<String,String>();
		hash.put("prodName", "Select Product");
		hash.put("commonSize", "amount");
		ProductList.add(0, hash);
        
		//SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(this, ProductList, R.layout.product_column,
        		new String[] {"prodName", "commonSize", "abberviation", "phePerUnit"}, new int[] { R.id.prodName, R.id.commonSize, R.id.abberviation, R.id.prodPHE });
        Spinner spin = (Spinner) findViewById(R.id.spinProd);
	    spin.setAdapter(sAdap);
	    spin.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> parent, View selectedItemView, int pos, long id) {
				// TODO Auto-generated method stub
				
				if(pos > 0){
					
					LayoutInflater layoutInflater = LayoutInflater.from(context);
	    	    	View promptView = layoutInflater.inflate(R.layout.meal_dialog, null);
	
	    	    	
	    	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    	    	alertDialogBuilder.setView(promptView);
	    	    	final EditText input = (EditText) promptView.findViewById(R.id.amount);
	    	    	input.requestFocus();
	    	        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	    	        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	    	    	
	    	    	alertDialogBuilder
		        	.setCancelable(false)
		        	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		        		public void onClick(DialogInterface dialog, int id) {
	    	        		final Spinner spinner1 = (Spinner) findViewById(R.id.spinProd);
	    	        		HashMap<String, String> hash = (HashMap) spinner1.getSelectedItem();
	    	        		double pheCal = Double.parseDouble((String)hash.get("phePerUnit")) / Double.parseDouble((String)hash.get("commonSize"));
	    	        		pheCal = pheCal * Double.parseDouble(input.getText().toString());
	    	        		hash.put("phePerUnit2", String.format("%.2f", pheCal));
	    	        		hash.put("amount" , input.getText().toString());
	    	        		
	    	        		long saveStatus = myDb.insertProductTemp((String)hash.get("prodName"),
	    	        				(String)hash.get("amount"), (String)hash.get("abberviation"), (String)hash.get("phePerUnit2"));

	    	        		productList2 = myDb.SelectAll("ProductTemp");
	    	        		prodListT = productList2;
	    	        		for(int i =productList.size()-1; i>=0 ; i--){
	    	        			HashMap<String,String> temp = productList.get(i);
	    	        			prodListT.add(0,temp);
	    	        		}
	    	        		
	    	        		
	    	        		
	    	        		listView1 = (ListView)findViewById(R.id.prodMealList); 
	    	                SimpleAdapter sAdap;
	    	                sAdap = new SimpleAdapter(DetailMealActivity.this, prodListT, R.layout.meal_product_column,
	    	                        new String[] {"prodName", "amount", "abberviation", "phePerUnit2"}, new int[] { R.id.prodName, R.id.amount, R.id.abb, R.id.prodPHE });      
	    	                listView1.setAdapter(sAdap);
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
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}});
	    registerForContextMenu(listView1);
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
		if(info.position <= productList.size()-1){
			String prodName = productList.get(info.position).get("prodName").toString();
			String amount = productList.get(info.position).get("amount");
			
  	       	 myDb.deleteMealProd(mealName, prodName, amount);
  	       	 finish();
  	       	 startActivity(getIntent());
			
		}else {
			String prodName = productList2.get(info.position).get("prodName").toString();
			String amount = productList2.get(info.position).get("amount");
			
			
  	       	myDb.deleteProductTemp( prodName, amount);
  	       	 finish();
  	       	 startActivity(getIntent());
		}
		
	   
    	return true;
    }
	
    /**
     * This method is to save selected products into a meal and create a meal.
     * After user have add or edit a product and its amount, then user can
     * press save for create a meal.
     * 
     * @return true if save successfully
     * @return false if not saved
     */
	private boolean SaveData() {
		final TextView mealNameTxt = (TextView) findViewById(R.id.mealName);
		String mealName = mealNameTxt.getText().toString();
		final AlertDialog.Builder adb = new AlertDialog.Builder(this);
		AlertDialog ad = adb.create();
		// save product to meal	
		long saveStatus = 0;
		productList2 = myDb.SelectAll("ProductTemp");
		for(int i = 0; i < productList2.size() ; i++){
			HashMap<String, String> temp = productList2.get(i);
			saveStatus = myDb.insertMeals(mealName.trim(), 
					(String)temp.get("prodName"), 
					Double.parseDouble((String)temp.get("amount")));
			
			if(saveStatus <=  0)
	    	{
				ad.setMessage("Unable to create a meal, Please try again. ");
	            ad.show();
	            return false;
	    	}
			 
		} 
		
		// Calculate total PHE
		for(int i = 0; i < productList2.size() ; i++){
			HashMap<String, String> temp = productList2.get(i);
			phe += Double.parseDouble((String)temp.get("phePerUnit2"));
		} 
		saveStatus = myDb.updateMealsPHE(mealName.trim(), phe); 
		
		return true;
	}
	
	/*
	 * A method to delete a particular meal.	
	 */
	public void deleteMeal(View view){
		myDb.deleteMeals(mealName);
		Intent newActivity = new Intent(this,MealActivity.class);
    	startActivity(newActivity);   
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
			getMenuInflater().inflate(R.menu.detail_meal, menu);
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
            	// Open Form Main
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
			View rootView = inflater.inflate(R.layout.fragment_detail_meal,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((DetailMealActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
