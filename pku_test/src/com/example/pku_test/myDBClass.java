package com.example.pku_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This is myDBClass class.
 * This class is to access and handle all database request.
 *
 */
public class myDBClass extends SQLiteOpenHelper {
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "pke_version_1";
 
    /** Constructor
     * Initialize of generate database
     */
	public myDBClass(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * This method is to create Table of the database
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// Create Products Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Products" + 
		          "(prodId INTEGER PRIMARY KEY," + 
		          " prodName TEXT(100)," + 
		          " unitName TEXT(100),"+
	    		  " catName TEXT(100),"+
		          " phePerUnit DOUBLE);");
	    
	    // Create Unit Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Unit"+ 
		          "(unitId INTEGER PRIMARY KEY," + 
		          " unitName TEXT(100)," + 
		          " abberviation TEXT(100),"+
	    		  " commonSize Double);");
	    
	    // Create Categories Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Categories"+ 
		          "(catId INTEGER PRIMARY KEY," + 
		          " catName TEXT(100));");
	    
	    // Create Meals Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Meals"+ 
		          "(mealId INTEGER PRIMARY KEY," + 
		          " mealName TEXT(100)," + 
		          " productName TEXT(100),"+
		          " AmountPerUnit DOUBLE);");

	    // Create MealsPHE Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS MealsPHE"
	    		+ "(mealPHEId INTEGER PRIMARY KEY,"
	    		+ "mealName TEXT(100),"
	    		+ "mealPHE DOUBLE);");
	    
	    /*
	     * Create ProductTemp Table
	     * Temporary product keeper	    
	     */
	    db.execSQL("CREATE TABLE IF NOT EXISTS ProductTemp"
	    		+ "(prodTempId INTEGER PRIMARY KEY,"
	    		+ "prodName TEXT(100),"
	    		+ "amount TEXT(100),"
	    		+ "abberviation TEXT(100),"
	    		+ "phePerUnit2 TEXT(100));");
	    
	    // Create Log Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Log"+ 
		          "(logId INTEGER PRIMARY KEY,"+ 
		          " date TEXT(100)," +
		          " time TEXT(100)," +
		          " foodId INTEGER," + 
		          " type TEXT(100)," +
	    		  " amount DOUBLE);" );
	 
	    // Create Log Table
	    db.execSQL("CREATE TABLE IF NOT EXISTS Info"+ 
		          "(infoId INTEGER PRIMARY KEY,"+ 
		          " pheMax DOUBLE);");
	    db.execSQL("INSERT INTO Info (pheMax) VALUES (500)");
	    
	   
//	    Log.d("CREATE TABLE","Create Table Successfully.1");
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Insert product by
	 * @param prodName - product name
	 * @param unitName - unit name
	 * @param catName - category name 
	 * @param phePerUnit - amount of PHE
	 * @return true if saved successfully
	 */
	public long insertProduct(String prodName,String unitName, String catName, double phePerUnit){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("prodName", prodName); 
	    	   Val.put("unitName", unitName);
	    	   Val.put("catName", catName);
	    	   Val.put("phePerUnit", phePerUnit);
			long rows = db.insert("Products", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}
	
	
	/**
	 * Insert a new added product of meal in to ProductTemp table
	 * @param prodName - product name
	 * @param amount - amount of unit
	 * @param abbreviation - abbreviation
	 * @param phePerUnit2 - amount of PHE
	 * @return true if saved successfully 
	 */
	public long insertProductTemp(String prodName,String amount, String abbreviation, String phePerUnit2){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("prodName", prodName); 
	    	   Val.put("amount", amount);
	    	   Val.put("abberviation", abbreviation);
	    	   Val.put("phePerUnit2", phePerUnit2);
			long rows = db.insert("ProductTemp", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}
	
	/**
	 * Insert Category
	 * @param catName - category name
	 * @return true if saved successfully 
	 */
	public long insertCatagories(String catName){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("catName", catName); 
			long rows = db.insert("Categories", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}
	
	/**
	 * Insert new created unit
	 * @param unitName - unit name
	 * @param abbreviation - abbreviation
	 * @param commonSize - common size
	 * @return true if saved successfully 
	 */
	public long insertUnit(String unitName, String abbreviation, Double commonSize){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("unitName", unitName); 
	    	   Val.put("abberviation", abbreviation);
	    	   Val.put("commonSize", commonSize);
			long rows = db.insert("Unit", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}

	/**
	 * Insert meal
	 * @param mealName - meal name
	 * @param productName - product name
	 * @param amountPerUnit - amount of unit
	 * @return true if saved successfully 
	 */
	public long insertMeals(String mealName, String productName, Double amountPerUnit){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("mealName", mealName); 
	    	   Val.put("productName", productName);
	    	   Val.put("amountPerUnit", amountPerUnit);
			long rows = db.insert("Meals", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}
	
	/**
	 * Insert Log or food per day
	 * @param date - date
	 * @param time - time
	 * @param foodId - id of a food
	 * @param type - type (meal or product)
	 * @param amount - amount of food
	 * @return true if saved successfully 
	 */
	public long insertLog(String date, String time, int foodId, String type, double amount){
		try {
			SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
			
	    	   ContentValues Val = new ContentValues();
	    	   Val.put("date", date); 
	    	   Val.put("time",time);
	    	   Val.put("foodId", foodId);
	    	   Val.put("type", type);
	    	   Val.put("amount", amount);
			long rows = db.insert("Log", null, Val);

			db.close();
			return rows; // return rows inserted.
	          
		 } catch (Exception e) {
		    return -1;
		 }
	}
	
	/**
	 * Update the maximum of PHE per day
	 * @param max - number of PHE
	 * @return true if saved successfully 
	 */
	public long updatePhe(String max){
				 try {
					
					SQLiteDatabase db;
		     		db = this.getWritableDatabase(); // Write Data
		     		
		            ContentValues Val = new ContentValues();
		            Val.put("pheMax", max);
		     
		            long rows = db.update("Info", Val, " infoId = ?",
		                    new String[] { String.valueOf("1") });
		            
		     		db.close();
		     		return rows; // return rows updated.
						
				 } catch (Exception e) {
					return -1;
				 }
		
	}
	
	/**
	 * Update product by
	 * @param prodId - id of product
	 * @param prodName - product name
	 * @param unitName - unit name
	 * @param catName - category name
	 * @param phePerUnit - amount of PHE
	 * @return true if saved successfully 
	 */
	public long updateProduct(String prodId, String prodName, String unitName, String catName, String phePerUnit){
		 try {
			
			SQLiteDatabase db;
    		db = this.getWritableDatabase(); // Write Data
    		
           ContentValues Val = new ContentValues();
           Val.put("prodName", prodName);
           Val.put("unitName", unitName);
           Val.put("catName", catName);
           Val.put("phePerUnit", phePerUnit);
    
           long rows = db.update("Products", Val, " prodId = ?",
                   new String[] { String.valueOf(prodId) });
           
    		db.close();
    		return rows; // return rows updated.
				
		 } catch (Exception e) {
			return -1;
		 }

	}
	
	/**
	 * Update log by
	 * @param logId - id of log
	 * @param time - time
	 * @param amount - amount of PHE
	 * @return true if saved successfully 
	 */
	public long updateLog(String logId,String time,String amount){
		 try {
			
			SQLiteDatabase db;
   		db = this.getWritableDatabase(); // Write Data
   		
          ContentValues Val = new ContentValues();
          Val.put("time", time);
          Val.put("amount", amount);
      
   
          long rows = db.update("Log", Val, " logId = ?",
                  new String[] { String.valueOf(logId) });
          
   		db.close();
   		return rows; // return rows updated.
				
		 } catch (Exception e) {
			return -1;
		 }

	}
	
	/**
	 * Delete product by
	 * @param prodId - product id
	 * @return true if saved successfully 
	 */
	public long deleteProduct(String prodId) {
		try {
			SQLiteDatabase db;
			db = this.getWritableDatabase(); // Write Data
			long rows = db.delete("Products", "prodId = ?",
					new String[] { String.valueOf(prodId) });
			db.close();
			return rows; // return rows deleted.
		} catch (Exception e) {
			return -1;
		}

	}

	/**
	 * Delete unit by
	 * @param unitId - unit id
	 * @return true if saved successfully 
	 */
	public long deleteUnit(String unitId) {
		try {
			SQLiteDatabase db;
			db = this.getWritableDatabase(); // Write Data
			long rows = db.delete("Unit", "unitId = ?",
					new String[] { String.valueOf(unitId) });
			db.close();
			return rows; // return rows deleted.

		} catch (Exception e) {
			return -1;
		}

	}
		
	/**
	 * Delete category by
	 * @param catId - category id
	 * @return true if saved successfully 
	 */
	public long deleteCategories(String catId) {
		try {
			SQLiteDatabase db;
			db = this.getWritableDatabase(); // Write Data
			long rows = db.delete("Categories", "catId = ?",
					new String[] { String.valueOf(catId) });
			db.close();
			return rows; // return rows deleted.
		} catch (Exception e) {
			return -1;
		}

	}

	/**
	 * Delete meal by
	 * @param mealName - meal name
	 * @return true if saved successfully 
	 */
		public long deleteMeals(String mealName) {
			try {
				SQLiteDatabase db;
				db = this.getWritableDatabase(); // Write Data
				long rows = db.delete("Meals", "mealName = ?",
						new String[] { String.valueOf(mealName) });
				rows = db.delete("MealsPHE", "mealName = ?",
						new String[] { String.valueOf(mealName) });
				db.close();
				return rows; // return rows deleted.
			} catch (Exception e) {
				return -1;
			}
		}
	
	/**
	 * Delete product in a meal
	 * @param mealName - meal name
	 * @param prodName - product name
	 * @param amount - amount of unit of a product
	 * @return true if saved successfully 
	 */
		public long deleteMealProd(String mealName, String prodName, String amount) {
				try {
					SQLiteDatabase db;
					db = this.getWritableDatabase(); // Write Data
					List<String> list = new ArrayList<String>();
					
					 db = this.getReadableDatabase(); // Read Data
						
					 String selectQuery = "SELECT mealId FROM Meals WHERE mealName=? AND productName=? AND amountPerUnit=?";
					 Cursor cursor = db.rawQuery(selectQuery, new String[] { mealName, prodName, amount });
					 
					 	if(cursor != null)
					 	{
							if (cursor.moveToFirst()) {
								list.add(cursor.getString(cursor.getColumnIndex("mealId")));
							}
					 	}
					 	cursor.close();
//					 	db.close();
					 	db = this.getReadableDatabase();
					 	long rows = db.delete("Meals", "mealId=?",
								new String[] { list.get(0) });
					 	
						db.close();
					
					
					return rows; // return rows deleted.
				} catch (Exception e) {
					return -1;
				}
			}
	/**
	 * Delete log by
	 * @param logId - log id
	 * @return true if saved successfully 
	 */
		public long deleteLog(String logId) {
			try {
				SQLiteDatabase db;
				db = this.getWritableDatabase(); // Write Data
				long rows = db.delete("Log", "logId = ?",
						new String[] { String.valueOf(logId) });
				db.close();
				return rows; // return rows deleted.
			} catch (Exception e) {
				return -1;
			}
		}
		
		/**
		 * Delete all new added products
		 * @return true if saved successfully 
		 */
		public long deleteAllProductTemp() {
			try {
				SQLiteDatabase db;
				db = this.getWritableDatabase(); // Write Data
				long rows = db.delete("ProductTemp", null, null);
				db.close();
				return rows; // return rows deleted.
			} catch (Exception e) {
				return -1;
			}
		}
		
		/**
		 * Delete new added product by
		 * @param prodName - product name
		 * @param amount - amount of unit
		 * @return true if saved successfully 
		 */
		public long deleteProductTemp( String prodName, String amount) {
			try {
				SQLiteDatabase db;
				db = this.getWritableDatabase(); // Write Data
//				long rows = db.delete("Meals", "productName = ? AND amountPerUnit = ? Limit1",
//						new String[] { String.valueOf(prodName), amount });
				List<String> list = new ArrayList<String>();
				
				 db = this.getReadableDatabase(); // Read Data
					
				 String selectQuery = "SELECT prodTempId FROM ProductTemp WHERE prodName=? AND amount=?";
				 Cursor cursor = db.rawQuery(selectQuery, new String[] { prodName, amount });
				 
				 	if(cursor != null)
				 	{
						if (cursor.moveToFirst()) {
							list.add(cursor.getString(cursor.getColumnIndex("prodTempId")));
						}
				 	}
				 	cursor.close();
//				 	db.close();
				 	db = this.getReadableDatabase();
				 	long rows = db.delete("ProductTemp", "prodTempId=?",
							new String[] { list.get(0) });
				 	
					db.close();
				
				
				return rows; // return rows deleted.
			} catch (Exception e) {
				return -1;
			}
		}
		
		
		/**
		 * Select product by
		 * @param prodName - product name
		 * @return String[] of product's information
		 */
		public String[] SelectProduct(String prodName) {
			
			 try {
				String arrData[] = null;	
				
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				 Cursor cursor = db.query("Products", new String[] { "*" }, 
						 	"prodName=?",
				            new String[] { String.valueOf(prodName) }, null, null, null, null);
				 
				 	if(cursor != null)
				 	{
						if (cursor.moveToFirst()) {
							arrData = new String[cursor.getColumnCount()];
							arrData[0] = cursor.getString(0);
							arrData[1] = cursor.getString(1);
							arrData[2] = cursor.getString(2);
							arrData[3] = cursor.getString(3);
							arrData[4] = cursor.getString(4);
						}
				 	}
				 	cursor.close();
					db.close();
					return arrData;
					
			 } catch (Exception e) {
			    return null;
			 }

		}

		/**
		 * Select meal by
		 * @param mealName - meal name
		 * @return List<String> of meal's information
		 */
			public List<String> SelectMeal(String mealName) {
					
				 try {
						 List<String> list = new ArrayList<String>();
						
						 SQLiteDatabase db;
						 db = this.getReadableDatabase(); // Read Data
							
						 String selectQuery = "SELECT productName, AmountPerUnit FROM Meals WHERE mealName=?";
						 Cursor cursor = db.rawQuery(selectQuery, new String[] { mealName });
						 
						 	if(cursor != null)
						 	{
						 		if (cursor.moveToFirst()) {
						            do {
						                list.add(cursor.getString(cursor.getColumnIndex("productName")));
						                list.add(cursor.getString(cursor.getColumnIndex("AmountPerUnit")));
						            } while (cursor.moveToNext());
						        }
						 	}
						 	cursor.close();
							db.close();
							return list;
							
					 } catch (Exception e) {
					    return null;
					 }

				}
			
			/**
			 * Select meal by
			 * @param mealName - meal name
			 * @return String[] of meal's information
			 */
			public String[] SelectIdMeal(String mealName) {
				
			 try {
					String arrData[] = null;
					
					 SQLiteDatabase db;
					 db = this.getReadableDatabase(); // Read Data
						
					Cursor cursor = db.query("Meals", new String[] { "*" }, 
					 	"mealName=?",
			            new String[] { String.valueOf(mealName) }, null, null, null, null);
			 
			 	if(cursor != null)
			 	{
					if (cursor.moveToFirst()) {
						arrData = new String[cursor.getColumnCount()];
						arrData[0] = cursor.getString(0);
					}
			 	}
			 	cursor.close();
				db.close();
				return arrData;
						
				 } catch (Exception e) {
				    return null;
				 }

			}
			
			
		/**
		 * Insert total meal's PHE 
		 * @param mealName - meal name
		 * @param mealPHE - total number of PHE
		 * @return true if saved successfully
		 */
		public long insertMealsPHE(String mealName, double mealPHE){
			try {
				SQLiteDatabase db;
		    		db = this.getWritableDatabase(); // Write Data
				
		    	   ContentValues Val = new ContentValues();
		    	   Val.put("mealName", mealName); 
		    	   Val.put("mealPHE", mealPHE);
				long rows = db.insert("MealsPHE", null, Val);

				db.close();
				return rows; // return rows inserted.
		          
			 } catch (Exception e) {
			    return -1;
			 }
		}

		/**
		 * Update meal's PHE
		 * @param mealName - meal name
		 * @param mealPHE - total number of PHE
		 * @return true if saved successfully
		 */
		public long updateMealsPHE(String mealName, double mealPHE){
			 try {
				
				SQLiteDatabase db;
	    		db = this.getWritableDatabase(); // Write Data
	    		
	           ContentValues Val = new ContentValues();
	           Val.put("mealName", mealName);
	           Val.put("mealPHE", mealPHE);
	    
	           long rows = db.update("MealsPHE", Val, " mealName = ?",
	                   new String[] { mealName });
	           
	    		db.close();
	    		return rows; // return rows updated.
					
			 } catch (Exception e) {
				return -1;
			 }

		}

		/**
		 * Select all MealsPHE Table
		 * @return ArrayList of MealsPHE
		 */
		public ArrayList<HashMap<String, String>> SelectAllMealsPHE(){
						String [] tab = {"mealName", "mealPHE"};
						
						 try {
							 
							 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
							 HashMap<String, String> map;
							 
							 SQLiteDatabase db;
							 db = this.getReadableDatabase(); // Read Data
								
							 String strSQL = "SELECT  mealName, mealPHE FROM MealsPHE ORDER BY mealName ASC";
							 Cursor cursor = db.rawQuery(strSQL, null);
							 
							 	if(cursor != null)
							 	{
							 	    if (cursor.moveToFirst()) {
							 	        do {
							 	        	map = new HashMap<String, String>();
							 	        	for(int i = 0;i<tab.length;i++){
							 	        		map.put(tab[i], cursor.getString(i));
							 	        	}
								 	        MyArrList.add(map);
							 	        } while (cursor.moveToNext());
							 	    }
							 	}
							 	cursor.close();
							 	db.close();
								return MyArrList;
								
						 } catch (Exception e) {
						    return null;
						 }
		}
		
		/**
		 * Select all from 
		 * @param table - table name
		 * @return ArrayList of that table
		 */
		public ArrayList<HashMap<String, String>> SelectAll(String table) {
			String [] tab = {};
			String [] prod = {"prodId","prodName","unitName","catName","phePerUnit"};
			String [] unit = {"unitId","unitName","abberviation","commonSize"};
			String [] cat = {"catId","catName"};
			String [] meal = {"mealId", "mealName","productName", "AmountPerUnit"};
			String [] info = {"infoId","pheMax"};
			String [] prodTemp = {"prodTempId", "prodName", "amount", "abberviation", "phePerUnit2"};

			if(table=="Products"){
				tab = prod;
			}
			else if(table=="Unit"){
				tab = unit;
			}
			else if(table=="Categories"){
				tab = cat;
			}
			else if(table=="Meals"){
				tab = meal;
			}
			else if(table=="Info"){
				tab = info;
			}else if(table=="ProductTemp"){
				tab = prodTemp;
			}
			 try {
				 
				 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				 HashMap<String, String> map;
				 
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				 String strSQL = "SELECT  * FROM "+table+" ORDER BY "+tab[1]+" ASC";
				 Cursor cursor = db.rawQuery(strSQL, null);
				 	if(cursor != null)
				 	{
				 	    if (cursor.moveToFirst()) {
				 	        do {
				 	        	map = new HashMap<String, String>();
				 	        	for(int i = 0;i<tab.length;i++){
				 	        		map.put(tab[i], cursor.getString(i));
				 	        	}
					 	        MyArrList.add(map);
				 	        } while (cursor.moveToNext());
				 	    }
				 	}
				 	cursor.close();
				 	db.close();
					return MyArrList;
					
			 } catch (Exception e) {
			    return null;
			 }
		}
		
		/**
		 * Select all Product
		 * @return ArrayList of all product
		 */
		public ArrayList<HashMap<String, String>> SelectAllProd() {
			 try {
				 
				 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				 HashMap<String, String> map;
				 
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				String [] test = {"prodId","prodName","unitName","phePerUnit","abberviation","commonSize"};
				String strSQL = "SELECT prodId,prodName,Products.unitName,phePerUnit,abberviation,commonsize "
						+ "FROM Products, Unit WHERE Products.unitName = Unit.unitName ORDER BY prodName ASC";
				 Cursor cursor = db.rawQuery(strSQL, null);
				 
				 	if(cursor != null)
				 	{
				 	    if (cursor.moveToFirst()) {
				 	        do {
				 	        	map = new HashMap<String, String>();
				 	        	for(int i = 0;i< test.length;i++){
				 	        		map.put(test[i], cursor.getString(i));
				 	        	}
				 	        	
					 	        MyArrList.add(map);
				 	        } while (cursor.moveToNext());
				 	    }
				 	}
				 	cursor.close();
				 	db.close();
					return MyArrList;
					
			 } catch (Exception e) {
			    return null;
			 }
		}
		
		/**
		 * Select all products in a category
		 * @param Category - category name
		 * @return ArrayList of products in a category
		 */
		public ArrayList<HashMap<String, String>> SelectCatalogProd(String Category) {
			 try {
				 
				 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				 HashMap<String, String> map;
				 
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				String [] test = {"prodId","prodName","unitName","phePerUnit","abberviation","commonSize"};
				String strSQL = "SELECT prodId,prodName,Products.unitName,phePerUnit,abberviation,commonsize "
						+ "FROM Products, Unit WHERE Products.unitName = Unit.unitName AND catName=? ORDER BY prodName ASC";
				 Cursor cursor = db.rawQuery(strSQL, new String[] { Category });
				 
				 	if(cursor != null)
				 	{
				 	    if (cursor.moveToFirst()) {
				 	        do {
				 	        	map = new HashMap<String, String>();
				 	        	for(int i = 0;i< test.length;i++){
				 	        		map.put(test[i], cursor.getString(i));
				 	        	}
				 	        	
					 	        MyArrList.add(map);
				 	        } while (cursor.moveToNext());
				 	    }
				 	}
				 	cursor.close();
				 	db.close();
					return MyArrList;
					
			 } catch (Exception e) {
			    return null;
			 }
		}
		
		/**
		 * Select Log
		 * @return ArrayList of log
		 */
		public ArrayList<HashMap<String, String>> SelectLog() {
			String [] food = {"logId","date","time", "foodName", "type", "amount", "abberviation","phePerUnit","commonSize","phe"};

			 try {
				 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				 HashMap<String, String> map;
				 
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				String strSQL = "SELECT * FROM Log ORDER BY time ASC";
				 Cursor cursor = db.rawQuery(strSQL, null);
				 
				 	if(cursor != null)
				 	{
				 	    if (cursor.moveToFirst()) {
				 	        do {
				 	        	if(cursor.getString(4).equals("product")){
				 	        		String sql = "SELECT Log.logId,Log.date,Log.time,Products.prodName,Log.type,Log.amount,Unit.abberviation,Products.phePerUnit,Unit.commonSize"
				 	        				+" FROM Log JOIN Products JOIN Unit WHERE Log.foodId=Products.prodId AND Products.unitName=Unit.unitName AND logId=? ";
				 					 Cursor cursor1 = db.rawQuery(sql, new String[]{cursor.getString(0)});
								 	    if (cursor1.moveToFirst()) {
								 	        do {
								 	        	map = new HashMap<String, String>();
								 	        	for(int i = 0;i< food.length-1;i++){
								 	        		map.put(food[i], cursor1.getString(i));
								 	        	}
								 	        	double phe = Double.parseDouble(cursor1.getString(5))
								 	        			*Double.parseDouble(cursor1.getString(7))/Double.parseDouble(cursor1.getString(8));
								 	        	map.put(food[9],""+phe);
								 	
									 	        MyArrList.add(map);
								 	        } while (cursor1.moveToNext());
								 	    }
				 	        	}
				 	        	
								if(cursor.getString(4).equals("meal")){
								 	    	String sql2 = "SELECT Log.logId,Log.date,Log.time,Meals.mealName,Log.type,MealsPHE.mealPHE"
						 	        				+" FROM Log JOIN Meals JOIN MealsPHE WHERE Log.foodId=Meals.mealId AND Meals.mealName=MealsPHE.mealName AND logId=? ";
						 					 Cursor cursor2 = db.rawQuery(sql2, new String[]{cursor.getString(0)});
										 	    if (cursor2.moveToFirst()) {
										 	        do {
										 	        	map = new HashMap<String, String>();
										 	        	
										 	        	map.put(food[0], cursor2.getString(0));
										 	        	map.put(food[1], cursor2.getString(1));
										 	        	map.put(food[2], cursor2.getString(2));
										 	        	map.put(food[3], cursor2.getString(3));
										 	        	map.put(food[4], cursor2.getString(4));
										 	        	map.put(food[5], "");
										 	        	map.put(food[6], "");
										 	        	map.put(food[7], "");
										 	        	map.put(food[8], "");
										 	        	map.put(food[9], cursor2.getString(5));
											 	        MyArrList.add(map);
										 	        } while (cursor2.moveToNext());
										 	    }
								}
				 	        	
				 	        } while (cursor.moveToNext());
				 	        
				 	    }
				 	}
				 	cursor.close();
				 	db.close();
					return MyArrList;
					
			 } catch (Exception e) {
			    return null;
			 }
		}
		
		/**
		 * Select log by date
		 * @param date - date
		 * @return ArrayList of Log in that date
		 */
		public ArrayList<HashMap<String, String>> SelectLogByDate(String date) {
			String [] food = {"logId","date","time", "foodName", "type", "amount", "abberviation","phePerUnit","commonSize","phe"};

			 try {
				 ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
				 HashMap<String, String> map;
				 
				 SQLiteDatabase db;
				 db = this.getReadableDatabase(); // Read Data
					
				String strSQL = "SELECT * FROM Log WHERE Log.date=? ORDER BY Log.time ASC";
				 Cursor cursor = db.rawQuery(strSQL, new String[] { date });
				 
				 	if(cursor != null)
				 	{
				 	    if (cursor.moveToFirst()) {
				 	        do {
				 	        	if(cursor.getString(4).equals("product")){
				 	        		String sql = "SELECT Log.logId,Log.date,Log.time,Products.prodName,Log.type,Log.amount,Unit.abberviation,Products.phePerUnit,Unit.commonSize"
				 	        				+" FROM Log JOIN Products JOIN Unit WHERE Log.foodId=Products.prodId AND Products.unitName=Unit.unitName AND logId=? ";
				 					 Cursor cursor1 = db.rawQuery(sql, new String[]{cursor.getString(0)});
								 	    if (cursor1.moveToFirst()) {
								 	        do {
								 	        	map = new HashMap<String, String>();
								 	        	for(int i = 0;i< food.length-1;i++){
								 	        		map.put(food[i], cursor1.getString(i));
								 	        	}
								 	        	double phe = Double.parseDouble(cursor1.getString(5))
								 	        			*Double.parseDouble(cursor1.getString(7))/Double.parseDouble(cursor1.getString(8));
								 	        	map.put(food[9],""+phe);
								 	
									 	        MyArrList.add(map);
								 	        } while (cursor1.moveToNext());
								 	    }
				 	        	}
				 	        	
								if(cursor.getString(4).equals("meal")){
								 	    	String sql2 = "SELECT Log.logId,Log.date,Log.time,Meals.mealName,Log.type,MealsPHE.mealPHE"
						 	        				+" FROM Log JOIN Meals JOIN MealsPHE WHERE Log.foodId=Meals.mealId AND Meals.mealName=MealsPHE.mealName AND logId=? ";
						 					 Cursor cursor2 = db.rawQuery(sql2, new String[]{cursor.getString(0)});
										 	    if (cursor2.moveToFirst()) {
										 	        do {
										 	        	map = new HashMap<String, String>();
										 	        	
										 	        	map.put(food[0], cursor2.getString(0));
										 	        	map.put(food[1], cursor2.getString(1));
										 	        	map.put(food[2], cursor2.getString(2));
										 	        	map.put(food[3], cursor2.getString(3));
										 	        	map.put(food[4], cursor2.getString(4));
										 	        	map.put(food[5], "");
										 	        	map.put(food[6], "");
										 	        	map.put(food[7], "");
										 	        	map.put(food[8], "");
										 	        	map.put(food[9], cursor2.getString(5));
											 	        MyArrList.add(map);
										 	        } while (cursor2.moveToNext());
										 	    }
								}
				 	        	
				 	        } while (cursor.moveToNext());
				 	        
				 	    }
				 	}
				 	cursor.close();
				 	db.close();
					return MyArrList;
					
			 } catch (Exception e) {
			    return null;
			 }
		}
}
