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
 * Time: 3:39 PM
 */
public class AppraiseFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = SettingsFragment.class.getSimpleName();

    protected AppraiseFragmentListener mListener;

    public interface AppraiseFragmentListener{
        void buttonClicked();
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AppraiseFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement SettingsFragment.SettingsItemCallback");
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