package com.smarthost.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import co.touchlab.android.superbus.BusHelper;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.smarthost.R;
import com.smarthost.superbus.GetCityListingsCommand;
import com.tyczj.extendedcalendarview.ExtendedCalendarView;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 1:45 PM
 */
public class SHMapFragment extends SupportMapFragment implements View.OnClickListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    Gson gson;


    protected CalendarFragmentListener mListener;

    public interface CalendarFragmentListener{
        void buttonClicked();
    }

    public static SHMapFragment newInstance() {
        return new SHMapFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getMap();

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        final IntentFilter filter = new IntentFilter();
//        filter.addAction(GetCityListingsCommand.SUCCESSFUL_LISTINGS);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);

//    }


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