package team.metropolia.fi.consumptor.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import team.metropolia.fi.consumptor.R;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/17/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
public class Settings {

    private static final String PREFERENCE_FILE_KEY = "team.metropolia.fi.consumptor.PREFERENCE_FILE_KEY";
    private static final String CURRENT_UNIT_KEY = "CURRENT_UNIT_KEY";

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

            return (int) (((100 * 3.785411784) / 1.609344) * (1 / (float) consumptionInLP100));
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
}
