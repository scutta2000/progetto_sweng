package org.pietroscuttari.data;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.pietroscuttari.utils.DateUtils;

@DatabaseTable(tableName = "trip")
public class Trip {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private Date start;
    @DatabaseField
    private Date end;
    
    @DatabaseField(foreign = true)
    private Bike bike;
    @DatabaseField(foreign = true)
    private Account account;
    @DatabaseField(foreign = true)
    private Rack rack;

    public Trip() {

    }

    public Trip(Rack rack, Account account, Bike bike) {
        this.account = account;
        this.bike = bike;
        this.rack = rack;
        start = DateUtils.now();
    }

    public int getId() {
        return id;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Bike getBike() {
        return bike;
    }

    public Account getAccount() {
        return account;
    }

    public Rack geTrack() {
        return this.rack;
    }
}
