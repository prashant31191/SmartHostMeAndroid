package com.echo.holographlibrary;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;

import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wdziemia
 * Date: 8/7/13
 * Time: 2:17 PM

 */
public class GraphStyle {

    //Formatting
    public static final String DATE_FORMAT = "M/dd";
    public static final int MAX_DECIMALS = 2;
    public static final int MIN_DECIMALS = 0;

    //Misc
    private static final int NOT_SET = -1;
    public static final int GRID_LINES = 4;

    //Text types
    public static final int RANGE   = 1;
    public static final int DOMAIN  = 2;

    //Values in DIPs  TODO GET FROM RES?
    public static final int DIP_PADDING = 5;
    public static final int DIP_GRID_LINE_WIDTH = 1;
    public static final int DIP_TEXT_SIZE       = 8;
    public static final int DIP_TITLE_TEXT_SIZE = 16;
    public static final int DIP_TITLE_TEXT_PAD  = 14;
    public static final int DIP_TEXT_PAD_LEFT   = 4;
    public static final int DIP_TEXT_PAD_RIGHT  = 6;
    public static final int DIP_TEXT_PAD_TOP    = 4;

    //Color values    TODO GET FROM RES?
    private int mGridLineColor      = 0xFFDEDEDE;
    private int mBorderLineColor    = 0xFF111111;
    private int mTextColor          = 0xFF777777; //0xFF111111
    private int mTitleTextColor     = 0xFF111111;
    private int mTextShadowColor    = 0x26111111;

    //Values in Pixels
    private float mGridLineWidth    = 1;
    private float mTextSize         = NOT_SET;
    private float mTopTextPad       = NOT_SET;
    private float mLeftTextPad      = NOT_SET;
    private float mRightTextPad     = NOT_SET;
    private float mBorderPad        = NOT_SET;
    private float mTitleTextPad     = NOT_SET;
    private float mTitleTextSize    = NOT_SET;

    private Typeface mTitleTypeFace = null;
    private NumberFormat mNumFormatter;
    private SimpleDateFormat mDateFormatter;

    public GraphStyle(Context context){
    //    mGridLineWidth   = Utils.dpToPx(context, DIP_GRID_LINE_WIDTH);

        mTextSize       = Utils.dpToPx(context, DIP_TEXT_SIZE);
        mTopTextPad     = Utils.dpToPx(context, DIP_TEXT_PAD_TOP);
        mLeftTextPad    = Utils.dpToPx(context, DIP_TEXT_PAD_LEFT);
        mRightTextPad   = Utils.dpToPx(context, DIP_TEXT_PAD_RIGHT);
        mBorderPad      = Utils.dpToPx(context, DIP_PADDING);
        mTitleTextPad   = Utils.dpToPx(context, DIP_TITLE_TEXT_PAD);
        mTitleTextSize  = Utils.dpToPx(context, DIP_TITLE_TEXT_SIZE);
        mTitleTypeFace  = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
    }

    /**
     * A helper method that sets paint parameters for drawing x & y labels
     * @param paint object we want to apply the params to
     * @param type Setting the type to DOMAIN will draw a shadow
     */
    public void setTextPaintParams(Paint paint, int type){
        if ( paint != null){
            paint.reset();
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setTextSize(getTextSize());
            paint.setColor(getTextColor());

            if (type == DOMAIN){
                paint.setShadowLayer(2,1,0, mTextShadowColor);
            } else {
                paint.setTextAlign(Paint.Align.RIGHT);
            }
        }
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

    /**
     * A helper method that sets paint parameters for drawing the chart grid lines
     * @param paint object we want to apply the params to
     */
    public void setGridPaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setColor(getGridLineColor());
            paint.setStrokeWidth(getGridLineWidth());
        }
    }

    public int getGridLineColor() {
        return mGridLineColor;
    }

    public int getBorderLineColor() {
        return mBorderLineColor;
    }

    public float getGridLineWidth() {
        return mGridLineWidth;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getTextSize() {
        return mTextSize;
    }

    public float getTopTextPad() {
        return mTopTextPad;
    }

    public float getDomainTextHeight(){
        return getTextSize() + getTopTextPad();
    }

    public float getLeftTextPad() {
        return mLeftTextPad;
    }

    public float getRightTextPad() {
        return mRightTextPad;
    }

    public int getBorderPad() {
        return (int) mBorderPad;
    }

    public float getTitleTextPad() {
        return mTitleTextPad;
    }

    public float getTitleTextSize() {
        return mTitleTextSize;
    }

    public float getTitleTextHeight(){
        return getTitleTextSize() + getTitleTextPad();
    }

    public Typeface getTitleTypeFace() {
        return mTitleTypeFace;
    }

    public int getTitleTextColor() {
        return mTitleTextColor;
    }

    /**
     * Measures the width of the y-axis labels.
     * @param maxY the highest y values of the data
     */
    public float getRangeTextWidth(float maxY) {
        Paint p = new Paint();
        setTextPaintParams(p, RANGE);
        getFormatter().setMinimumFractionDigits(MAX_DECIMALS);
        float width = p.measureText(getFormatter().format(maxY));
        getFormatter().setMinimumFractionDigits(MIN_DECIMALS);
        return width + getRightTextPad();
    }

    public NumberFormat getFormatter() {
        if (mNumFormatter == null) {
            mNumFormatter = NumberFormat.getInstance(); //TODO Locale
            mNumFormatter.setMaximumFractionDigits(MAX_DECIMALS);
            mNumFormatter.setMinimumFractionDigits(MIN_DECIMALS);
        }
        return mNumFormatter;
    }

    public SimpleDateFormat getDateFormatter() {
        return (mDateFormatter == null) ?
                new SimpleDateFormat(DATE_FORMAT) : mDateFormatter;            //TODO Locale;
    }

}