package rol.metropolia.fi.consumptor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.content.ContentProvider;

import org.w3c.dom.Text;

import java.text.DateFormat;

import rol.metropolia.fi.consumptor.Models.FuelEntry;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class FuelEntriesListActivity extends AppCompatActivity {

    private ListView listView;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_entries_list);

        ActiveAndroid.initialize(this);

        listView = (ListView) findViewById(R.id.list_view);
        addButton = (FloatingActionButton) findViewById(R.id.add_button);

        listView.setAdapter(new FuelEntriesAdapter(this));

        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(FuelEntriesListActivity.this,
                        ContentProvider.createUri(FuelEntry.class, null),
                        null,
                        null,
                        null,
                        "createdOn");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ((CursorAdapter) listView.getAdapter()).swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                ((CursorAdapter) listView.getAdapter()).swapCursor(null);
            }
        });
    }

    public void onAddButtonPressed(View view) {

//        Intent intent = new Intent(this, FuelEntryActivity.class);
//
//        startActivityForResult(intent, -1);

        showFuelAmountPicker();
    }

    private void showFuelAmountPicker() {

        final NumberPicker picker = new NumberPicker(this);

        String[] nums = new String[20];
        for (int i = 0; i < nums.length; i++)
            nums[i] = Integer.toString(i);

        picker.setMinValue(1);
        picker.setMaxValue(20);
        picker.setWrapSelectorWheel(false);
        picker.setDisplayedValues(nums);
        picker.setValue(1);

        new AlertDialog.Builder(this).setView(picker).setTitle("Enter fuel amount")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onFuelAmountReceived(picker.getValue());
                    }
                }).show();
    }

    private void onFuelAmountReceived(int amount) {

        FuelEntry entry = new FuelEntry();

        entry.odometer = 1000;
        entry.fuel = amount;

        entry.save();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

//            String result = data.getStringExtra(CurrencyPickerActivity.CONVERSION_RESULT);
//
//            resultTextView.setText(result);
        }
    }


    private class FuelEntriesAdapter extends ResourceCursorAdapter {

        private FuelEntriesAdapter(Context context) {

            super(context, R.layout.item_fuel_entry_list, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            FuelEntry entry = new FuelEntry();

            entry.loadFromCursor(cursor);

            TextView dateTextView = (TextView) view.findViewById(R.id.date_text_view);
            TextView odometerTextView = (TextView) view.findViewById(R.id.odometer_text_view);
            TextView fuelTextView = (TextView) view.findViewById(R.id.fuel_text_view);

            String dateTitle = DateFormat.getDateInstance().format(entry.createdOn);
            String odometerTitle = context.getResources().getString(R.string.odometer_km_format, entry.odometer);
            String fuelTitle = context.getResources().getString(R.string.fuel_liter_format, entry.fuel);

            dateTextView.setText(dateTitle);
            odometerTextView.setText(odometerTitle);
            fuelTextView.setText(fuelTitle);
        }
    }
}
