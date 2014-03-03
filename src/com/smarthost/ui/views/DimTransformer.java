package com.smarthost.ui.views;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 10:48 AM
 */
public class DimTransformer implements ViewPager.PageTransformer {

    private static final int COLOR_VALUE = 204;

    @Override
    public void transformPage(View page, float position) {
        if (position != 0 && position != 1 && position != -1) {
            int color = Color.argb((int) Math.abs(100 * position), COLOR_VALUE, COLOR_VALUE,
                    COLOR_VALUE);
            page.setBackgroundColor(color);
        } else {
            page.setBackgroundResource(0);
        }
    }
}