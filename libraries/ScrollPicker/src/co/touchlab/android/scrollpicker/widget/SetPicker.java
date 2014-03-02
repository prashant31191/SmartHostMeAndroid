
package co.touchlab.android.scrollpicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import co.touchlab.android.scrollpicker.R;
import co.touchlab.android.scrollpicker.widget.ScrollPicker.OnScrollListener;
import co.touchlab.android.scrollpicker.widget.ScrollPicker.OnValueChangeListener;

import java.util.ArrayList;

public class SetPicker extends LinearLayout implements OnValueChangeListener, OnScrollListener,
        ScrollPicker.OnCustomWeightAddedListener, ScrollPicker.OnCustomRepAddedListener{

    private static final int MIN_VALUE = 1;
    private static final int DEFAULT_MAX_REPS = 40;
    private static final int DEFAULT_MAX_WEIGHT = 1000;
    private static final int DEFAULT_WEIGHT_INTERVAL = 5;

    /**
     * Listener for value change events.
     */
    public interface OnChangeListener {
        public void onSetChanged(int weight, int reps);
    }

    private ScrollPicker mPickerWeight, mPickerReps;
    private OnChangeListener mChangeListener;

    private int mMaxWeight;
    private int mMaxReps;
    private int mWeightInterval;
    private int mScrollState;

    public SetPicker(Context context) {
        this(context, null);
    }

    public SetPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SetPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.SetPicker);

        mMaxWeight = attributesArray.getInt(R.styleable.SetPicker_maxWeight, DEFAULT_MAX_WEIGHT);
        mWeightInterval = attributesArray.getInt(R.styleable.SetPicker_weightInterval,
                DEFAULT_WEIGHT_INTERVAL);

        ArrayList<String> weights = createWeights();

        int defWeightValue = weights.size() / 2;
        int weightValue = attributesArray.getInt(R.styleable.SetPicker_weightValue,
                defWeightValue * mWeightInterval) / mWeightInterval;
        if (weightValue > weights.size())
            throw new IllegalArgumentException("The default weight value cannot exceed the max value.");

        mMaxReps = attributesArray.getInt(R.styleable.SetPicker_maxReps, DEFAULT_MAX_REPS);

        ArrayList<String> reps = createReps();


        int defRepsValue = mMaxReps / 2;
        int repsValue = attributesArray.getInt(R.styleable.SetPicker_repsValue, defRepsValue);
        if (repsValue > mMaxReps) 
            throw new IllegalArgumentException("The default rep value cannot exceed the max value.");
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.set_picker, this, true);

        mPickerWeight = (ScrollPicker) findViewById(R.id.picker_weight);
        mPickerWeight.setMinValue(MIN_VALUE);
        updateWeights(weights);
        mPickerWeight.setValue(weightValue);
        mPickerWeight.setOnValueChangedListener(this);
        mPickerWeight.setOnScrollListener(this);
        mPickerWeight.setOnCustomWeightAddedListener(this);

        mPickerReps = (ScrollPicker) findViewById(R.id.picker_reps);
        mPickerReps.setMinValue(MIN_VALUE);
        updateReps(reps);
        mPickerReps.setValue(repsValue);
        mPickerReps.setOnValueChangedListener(this);
        mPickerReps.setOnScrollListener(this);
        mPickerReps.setOnCustomRepAddedListener(this);

        attributesArray.recycle();
    }

    @Override
    public void onValueChange(ScrollPicker picker, int oldVal, int newVal) {
        if (SCROLL_STATE_IDLE == mScrollState) {
            updateListener();
        }
    }

    @Override
    public void onCustomWeightAdded(String newVal) {
        addCustomWeightAndUpdateView(newVal);
    }


    @Override
    public void onCustomRepAdded(String newVal) {
        addCustomRepAndUpdateView(newVal);
    }

    @Override
    public void onScrollStateChange(ScrollPicker view, int scrollState) {
        mScrollState = scrollState;
        switch (mScrollState) {
            case SCROLL_STATE_IDLE:
                updateListener();
                break;
        }
    }

    private ArrayList<String> createWeights() {
        int total = mMaxWeight / mWeightInterval + 1;
        ArrayList<String> weights = new ArrayList<String>();
        // We don't want zero as an option
        for (int i = 1; i <= total; i++) {
            weights.add((i-1),String.valueOf(i * mWeightInterval));
        }
        //addCustomWeighs(weights);
        return weights;
    }

    private ArrayList<String> createReps() {
        ArrayList<String> reps = new ArrayList<String>();
        // We don't want zero as an option
        for (int i = 1; i <= mMaxReps; i++) {
            reps.add(String.valueOf(i));
        }

        reps.add(String.valueOf("100"));

        return reps;
    }

    /* Not is use
    private void addCustomWeighs(ArrayList<String> weights){
        int j=0;
        if(customWeights.size()>0){
            for(int i = 0;i<weights.size();i++){
                if(customWeights.size()<j&&
                    Integer.parseInt(weights.get(i))>Integer.parseInt(customWeights.get(j))){
                    weights.add(i,customWeights.get(j));
                    j++;
                }
            }
            while (j<customWeights.size()){
                weights.add(customWeights.get(j));
                j++;
            }
        }
    }
    */

    public void addCustomWeight(ArrayList<String> customWeights){
        ArrayList<String> displayedWeights = mPickerWeight.getDisplayedValues();
        for (String customVal : customWeights) {
            if(!displayedWeights.contains(customVal)){
                displayedWeights.add(findLocationToAddValue(displayedWeights, customVal), customVal);
            }
        }
        updateWeights(displayedWeights);
    }

    public void addCustomReps(ArrayList<String> customReps){
        ArrayList<String> displayedReps = mPickerReps.getDisplayedValues();
        for (String customVal : customReps) {
            if(!displayedReps.contains(customVal)){
                displayedReps.add(findLocationToAddValue(displayedReps, customVal), customVal);
            }
        }
        updateReps(displayedReps);
    }

    public void addCustomWeightAndUpdateView(String customVal){
        ArrayList<String> displayedWeights = mPickerWeight.getDisplayedValues();
        if(!displayedWeights.contains(customVal))
            displayedWeights.add(findLocationToAddValue(displayedWeights, customVal), customVal);{
            updateWeights(displayedWeights);
        }
        mPickerWeight.setValueInternal(customVal);
    }

    public void addCustomRepAndUpdateView(String customVal){
        ArrayList<String> displayedReps = mPickerReps.getDisplayedValues();
        if(!displayedReps.contains(customVal))
            displayedReps.add(findLocationToAddValue(displayedReps, customVal), customVal);{
            updateReps(displayedReps);
        }

        mPickerReps.setValueInternal(customVal);
    }

    private int findLocationToAddValue(ArrayList<String> values, String newVal){
        int i=0;
        for (String value : values) {
            if(Integer.parseInt(value)>Integer.parseInt(newVal))
                return i;
            else
                i++;
        }
        return values.size();
    }

    private void updateWeights(ArrayList<String> weights) {
        mPickerWeight.setMaxValue(weights.size() - 1);
        mPickerWeight.setDisplayedValues(weights);
        mPickerWeight.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void updateReps(ArrayList<String> reps) {
        mPickerReps.setMaxValue(reps.size() - 1);
        mPickerReps.setDisplayedValues(reps);
        mPickerReps.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void updateListener() {
        if (mChangeListener != null) {
            int weight = getWeightValue();
            int reps = getRepsValue();
            mChangeListener.onSetChanged(weight, reps);
        }
    }


    /**
     * @return The {@link co.touchlab.android.scrollpicker.widget.SetPicker.OnChangeListener} currently set. Null if none is set.
     */
    public OnChangeListener getOnChangeListener() {
        return mChangeListener;
    }

    /**
     * Set a listener to receive callbacks when the weight or reps are changed.
     * This will only update in Idle scrolling position.
     * 
     * @param listener
     */
    public void setOnChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    /**
     * @return The weight {@link ScrollPicker}
     */
    public ScrollPicker getWeightPicker() {
        return mPickerWeight;
    }

    /**
     * @return The reps {@link ScrollPicker}
     */
    public ScrollPicker getRepsPicker() {
        return mPickerReps;
    }

    /**
     * @return The current weight value, in pounds.
     */
    public int getWeightValue() {
        return Integer.parseInt(mPickerWeight.getDisplayedValues().get(mPickerWeight.getValue() - 1));
    }


    /**
     * @return The current weight value, in pounds.
     */
    public int getInputWeightTextValue() {
        if(mPickerWeight.mInputText.getVisibility()==VISIBLE&&!TextUtils.isEmpty(mPickerWeight.mInputText.getText().toString()))
            return Integer.parseInt(mPickerWeight.mInputText.getText().toString());
        else
            return -1;
    }


    /**
     * @return The current weight value, in pounds.
     */
    public int getInputRepsTextValue() {
        if(mPickerReps.mInputText.getVisibility()==VISIBLE&&!TextUtils.isEmpty(mPickerReps.mInputText.getText().toString()))
            return Integer.parseInt(mPickerReps.mInputText.getText().toString());
        else
            return -1;
    }

    /**
     * @param weight The weight, in pounds.
     */
    public void setWeightValue(int weight) {
        int index = weight / mWeightInterval;
        mPickerWeight.setValueInternal(weight+"");//.setValue(index);
    }

    /**
     * @return The current reps value.
     */
    public int getRepsValue() {
        return Integer.parseInt(mPickerReps.getDisplayedValues().get(mPickerReps.getValue() - 1));
        //Integer.parseInt(mPickerWeight.getDisplayedValues().get(mPickerWeight.getValue() - 1));
    }

    /**
     * @param rep The value to set.
     */
    public void setRepsValue(int rep) {
        mPickerReps.setValueInternal(""+rep);
    }

    /**
     * @return The maximum allowed weight, in pounds.
     */
    public int getMaxWeight() {
        return mMaxWeight;
    }

    /**
     * Set the maximum allowed weight, in pounds. Default is 1000.
     * 
     * @param max
     */
    public void setMaxWeight(int max) {
        mMaxWeight = max;
        updateWeights(createWeights());
    }

    /**
     * @return Maximum allowed reps.
     */
    public int getMaxReps() {
        return mMaxReps;
    }

    /**
     * Set the maximum allowed reps. Defaults is 40.
     * 
     * @param reps
     */
    public void setMaxReps(int reps) {
        mMaxReps = reps;
        updateReps(createReps());
    }

    /**
     * @return The interval between weight options.
     */
    public int getWeightInterval() {
        return mWeightInterval;
    }

    /**
     * Set the interval between weight options. Defaults is 5.
     * 
     * @param interval
     */
    public void setWeightInterval(int interval) {
        mWeightInterval = interval;
        updateWeights(createWeights());
    }

    
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.maxWeight = mMaxWeight;
        ss.maxReps = mMaxReps;
        ss.weightInterval = mWeightInterval;
        ss.currentWeightValue = mPickerWeight.getValue();
        ss.currentRepsValue = mPickerReps.getValue();
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

        mMaxWeight = ss.maxWeight;
        mMaxReps = ss.maxReps;
        mWeightInterval = ss.weightInterval;
        mPickerWeight.setValue(ss.currentWeightValue);
        mPickerReps.setValue(ss.currentRepsValue);
    }

    static class SavedState extends BaseSavedState {
        int maxWeight;
        int maxReps;
        int weightInterval;

        int currentWeightValue;
        int currentRepsValue;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.maxWeight = in.readInt();
            this.maxReps = in.readInt();
            this.weightInterval = in.readInt();
            this.currentWeightValue = in.readInt();
            this.currentRepsValue = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.maxWeight);
            out.writeInt(this.maxReps);
            out.writeInt(this.weightInterval);
            out.writeInt(this.currentWeightValue);
            out.writeInt(this.currentRepsValue);
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
