package org.pietroscuttari.managers;

import java.sql.SQLException;
import java.util.Date;

import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Card;
import org.pietroscuttari.data.Database;
import org.pietroscuttari.data.Lock;
import org.pietroscuttari.data.Transaction;
import org.pietroscuttari.data.Trip;
import org.pietroscuttari.utils.DateUtils;

public class PaymentManager {

    private static Database db = Database.getInstance();

    private PaymentManager(){
        
    }

    // @requires expireDate != null
    // @requires number.length == 16
    public static boolean isValidCard(String number, Date expireDate, String cvv, AccountType type) {
        var isValid = true; // Simulated
        boolean isExpired = false;

        switch (type) {
            case day:
                isExpired = DateUtils.oneDay().getTime() > expireDate.getTime();
                break;
            case week:
                isExpired = DateUtils.oneWeek().getTime() > expireDate.getTime();
                break;
            case year:
                isExpired = DateUtils.years(1).getTime() > expireDate.getTime();
                break;
            case admin:
                isExpired = false;
                break;
            default:
                isExpired = true;
        }

        return isValid && !isExpired;
    }

    // @requires amount >=0
    // @requires card != null
    public static boolean pay(Card card, double amount) {
        //if card == null
        return true; //simulated
    }

    // @requires t != null
    // @requires t.getlock() != null
    // @requires t.getTrip() != null
    public static boolean transactionCanceled(Transaction t) {
        try {
            Trip trip = t.getTrip();
            Lock lock = t.getLock();

            lock = db.getLock(lock.getId());
            trip = db.getTrip(trip.getId());

            if (lock.getBike() != null) {
                db.removeTrip(trip.getId());
            } else {
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}
