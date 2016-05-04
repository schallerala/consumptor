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

//        List<FuelEntry> entries = new Select().from(FuelEntry.class)
//                .orderBy("createdOn DESC")
//                .execute();
//
//        if (entries.size() < 2) {
//            return 0;
//        }
//
//        FuelEntry firstEntry = entries.get(0);
//        FuelEntry lastEntry = entries.get(entries.size() - 1);
//        float distance = lastEntry.odometer - firstEntry.odometer;
//        float fuelUsed = calculateFuelUsed(entries);

        return Settings.calculateAverage();
    }

    public static int getMaxConsumption() {

//        List<Integer> individualConsumptions = getConsumptionSlices();
//
//        if (individualConsumptions.isEmpty()) {
//            return 0;
//        }
//
//        return individualConsumptions.get(individualConsumptions.size() - 1);
        return Settings.retrieve_maxConsumption();
    }

    public static int getMinConsumption() {

//        List<Integer> individualConsumptions = getConsumptionSlices();
//
//        if (individualConsumptions.isEmpty()) {
//            return 0;
//        }
//
//        return individualConsumptions.get(0);
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

        FuelEntry firstEntry = entries.get(entries.size() - 1);
        FuelEntry lastEntry = entries.get(0);
        double distanceTravelled = lastEntry.odometer - firstEntry.odometer;
        double fuelUsed = lastEntry.fuel;

        int newLastComsumption = (int) (100.0 * fuelUsed / distanceTravelled);

        Settings.updateMinMaxWith(newLastComsumption);

        return newLastComsumption;
    }

    @NonNull
    private static List<Integer> getConsumptionSlices() {

        List<Integer> consumptions = new ArrayList<>();
        List<FuelEntry> entries = new Select().from(FuelEntry.class)
                .orderBy("createdOn DESC")
                .execute();

        if (entries.size() < 2) {
            return consumptions;
        }

        for (int i = 0; i < entries.size() - 1; i++) {

            FuelEntry current = entries.get(i);
            FuelEntry next = entries.get(i + 1);
            float distance = next.odometer - current.odometer;
            float fuel = next.fuel + current.fuel;
            Integer consumption = (int) ((distance / fuel) * 100);

            consumptions.add(consumption);
        }

        Collections.sort(consumptions);

        return consumptions;
    }

    private static float calculateFuelUsed(List<FuelEntry> entries) {

        float fuelUsed = 0;

        for (FuelEntry entry : entries) {
            fuelUsed += entry.fuel;
        }
        return fuelUsed;
    }
}
