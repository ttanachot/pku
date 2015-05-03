package com.example.pku_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This class is DemoActivity class.
 * This class is to show a notification bar after the alarm had alert.
 *
 */
public class DemoActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);	
				
		/** Creating an Alert Dialog Window */
		AlertDemo alert = new AlertDemo();
		
		Notify("PKU: Alarm", "Please get something to eat..");
		
		/** Opening the Alert Dialog Window */
		alert.show(getSupportFragmentManager(), "AlertDemo");		
	}
	
	 @SuppressWarnings("deprecation")
	 private void Notify(String notificationTitle, String notificationMessage) {
	  NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//	  @SuppressWarnings("deprecation"
	  Notification notification = new Notification(R.drawable.ic_launcher,
	    "New Message", System.currentTimeMillis());

	   Intent notificationIntent = new Intent(this, MainActivity.class);
	  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
	    notificationIntent, 0);

	   notification.setLatestEventInfo(DemoActivity.this, notificationTitle,
	    notificationMessage, pendingIntent);
	  
	  notificationManager.notify(9999, notification);
	 }
}
