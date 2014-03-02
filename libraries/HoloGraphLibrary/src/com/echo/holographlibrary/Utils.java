package com.echo.holographlibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wdziemia
 * Date: 8/6/13
 * Time: 3:12 PM

 */
public class Utils {


    public static final String CAL_FORMAT = "yyyyMMdd";
    public static final int DAY_IN_MILLIS = 86400000;


    public static float dpToPx( Context context, int dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    public static float spToPx( Context context, int dp){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.scaledDensity / 160f);
        return px;
    }

    public static int getTextWidth(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.left + bounds.width();
        return width;
    }

    public static float getCeil(float value) {
        return (float) Math.ceil(value);
    }

    public static float getFloor(float value) {
        return (float) Math.floor(value);
    }

    public static float getRoundedCeil(float value) {
        float pos = getRoundingPosition(value);
        return (float) Math.ceil(value/pos) * pos;
    }

  //  public static float getRoundedFloor(float value) {
        //   float pos = getRoundingPosition(value);
        //   return (float) Math.floor(value/pos) * pos;
        //   return (float) Math.floor(value);
   // }

    public static float getRoundingPosition(float diff){
        if (diff >= 1000){
            return 100;
        } else if (diff >= 10) {
            return 10f;
        }  else {
            return .1f;
        }
    }

    /**
     * Get the difference in days for any two given dates. The method only handles timestamps formatted in yyyyMMdd.
     * @param startDate formatted in yyyyMMdd
     * @param endDate formatted in yyyyMMdd
     * @return int represting the difference in days. Default return value is 0.
     */
    public static long getDiffInDays(long startDate, long endDate) {
        long startInMillis = getTimeInMillis(startDate);
        long endInMillis = getTimeInMillis(endDate);
        return Math.abs((endInMillis - startInMillis)/DAY_IN_MILLIS);
    }

    public static long getTimeInMillis(long date) {
        try {
            SimpleDateFormat frm = new SimpleDateFormat(CAL_FORMAT);
            return frm.parse(String.valueOf(date)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getMillisFromStart(long startDate, long xValue) {
       return getTimeInMillis(startDate) + (xValue * DAY_IN_MILLIS);
    }

}
