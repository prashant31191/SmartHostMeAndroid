package com.smarthost.data;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import com.google.android.maps.*;

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

    public void addOverlay(OverlayItem overlayItem){
        overlayItems.add(overlayItem);
        populate();
    }


    @Override
    protected boolean onTap(int i) {

        OverlayItem item = overlayItems.get(i);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(item.getTitle());
        builder.setMessage(item.getSnippet());
        builder.show();

        return true;
    }
}
