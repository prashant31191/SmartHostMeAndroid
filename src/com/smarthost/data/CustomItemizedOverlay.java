package com.smarthost.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 10:22 PM
 */
public class CustomItemizedOverlay extends ItemizedOverlay<OverlayItem> {


    private ArrayList<OverlayItem> overlayItems = new ArrayList<OverlayItem>();
    Context context;

    public CustomItemizedOverlay(Drawable drawable, Context context) {
        super(boundCenterBottom(drawable));
        this.context = context;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return overlayItems.get(i);
    }

    @Override
    public int size() {
        return overlayItems.size();
    }


}
