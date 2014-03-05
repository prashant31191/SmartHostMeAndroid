package com.smarthost;



import android.content.Context;
import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;
import com.smarthost.data.DataProcessor;
import com.smarthost.data.Listing;
import com.smarthost.loaders.ListingsLoader;
import com.smarthost.network.asynctasks.GetLocalListings;
import com.smarthost.services.GPSTracker;
import com.smarthost.ui.adapters.SHInfoWindowAdapter;

import java.util.List;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 11:17 PM
 */
public class SHMapActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Listing>>,
        GoogleMap.OnInfoWindowClickListener{

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, SHMapActivity.class);

        return i;
    }

    private GoogleMap googleMap;
    private ListingsLoader loader;

    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportLoaderManager().initLoader(0, null, this);

        try {
            // Loading map
            initilizeMap();

            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

            googleMap.setInfoWindowAdapter(new SHInfoWindowAdapter(getLayoutInflater()));
            googleMap.setOnInfoWindowClickListener(this);

            gps = new GPSTracker(SHMapActivity.this);

            // check if GPS enabled
            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                if(googleMap!=null){

                    CameraPosition me = new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                            .zoom(10.5f)
                            .bearing(0)
                            .tilt(0)
                            .build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(me), 1000, null);

                }
                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                getListings(latitude+", "+longitude);

            }else{
                gps.showSettingsAlert();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    private void getListings(String searchCriteria) {

        loader.setSearchQuery(searchCriteria);
        GetLocalListings getLocalListingsTask = new GetLocalListings(this, searchCriteria);
        DataProcessor.runProcess(getLocalListingsTask);



    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }



    @Override
    public Loader<List<Listing>> onCreateLoader(int id, Bundle args) {

        loader = new ListingsLoader(this, "");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Listing>> listLoader, List<Listing> listings) {

        if(listings!=null&&listings.size()>0){

            googleMap.clear();

            for (Listing listing : listings) {
                if(!(TextUtils.isEmpty(listing.getLongitude())||TextUtils.isEmpty(listing.getLatitude()))){
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.valueOf(listing.latitude), Double.valueOf(listing.longitude))).title(listing.latitude+", "+listing.longitude).snippet(listing.json);
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                    googleMap.addMarker(marker);
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Listing>> listLoader) {

    }


    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}