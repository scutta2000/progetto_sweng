package org.pietroscuttari.data;

public enum BikeType {
    standard, electric, electricChildSeat;

    @Override
    public String toString() {
        switch (this) {
            case standard:
                return "Standard";
            case electric:
                return "Electric";
            case electricChildSeat:
                return "Electric with baby seat";
            default:
                return "";
        }
    }
}
