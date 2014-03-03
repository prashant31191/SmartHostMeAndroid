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
//        final IntentFilter filter = new IntentFilter();
//        filter.addAction(GetCityListingsCommand.SUCCESSFUL_LISTINGS);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

    }


    @Override
    public void onPause() {
        super.onPause();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);

    }

//
//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, final Intent intent) {
//
//
//            String text = intent.getExtras().getString(GetCityListingsCommand.LISTINGS, "Broken link");
//
//            JsonReader reader = new JsonReader(new StringReader(text));
//
//            ArrayList<Listing> listings = new ArrayList<Listing>();
//
//            try {
//                reader.beginArray();
//                while (reader.hasNext()){
//                  listings.add((Listing) gson.fromJson(reader, Listing.class));
//                }
//                reader.endArray();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            int total = 0;
//            for (Listing listing : listings) {
//                total+= listing.getPrice();
//            }
//            total = total/listings.size();
//
//
//            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
//            EditText city = (EditText)getView().findViewById(R.id.searchEditText);
//
//            TextView listingResults = (TextView)getView().findViewById(R.id.listingResults);
//            listingResults.setText("In " + city.getText().toString() + " you should list your place for about: " +total);
//            listingResults.setVisibility(View.VISIBLE);
//
//            TextView amen = (TextView)getView().findViewById(R.id.amenities);
//
//            //amen.setText(listings.get(0).getAmenities().get(1));
//
//        }
//
//    };


    @Override
    public Loader<List<Listing>> onCreateLoader(int id, Bundle args) {

        loader = new ListingsLoader(getActivity(), searchQuery);
        return loader;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Listing>> listLoader, List<Listing> listings) {

        if(listings!=null&&listings.size()>0){

            getView().findViewById(R.id.progressBar).setVisibility(View.GONE);

            getView().findViewById(R.id.listingResults).setVisibility(View.VISIBLE);
            ((TextView)getView().findViewById(R.id.listingResults)).setText(listings.get(0).getAmenities());
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
                    searchQuery = searchCriteria.getText().toString().toLowerCase();
                    loader.setSearchQuery(searchQuery);

                    GetLocalListings deleteExerciseAsyncTask = new GetLocalListings(getActivity(), searchQuery);
                    DataProcessor.runProcess(deleteExerciseAsyncTask);


                    getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getActivity(), "Please enter a loctation to search for.", Toast.LENGTH_SHORT).show();
                }

        }
    }

}