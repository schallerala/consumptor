package rol.metropolia.fi.consumptor.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.sql.Date;
import java.util.Calendar;

/**
 * iConnect iCR
 * <p/>
 * Created by Roman Laitarenko on 4/8/16.
 * Copyright (c) 2016 iConnect POS. All rights reserved
 */
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
}
