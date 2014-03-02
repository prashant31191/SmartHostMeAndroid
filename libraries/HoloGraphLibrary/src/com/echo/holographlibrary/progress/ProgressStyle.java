package com.echo.holographlibrary.progress;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import com.echo.holographlibrary.Utils;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wdziemia
 * Date: 8/7/13
 * Time: 2:17 PM

 */
public class ProgressStyle {

    //Misc
    private static final int NOT_SET = -1;
    public static final int MAX_DECIMALS = 2;
    public static final int MIN_DECIMALS = 0;
    private String mUnit = " lbs.";

    //Values in DIPs  TODO GET FROM RES
    public static final int DIP_TEXT_SIZE       = 16;
    public static final int DIP_TEXT_PAD        = 6;
    public static final int DIP_TITLE_TEXT_SIZE = 16;
    public static final int DIP_TITLE_TEXT_PAD  = 36;
    public static final float PROG_BAR_WIDTH    = .7f;

    //Color values    TODO GET FROM RES
    private int mPreviousColor      = 0xFFffbd17;
    private int mNoValueColor       = 0x4B111111;
    private int mCurrentColor       = 0xFF27b4e7;
    private int mTextColor          = 0xFFFFFFFF;
    private int mTitleTextColor     = 0xFF111111;

    //Dimens
    private float mTextSize         = NOT_SET;
    private float mTextPad          = NOT_SET;
    private float mTitleTextSize    = NOT_SET;
    private float mTitleTextPad     = NOT_SET;

    //Typefaces
    private Typeface mTextTypeFace  = null;
    private Typeface mTitleTypeFace = null;

    //Formatter
    private NumberFormat mNumFormatter;

    public ProgressStyle(Context context){
        mTextSize       = Utils.dpToPx(context, DIP_TEXT_SIZE);
        mTextPad        = Utils.dpToPx(context, DIP_TEXT_PAD);
        mTitleTextPad   = Utils.dpToPx(context, DIP_TITLE_TEXT_PAD);
        mTitleTextSize  = Utils.dpToPx(context, DIP_TITLE_TEXT_SIZE);
        mTitleTypeFace  = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        mTextTypeFace   = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");

        //TODO mUnit = new PreferenceHelper(context).getWeightUnit(); - get the unit that the user has selected from preferences
    }

    /**
     * A helper method that sets paint parameters for drawing the chart title
     * @param paint object we want to apply the params to
     */
    public void setTitlePaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setTextSize(getTitleTextSize());
            paint.setColor(getTitleTextColor());
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTypeface(getTitleTypeFace());
        }
    }

    public void setTextPaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setTextSize(getTextSize());
            paint.setColor(getTextColor());
            paint.setTextAlign(Paint.Align.RIGHT);
            paint.setTypeface(getTextTypeFace());
        }
    }

    public void setBarPaintParams(Paint paint, int type) {
        if ( paint != null){
            paint.reset();
            paint.setColor((type == ProgressBarGraph.TYPE_WEEK_CURRENT) ?
                getCurrentColor() : getPreviousColor());
        }
    }

    public int getPreviousColor() {
        return mPreviousColor;
    }

    public int getCurrentColor() {
        return mCurrentColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    public Typeface getTitleTypeFace() {
        return mTitleTypeFace;
    }

    public float getTitleTextSize() {
        return mTitleTextSize;
    }

    private Typeface getTextTypeFace() {
        return mTextTypeFace;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public float getTitleTextPad() {
        return mTitleTextPad;
    }

    public int getNoValueColor() {
        return mNoValueColor;
    }

    public float getTextPad() {
        return mTextPad;
    }

    public NumberFormat getFormatter() {
        if (mNumFormatter == null) {
            mNumFormatter = NumberFormat.getInstance(); //TODO Locale
            mNumFormatter.setMaximumFractionDigits(MAX_DECIMALS);
            mNumFormatter.setMinimumFractionDigits(MIN_DECIMALS);
        }
        return mNumFormatter;
    }

    public String getUnit() {
        return mUnit;
    }
}