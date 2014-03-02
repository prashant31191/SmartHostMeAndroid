package com.echo.holographlibrary.line;

import java.util.ArrayList;

import android.graphics.*;
import android.text.TextUtils;
import com.echo.holographlibrary.GraphStyle;
import com.echo.holographlibrary.Utils;

public class LineFactory {

    private static final String TAG = "LineFactory";

    private String mTitle;

    private GraphStyle mStyle;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mPaint;
	private Rect mRect;
	private ArrayList<Line> mLines;

    private static final int MIN = 0, MAX = 1, DIFF = 2;
    private float [] x = new float [] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};
    private float [] y = new float [] {Integer.MAX_VALUE, Integer.MIN_VALUE, 0};

    private boolean mIsDomainManuallySet = false;
    private int mFillLineIndex = 0;

    public LineFactory(GraphStyle style){
        mStyle = style;
        mLines = new ArrayList<Line>();
        mPath = new Path();
        mPaint = new Paint();
    }
	
	public void drawChart(Canvas canvas, Rect rect){

        if (getSize() == 0 || getFillLineSize() == 0){
            //TODO Draw here instead of using callback
            mErrorCallback.OnDrawingError("No data present");
        } else {

            if (getFillLineSize() == 1) {
                getFillLine().addBufferData();
                calculateXValues();
            }

            mRect = rect;
            mRect.bottom -= mStyle.getDomainTextHeight();   //Remove height from the canvas to allow the x-axis labels to be draw
            mRect.left += mStyle.getRangeTextWidth(y[MAX]); //Remove width from the canvas to allow the y-axis labels to be draw
            mCanvas = canvas;

            //Methods for drawing. Do not change the order!
            drawTitle();
            drawDomainAxis();
            drawRangeAxis();
            drawSeriesData();
        }

	}

    private void drawSeriesData() {
        drawSeriesBackground();
        drawSeriesLine();
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
            mRect.top += mStyle.getTitleTextHeight();
        }

    }

    /**
     * Draws the background for the series set by mFillLineIndex
     */
	private void drawSeriesBackground() {
        mPath.reset();

        int lineCount = 0;
        for (Line line : mLines){

            float lastXPosition = mRect.left;

            if (lineCount == mFillLineIndex){

                line.getStyle().setBackgroundPaintParams(mPaint);
                mPath.moveTo(lastXPosition, mRect.bottom);

                for (LinePoint p : line.getPoints()){
                    if ( p.getX() >= x[MIN] && p.getX() <= x[MAX]) {
                        float xPercent = (p.getX()-x[MIN])/x[DIFF];
                        float xPosition = mRect.left + (xPercent * mRect.width());

                        float yPercent = (p.getY()-y[MIN])/y[DIFF];
                        float yPosition = mRect.bottom - (yPercent * mRect.height());

                        mPath.lineTo(xPosition, yPosition);
                        lastXPosition = xPosition;
                    }
                }
                mPath.lineTo(lastXPosition, mRect.bottom);
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
            }
            lineCount++;
        }
		
	}

    /**
     * Draws the series line along with a circle representing each data point.
     */
	private void drawSeriesLine() {
        mPath.reset();

		for (Line line : mLines){
            float lastXPixels = 0, newYPixels;
            float lastYPixels = 0, newXPixels;
            int count = 0;

            line.getStyle().setLinePaintParams(mPaint);
            for (LinePoint p : line.getPoints()){
                if ( p.getX() >= x[MIN] && p.getX() <= x[MAX]) {
                    float xPercent = (p.getX()-x[MIN])/x[DIFF];
                    float yPercent = (p.getY()-y[MIN])/y[DIFF];
                    newXPixels = mRect.left + (xPercent * mRect.width());
                    newYPixels = mRect.bottom - (yPercent * mRect.height());

                    if (count != 0) {
                        mCanvas.drawLine(lastXPixels, lastYPixels, newXPixels, newYPixels, mPaint);
                    }

                    if (p.shouldDrawPoint())
                        mCanvas.drawCircle(newXPixels, newYPixels,
                                line.getStyle().getPointRadius(), mPaint);

                    lastXPixels = newXPixels;
                    lastYPixels = newYPixels;

                    count++;
                }
            }
        }
	}


    /**
     * Draws the vertical lines along the x-axis. The amount of lines is set as the static variable
     * GraphStyle.GRID_SECTIONS
     */
	private void drawDomainAxis() {


        int count = 0;
        int gridWidth = getGridWidth();
        float xPosition = mRect.left;
        float yPosition = mRect.bottom + mStyle.getDomainTextHeight();
        boolean isRightBorderDrawn = false;

        while(xPosition <= mRect.right) {
            isRightBorderDrawn = (xPosition == mRect.right);

            //calculate position and value of text
            float xPercent = (xPosition - mRect.left)/(mRect.width());
            long xValue =  (int) (x[MIN] + (xPercent * x[DIFF]));

            //Draw Text
            mStyle.setTextPaintParams(mPaint, GraphStyle.DOMAIN);
            mCanvas.drawText(mStyle.getDateFormatter().format(
                    getFillLine().getTimeForPosition(xValue)), xPosition, yPosition, mPaint);

            //Draw Grid Line
            mStyle.setGridPaintParams(mPaint);
            mCanvas.drawLine(xPosition, mRect.top, xPosition, mRect.bottom, mPaint);

            xPosition = mRect.left + (gridWidth * ++count);
        }

        if (!isRightBorderDrawn) {
            mStyle.setGridPaintParams(mPaint);
            mCanvas.drawLine(mRect.right, mRect.top, mRect.right, mRect.bottom, mPaint);
        }

	}


    /**
     * Used to calculate the interval of where each grid line should be spaced. If the line does not have enough
     * points to fulfill the default domain sections, it will return 1 (i.e. draw a line at every point).
     * @return cellWidth in pixels
     * TODO Crash when mLines.size == 0  && dataPoints Size == 0
     */
    public int getLineInterval(){
        long timeSpanInDays = getFillLine().getTimeSpanInDays();
        return (timeSpanInDays <= GraphStyle.GRID_LINES) ?
                1 : (int) Math.ceil(timeSpanInDays / GraphStyle.GRID_LINES);
    }

    public int getGridWidth(){
        int interval = getLineInterval();
        float xRatio = interval/x[DIFF];
        return (int) Math.ceil(xRatio * mRect.width());
    }


    /**
     * Draws the horizontal lines along the y-axis. The amount of lines is set as the static variable
     * GraphStyle.GRID_SECTIONS
     */
    private void drawRangeAxis() {

        recalculateRange();

        int count = 0;
        int gridWidth = getGridWidth();
        float yPosition = mRect.bottom - (gridWidth * count);
        float xPosition = mRect.left - mStyle.getRightTextPad();
        boolean isTopBorderDrawn = false;

        while(yPosition >= mRect.top) {
            isTopBorderDrawn = (yPosition == mRect.top);

            //calculate position and value of text
            float yPercent = (mRect.height() - (yPosition - mRect.top))/mRect.height();
            float yValue =  y[MIN] + (yPercent * y[DIFF]);

            //Draw Text
            mStyle.setTextPaintParams(mPaint, GraphStyle.RANGE);
            float yCenterPosition = yPosition - ((mPaint.descent() + mPaint.ascent()) / 2);
            mCanvas.drawText(mStyle.getFormatter().format(yValue),
                    xPosition, yCenterPosition, mPaint);

            //Draw Grid Line
            mStyle.setGridPaintParams(mPaint);
            mCanvas.drawLine(mRect.left - 1, yPosition, mRect.right + 1, yPosition, mPaint);

            yPosition = mRect.bottom - (gridWidth * ++count);
        }

        if (!isTopBorderDrawn) {
            mStyle.setGridPaintParams(mPaint);
            mCanvas.drawLine(mRect.left-1, mRect.top, mRect.right+1, mRect.top, mPaint);
        }
    }

    private void recalculateRange(){
        float yDiff = (y[DIFF] == 0) ? 1 : y[DIFF]/2;
        y[MIN] -= yDiff;
        y[MAX] += yDiff;
        y[DIFF] = y[MAX] - y[MIN];
    }

    public float getLastLabelPosition(){
        return  getGridWidth() * (getRangeLineCount() - 1);
    }

    public int getRangeLineCount(){
        return  (mRect.height()/getGridWidth()) ; //Add 1 to include the origin line (0,0)
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    // Getters and Setters
    ////////////////////////////////////////////////////////////////////////////////////////


	public void setLines(ArrayList<Line> mLines) {
		this.mLines = mLines;
	}

    public void removeAllLines(){
        mLines.clear();
    }

    /**
     * Adds a line to a set of lines and recalculates the min max and diff both x and y axis.
     */
    public void addLine(Line line) {
        mLines.add(line);
        calculateXValues();
        calculateYValues();
    }


    public void calculateXValues(){
        x[MIN] = getMinX();
        x[MAX] = getMaxX();
        x[DIFF] = x[MAX] - x[MIN];
    }

    /**
     * Calculates the min, max, and diff for the y-axis. These values are skewed to give the chart a smoother line
     */
    public void calculateYValues(){
        y[MIN] = getMinY();
        y[MAX] = getMaxY();
        y[DIFF] = y[MAX] - y[MIN];
    }

    /**
     * The mFillLineIndex is the variable responsible for telling the drawSeriesBackground() which line to apply a
     * background to.
     */
    public void setFillLineIndex(int indexOfLine) {
        mFillLineIndex = indexOfLine;
    }

    public ArrayList<Line> getLines() {
        return mLines;
    }

    public int getFillLineIndex(){
        return mFillLineIndex;
    }

    public Line getLine(int index) {
        return getLines().get(index);
    }

    public int getSize(){
        return getLines().size();
    }

    public Line getFillLine() {
        return getLine(getFillLineIndex());
    }

    public ArrayList<LinePoint> getFillLineData(){
        return getFillLine().getPoints();
    }

    public int getFillLineSize(){
        return getFillLineData().size();
    }


    /**
     * Set the time span of a graph to custom values. This allows for custom ranges (e.g. 1 Month, 1 Year,
     * Year to Date,etc). These values must be in milliseconds.
     * @param startPos
     * @param endPos
     */
    public void setDomainSpan(long startPos, float endPos) {
        mIsDomainManuallySet = true;
        x[MIN] = startPos;
        x[MAX] = endPos;
        x[DIFF] = x[MAX] - x[MIN];
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    // Min and Max
    // TODO Make this better
    // compare max + min + diff when adding points to a line leaving only a line to line
    // comparison during onDraw
    ////////////////////////////////////////////////////////////////////////////////////////


    public float getMaxY(){
        float max = Integer.MIN_VALUE;
        for (Line line : mLines){
            for (LinePoint point : line.getPoints()){
                max = Math.max(max, point.getY());
            }
        }
        return max;
    }

    public float getMinY(){
        float min = Integer.MAX_VALUE;
        for (Line line : mLines){
            for (LinePoint point : line.getPoints()){
                min = Math.min(min, point.getY());
            }
        }
        return min;
    }

    public float getMaxX(){
        if (!mIsDomainManuallySet){
            float max = Integer.MIN_VALUE;
            for (Line line : mLines){
                for (LinePoint point : line.getPoints()){
                    max = Math.max(max, point.getX());
                }
            }
            return max;
        } else {
            return x[MAX];
        }
    }

    public float getMinX(){
        if (!mIsDomainManuallySet){
            float min = Integer.MAX_VALUE;
            for (Line line : mLines){
                for (LinePoint point : line.getPoints()){
                    min = Math.min(min, point.getX());
                }
            }
            return min;
        } else {
            return x[MIN];
        }
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    private DrawingErrorCallback mErrorCallback;

    public void setDrawingErrorCallback(DrawingErrorCallback errorCallback) {
        mErrorCallback = errorCallback;
    }
}
