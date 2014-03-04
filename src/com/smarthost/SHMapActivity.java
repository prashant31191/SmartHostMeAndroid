package com.smarthost;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smarthost.data.DataProcessor;
import com.smarthost.data.Listing;
import com.smarthost.loaders.ListingsLoader;
import com.smarthost.network.asynctasks.GetLocalListings;
import com.smarthost.services.GPSTracker;

import java.util.List;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 11:17 PM
 */
public class SHMapActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<Listing>> {

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, SHMapActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

//
//            MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(getResources().getString(R.string.app_name));
//
//            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
//
//
//            googleMap.addMarker(marker);

//            CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(17.385044, 78.486671)).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            gps = new GPSTracker(SHMapActivity.this);

            // check if GPS enabled
            if(gps.canGetLocation()){

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

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
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.valueOf(listing.latitude), Double.valueOf(listing.longitude))).title(listing.latitude+", "+listing.longitude);
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
                    googleMap.addMarker(marker);
                }
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Listing>> listLoader) {

    }



}