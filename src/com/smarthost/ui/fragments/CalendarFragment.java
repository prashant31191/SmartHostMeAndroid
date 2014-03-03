package com.smarthost.ui.fragments;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import co.touchlab.android.superbus.BusHelper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.smarthost.R;
import com.smarthost.data.Listing;
import com.smarthost.superbus.GetCityListingsCommand;
import com.tyczj.extendedcalendarview.CalendarAdapter;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 10:42 AM
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    Gson gson;


    protected CalendarFragmentListener mListener;

    public interface CalendarFragmentListener{
        void buttonClicked();
    }

    public static CalendarFragment newInstance() {
        return new CalendarFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (CalendarFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement ListingsFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getView();

        ExtendedCalendarView calendarView = (ExtendedCalendarView) root.findViewById(R.id.calendar);





    }

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
//                    listings.add((Listing) gson.fromJson(reader, Listing.class));
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
//            amen.setText(listings.get(0).getAmenities().get(1));
//
//        }
//
//    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchButton:
                EditText searchCriteria = (EditText) getView().findViewById(R.id.searchEditText);

                if(!TextUtils.isEmpty(searchCriteria.getText().toString())){
                    BusHelper.submitCommandAsync(getActivity(), new GetCityListingsCommand(searchCriteria.getText().toString()));
                    getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getActivity(), "Please enter a loctation to search for.", Toast.LENGTH_SHORT).show();
                }

        }
    }

}