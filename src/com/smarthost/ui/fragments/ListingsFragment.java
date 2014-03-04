package com.smarthost.ui.fragments;

import android.app.Activity;


import android.content.*;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.*;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.touchlab.android.superbus.BusHelper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.smarthost.ListingsActivity;
import com.smarthost.R;
import com.smarthost.data.AllListings;
import com.smarthost.data.DataProcessor;
import com.smarthost.data.Listing;
import com.smarthost.loaders.ListingsLoader;
import com.smarthost.network.asynctasks.GetLocalListings;
import com.smarthost.superbus.GetCityListingsCommand;
import com.smarthost.util.TLog;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 3:39 PM
 */
public class ListingsFragment extends ListFragment implements View.OnClickListener,
        LoaderManager.LoaderCallbacks<List<Listing>>{


    public static final String TAG = SettingsFragment.class.getSimpleName();

    private static final String STATE_LIST_POSITION = "listViewPosition";
    private static final String STATE_LIST_OFFSET = "listViewOffset";

    Gson gson;
    private String searchQuery = "";

    private ListingsLoader loader;

    protected ListingsFragmentListener mListener;



    public interface ListingsFragmentListener{
        void buttonClicked();
    }

    public static ListingsFragment newInstance() {
        return new ListingsFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ListingsFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement ListingsFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_listings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getView();

        root.findViewById(R.id.searchButton).setOnClickListener(this);
        root.findViewById(R.id.listingResults).setVisibility(View.INVISIBLE);
        root.findViewById(R.id.progressBar).setVisibility(View.GONE);

//        if (savedInstanceState != null) {
//            mCurrentPosition = savedInstanceState.getInt(STATE_LIST_POSITION, -1);
//            mScrollOffset = savedInstanceState.getInt(STATE_LIST_OFFSET, 0);
//        } else {
//            mScrollOffset = getResources().getDimensionPixelOffset(R.dimen.default_margin);
//        }




        gson = new Gson();

        getLoaderManager().initLoader(0, null, this);


    }


//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        int index = getListView().getFirstVisiblePosition();
//        outState.putInt(STATE_LIST_POSITION, index);
//
//        View v = getListView().getChildAt(0);
//        int offset = (v == null) ? 0 : v.getTop();
//        outState.putInt(STATE_LIST_OFFSET, offset);
//    }
//


    @Override
    public void onResume() {
        super.onResume();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ListingsActivity.GOT_LOCATION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

    }


    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);

    }


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {


            String address = intent.getExtras().getString(ListingsActivity.ADDRESS);
            getListings(address.toLowerCase());

        }

    };


    @Override
    public Loader<List<Listing>> onCreateLoader(int id, Bundle args) {

        loader = new ListingsLoader(getActivity(), searchQuery);
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Listing>> listLoader, List<Listing> listings) {

        if(listings!=null&&listings.size()>0){

            int total = 0;
            int numCounted = 0;
            for (Listing listing : listings) {
                if(listing.price>0){
                    total+=listing.price;
                    numCounted++;
                }
            }

                 String avg = (total/numCounted)+"";

            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);

            getView().findViewById(R.id.listingResults).setVisibility(View.VISIBLE);
            ((TextView)getView().findViewById(R.id.listingResults)).setText("The average price \nin this area is: \n"+avg);
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Listing>> listLoader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                EditText searchCriteria = (EditText) getView().findViewById(R.id.searchEditText);

                if(!TextUtils.isEmpty(searchCriteria.getText().toString())){
                    getListings(searchCriteria.getText().toString().toLowerCase());
                }else{
                    Toast.makeText(getActivity(), "Please enter a loctation to search for.", Toast.LENGTH_SHORT).show();
                }

        }
    }

    private void getListings(String searchCriteria) {
        searchQuery = searchCriteria;
        loader.setSearchQuery(searchQuery);

        GetLocalListings getLocalListingsTask = new GetLocalListings(getActivity(), searchQuery);
        DataProcessor.runProcess(getLocalListingsTask);


        getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

}