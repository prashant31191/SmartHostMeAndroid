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

import co.touchlab.android.scrollpicker.widget.ScrollDatePicker;
import co.touchlab.android.scrollpicker.widget.ScrollDatePicker.OnDateChangedListener;

import java.util.Calendar;

public class SchedulerDurationFragment extends SchedulerFragment implements OnDateChangedListener {

    private static final String ARGS_CURRENT_VALUES = "current_values";

    private View mContainer, mOverlayView;
    //private Button mBtnInfinite;
    private Button mBtnDate;
    private ScrollDatePicker mDatePicker;
    private ViewAnimator mViewSwitcher;

    private DurationValue mCurrentValues;

    private ScheduleChangeListener mListener;

    public SchedulerDurationFragment() {
        // TODO Auto-generated constructor stub
    }

    public static SchedulerDurationFragment newInstance(DurationValue values) {
        SchedulerDurationFragment f = new SchedulerDurationFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGS_CURRENT_VALUES, values);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scheduler_date, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        View root = getView();

        mContainer = root.findViewById(R.id.scheduler_container);

        mViewSwitcher = (ViewAnimator) root.findViewById(R.id.scheduler_switcher);

        mDatePicker = (ScrollDatePicker) root.findViewById(R.id.scheduler_date_picker);

        mOverlayView = root.findViewById(R.id.scheduler_overlay);
        mOverlayView.setOnClickListener(mOverlayClickListener);

        //mBtnInfinite = (Button) root.findViewById(R.id.btn_infinite);
       // mBtnInfinite.setOnClickListener(mBtnClickListener);
        mBtnDate = (Button) root.findViewById(R.id.btn_date);
        mBtnDate.setOnClickListener(mBtnClickListener);

        if (savedInstanceState != null) {
            mCurrentValues = (DurationValue) savedInstanceState.getParcelable(ARGS_CURRENT_VALUES);
        } else {
            mContainer.setAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.slide_in_bottom));
            //mOverlayView.setAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));

            mCurrentValues = (DurationValue) getArguments().getParcelable(ARGS_CURRENT_VALUES);
        }

        if (mCurrentValues == null) {
            mCurrentValues = new InfiniteValue();
        }

        updateViews();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(ARGS_CURRENT_VALUES, (Parcelable) mCurrentValues);

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

    private OnClickListener mBtnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
    //        if (mBtnInfinite == v) {
     //           mCurrentValues = new InfiniteValue();
      //      }
       //    else
        if (mBtnDate == v) {
                Calendar date = Calendar.getInstance();
                date.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                mCurrentValues = new DateValue(date);
            }

            updateViews();
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
            case DurationValue.TYPE_DATE:
                Calendar calendar = ((DateValue) mCurrentValues).date;
                mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), this);
                break;
        }

     //   mBtnInfinite.setEnabled(mCurrentValues.type != DurationValue.TYPE_INFINITE);
        mBtnDate.setEnabled(mCurrentValues.type != DurationValue.TYPE_DATE);

        mViewSwitcher.setDisplayedChild(mCurrentValues.type);
    }

    @Override
    public void dismiss() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_bottom);
        animation.setAnimationListener(mAnimListener);
        mContainer.startAnimation(animation);

        //mOverlayView.setAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
    }

    @Override
    public void save() {

        switch (mCurrentValues.type) {
            case DurationValue.TYPE_INFINITE:
                mCurrentValues = new InfiniteValue();
                break;

            case DurationValue.TYPE_DATE:
                Calendar calendar = ((DateValue) mCurrentValues).date;
                calendar.set(mDatePicker.getYear(), mDatePicker.getMonth(),
                        mDatePicker.getDayOfMonth());
                mCurrentValues = new DateValue(calendar);
                break;
        }

        mListener.onSchedulerDurationChange(this, mCurrentValues);
    }

    public DurationValue getCurrentValues() {
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
                mListener.onSchedulerDismiss(SchedulerDurationFragment.this);
            }
        }
    };

    public static abstract class DurationValue implements Parcelable {

        public static final int TYPE_INFINITE = 0;
        public static final int TYPE_DATE = 1;

        public int type;

        public DurationValue(int type) {
            this.type = type;
        }
    }

    public static class InfiniteValue extends DurationValue implements Parcelable {

        public InfiniteValue() {
            super(TYPE_INFINITE);
        }

        protected InfiniteValue(Parcel in) {
            super(TYPE_INFINITE);
            type = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(type);
        }

        public static final Creator<InfiniteValue> CREATOR = new Creator<InfiniteValue>() {
            @Override
            public InfiniteValue createFromParcel(Parcel in) {
                return new InfiniteValue(in);
            }

            @Override
            public InfiniteValue[] newArray(int size) {
                return new InfiniteValue[size];
            }
        };

    }

    public static class DateValue extends DurationValue implements Parcelable {

        public Calendar date;

        public DateValue(Calendar date) {
            super(TYPE_DATE);

            this.date = date;
        }

        protected DateValue(Parcel in) {
            super(TYPE_DATE);
            date = (Calendar) in.readValue(null);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(date);
        }

        public static final Creator<DateValue> CREATOR = new Creator<DateValue>() {
            @Override
            public DateValue createFromParcel(Parcel in) {
                return new DateValue(in);
            }

            @Override
            public DateValue[] newArray(int size) {
                return new DateValue[size];
            }
        };
    }

    @Override
    public void onDateChanged(ScrollDatePicker view, int year, int monthOfYear, int dayOfMonth) {
        save();
    }
}
