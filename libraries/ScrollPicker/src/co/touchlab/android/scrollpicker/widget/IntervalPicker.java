
package co.touchlab.android.scrollpicker.widget;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import co.touchlab.android.scrollpicker.R;
import co.touchlab.android.scrollpicker.widget.ScrollPicker.OnScrollListener;
import co.touchlab.android.scrollpicker.widget.ScrollPicker.OnValueChangeListener;

import java.util.ArrayList;
import java.util.Calendar;

public class IntervalPicker extends LinearLayout implements OnValueChangeListener, OnScrollListener {

    public static final int UNIT_DAYS = 0;
    public static final int UNIT_WEEKS = 1;
    public static final int UNIT_MONTHS = 2;

    private static final int MAX_DAYS = 31;
    private static final int MAX_WEEKS = 52;
    private static final int MAX_MONTHS = 12;

    public interface OnChangeListener {
        /**
         * @param unit One of {@link java.util.Calendar#DAY_OF_MONTH},
         *            {@link java.util.Calendar#WEEK_OF_YEAR}, or {@link java.util.Calendar#MONTH}
         * @param number The amount of days, weeks or months.
         */
        public void onIntervalChanged(int unit, int number);
    }

    private ScrollPicker mNumberPicker, mUnitPicker;

    private OnChangeListener mChangeListener;
    private int mScrollState;

    public IntervalPicker(Context context) {
        this(context, null);
    }

    public IntervalPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IntervalPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.interval_picker, this, true);

        mNumberPicker = (ScrollPicker) findViewById(R.id.picker_number);
        mNumberPicker.setMinValue(1);
        mNumberPicker.setMaxValue(MAX_DAYS);
        mNumberPicker.setOnValueChangedListener(this);
        mNumberPicker.setOnScrollListener(this);
        mNumberPicker.setValue(2);

        mUnitPicker = (ScrollPicker) findViewById(R.id.picker_unit);
        mUnitPicker.setMinValue(0);
        updateUnits();
        mUnitPicker.setOnValueChangedListener(this);
        mUnitPicker.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChange(ScrollPicker picker, int scrollState) {
        mScrollState = scrollState;
        refresh(picker);
    }

    @Override
    public void onValueChange(ScrollPicker picker, int oldVal, int newVal) {
        refresh(picker);
    }

    private void refresh(ScrollPicker picker) {
        if (SCROLL_STATE_IDLE == mScrollState) {
            if (picker == mUnitPicker) {
                updateNumbers(picker.getValue());
            } else {
                updateUnits();
            }
            updateListener();
        }
    }

    private void updateNumbers(int unit) {
        switch (unit) {
            case UNIT_DAYS:
                mNumberPicker.setMaxValue(MAX_DAYS);
                break;
            case UNIT_WEEKS:
                mNumberPicker.setMaxValue(MAX_WEEKS);
                break;
            case UNIT_MONTHS:
                // Months
                mNumberPicker.setMaxValue(MAX_MONTHS);
                break;
        }
    }

    private ArrayList<String> createUnits() {
        ArrayList<String> units = new ArrayList<String>();
        int value = mNumberPicker.getValue();
        units.add(0, getContext().getResources().getQuantityString(R.plurals.day, value));
        units.add(1, getContext().getResources().getQuantityString(R.plurals.week, value));
        units.add(2, getContext().getResources().getQuantityString(R.plurals.month, value));
        return units;
    }

    private void updateUnits() {
        ArrayList<String> units = createUnits();
        mUnitPicker.setMaxValue(units.size() - 1);
        mUnitPicker.setDisplayedValues(units);
    }

    private void updateListener() {
        if (mChangeListener != null) {
            mChangeListener.onIntervalChanged(getUnit(), mNumberPicker.getValue());
        }
    }

    /**
     * @return The unit {@link ScrollPicker}
     */
    public ScrollPicker getUnitPicker() {
        return mUnitPicker;
    }

    /**
     * @return The number {@link ScrollPicker}
     */
    public ScrollPicker getNumberPicker() {
        return mNumberPicker;
    }

    /**
     * @return The currently set listener. Null if there is none.
     */
    public OnChangeListener getOnChangeListener() {
        return mChangeListener;
    }

    /**
     * Set a listener to be notified of value change events.
     *
     * @param listener
     */
    public void setOnChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    /**
     * @param unit One of {@link java.util.Calendar#DAY_OF_MONTH},
     *            {@link java.util.Calendar#WEEK_OF_YEAR}, or {@link java.util.Calendar#MONTH}
     */
    public void setUnit(int unit) {
        switch (unit) {
            case Calendar.DAY_OF_MONTH:
                mUnitPicker.setValue(UNIT_DAYS);
                break;
            case Calendar.WEEK_OF_YEAR:
                mUnitPicker.setValue(UNIT_WEEKS);
                break;
            case Calendar.MONTH:
                mUnitPicker.setValue(UNIT_MONTHS);
                break;
        }
    }

    /**
     * @return One of {@link java.util.Calendar#DAY_OF_MONTH},
     *         {@link java.util.Calendar#WEEK_OF_YEAR}, or {@link java.util.Calendar#MONTH}
     */
    public int getUnit() {
        int unit = -1;
        switch (mUnitPicker.getValue()) {
            case UNIT_DAYS:
                unit = Calendar.DAY_OF_MONTH;
                break;
            case UNIT_WEEKS:
                unit = Calendar.WEEK_OF_YEAR;
                break;
            case UNIT_MONTHS:
                unit = Calendar.MONTH;
                break;
        }
        return unit;
    }

    /**
     * @return The index of the current unit.
     */
    public int getUnitValue() {
        return mUnitPicker.getValue();
    }

    /**
     * @return The string representation of the current value
     */
    public String getUnitString() {
        return resolveUnitString(mUnitPicker.getValue());
    }

    /**
     * @param unit One of {@link #UNIT_DAYS}, {@link #UNIT_WEEKS}, or {@link #UNIT_MONTHS}
     */
    public void setUnitValue(int unit) {
        mUnitPicker.setValue(unit);
    }

    /**
     * @return The current value for the number {@link ScrollPicker}
     */
    public int getNumberValue() {
        return mNumberPicker.getValue();
    }

    /**
     * @param value The current value for the number {@link ScrollPicker}
     */
    public void setNumberValue(int value) {
        int unit = getUnitValue();
        switch (unit) {
            case UNIT_DAYS:
                if (value > MAX_DAYS)
                    throw new IllegalArgumentException("There are only 31 days in a month.");
            case UNIT_WEEKS:
                if (value > MAX_WEEKS)
                    throw new IllegalArgumentException("There are only 52 weeks in a year.");
            case UNIT_MONTHS:
                if (value > MAX_MONTHS)
                    throw new IllegalArgumentException("There are only 12 months in a year.");
        }
        mNumberPicker.setValue(value);
    }

    /**
     * @param value One of {@link #UNIT_DAYS}, {@link #UNIT_WEEKS}, or
     *            {@link #UNIT_MONTHS}
     * @return The human-readable String representation of this unit.
     */
    public String resolveUnitString(int value) {
        ArrayList<String> units = createUnits();
        return units.get(value);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.currentNumberValue = getNumberValue();
        ss.currentUnitValue = getUnitValue();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        mNumberPicker.setValue(ss.currentNumberValue);
        mUnitPicker.setValue(ss.currentUnitValue);
    }

    static class SavedState extends BaseSavedState {
        int currentNumberValue;
        int currentUnitValue;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.currentNumberValue = in.readInt();
            this.currentUnitValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.currentNumberValue);
            out.writeInt(this.currentUnitValue);
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
