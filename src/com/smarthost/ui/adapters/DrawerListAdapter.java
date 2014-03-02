package com.smarthost.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.smarthost.R;
import com.smarthost.util.Navigation;

import java.util.List;

public class DrawerListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Navigation> mItems;

    public DrawerListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mItems = Navigation.getItems();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = null;

        if (convertView == null) {
            view = (TextView) mInflater.inflate(R.layout.item_drawer, parent, false);
        } else {
            view = (TextView) convertView;
        }

        Navigation item = mItems.get(position);
        view.setText(item.title);
        view.setCompoundDrawablesWithIntrinsicBounds(item.icon, 0, 0, 0);

        return view;
    }

    @Override
    public int getCount() {
        return Navigation.COUNT;
    }

    @Override
    public Navigation getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).id;
    }
}