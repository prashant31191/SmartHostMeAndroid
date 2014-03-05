package com.smarthost.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.smarthost.AppPreferences;
import com.smarthost.R;

/**
 * User: davidredding
 * Date: 2/17/14
 * Time: 2:05 AM
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{

    public static final String TAG = SettingsFragment.class.getSimpleName();

    public static SettingsFragment newInstance() {
        return new SettingsFragment ();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getView();

        final EditText addressEdittext = (EditText) root.findViewById(R.id.addressEdittext);
        addressEdittext.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            AppPreferences.getInstance(getActivity()).setAddress(addressEdittext.getText().toString());
                            return false;
                        }
                        return false;
                    }
                });
        addressEdittext.setText(AppPreferences.getInstance(getActivity()).getAddress());

        final EditText bathroomsEdittext = (EditText) root.findViewById(R.id.bathroomsEdittext);
        bathroomsEdittext.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            AppPreferences.getInstance(getActivity()).setBathrooms(Integer.parseInt(bathroomsEdittext.getText().toString()));
                            return false;
                        }
                        return false;
                    }
                });
        bathroomsEdittext.setText(AppPreferences.getInstance(getActivity()).getBathrooms()+"");

        final EditText occupancyEdittext = (EditText) root.findViewById(R.id.occupancyEdittext);
        occupancyEdittext.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                            AppPreferences.getInstance(getActivity()).setOccupancy(Integer.parseInt(occupancyEdittext.getText().toString()));
                            return false;
                        }
                        return false;
                    }
                });
        occupancyEdittext.setText(AppPreferences.getInstance(getActivity()).getOccupancy()+"");

        Switch emptyRoom = (Switch) root.findViewById(R.id.entireHomeSwitch);
        emptyRoom.setChecked(AppPreferences.getInstance(getActivity()).getEntireHouse());
        emptyRoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppPreferences.getInstance(getActivity()).setEntireHouse(true);
                    AppPreferences.getInstance(getActivity()).setPrivateRoom(false);
                }else {
                    AppPreferences.getInstance(getActivity()).setEntireHouse(false);
                    AppPreferences.getInstance(getActivity()).setPrivateRoom(true);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

}