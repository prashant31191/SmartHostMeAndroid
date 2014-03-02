package co.touchlab.android.scrollpicker;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewAnimator;

import co.touchlab.android.scrollpicker.widget.IntervalPicker;
import co.touchlab.android.scrollpicker.widget.IntervalPicker.OnChangeListener;
import co.touchlab.android.scrollpicker.widget.RepeatPicker;
import co.touchlab.android.scrollpicker.widget.RepeatPicker.OnToggleListener;
import co.touchlab.android.scrollpicker.widget.ScrollDatePicker;
import co.touchlab.android.scrollpicker.widget.ScrollDatePicker.OnDateChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SchedulerDayFragment extends SchedulerFragment implements OnDateChangedListener,
        OnToggleListener, OnChangeListener {

    private static final String TAG = "SchedulerFragment";

    private static final String STATE_CURRENT_VALUES = "current_values";

    private Button mBtnOneShot, mBtnRepeat, mBtnEvery;
    private View mContainer, mOverlayView;
    private ScrollDatePicker mDatePicker;
    private RepeatPicker mRepeatPicker;
    private IntervalPicker mIntervalPicker;
    private ViewAnimator mViewSwitcher;

    private DayValues mCurrentValues;

    private ScheduleChangeListener mListener;

    public SchedulerDayFragment() {
        // TODO Auto-generated constructor stub
    }

    public static SchedulerDayFragment newInstance(DayValues currentValues) {
        SchedulerDayFragment f = new SchedulerDayFragment();
        Bundle args = new Bundle();
        args.putParcelable(STATE_CURRENT_VALUES, currentValues);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scheduler, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        View root = getView();

        mContainer = root.findViewById(R.id.scheduler_container);

        mViewSwitcher = (ViewAnimator) root.findViewById(R.id.scheduler_switcher);

        mBtnOneShot = (Button) root.findViewById(R.id.btn_one_shot);
        mBtnOneShot.setOnClickListener(mBtnClickListener);
        mBtnRepeat = (Button) root.findViewById(R.id.btn_repeat);
        mBtnRepeat.setOnClickListener(mBtnClickListener);
        mBtnEvery = (Button) root.findViewById(R.id.btn_every);
        mBtnEvery.setOnClickListener(mBtnClickListener);

        mDatePicker = (ScrollDatePicker) root.findViewById(R.id.scheduler_date_picker);
        Calendar calendar = Calendar.getInstance();
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);

        mRepeatPicker = (RepeatPicker) root.findViewById(R.id.scheduler_repeat_picker);
        mRepeatPicker.setListener(this);

        mIntervalPicker = (IntervalPicker) root.findViewById(R.id.scheduler_every_picker);
        mIntervalPicker.setOnChangeListener(this);

        mOverlayView = root.findViewById(R.id.scheduler_overlay);
        mOverlayView.setOnClickListener(mOverlayClickListener);

        if (savedInstanceState != null) {
            mCurrentValues = savedInstanceState.getParcelable(STATE_CURRENT_VALUES);
        } else {
            mContainer.setAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_in_bottom));
            mOverlayView.animate().alpha(1.0f);

            mCurrentValues = getArguments().getParcelable(STATE_CURRENT_VALUES);
        }

        if (mCurrentValues == null) {
            mCurrentValues = new OneShotValues(Calendar.getInstance());
        }

        //save();     this overwrites the loaded schedule ;(

        updateViews();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(STATE_CURRENT_VALUES, mCurrentValues);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (getParentFragment() != null) {
            try {
                mListener = (ScheduleChangeListener) getParentFragment();
            } catch (ClassCastException e) {
                throw new ClassCastException(getParentFragment().getClass().getSimpleName()
                        + " must implement ScheduleChangeListener");
            }
        } else {
            try {
                mListener = (ScheduleChangeListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement ScheduleChangeListener");
            }
        }
    }

    public OnClickListener mBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mBtnOneShot == v) {
                Calendar date = Calendar.getInstance();
                date.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                mCurrentValues = new OneShotValues(date);
            }
            else if (mBtnRepeat == v) {
                List<Integer> days = mRepeatPicker.getSelectedDays();
                mCurrentValues = new RepeatValues(days, Calendar.getInstance());
            }
            else if (mBtnEvery == v) {
                mCurrentValues = new IntervalValues(mIntervalPicker.getUnit(),
                        mIntervalPicker.getNumberValue(), Calendar.getInstance());
            }

            updateViews();
            save();
        }
    };

    private OnClickListener mOverlayClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private void updateViews() {

        switch (mCurrentValues.type) {
            case DayValues.TYPE_ONE_SHOT:
                Calendar calendar = ((OneShotValues)mCurrentValues).date;
                mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), this);
                break;

            case DayValues.TYPE_REPEAT:
                List<Integer> days = ((RepeatValues)mCurrentValues).days;
                mRepeatPicker.setSelectedDays(days);
                break;

            case DayValues.TYPE_EVERY:
                IntervalValues values = (IntervalValues) mCurrentValues;
                mIntervalPicker.setUnit(values.unit);
                mIntervalPicker.setNumberValue(values.number);
                break;
        }

        mBtnOneShot.setEnabled(mCurrentValues.type != DayValues.TYPE_ONE_SHOT);
        mBtnRepeat.setEnabled(mCurrentValues.type != DayValues.TYPE_REPEAT);
        mBtnEvery.setEnabled(mCurrentValues.type != DayValues.TYPE_EVERY);

        mViewSwitcher.setDisplayedChild(mCurrentValues.type);
        mListener.onCurrentValuesTypeChanged(mCurrentValues.type);
    }

    @Override
    public void dismiss() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
        animation.setAnimationListener(mAnimListener);
        mContainer.startAnimation(animation);

        mOverlayView.animate().alpha(0f);
    }

    @Override
    public void save() {
        DayValues dayValues = null;

        if (mListener != null) {
            switch (mCurrentValues.type) {
                case DayValues.TYPE_ONE_SHOT:
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),
                            mDatePicker.getDayOfMonth());
                    dayValues = new OneShotValues(calendar);
                    break;

                case DayValues.TYPE_REPEAT:
                    dayValues = new RepeatValues(mRepeatPicker.getSelectedDays(),
                            Calendar.getInstance());
                    break;

                case DayValues.TYPE_EVERY:
                    dayValues = new IntervalValues(mIntervalPicker.getUnit(),
                            mIntervalPicker.getNumberValue(), Calendar.getInstance());
                    break;
            }
            mCurrentValues = dayValues;
            mListener.onSchedulerDayChange(this, dayValues);
        }
    }

    public DayValues getCurrentValues() {
        return mCurrentValues;
    }

    private AnimationListener mAnimListener = new AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mListener != null) {
                mListener.onSchedulerDismiss(SchedulerDayFragment.this);
            }
        }
    };

    /**
     * Represents the scheduler values selected. Must include a type, which is
     * one of {@link #TYPE_ONE_SHOT}, {@link #TYPE_REPEAT}, or
     * {@link #TYPE_EVERY}.
     *
     * @see co.touchlab.android.scrollpicker.SchedulerDayFragment.OneShotValues
     * @see co.touchlab.android.scrollpicker.SchedulerDayFragment.RepeatValues
     * @see co.touchlab.android.scrollpicker.SchedulerDayFragment.IntervalValues
     */
    public static abstract class DayValues implements Parcelable {

        public static final int TYPE_ONE_SHOT = 0;
        public static final int TYPE_REPEAT = 1;
        public static final int TYPE_EVERY = 2;

        public int type;

        public DayValues(int type) {
            this.type = type;
        }
    }

    /**
     * Values returned when the user selects a one-shot schedule.
     */
    public static class OneShotValues extends DayValues implements Parcelable {

        public Calendar date;

        /**
         * @param date The single-shot date.
         */
        public OneShotValues(Calendar date) {
            super(TYPE_ONE_SHOT);

            this.date = date;
        }

        protected OneShotValues(Parcel in) {
            super(TYPE_ONE_SHOT);
            type = in.readInt();
            date = (Calendar)in.readValue(null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeValue(date);
        }

        public static final Creator<OneShotValues> CREATOR = new Creator<OneShotValues>() {
            @Override
            public OneShotValues createFromParcel(Parcel in) {
                return new OneShotValues(in);
            }

            @Override
            public OneShotValues[] newArray(int size) {
                return new OneShotValues[size];
            }
        };

        @Override
        public String toString() {
            return "OneShotValues [date=" + date.getTime().toString() + "]";
        }
    }

    /**
     * Values returned when the user selects a repeating schedule.
     */
    public static class RepeatValues extends DayValues implements Parcelable {

        public List<Integer> days;
        public Calendar endDate;

        /**
         *  @param days Integer list of {@link java.util.Calendar} day representations.
         *            E.g. {@link java.util.Calendar#MONDAY}
         */
        public RepeatValues(List<Integer> days, Calendar endDate) {
            super(TYPE_REPEAT);

            this.days = days;
            this.endDate = endDate;
        }

        protected RepeatValues(Parcel in) {
            super(TYPE_REPEAT);
            type = in.readInt();
            days = new ArrayList<Integer>();
            in.readList(days, null);
            endDate = (Calendar)in.readValue(null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeList(days);
            dest.writeValue(endDate);
        }

        public static final Creator<RepeatValues> CREATOR = new Creator<RepeatValues>() {
            @Override
            public RepeatValues createFromParcel(Parcel in) {
                return new RepeatValues(in);
            }

            @Override
            public RepeatValues[] newArray(int size) {
                return new RepeatValues[size];
            }
        };

        @Override
        public String toString() {
            return "RepeatValues [days=" + days.toString() + "]";
        }
    }

    /**
     * Values returned when the user selects an interval schedule.
     */
    public static class IntervalValues extends DayValues implements Parcelable {

        public int unit;
        public int number;
        public Calendar endDate;

        /**
         * @param unit One of {@link java.util.Calendar#DAY_OF_MONTH}, {@link java.util.Calendar#WEEK_OF_YEAR}, or {@link java.util.Calendar#MONTH}
         * @param number The amount of days, weeks or months.
         */
        public IntervalValues(int unit, int number, Calendar endDate) {
            super(TYPE_EVERY);

            this.unit = unit;
            this.number = number;
            this.endDate = endDate;
        }

        protected IntervalValues(Parcel in) {
            super(TYPE_EVERY);
            type = in.readInt();
            unit = in.readInt();
            number = in.readInt();
            endDate = (Calendar)in.readValue(null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
            dest.writeInt(unit);
            dest.writeInt(number);
            dest.writeValue(endDate);
        }

        public static final Creator<IntervalValues> CREATOR = new Creator<IntervalValues>() {
            @Override
            public IntervalValues createFromParcel(Parcel in) {
                return new IntervalValues(in);
            }

            @Override
            public IntervalValues[] newArray(int size) {
                return new IntervalValues[size];
            }
        };

        @Override
        public String toString() {
            return "EveryValues [unit=" + unit + ", number=" + number + "]";
        }
    }

    @Override
    public void onIntervalChanged(int unit, int number) {
        save();
    }

    @Override
    public void onDayToggled() {
        save();
    }

    @Override
    public void onDateChanged(ScrollDatePicker view, int year, int monthOfYear, int dayOfMonth) {
        save();
    }
}
