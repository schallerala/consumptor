package team.metropolia.fi.consumptor.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.activeandroid.query.Select;

import team.metropolia.fi.consumptor.Models.FuelEntry;
import team.metropolia.fi.consumptor.R;

public class Settings {

    private static final String PREFERENCE_FILE_KEY = "team.metropolia.fi.consumptor.PREFERENCE_FILE_KEY";
    private static final String CURRENT_UNIT_KEY = "CURRENT_UNIT_KEY";
    private static final String TOTAL_FUEL = "team.metropolia.fi.consumptor.TOTAL_FUEL_KEY";
    private static final String TOTAL_DISTANCE = "team.metropolia.fi.consumptor.TOTAL_DISTANCE_KEY";
    private static final String MIN_CONSUMPTION = "team.metropolia.fi.consumptor.MIN_CONSUMPTION_KEY";
    private static final String MAX_CONSUMPTION = "team.metropolia.fi.consumptor.MAX_CONSUMPTION_KEY";

    private static Context sContext;

    public enum Unit {
        LP100KM(R.string.lp100km_title, R.string.lp100km_short_title),
        MPG(R.string.mpg_title, R.string.mpg_short_title);

        private
        @StringRes
        int descriptionResId;
        private
        @StringRes
        int shortDescriptionResId;

        @NonNull
        protected static Unit unitFromIndex(int index) {

            if (index >= Unit.values().length) {
                return LP100KM;
            }

            return Unit.values()[index];
        }

        Unit(@StringRes int descriptionResId, @StringRes int shortDescriptionResId) {
            this.descriptionResId = descriptionResId;
            this.shortDescriptionResId = shortDescriptionResId;
        }

        public
        @StringRes
        int getDescriptionResId() {
            return descriptionResId;
        }

        public
        @StringRes
        int getShortDescriptionResId() {
            return shortDescriptionResId;
        }

        public int convert(int consumptionInLP100) {

            if (this == LP100KM) {
                return consumptionInLP100;
            }

            return (int) (((100.0 * 3.785411784) / 1.609344) * (1.0 / (float) consumptionInLP100));
        }
    }

    public static void initialize(@NonNull Context context) {
        sContext = context;
    }

    @NonNull
    private static SharedPreferences getSharedPreferences() {
        return sContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    @NonNull
    public static Unit getCurrentUnit() {

        int unitIndex = getSharedPreferences().getInt(CURRENT_UNIT_KEY, Unit.LP100KM.ordinal());

        return Unit.unitFromIndex(unitIndex);
    }

    public static void setCurrentUnit(@NonNull Unit unit) {

        getSharedPreferences().edit().putInt(CURRENT_UNIT_KEY, unit.ordinal()).commit();
    }

    public static void addFuelAndDistance(int fuel, int distance) {
        int currentFuel = getSharedPreferences().getInt(TOTAL_FUEL, Context.MODE_PRIVATE),
                currentDistance = getSharedPreferences().getInt(TOTAL_DISTANCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = getSharedPreferences().edit();
        edit.putInt(TOTAL_FUEL, currentFuel + fuel);
        edit.putInt(TOTAL_DISTANCE, currentDistance + distance);
        edit.commit();
    }

    public static int retrieve_minConsumption() {
        return getSharedPreferences().getInt(MIN_CONSUMPTION, 0);
    }

    public static int retrieve_maxConsumption() {
        return getSharedPreferences().getInt(MAX_CONSUMPTION, 0);
    }

    public static int calculateAverage() {
        SharedPreferences pref = getSharedPreferences();

        return (int)
                (100*((double)pref.getInt(TOTAL_FUEL, 0) / (double)pref.getInt(TOTAL_DISTANCE, 1)));
    }

    public static void updateMinMaxWith(int newFuelConsumption) {
        // create handles from SharedPreferences
        SharedPreferences pref = getSharedPreferences();
        SharedPreferences.Editor edit = pref.edit();

        // get min max fuel consumption from SharedPreferences
        int minConsumption = pref.getInt(MIN_CONSUMPTION, -3_14_15);
        int maxConsumption = pref.getInt(MAX_CONSUMPTION, -3_14_15);

        if (new Select().from(FuelEntry.class).count() == 2) {
            edit.putInt(MAX_CONSUMPTION, newFuelConsumption);
            edit.putInt(MIN_CONSUMPTION, newFuelConsumption);
        }
        if (minConsumption == -3_14_15 || newFuelConsumption < minConsumption) {
            edit.putInt(MIN_CONSUMPTION, newFuelConsumption);
        }

        if (maxConsumption == -3_14_15 || newFuelConsumption > maxConsumption) {
            edit.putInt(MAX_CONSUMPTION, newFuelConsumption);
        }

        // commit changes from all putInt calls above (within the scope of THIS method)
        edit.commit();
    }
}
