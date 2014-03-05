package com.smarthost.ui.fragments;


import android.app.Activity;
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

    TextView numBedrooms;
    TextView numBathrooms;

    int value;

    private FormFragmentListener listener;

    public interface FormFragmentListener{
        void updateBedrooms(int number);
        void updateBathrooms(int number);
        void updateHomeOrPrivate(boolean home, boolean private_Room);

    }


    public static FormFragment newInstance(int layoutId) {
        FormFragment f = new FormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_ID, layoutId);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (FormFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FormFragmentListener");
        }

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
        np.setMaxValue(8);
        np.setMinValue(0);
        np.setValue(value==R.id.bedrooms ? Integer.parseInt(numBedrooms.getText().toString()) : Integer.parseInt(numBathrooms.getText().toString()));
        np.setWrapSelectorWheel(false);
        //np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                switch (value){
                    case R.id.bedrooms:
                        numBedrooms.setText(String.valueOf(np.getValue()));
                        listener.updateBedrooms(np.getValue());

                        break;
                    case R.id.bathrooms:
                        numBathrooms.setText(String.valueOf(np.getValue()));
                        listener.updateBathrooms(np.getValue());
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

                getView().findViewById(R.id.privateButton).setOnClickListener(this);
                getView().findViewById(R.id.entireHomeButton).setOnClickListener(this);


                break;
            case R.layout.fragment_form_sleeping:
                numBathrooms = (TextView)getView().findViewById(R.id.numBathrooms);
                numBedrooms = (TextView)getView().findViewById(R.id.numBedrooms);
                getView().findViewById(R.id.bedrooms).setOnClickListener(this);
                getView().findViewById(R.id.bathrooms).setOnClickListener(this);

                break;




        }





    }


    @Override
    public void onClick(View v) {


        TextView privateButton = (TextView) getView().findViewById(R.id.privateButton);
        TextView entireHome = (TextView) getView().findViewById(R.id.entireHomeButton);

        switch (v.getId()){
            case R.id.bedrooms:

                value = R.id.bedrooms;
                show();

                break;
            case R.id.bathrooms:

                value = R.id.bathrooms;
                show();

                break;
            case R.id.privateButton:


                if(privateButton.getCurrentTextColor()==getActivity().getResources().getColor(R.color.blue_lettering)){
//                    privateButton.setTextColor(getActivity().getResources().getColor(R.color.blue_lettering));
//                    privateButton.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.blue_door), null, null);
//                    entireHome.setTextColor(getActivity().getResources().getColor(R.color.sh_gray));
//                    entireHome.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.gray_house), null, null);
                }else{
                    privateButton.setTextColor(getActivity().getResources().getColor(R.color.blue_lettering));
                    privateButton.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.blue_door), null, null);
                    entireHome.setTextColor(getActivity().getResources().getColor(R.color.sh_gray));
                    entireHome.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.gray_house), null, null);


                }

                listener.updateHomeOrPrivate(false, true);
                break;
            case R.id.entireHomeButton:


                if(entireHome.getCurrentTextColor()==getActivity().getResources().getColor(R.color.blue_lettering)){
//                    privateButton.setTextColor(getActivity().getResources().getColor(R.color.sh_gray));
//                    privateButton.setCompoundDrawables(null, getActivity().getResources().getDrawable(R.drawable.gray_door), null, null);
//                    entireHome.setTextColor(getActivity().getResources().getColor(R.color.blue_lettering));
//                    entireHome.setCompoundDrawables(null, getActivity().getResources().getDrawable(R.drawable.blue_house), null, null);
                }else{
                    privateButton.setTextColor(getActivity().getResources().getColor(R.color.sh_gray));
                    privateButton.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.gray_door), null, null);
                    entireHome.setTextColor(getActivity().getResources().getColor(R.color.blue_lettering));
                    entireHome.setCompoundDrawablesWithIntrinsicBounds(null, getActivity().getResources().getDrawable(R.drawable.blue_house), null, null);
                }
                listener.updateHomeOrPrivate(true, false);


                break;


        }


    }

}
