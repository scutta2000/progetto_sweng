package org.pietroscuttari.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "lock")
public class Lock {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private BikeType type;
    // @ invariant position >= 0
    @DatabaseField
    private int position;
    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    private Bike bike;
    @DatabaseField(foreign = true)
    private Rack rack;
    
    public Lock() {

    }

    public Lock(Rack rack, BikeType type, int position) {
        if (position<0){
            throw new IllegalArgumentException("Position must be greater than zero");
        }

        this.type = type;
        this.position = position;
        this.rack = rack;
    }

    public int getId() {
        return id;
    }
    public BikeType getType() {
        return type;
    }
    // @ ensures position >= 0
    public int getPosition() {
        return position;
    }
    public Bike getBike() {
        return bike;
    }
    public Rack getRack() {
        return rack;
    }

    public void setBike(Bike bike) {
        this.bike = bike;
    }

    @Override
    public String toString() {
        return "N°"+ position + ": " + type;
    }
}
