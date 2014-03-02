package com.echo.holographlibrary.progress;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.echo.holographlibrary.R;

import java.text.NumberFormat;

public class ProgressBarGraph extends LinearLayout {

    //TODO FOR DEV USE
    private static final String TAG = "ProgressBarGraph";

    //Animation vars
    private boolean mUseAnimations = true;

    //Constants
    public static final int NOT_SET = -1;
    public static final int TYPE_WEEK_CURRENT = 0;
    public static final int TYPE_WEEK_LAST = 1;
    public static final int OFFSET_TIME = 200;
    private static final float BAR_VIEW_WEIGHT = .7f;  //TODO Do something about this weight, should get the weight from XML file on init

    //Our views to display the data
    private View        mCurrentBar;
    private TextView    mCurrentValueLabel;
    private View        mPreviousBar;
    private TextView    mPreviousValueLabel;

    //floats to store the previous, current and max values.
    private float mPreviousWeekValue, mCurrentWeekValue, mMaxValue = NOT_SET;

    //Misc
    private static float mMaxBarWidth = NOT_SET;
    private int mDefaultTextColor   = NOT_SET;
    private int mBarTextColor       = NOT_SET;
    private int mCurrentBarColor    = NOT_SET;
    private int mPreviousBarColor   = NOT_SET;

    private String noData;

    private NumberFormat mNumberFormatter;


    public ProgressBarGraph(Context context) {
        this(context, null);
    }

    public ProgressBarGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void init(Context context){
        super.setOrientation(VERTICAL);

        Resources res = getResources();

        //Set styling params.
        noData = res.getString(R.string.no_data);
        mDefaultTextColor = res.getColor(R.color.text_color);
        mBarTextColor = res.getColor(R.color.text_color_bar);
        mCurrentBarColor = res.getColor(R.color.current_color);
        mPreviousBarColor = res.getColor(R.color.previous_color);
        mNumberFormatter = NumberFormat.getNumberInstance();
        mNumberFormatter.setMaximumFractionDigits(2);

        //Get inflater and default params
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

        //Set member view vars and add to parent views
        View currentWeekParent = initCurrentWeekBar(context, inflater);
        addView(currentWeekParent, params);

        View previousWeekParent = initPreviousWeekBar(context, inflater);
        addView(previousWeekParent, params);

        final View parentContainer = previousWeekParent.findViewById(R.id.progress_bar_container);
        if (mMaxBarWidth == NOT_SET) {
            parentContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        parentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        parentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    mMaxBarWidth = parentContainer.getMeasuredWidth();
                    setValues(mCurrentWeekValue,mPreviousWeekValue);
                }
            });

        }
    }


    public View initCurrentWeekBar(Context context, LayoutInflater inflater){
        View parent = inflater.inflate(R.layout.progress_bars_container, this, false);
        ((TextView) parent.findViewById(R.id.progress_bar_title)).setText(
                context.getString(R.string.week_current) );

        mCurrentBar = parent.findViewById(R.id.progress_bar);
        mCurrentBar.setBackgroundColor(mCurrentBarColor);
        mCurrentBar.setPivotX(0);
        mCurrentValueLabel = (TextView) parent.findViewById(R.id.progress_bar_value);
        mCurrentValueLabel.setText(noData);
        mCurrentValueLabel.setVisibility(VISIBLE);

        return parent;
    }

    public View initPreviousWeekBar(Context context, LayoutInflater inflater){
        View parent = inflater.inflate(R.layout.progress_bars_container, this, false);
        ((TextView) parent.findViewById(R.id.progress_bar_title)).setText(
                getContext().getString(R.string.week_previous) );
        mPreviousBar = parent.findViewById(R.id.progress_bar);
        mPreviousBar.setBackgroundColor(mPreviousBarColor);
        mPreviousBar.setPivotX(0);
        mPreviousValueLabel = (TextView) parent.findViewById(R.id.progress_bar_value);
        mPreviousValueLabel.setText(noData);

        return parent;
    }

    public void setValues(float currentWeekValue, float previousWeekValue) {

        mMaxValue = Math.max(currentWeekValue, previousWeekValue);

        //Generate and set the label for the current week
        String currentLabel = getFormattedValue(currentWeekValue);
        mCurrentWeekValue = currentWeekValue;
        mCurrentValueLabel.setText(currentLabel);
        mCurrentBar.setScaleX(currentWeekValue / mMaxValue);


        float widthC = mMaxBarWidth * (currentWeekValue/mMaxValue);
        widthC = Float.isNaN(widthC) ? 0 : widthC;
        float textWidthC = getWidth(mCurrentValueLabel);
        if ( (textWidthC + widthC) >= mMaxBarWidth  ) {
            mCurrentValueLabel.setX(widthC - textWidthC);
            mCurrentValueLabel.setTextColor(mBarTextColor);
        } else {
            mCurrentValueLabel.setX(widthC);
            mCurrentValueLabel.setTextColor(mDefaultTextColor);
        }


        //Generate and set the label for the previous week
        String previousLabel = getFormattedValue(previousWeekValue);
        mPreviousWeekValue = previousWeekValue;
        mPreviousValueLabel.setText(previousLabel);
        mPreviousBar.setScaleX(previousWeekValue / mMaxValue);

        float width = mMaxBarWidth * (previousWeekValue/mMaxValue);
        width = Float.isNaN(width) ? 0 : width;
        float textWidth = getWidth(mPreviousValueLabel);

        Log.i(TAG, "max" + (mMaxBarWidth));
        Log.i(TAG, "bar width " + (width));
        Log.i(TAG, "text width " + (textWidth));
        if ( (textWidth + width) >= mMaxBarWidth  ) {
            Log.i(TAG, "label POS " + (width - textWidth));
            mPreviousValueLabel.setX(width - textWidth);
            mPreviousValueLabel.setTextColor(mBarTextColor);
        } else {
            Log.i(TAG, "label POS " + (width));
            mPreviousValueLabel.setX(width);
            mPreviousValueLabel.setTextColor(mDefaultTextColor);
        }

    }


    public float getWidth(View tv){
        tv.measure(0,0);
        return tv.getMeasuredWidth();
        //return tv.getPaint().measureText(tv.getText().toString());
    }

    public String getFormattedValue(float value){
        return ((int) value == 0) ? getContext().getString(R.string.no_data) :
                getResources().getQuantityString(
                        R.plurals.formattedWeight, (int) value,
                        mNumberFormatter.format(value));
    }

    public void setUseAnimations( boolean useAnimations){
        mUseAnimations = useAnimations;
    }



}