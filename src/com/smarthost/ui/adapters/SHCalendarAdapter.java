package com.smarthost.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.tyczj.extendedcalendarview.CalendarAdapter;

import java.util.Calendar;

/**
 * User: davidredding
 * Date: 3/3/14
 * Time: 1:26 PM
 */
public class SHCalendarAdapter extends CalendarAdapter {


    public SHCalendarAdapter(Context context, Calendar cal) {
        super(context, cal);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getPrevMonth() {
        return super.getPrevMonth();
    }

    @Override
    public int getMonth() {
        return super.getMonth();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public void refreshDays() {
        super.refreshDays();
    }
}
