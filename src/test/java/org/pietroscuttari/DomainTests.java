package org.pietroscuttari;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Bike;
import org.pietroscuttari.data.BikeType;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DomainTests {
    @DisplayName("Bike cost")
    @Test
    void testBikeCost() {
        Bike b1 = new Bike(BikeType.standard);
        Bike b2 = new Bike(BikeType.electric);
        Bike b3 = new Bike(BikeType.electricChildSeat);

        assertTrue(b1.getCost(1) == 0);
        assertTrue(b1.getCost(4) == 1.5);
        assertTrue(b1.getCost(5) == 7.5);

        assertTrue(b2.getCost(1) == 0.25);
        assertTrue(b2.getCost(3) == 1);
        assertTrue(b2.getCost(5) == 6);

        assertTrue(b3.getCost(2) == 0.5);
        assertTrue(b3.getCost(4) == 2);
        assertTrue(b3.getCost(6) == 10);
    }

    @DisplayName("account expiration")
    @Test
    void testaccountExpiration() {
        Account s = new Account("test", AccountType.day);
        assertTrue(!s.isExpired());
        s.setNumberOfStrikes(3);
        assertTrue(s.isExpired());
    }

    @DisplayName("account cost")
    @Test
    void testaccountCost() {
        assertTrue(new Account("test", AccountType.day).getCost() == 4.5);
        assertTrue(new Account("test", AccountType.week).getCost() == 9);
        assertTrue(new Account("test", AccountType.year).getCost() == 36);
        assertTrue(new Account("test", AccountType.admin).getCost() == 0);
        assertTrue(new Account("test", AccountType.student).getCost() == 0);
    }
}