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

public class LinePoint {

	private float y = 0;
    private long x = 0;
    private long timestamp = 0;
    private boolean drawPoint = true;


    public long getX() {
        return x;
    }

	public float getY() {
		return y;
	}

	public void setY(float y) {
        this.y = y;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean shouldDrawPoint() {
        return drawPoint;
    }

    public void setShouldDrawPoint(boolean drawPoint) {
        this.drawPoint = drawPoint;
    }

    public LinePoint clone(){
        LinePoint cloned = new LinePoint();
        cloned.setX(getX());
        cloned.setY(getY());
        cloned.setTimestamp(getTimestamp());
        cloned.setShouldDrawPoint(shouldDrawPoint());
        return cloned;
    }
}
