package com.smarthost;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.smarthost.services.GPSTracker;
import com.smarthost.ui.adapters.ListingsPagerAdapter;
import com.smarthost.ui.fragments.CalendarFragment;
import com.smarthost.ui.fragments.ListingsFragment;
import com.smarthost.util.TLog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 2:52 PM
 */
public class ListingsActivity extends BaseActivity implements
        ListingsFragment.ListingsFragmentListener,
        CalendarFragment.CalendarFragmentListener{

    String title;
    private ListingsPagerAdapter pagerAdapter;

    GPSTracker gps;

    public static String GOT_LOCATION = "got_location";
    public static String ADDRESS = "address";

    private GoogleMap googleMap;


    private TextView gpsLocationView;
    private List<Address> addresses;
    private    String text;


    LocationListener locationListener;

    public static Intent getLaunchIntent(Context context) {
        Intent i = new Intent(context, ListingsActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        title = getString(R.string.listings);

        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);


        pagerAdapter = new ListingsPagerAdapter(getSupportFragmentManager(), this);
        initViews(title, pagerAdapter);


        gps = new GPSTracker(ListingsActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            final Intent intent = new Intent(GOT_LOCATION).putExtra(ADDRESS, latitude+", "+longitude);
            LocalBroadcastManager.getInstance(ListingsActivity.this).sendBroadcast(intent);

        }else{
            gps.showSettingsAlert();
        }


    }



    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener    {

        @Override
        public void onLocationChanged(Location loc){
            loc.getLatitude();
            loc.getLongitude();
            Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String text=(addresses!=null)?"City : "+addresses.get(0).getSubLocality()+"\n Country : "+addresses.get(0).getCountryName():"Unknown Location";

            String locationValue = "My current location is: "+ text;
            gpsLocationView.setText(locationValue);

            final Intent intent = new Intent(GOT_LOCATION).putExtra(ADDRESS, text);
            LocalBroadcastManager.getInstance(ListingsActivity.this).sendBroadcast(intent);

        }

        @Override
        public void onProviderDisabled(String provider){
            Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
        }

        @Override
        public void onProviderEnabled(String provider){
            Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){
        }
    }




    @Override
    public void buttonClicked() {

    }


}
