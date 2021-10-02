package org.pietroscuttari.managers;

import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;

import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Bike;
import org.pietroscuttari.data.Card;
import org.pietroscuttari.data.Database;
import org.pietroscuttari.data.Trip;

import java.util.Comparator;

public class AccountManager {
	private static Database db = Database.getInstance();

	private AccountManager(){
		
	}

	public static double getPayAmount(Account account, Bike bike, Duration duration) {
		var minutes = duration.toMinutes();
		var halfHours = (minutes + 29) / 30; // approximate by excess

		double toPay = 0;

		if (duration.toHours() >= 24) {
			toPay = 150;
		} else if (duration.toMinutes() > 120) {
			int exceeds = account.getNumberOfStrikes();
			exceeds += 1;
			account.setNumberOfStrikes(exceeds);
		}

		toPay += bike.getCost(halfHours);

		return toPay;
	}

	public static boolean checkReturn(Account account) {
		return account.getTrips().stream().allMatch(x -> x.getEnd() != null);
	}

	// @requires type != null
	// @requires password != null
	// @requires cardExpireDate != null
	public static Account register(String password, AccountType type, String cardCode, Date cardExpireDate,
			String cvv) {
		try {
			Account account = new Account(password, type);

			if (!PaymentManager.isValidCard(cardCode, cardExpireDate, cvv, type)){
				return null;
			}

			var card = new Card(cardCode, cvv, cardExpireDate);

			if (PaymentManager.pay(card, account.getCost())) {
				account.setCard(card);
				if (type == AccountType.year) {
					account.activate();
					db.createOrUpdateCard(card);
				}
				db.createOrUpdateAccount(account);
				return account;
			} else {
				return null;
			}

		} catch (SQLException e) {
			return null;
		}
	}

	// @requires type != null
	// @requires password != null
	// @requires cardExpireDate != null
	public static Account registerStudent(String password) {
		try {
			Account account = new Account(password, AccountType.student);
			// Card card = new Card(cardCode, cvv, cardExpireDate);
			account.setCard(null);

			account.activate();
			db.createOrUpdateAccount(account);
			return account;
		} catch (SQLException e) {
			return null;
		}
	}

	// @requires password != null
	public static Account login(int id, String password) {
		try {
			var account = db.getAccount(id);
			if (account == null) {
				return null;
			}
			return account.getPassword().equals(password) ? account : null;
		} catch (SQLException e) {
			return null;
		}
	}

	public static Trip getLastTrip(Account account) {
		try {
			account = db.getAccount(account.getId());
		} catch (SQLException e) {
			return null;
		}

		var sorted = account.getTrips().stream().sorted(new Comparator<Trip>() {
			@Override
			public int compare(Trip o1, Trip o2) {
				var end1 = o1.getEnd();
				var end2 = o2.getEnd();

				if (end1 == null) {
					return -1;
				} else if (end2 == null) {
					return 1;
				} else {
					return end1.compareTo(end2);
				}
			}
		});

		var first = sorted.findFirst();
		if (first.isPresent()) {
			return first.get();
		} else {
			return null;
		}
	}

	public static boolean addStrike(Account a) {
		a.setNumberOfStrikes(a.getNumberOfStrikes() + 1);
		try{
			db.createOrUpdateAccount(a);
		}catch (SQLException e){
			e.printStackTrace();
		}
		return true;
	}

	public static boolean isStudent(int id, String email) {
		return email.endsWith("@studenti.unimi.it"); // Simulated
	}
}