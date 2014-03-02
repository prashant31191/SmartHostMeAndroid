package co.touchlab.android.scrollpicker;


import android.support.v4.app.Fragment;
import co.touchlab.android.scrollpicker.SchedulerDayFragment.DayValues;
import co.touchlab.android.scrollpicker.SchedulerDurationFragment.DurationValue;

public class SchedulerFragment extends Fragment {

    /**
     * Callbacks for events in the Scheduler Fragments.
     */
    public interface ScheduleChangeListener {
        public void onSchedulerDayChange(SchedulerFragment fragment, DayValues dayValues);

        public void onSchedulerDurationChange(SchedulerFragment fragment,
                DurationValue durationValue);

        public void onSchedulerDismiss(SchedulerFragment fragment);

        public void onCurrentValuesTypeChanged(int type);
    }

    /**
     * Override to handle dismiss events.
     */
    public void dismiss() {
    }

    /**
     * Override to handle save events.
     */
    public void save() {
    }

}
