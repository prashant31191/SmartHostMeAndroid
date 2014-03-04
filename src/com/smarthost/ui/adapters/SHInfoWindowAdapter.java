package com.smarthost.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.smarthost.R;

/**
 * User: davidredding
 * Date: 3/4/14
 * Time: 1:31 AM
 */
public class SHInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {



    LayoutInflater inflater=null;

    public SHInfoWindowAdapter (LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return(null);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup=inflater.inflate(R.layout.marker_listing, null);

        //Parse JSON and make a pretty popup


        TextView address = (TextView)popup.findViewById(R.id.address);
        address.setText(marker.getId());
        TextView price = (TextView)popup.findViewById(R.id.price);
        price.setText(marker.getSnippet());
        TextView bedrooms= (TextView)popup.findViewById(R.id.bedrooms);
        bedrooms.setText(marker.getTitle());


        return(popup);
    }
}

