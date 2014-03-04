package com.smarthost.util;

import android.app.Activity;
import com.smarthost.AppraiseActivity;
import com.smarthost.ListingsActivity;
import com.smarthost.R;


import java.util.ArrayList;
import java.util.List;

public class Navigation {

    public static final int COUNT = 2;

    public static final int
            ID_LISTINGS= 0,
            ID_APPRAISALS= ID_LISTINGS+ 1;


    public int id;
    public int title;
    public int icon;

    public Navigation(int id) {
        this.id = id;
    }

    public static List<Navigation> getItems() {
        List<Navigation> items = new ArrayList<Navigation>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            Navigation item = new Navigation(i);
            switch (item.id) {
                case ID_APPRAISALS:
                    item.title = R.string.appraisals;
                    item.icon = R.drawable.ic_launcher;
                    break;
                case ID_LISTINGS:
                    item.title = R.string.listings;
                    item.icon = R.drawable.ic_launcher;
                    break;

            }
            items.add(item);
        }
        return items;
    }

    public static int getId(Activity activity) {
        if (activity instanceof AppraiseActivity) {
            return Navigation.ID_APPRAISALS;
        } else if (activity instanceof ListingsActivity) {
            return Navigation.ID_LISTINGS;
        }
        return -1;
    }
}
