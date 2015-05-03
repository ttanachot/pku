package com.example.pku_test;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
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
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is a HistoryActivity class.
 * This class is to view the record of the past.
 * Also in this class, user can add, edit, delete the record in the past.
 */
public class HistoryActivity extends ActionBarActivity implements
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
	// progressBar - is the progressbar on the view
	private ProgressBar progressBar;
	// myDb - is the database class
	final myDBClass myDb = new myDBClass(this);
	// FoodList - is the arraylist of food
	private ArrayList<HashMap<String, String>> FoodList;
	// date - is the date that specifies by user
	private String date;
	// check - is the counter to check when user press the forward and back button
	int check;

	/*
	 * This is the onCreate method which initiates by Android
	 * Also the method is modified to initiate the view of this class.
	 * Set up the listener of forward and back buttons
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		check = 0;
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// Get pass value of from the calendar
		Intent intent = getIntent();
		date = intent.getStringExtra("date");
		// Call init() to set up the view
		init();
		
		// Set up the listener for the goBack button
		final ImageButton goBack = (ImageButton) findViewById(R.id.prev);
        goBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	check--;
            	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
            	Calendar cal = Calendar.getInstance();
            	String [] args = date.split("/");
            	cal.set(Calendar.YEAR, Integer.parseInt(args[2]));
    			cal.set(Calendar.MONTH, Integer.parseInt(args[1])-1);
    			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(args[0]));
                cal.add(Calendar.DATE, check);    
        		TextView tDate = (TextView) findViewById(R.id.tDate);
        		String current = dateformat.format(cal.getTime());
            	tDate.setText(current);
            	// Refresh the view
            	date = current;
            	init();
            	
            }
        });
        
        // Set up the listener for the goFwd button
        final ImageButton goFwd = (ImageButton) findViewById(R.id.fwd);
        goFwd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	int run = check+1;
            	Calendar cal = Calendar.getInstance();
            	Date now = cal.getTime();
            	String [] args = date.split("/");
            	cal.set(Calendar.YEAR, Integer.parseInt(args[2]));
    			cal.set(Calendar.MONTH, Integer.parseInt(args[1])-1);
    			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(args[0]));
                cal.add(Calendar.DATE, check);
            	
                cal.add(Calendar.DATE, run);  
                Date dateSpecified = cal.getTime();
                
                // Check the date is before or today
            	if(dateSpecified.before(now) || dateSpecified.equals(now)){
	            	check++;
	            	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy"); 
	        		TextView tDate = (TextView) findViewById(R.id.tDate);
	        		String current = dateformat.format(cal.getTime());
	            	tDate.setText(current);
	            	date = current;
	            	// Refresh the view
	            	init();
            	}
            }
        });
	}
	
	/*
	 * This is an init method which sets up the layout.
	 * Especially to update the date, progressbar, listview, and phe
	 */
	public void init(){
		check = 0;
		TextView tDate = (TextView) findViewById(R.id.tDate);
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
		TextView tPhe = (TextView) findViewById(R.id.tPhe);
		final myDBClass myDb = new myDBClass(this);
		ArrayList<HashMap<String, String>> List =  myDb.SelectAll("Info");
		String Data = List.get(0).get("pheMax");
		// Set the progressbar
		progressBar.setProgress(200);
		progressBar.setMax(Integer.parseInt(Data));
		
		SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
		String td = dateformat.format(new Date()).toString();
		if(td.equals(date)){
			tDate.setText("     Today     ");
		}
		else{
			tDate.setText(date);
		}
		// Get the food from database
		FoodList = myDb.SelectLogByDate(date);
		ArrayList<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
		for(HashMap<String, String> t: FoodList){
			String t2;
			if(t.get("foodName").length() > 10){
				t2 = t.get("foodName").substring(0, 10) + "...";
			}else{
				t2 = t.get("foodName");
			}
			HashMap<String, String> t3 = new HashMap<String, String>();
			t3.put("foodName", t2);
			t3.put("amount", t.get("amount"));
			t3.put("abberviation", t.get("abberviation"));
			t3.put("phe", t.get("phe"));
			t3.put("time", t.get("time"));
			temp.add(t3);
		}
		
		ListView lisView1 = (ListView)findViewById(R.id.listView1); 
        
        SimpleAdapter sAdap;
        sAdap = new SimpleAdapter(HistoryActivity.this, temp, R.layout.log_column,
                new String[] {"foodName","amount","abberviation","phe","time"}, new int[] { R.id.foodName, R.id.amount, R.id.abberviation,R.id.phe,R.id.time});      
        lisView1.setAdapter(sAdap);
        registerForContextMenu(lisView1);
        
        // Calculate the total PHE from food
        double total = 0;
        for(int i =0;i<FoodList.size();i++){
        	total+= Double.parseDouble(FoodList.get(i).get("phe"));
        }
        
        // Set the total PHE on the progressBar
        progressBar.setProgress((int)total);
        if(total > Integer.parseInt(Data)){
        	Drawable drawable = progressBar.getProgressDrawable();
        	drawable.setColorFilter(new LightingColorFilter(0xFF0000, 0xFF0000));

            AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();

            // Setting Dialog Title
            alertDialog.setTitle("Your PHE");

            // Setting Dialog Message
            alertDialog.setMessage("is over the limit.");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
            });
            // Showing Alert Message
            alertDialog.show();
        } else{
        	Drawable drawable = progressBar.getProgressDrawable();
        	drawable.setColorFilter(new LightingColorFilter(0x00FFFF, 0x808080));
        }
        
        // Decorate the text decimal
        DecimalFormat df = new DecimalFormat("#.00");
        if(total == 0){
        	tPhe.setText("0.00"+"/"+df.format(Double.parseDouble(Data)));
        }else{
        	tPhe.setText(df.format(total)+"/"+df.format(Double.parseDouble(Data)));
        }
        
        // Set the listener for the listview to go to the EditLogActivity
        lisView1.setOnItemClickListener(new OnItemClickListener() {
		      public void onItemClick(AdapterView<?> myAdapter, View myView, int position, long mylng) {
		    	  	// Pass by value to the EditLogActivity
	            	Intent newActivity = new Intent(HistoryActivity.this,EditLogActivity.class);
	            	newActivity.putExtra("date", date);
	            	newActivity.putExtra("logId", FoodList.get(position).get("logId").toString());
	            	newActivity.putExtra("foodName", FoodList.get(position).get("foodName").toString());
	            	newActivity.putExtra("type", FoodList.get(position).get("type").toString());
	            	newActivity.putExtra("amount", FoodList.get(position).get("amount").toString());
	            	startActivity(newActivity);
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
		String logId = FoodList.get(info.position).get("logId").toString();
	    
	    // Check Event Command
        if ("Edit".equals(CmdName)) {
          	Intent newActivity = new Intent(HistoryActivity.this,EditLogActivity.class);
          	newActivity.putExtra("date", date);
        	newActivity.putExtra("logId", FoodList.get(info.position).get("logId").toString());
        	newActivity.putExtra("foodName", FoodList.get(info.position).get("foodName").toString());
        	newActivity.putExtra("type", FoodList.get(info.position).get("type").toString());
        	newActivity.putExtra("amount", FoodList.get(info.position).get("amount").toString());
        	startActivity(newActivity);
        } else if ("Delete".equals(CmdName)) {
	       	 myDb.deleteLog(logId);
	       	 init();
        }
    	return true;
    }
    
    /*
     * This is a goToAdd method which is to go to MenuActivity.
     */
    public void goToAdd(){
		Intent newActivity = new Intent(HistoryActivity.this,MenuActivity.class);
		newActivity.putExtra("date", date);
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
			getMenuInflater().inflate(R.menu.history, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_history,
					container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((HistoryActivity) activity).onSectionAttached(getArguments()
					.getInt(ARG_SECTION_NUMBER));
		}
	}

}
