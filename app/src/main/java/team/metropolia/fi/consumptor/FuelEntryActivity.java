package team.metropolia.fi.consumptor;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import team.metropolia.fi.consumptor.Models.FuelEntry;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class FuelEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private EditText fuelEditText;
    private EditText odometerEditText;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_entry);

        fuelEditText = (EditText) findViewById(R.id.fuel_edit_text);
        odometerEditText = (EditText) findViewById(R.id.odometer_edit_text);
        datePicker = (DatePicker) findViewById(R.id.date_picker);
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        createButton = (Button) findViewById(R.id.create_button);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createButton.setOnClickListener(this);
    }

    private boolean validateForm() {

        boolean isFuelEmpty = TextUtils.isEmpty(fuelEditText.getText());
        boolean isOdometerEmpty = TextUtils.isEmpty(odometerEditText.getText());

        if (isFuelEmpty || isOdometerEmpty) {

            fuelEditText.setError(isFuelEmpty ? getString(R.string.fuel_validation_failed) : null);
            odometerEditText.setError(isOdometerEmpty ? getString(R.string.odometer_validation_failed) : null);

            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        if (!validateForm()) {
            return;
        }

        int fuel;
        int odometerValue;

        try {

            fuel = Integer.parseInt(fuelEditText.getText().toString());
            odometerValue = Integer.parseInt(odometerEditText.getText().toString());

        } catch (NumberFormatException e) {

            Snackbar.make(fuelEditText, "Cannot save entry", Snackbar.LENGTH_SHORT).show();

            return;
        }

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.YEAR, datePicker.getYear());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

        createFuelEntryAndFinish(fuel, odometerValue, calendar.getTime());
    }

    private void createFuelEntryAndFinish(int fuel, int odometer, Date date) {

        FuelEntry entry = new FuelEntry();

        entry.fuel = fuel;
        entry.odometer = odometer;
        entry.createdOn = new java.sql.Date(date.getTime());
        entry.save();

        finish();
    }
}
