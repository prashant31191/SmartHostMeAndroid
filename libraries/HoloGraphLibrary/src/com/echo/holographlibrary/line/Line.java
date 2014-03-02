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

import com.echo.holographlibrary.Utils;

import java.util.ArrayList;

public class Line {
	private ArrayList<LinePoint> points = new ArrayList<LinePoint>();
	private boolean showPoints = true;
    private boolean mIsBufferDataAdded = false;
	private LineStyle style;

    public Line(){ }

    public Line(LineStyle style){
        setStyle(style);
    }

	public ArrayList<LinePoint> getPoints() {
		return points;
	}

	public void setPoints(ArrayList<LinePoint> linePoints) {
		this.points = linePoints;
	}

	public void addPoint(LinePoint point){
        if (getSize() == 0){
            point.setTimestamp(point.getX());
            point.setX(0);
        } else {
            point.setTimestamp(point.getX());
            long x = Utils.getDiffInDays(getFirstTimeStamp(),point.getTimestamp());
            point.setX(x);
        }
		points.add(point);
	}

	public LinePoint getPoint(int index){
		return points.get(index);
	}

	public int getSize(){
		return points.size();
	}

	public boolean isShowingPoints() {
		return showPoints;
	}

	public void setShowingPoints(boolean showPoints) {
		this.showPoints = showPoints;
	}

    public void setStyle(LineStyle style) {
        this.style = style;
    }

    public LineStyle getStyle() {
        return style;
    }

    public long getFirstTimeStamp() {
        return points.get(0).getTimestamp();
    }

    public long getLastTimeStamp() {
        return getLastPoint().getTimestamp();
    }

    public LinePoint getLastPoint() {
        return points.get(getSize() - 1);
    }

    public long getTimeSpanInDays(){
        return Utils.getDiffInDays(getFirstTimeStamp(), getLastTimeStamp());
    }

    public long getTimeForPosition(long xValue) {
        return Utils.getMillisFromStart(getFirstTimeStamp(),
                (mIsBufferDataAdded) ? 0 : xValue);
    }

    public void addBufferData() {
        mIsBufferDataAdded = true;

        LinePoint firstPoint = getPoints().get(0);
        LinePoint zeroPoint = firstPoint.clone();
        LinePoint secondPoint = firstPoint.clone();;

        firstPoint.setX(1);

        zeroPoint.setShouldDrawPoint(false);
        points.add(0, zeroPoint);

        secondPoint.setX(2);
        secondPoint.setShouldDrawPoint(false);
        points.add(secondPoint);
    }

    public void removePoint(LinePoint point) {
        points.remove(point);
    }

}
