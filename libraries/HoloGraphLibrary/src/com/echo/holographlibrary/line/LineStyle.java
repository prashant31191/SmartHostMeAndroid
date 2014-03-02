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

import android.content.Context;
import android.graphics.Paint;
import com.echo.holographlibrary.Utils;

public class LineStyle {

    private static final int NOT_SET = -1;

    // TODO GET FROM RES?
    public static final int DIP_LINE_WIDTH   = 3;
    public static final int DIP_POINT_RADIUS = 5;

	private float mLineWidth    = NOT_SET;
    private float mPointRadius  = NOT_SET;

	private int mLineColor          = 0xFFca3d3c;
	private int mBackgroundColor    = 0x54FDC5AF;
    private int mPointColor         = mLineColor;

    public LineStyle(Context context){
        mLineWidth      = Utils.dpToPx(context, DIP_LINE_WIDTH);
        mPointRadius    = Utils.dpToPx(context, DIP_POINT_RADIUS);
    }
	
	public int getLineColor() {
		return mLineColor;
	}
	
	public void setLineColor(int color) {
		this.mLineColor = color;
	}
	
	public int getBackgroundColor() {
		return mBackgroundColor;
	}
	
	public void setBackgroundColor(int color) {
		this.mBackgroundColor = color;
	}
	
	public float getPointRadius() {
		return mPointRadius;
	}
	
	public void setPointRadius(int pointRadius) {
		this.mPointRadius = pointRadius;
	}
	
	public float getLineWidth() {
		return mLineWidth;
	}
	
	public void setLineWidth(int lineWidth) {
		this.mLineWidth = lineWidth;
	}
	
	public int getPointColor() {
		return mPointColor;
	}
	
	public void setPointColor(int pointColor) {
		this.mPointColor = pointColor;
	}

    public void setBackgroundPaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setColor(getBackgroundColor());
            paint.setStyle(Paint.Style.FILL);
        }
    }

    public void setLinePaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setColor(getLineColor());
            paint.setStrokeWidth(getLineWidth());
        }
    }

    public void setPointPaintParams(Paint paint) {
        if ( paint != null){
            paint.reset();
            paint.setColor(getPointColor());
        }
    }
}
