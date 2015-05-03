package com.example.pku_test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.WindowManager.LayoutParams;
 
/**
 * Developed by Nuttapon Phannurat and Tanachot Techajarupan
 * @date 2014
 * 
 * This is a AlertDemo class.
 * This class provide an alert dialog for an alarm.
 *
 */
public class AlertDemo extends DialogFragment {
 
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
 
        /** Turn Screen On and Unlock the keypad when this alert dialog is displayed */
        getActivity().getWindow().addFlags(LayoutParams.FLAG_TURN_SCREEN_ON | LayoutParams.FLAG_DISMISS_KEYGUARD);
 
        /** Creating a alert dialog builder */
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
 
        /** Setting title for the alert dialog */
        builder.setTitle("PKU Alarm");
 
        /** Setting the content for the alert dialog */
        builder.setMessage("Please get something to eat..");
 
        /** Defining an OK button event listener */
        builder.setPositiveButton("OK", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /** Exit application on click OK */
                getActivity().finish();
            }
        });
 
        /** Creating the alert dialog window */
        return builder.create();
    }
 
    /** 
     * The application should be exit, if the user presses the back button 
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }
}
