package com.smarthost.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import co.touchlab.android.superbus.BusHelper;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.smarthost.R;
import com.smarthost.data.Listing;
import com.smarthost.superbus.GetCityListingsCommand;
import com.smarthost.superbus.GetPriceForAddressCommand;
import com.smarthost.ui.adapters.AppraiseFormFragmentPagerAdapter;
import com.viewpagerindicator.LinePageIndicator;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: davidredding
 * Date: 8/8/13
 * Time: 4:02 PM
 */

public class AppraiseFragment extends Fragment implements
        ViewPager.OnPageChangeListener{

    public static final String TAG = "AppraiseFragment";
    public static final String PAGER_POSITION = "pager_position";

    private FrameLayout mediaFrameLayout;
    private LinePageIndicator indicator;

    private ViewPager mPager;
    private AppraiseFormFragmentPagerAdapter mPagerAdapter;

    TextView searchButton;
    TextView actualAmount;

    private int pagerPosition = 0;
    private boolean portrait;

    EditText searchEditText;

    Listing formListing = new Listing();

    public interface AppraiseFragmentListener{

    }



    public static AppraiseFragment newInstance() {
        AppraiseFragment f = new AppraiseFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(PAGER_POSITION);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.fragment_main_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");

        initViews();


    }


    public void updateListing() {
        if(actualAmount!=null ){
            actualAmount.setText(((int)(Math.random() * 999)+100)+"");
        }



//        formListing.addrees = "Philadelphia, Pa";
//        formListing.bedrooms = 1;
//        formListing.bathrooms = 1;
//        formListing.occupancy = 2;
//        BusHelper.submitCommandAsync(getActivity(), new GetCityListingsCommand("Philadelphia, Pa"));

    }


    private void initViews() {
        Log.d(TAG, "initViews");

        View parent = ((ViewGroup)getView()).getChildAt(0);

        searchButton = (TextView) parent.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateListing();
            }
        });

        searchEditText = (EditText)parent.findViewById(R.id.searchEditText);
        searchEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                                actionId == EditorInfo.IME_ACTION_DONE ||
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                            updateListing();
                            return false;
                        }
                        return false;
                    }
                });

        actualAmount = (TextView) parent.findViewById(R.id.actualAmount);
        actualAmount.setText("");

        updateListing();

        mPager = (ViewPager) parent.findViewById(R.id.formpager);
        indicator = (LinePageIndicator)parent.findViewById(R.id.indicator);
        mediaFrameLayout = (FrameLayout) parent.findViewById(R.id.formLayout);


        mPager = (ViewPager) getView().findViewById(R.id.formpager);
        indicator = (LinePageIndicator)getView().findViewById(R.id.indicator);
        mediaFrameLayout = (FrameLayout) getView().findViewById(R.id.formLayout);

        initMediaPager();

    }


    private void initMediaPager() {

        Log.d(TAG, "initMediaPager");
        mPagerAdapter = new AppraiseFormFragmentPagerAdapter(getChildFragmentManager());

//        if(portrait)
//        {
//            int mediaHeight = getResources().getDimensionPixelSize(R.dimen.media_height);
//            mediaFrameLayout.setLayoutParams( new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mediaHeight));
//
//        }

//        mediaFrameLayout.setVisibility(View.VISIBLE);

//        mPager.setOffscreenPageLimit((exerciseItem.video == null ? 0 : 1) + exerciseItem.images.size());


        mPager.setAdapter(mPagerAdapter);

//        getView().findViewById(R.id.indicatorLayout).setVisibility(View.VISIBLE);

        indicator.setViewPager(mPager);
        indicator.setOnPageChangeListener(this);

        //Setting the following line through XML does not work.
        final float density = getResources().getDisplayMetrics().density;
        indicator.setGapWidth(12 * density);
        indicator.setStrokeWidth(8 * density);
        indicator.setLineWidth(8 * density);
        indicator.setSelectedColor(0xFF4FE2C7);
        indicator.setUnselectedColor(0x99FFFFFF);

        indicator.setCurrentItem(pagerPosition);

//        mediaFrameLayout.setVisibility(View.VISIBLE);

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if(mPager != null) {
            int currentItem = mPager.getCurrentItem();
            outState.putInt(PAGER_POSITION, currentItem);
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int page) {
        pagerPosition = page;

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

}