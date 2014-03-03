package com.smarthost.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.smarthost.R;

/**
 * User: davidredding
 * Date: 3/2/14
 * Time: 4:03 PM
 */
public class MyFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    protected MyFragmentListner mListener;

    public interface MyFragmentListner{
        void buttonClicked();
    }

    public static MyFragment newInstance() {
        return new MyFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MyFragmentListner) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement MyFragmentListner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getView();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}