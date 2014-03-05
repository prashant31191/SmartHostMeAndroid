package com.smarthost.ui.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.smarthost.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * User: davidredding
 * Date: 3/4/14
 * Time: 6:47 PM
 */
public class FormFragment extends Fragment implements View.OnClickListener{


    public static String ARG_LAYOUT_ID = "arg_layout";

    int layoutId = 0;

    TextView numAccommadations;
    TextView numBedrooms;
    TextView numBathrooms;

    int value;

    public interface OnExerciseChangeListener {
        public void onExerciseDeleted(int exerciseId);

        void setExerciseName(String name);

        void onExerciseAdd(int exerciseId);
    }


    public static FormFragment newInstance(int layoutId) {
        FormFragment f = new FormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments().containsKey(ARG_LAYOUT_ID))
            layoutId = getArguments().getInt(ARG_LAYOUT_ID);





    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutId, container, false);
    }

    public void show()
    {

        final Dialog d = new Dialog(getActivity());
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        //np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (value){
                    case R.id.accommodates:
                        numAccommadations.setText(String.valueOf(np.getValue()));
                        break;
                    case R.id.bedrooms:
                        numBedrooms.setText(String.valueOf(np.getValue()));
                        break;
                    case R.id.bathrooms:
                        numBathrooms.setText(String.valueOf(np.getValue()));
                        break;

                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        View root = getView();


        switch (layoutId){


            case R.layout.fragment_form_rooms:




                break;
            case R.layout.fragment_form_sleeping:

                numAccommadations = (TextView)getView().findViewById(R.id.numAccommodations);
                numBathrooms = (TextView)getView().findViewById(R.id.numBathrooms);
                numBedrooms = (TextView)getView().findViewById(R.id.numBedrooms);
                getView().findViewById(R.id.accommodates).setOnClickListener(this);
                getView().findViewById(R.id.bedrooms).setOnClickListener(this);
                getView().findViewById(R.id.bathrooms).setOnClickListener(this);

                break;




        }





    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.accommodates:

                value = R.id.accommodates;
                show();

                break;
            case R.id.bedrooms:

                value = R.id.bedrooms;
                show();

                break;
            case R.id.bathrooms:

                value = R.id.bathrooms;
                show();

                break;


        }

    }

}
