package team.metropolia.fi.consumptor.FuelEntry;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
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

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import team.metropolia.fi.consumptor.Models.FuelEntry;
import team.metropolia.fi.consumptor.R;
import team.metropolia.fi.consumptor.Settings.Settings;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class FuelEntryActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Toolbar toolbar;
    private EditText dateEditText;
    private EditText timeEditText;
    private EditText fuelEditText;
    private EditText odometerEditText;

    private Calendar selectedDate = Calendar.getInstance();
    private DateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    private DateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_entry);

        dateEditText = (EditText) findViewById(R.id.date_edit_text);
        timeEditText = (EditText) findViewById(R.id.time_edit_text);
        fuelEditText = (EditText) findViewById(R.id.fuel_edit_text);
        odometerEditText = (EditText) findViewById(R.id.odometer_edit_text);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        invalidateView();
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

    private void invalidateView() {

        dateEditText.setText(dateFormatter.format(selectedDate.getTime()));
        timeEditText.setText(timeFormatter.format(selectedDate.getTime()));
    }

    public void onDatePressed(View v) {

        new DatePickerDialog(this,
                this,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void onTimePressed(View v) {

        new TimePickerDialog(this,
                this,
                selectedDate.get(Calendar.HOUR_OF_DAY),
                selectedDate.get(Calendar.MINUTE),
                true)
                .show();
    }

    public void onCreateButtonPressed(View v) {

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

        createFuelEntryAndFinish(fuel, odometerValue, selectedDate.getTime());
    }

    private void createFuelEntryAndFinish(int fuel, int odometer, Date date) {

        FuelEntry prevEntry = new Select().from(FuelEntry.class).orderBy("createdOn desc").executeSingle();

        FuelEntry entry = new FuelEntry();

        entry.fuel = fuel;
        entry.odometer = odometer;
        entry.createdOn = new java.sql.Date(date.getTime());
        entry.save();

        if (prevEntry != null) {
            Settings.addFuelAndDistance(entry.fuel, entry.odometer - prevEntry.odometer);
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        selectedDate.set(Calendar.YEAR, year);
        selectedDate.set(Calendar.MONTH, monthOfYear);
        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        invalidateView();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        selectedDate.set(Calendar.MINUTE, minute);

        invalidateView();
    }
}
