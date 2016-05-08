package team.metropolia.fi.consumptor.Models;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import team.metropolia.fi.consumptor.Settings.Settings;

@Table(name = "FuelEntry", id = "_id")
public class FuelEntry extends Model {
    @Column
    public Date createdOn;
    @Column
    public int odometer;
    @Column
    public int fuel;

    public FuelEntry() {
        createdOn = new Date(Calendar.getInstance().getTimeInMillis());
    }

    public static int getAverageConsumption() {

        return Settings.calculateAverage();
    }

    public static int getMaxConsumption() {

        return Settings.retrieve_maxConsumption();
    }

    public static int getMinConsumption() {

        return Settings.retrieve_minConsumption();
    }

    public static int getLastConsumption() {

        List<FuelEntry> entries = new Select().from(FuelEntry.class)
                .orderBy("createdOn DESC")
                .limit(2)
                .execute();

        if (entries.size() < 2) {
            return 0;
        }

        // un-optimized code ...
        // for clarity of calculation
        FuelEntry firstEntry = entries.get(entries.size() - 1);
        FuelEntry lastEntry = entries.get(0);
        double distanceTravelled = lastEntry.odometer - firstEntry.odometer;
        double fuelUsed = lastEntry.fuel;

        int newLastConsumption = (int) (100.0 * fuelUsed / distanceTravelled);

        Settings.updateMinMaxWith(newLastConsumption);

        return newLastConsumption;
    }
}
