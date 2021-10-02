package org.pietroscuttari.data;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "rack")
public class Rack {
    @DatabaseField(generatedId = true)
    private int id;
    @ForeignCollectionField
    ForeignCollection<Lock> locks;
    @DatabaseField
    private int x;
    @DatabaseField
    private int y;
    // @require address != null
    @DatabaseField
    private String address;
    @ForeignCollectionField
    ForeignCollection<Trip> trips;


    public Rack() {

    }

    public Rack(String address, int x, int y) {
        this.address = address;
        this.x = x;
        this.y = y;
    }


    public int getId() {
        return id;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public String getAddress() {
        return address;
    }


    public ForeignCollection<Lock> getLocks() {
        return locks;
    }


    public ForeignCollection<Trip> getTrips() {
        return this.trips;
    }

    @Override
    public String toString() {
        return "N°"+ id + ": " + address;
    }
}
