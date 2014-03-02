
package co.touchlab.android.scrollpicker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import co.touchlab.android.scrollpicker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RepeatPicker extends LinearLayout implements CompoundButton.OnCheckedChangeListener {

    private OnToggleListener listener;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(listener != null)
            listener.onDayToggled();

    }

    public void setListener(OnToggleListener listener) {
        this.listener = listener;
    }

    public interface OnToggleListener {

        public void onDayToggled();
    }
    
    private static final String TAG = "RepeatPicker";
    
    ToggleButton mBtnMon, mBtnTue, mBtnWed, mBtnThu, mBtnFri, mBtnSat, mBtnSun;

    public RepeatPicker(Context context) {
        super(context);

        init(context, null, 0);
    }

    public RepeatPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        init(context, attrs, 0);
    }

    public RepeatPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.repeat_picker, this, true);
        
        mBtnMon = (ToggleButton) findViewById(R.id.repeat_btn_mon);
        mBtnMon.setOnCheckedChangeListener(this);
        mBtnTue = (ToggleButton) findViewById(R.id.repeat_btn_tue);
        mBtnTue.setOnCheckedChangeListener(this);
        mBtnWed = (ToggleButton) findViewById(R.id.repeat_btn_wed);
        mBtnWed.setOnCheckedChangeListener(this);
        mBtnThu = (ToggleButton) findViewById(R.id.repeat_btn_thu);
        mBtnThu.setOnCheckedChangeListener(this);
        mBtnFri = (ToggleButton) findViewById(R.id.repeat_btn_fri);
        mBtnFri.setOnCheckedChangeListener(this);
        mBtnSat = (ToggleButton) findViewById(R.id.repeat_btn_sat);
        mBtnSat.setOnCheckedChangeListener(this);
        mBtnSun = (ToggleButton) findViewById(R.id.repeat_btn_sun);
        mBtnSun.setOnCheckedChangeListener(this);

    }
    
    private void removeAllChecks(ToggleButton... buttons) {
        for (ToggleButton button : buttons) {
            button.setChecked(false);
        }
    }
    
    public ArrayList<Integer> getSelectedDays() {
        ArrayList<Integer> selectedDays = new ArrayList<Integer>();
        if (mBtnMon.isChecked()) {
            selectedDays.add(Calendar.MONDAY);
        }
        if (mBtnTue.isChecked()) {
            selectedDays.add(Calendar.TUESDAY);
        }
        if (mBtnWed.isChecked()) {
            selectedDays.add(Calendar.WEDNESDAY);
        }
        if (mBtnThu.isChecked()) {
            selectedDays.add(Calendar.THURSDAY);
        }
        if (mBtnFri.isChecked()) {
            selectedDays.add(Calendar.FRIDAY);
        }
        if (mBtnSat.isChecked()) {
            selectedDays.add(Calendar.SATURDAY);
        }
        if (mBtnSun.isChecked()) {
            selectedDays.add(Calendar.SUNDAY);
        }
        
        return selectedDays;
    }
    
    public void setSelectedDays(List<Integer> selectedDays) {
        Log.d(TAG, "setSelectedList list = "+selectedDays.toString());
        
        removeAllChecks(mBtnMon, mBtnTue, mBtnWed, mBtnThu, mBtnFri, mBtnSat, mBtnSun);
        
        for (Integer day : selectedDays) {
            switch(day) {
                case Calendar.MONDAY:
                    mBtnMon.setChecked(true);
                    break;
                case Calendar.TUESDAY:
                    mBtnTue.setChecked(true);
                    break;
                case Calendar.WEDNESDAY:
                    mBtnWed.setChecked(true);
                    break;
                case Calendar.THURSDAY:
                    mBtnThu.setChecked(true);
                    break;
                case Calendar.FRIDAY:
                    mBtnFri.setChecked(true);
                    break;
                case Calendar.SATURDAY:
                    mBtnSat.setChecked(true);
                    break;
                case Calendar.SUNDAY:
                    mBtnSun.setChecked(true);
                    break;
            }
        }
    }
}
