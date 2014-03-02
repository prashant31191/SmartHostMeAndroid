/*
 * 	   Created by Daniel Nadeau
 * 	   daniel.nadeau01@gmail.com
 * 	   danielnadeau.blogspot.com
 * 
 * 	   Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.echo.holographlibrary.line;

import android.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.echo.holographlibrary.GraphStyle;

public class LineGraph extends View implements DrawingErrorCallback{

    private static final String TAG = "ProgressBarGraph";
    private static final int[] ATTRS = new int[]{
            R.attr.text,
    };

    private LineFactory mFactory;
    private GraphStyle mStyle;

	private Bitmap fullImage;
	private boolean shouldUpdate = false;
    private boolean mError = false;
    private String mErrorDescription;

    public LineGraph(Context context, String title) {
        super(context);
        init(title);
    }

    public LineGraph(Context context) {
        this(context, null, 0);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineGraph(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        String title = a.getString(0);
        a.recycle();

        init(title);
    }

    public void init(String title){
        mStyle = new GraphStyle(getContext());
        mFactory = new LineFactory(mStyle);
        mFactory.setTitle(title);
        mFactory.setDrawingErrorCallback(this);
    }

    /**
     * A call to update() will redraw the entire view
     */
    public void update(){
        shouldUpdate = true;
        postInvalidate();
    }

    /**
     * The onDraw method utilizes a bitmap to draw on. This allows the view to be moved around freely without it needing
     * to be redrawn through the ProgressFactory.
     */
	public void onDraw(Canvas ca) {
		if ( fullImage == null || fullImage.isRecycled() || shouldUpdate) {
            shouldUpdate = false;
			fullImage = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);

            Canvas canvas = new Canvas(fullImage);
            canvas.setDrawFilter(new PaintFlagsDrawFilter(
                    0, Paint.ANTI_ALIAS_FLAG));

            Rect rect = new Rect(0,0,getWidth(),getHeight());

            if (!mError){
                rect.top += mStyle.getBorderPad();
                rect.right -= mStyle.getBorderPad();
                rect.bottom -= mStyle.getBorderPad();
                mFactory.drawChart(canvas,rect);
            } else {
                Paint paint = new Paint();
                paint.setColor(mStyle.getGridLineColor());
                canvas.drawRect(rect,paint);

                mStyle.setTitlePaintParams(paint);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(!TextUtils.isEmpty(mErrorDescription) ? mErrorDescription : "An error occurred" ,
                        rect.width()/2,
                        rect.height()/2 - ((paint.descent() + paint.ascent()) / 2),
                        paint);

                mError = false;
            }


		}
		ca.drawBitmap(fullImage, 0, 0, null);
	}


    public void setDomainSpan(long startTime, long endTime) {
        mFactory.setDomainSpan(startTime,endTime);
        update();
    }

    @Override
    public void OnDrawingError(String errorDescription) {
        mErrorDescription = errorDescription;
        mError = true;
        update();
    }

    public void removeAllLines(){
        mFactory.removeAllLines();
        update();
    }

    public void addLine(Line line) {
        mFactory.addLine(line);
        update();
    }

    public void setLine(Line line) {
        mFactory.removeAllLines();
        mFactory.addLine(line);
        update();
    }

    public void setLineToFill(int indexOfLine) {
        mFactory.setFillLineIndex(indexOfLine);
        update();
    }
}
