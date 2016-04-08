package rol.metropolia.fi.consumptor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class FuelEntryActivity extends AppCompatActivity {

    private NumberPicker picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_entry);

        picker = (NumberPicker) findViewById(R.id.number_picker);

        String[] nums = new String[20];
        for(int i=0; i<nums.length; i++)
            nums[i] = Integer.toString(i);

        picker.setMinValue(1);
        picker.setMaxValue(20);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(nums);
        picker.setValue(1);

    }

    // when done
//    Intent resultIntent = new Intent();
//
//    resultIntent.putExtra(CONVERSION_RESULT, result);
//
//    setResult(Activity.RESULT_OK, resultIntent);
//
//    finish();

}
