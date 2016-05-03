package team.metropolia.fi.consumptor.FuelList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.content.ContentProvider;

import java.text.DateFormat;

import team.metropolia.fi.consumptor.FuelEntry.FuelEntryActivity;
import team.metropolia.fi.consumptor.Models.FuelEntry;
import team.metropolia.fi.consumptor.R;
import team.metropolia.fi.consumptor.Settings.Settings;
import team.metropolia.fi.consumptor.Settings.SettingsDialogBuilder;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class FuelEntriesListActivity extends AppCompatActivity implements SettingsDialogBuilder.SettingsChangeListener {

    static final int FUEL_ENTRY_ACTIVITY_REQUEST_CODE = 8;

    private RecyclerView recyclerView;
    private LinearLayout subTitlesContainer;
    private Toolbar toolbar;
    private ConsumptionView mainConsumptionView;
    private ConsumptionView maxConsumptionView;
    private ConsumptionView minConsumptionView;
    private ConsumptionView lastConsumptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fuel_entries_list);

        ActiveAndroid.initialize(this);
        Settings.initialize(this);

        bindActivity();

        toolbar.inflateMenu(R.menu.main_menu);

        setSupportActionBar(toolbar);

        setupRecyclerView();
        invalidateView();
    }

    private void bindActivity() {

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        subTitlesContainer = (LinearLayout) findViewById(R.id.subtitles_container);
        recyclerView = (RecyclerView) findViewById(R.id.list_view);
        mainConsumptionView = (ConsumptionView) findViewById(R.id.main_consumption_view);
        maxConsumptionView = (ConsumptionView) findViewById(R.id.max_consumption_view);
        minConsumptionView = (ConsumptionView) findViewById(R.id.min_consumption_view);
        lastConsumptionView = (ConsumptionView) findViewById(R.id.last_consumption_view);
    }

    private void invalidateView() {

        Settings.Unit currentUnit = Settings.getCurrentUnit();

        final int average = currentUnit.convert(FuelEntry.getAverageConsumption());
        final int max = currentUnit.convert(FuelEntry.getMaxConsumption());
        final int min = currentUnit.convert(FuelEntry.getMinConsumption());
        final int last = currentUnit.convert(FuelEntry.getLastConsumption());

        mainConsumptionView.getValueTextView().setText(String.valueOf(average));
        maxConsumptionView.getValueTextView().setText(String.valueOf(max));
        minConsumptionView.getValueTextView().setText(String.valueOf(min));
        lastConsumptionView.getValueTextView().setText(String.valueOf(last));

        mainConsumptionView.getTitleTextView().setText(currentUnit.getShortDescriptionResId());
        maxConsumptionView.getTitleTextView().setText(currentUnit.getShortDescriptionResId());
        minConsumptionView.getTitleTextView().setText(currentUnit.getShortDescriptionResId());
        lastConsumptionView.getTitleTextView().setText(currentUnit.getShortDescriptionResId());
    }

    private void setupRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(this));

        recyclerView.setAdapter(new MyRecyclerAdapter(this));


        getSupportLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return new CursorLoader(FuelEntriesListActivity.this,
                        ContentProvider.createUri(FuelEntry.class, null),
                        null,
                        null,
                        null,
                        "createdOn DESC");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ((MyRecyclerAdapter) recyclerView.getAdapter()).mCursorAdapter.swapCursor(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                ((MyRecyclerAdapter) recyclerView.getAdapter()).mCursorAdapter.swapCursor(null);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        showSettingsDialog();

        return true;
    }

    public void onAddButtonPressed(View view) {

        Intent intent = new Intent(this, FuelEntryActivity.class);

        startActivityForResult(intent, FUEL_ENTRY_ACTIVITY_REQUEST_CODE);
    }

    private void showSettingsDialog() {

        SettingsDialogBuilder.buildSettingsDialog(this, this).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            invalidateView();
        }
    }

    @Override
    public void onSettingsChanged() {
        invalidateView();
    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

        // Because RecyclerView.Adapter in its current form doesn't natively
        // support cursors, we wrap a CursorAdapter that will do all the job
        // for us.
        CursorAdapter mCursorAdapter;

        Context mContext;

        public MyRecyclerAdapter(Context context) {

            mContext = context;

            mCursorAdapter = new FuelEntriesAdapter(mContext);
            mCursorAdapter.registerDataSetObserver(new NotifyingDataSetObserver());
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public int getItemCount() {
            return mCursorAdapter.getCount();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            // Passing the binding operation to cursor loader
            mCursorAdapter.getCursor().moveToPosition(position);
            mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // Passing the inflater job to the cursor-adapter
            View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
            return new ViewHolder(v);
        }

        private class NotifyingDataSetObserver extends DataSetObserver {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
                //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
            }
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

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        /**
         * Custom divider will be used
         */
        public DividerItemDecoration(Context context, int resId) {
            mDivider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
