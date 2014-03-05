package com.smarthost.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * User: davidredding
 * Date: 3/5/14
 * Time: 2:18 AM
 */
public class BadCallToServerDialogFragment extends DialogFragment {
    Context mContext;
    public BadCallToServerDialogFragment() {
        mContext = getActivity();
    }
    public BadCallToServerDialogFragment(Context context) {
        mContext = context;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Oh no!?");
        alertDialogBuilder.setMessage("Something bad happened, please try again later.?");
        //null should be your on click listener
        alertDialogBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return alertDialogBuilder.create();
    }
}
