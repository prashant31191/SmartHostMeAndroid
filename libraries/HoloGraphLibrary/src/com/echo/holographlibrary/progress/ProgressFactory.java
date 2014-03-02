package com.echo.holographlibrary.progress;

import android.graphics.*;
import android.text.TextUtils;
import android.util.Log;
import com.echo.holographlibrary.Utils;

public class ProgressFactory {

    private static final String TAG = "ProgressFactory";

    private String mTitle;

    private ProgressStyle mStyle;
	private Canvas mCanvas;
	private Paint mPaint;
	private Rect mRect;

    private int mType = ProgressBarGraph.TYPE_WEEK_CURRENT;

    private float mMaxValue = 0;
    private float mCurrentValue = 0;
    private float mPreviousValue = 0;

    public ProgressFactory(ProgressStyle style){
        mStyle = style;
        mPaint = new Paint();
    }
	
	public void drawChart(Canvas canvas, Rect rect){
		mRect = rect;
		mCanvas = canvas;
        mCanvas.setDrawFilter(new PaintFlagsDrawFilter(
                0, Paint.ANTI_ALIAS_FLAG));

        //Methods for drawing. Do not change the order!
        drawTitle();
        drawProgressBar();
	}

    /**
     * Draws the title if the mTitle is not null. The title can be set either through XML (android:text) or
     * programmatically ProgressBarGraph(Context ctx, String title)
     */
    private void drawTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            mStyle.setTitlePaintParams(mPaint);
            mCanvas.drawText(mTitle , mRect.left,
                    mRect.top + mStyle.getTitleTextSize(), mPaint);
        }

    }


    private void drawProgressBar() {
        boolean isCurrentValue = mType == ProgressBarGraph.TYPE_WEEK_CURRENT;
        float value =  (isCurrentValue) ? mCurrentValue : mPreviousValue ;
        int rightBound = mRect.right;

        //Calculate the bounds of the bar
        mRect.left = mRect.right - (int) (mRect.width() * ProgressStyle.PROG_BAR_WIDTH);
        float percentOffset = (isCurrentValue) ?
                mCurrentValue/mMaxValue :
                mPreviousValue/mMaxValue;
        mRect.right = (int) (mRect.left + (percentOffset * mRect.width()));

        //Draw the Rect
        mStyle.setBarPaintParams(mPaint, mType);
        if (value == 0){
            mPaint.setColor(mStyle.getNoValueColor());
           // mRect.right = rightBound;
        }
        mCanvas.drawRect(mRect, mPaint);

        //Calculate and draw the Text
        mStyle.setTextPaintParams(mPaint);
        String valueString = mStyle.getFormatter().format(value) + mStyle.getUnit();
        float valueStringWidth = Utils.getTextWidth(valueString, mPaint) + (mStyle.getTextPad()*2);

        if (mRect.width() < valueStringWidth ){
            mRect.right += mStyle.getTextPad();
            mPaint.setColor(mStyle.getTitleTextColor());
            mPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            mRect.right -= mStyle.getTextPad();
        }

        float yPosition = mRect.top + (mRect.height()/2) - ((mPaint.descent() + mPaint.ascent()) / 2);;
        mCanvas.drawText(valueString , mRect.right , yPosition, mPaint);
    }

    public void setType(int type) {
        mType = type;
    }

    public void setValues(float currentValue, float previousValue) {
        mCurrentValue = currentValue;
        mPreviousValue = previousValue;
        mMaxValue = Math.max(currentValue,previousValue);
    }

    public void setTitle(String title) {
        mTitle = title;
    }


}
