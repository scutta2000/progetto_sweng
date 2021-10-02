package org.pietroscuttari.managers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.pietroscuttari.data.*;
import org.pietroscuttari.utils.DateUtils;

public class RackManager {

	static Database db = Database.getInstance();

	private RackManager() {
	}

	// @require account != null
	// @require !account.isExpired()
	public static Transaction unlockBike(int rackId, Account account, BikeType type) {
		try {
			var rack = db.getRack(rackId);

			if ((account.getType() == AccountType.day || account.getType() == AccountType.week) && !account.isAdmin()) {
				account.activate();
			}
			db.createOrUpdateAccount(account);

			var _locks = rack.getLocks();
			var maybeLock = _locks.stream().filter(x -> x.getType() == type && x.getBike() != null).findFirst();
			if (!maybeLock.isPresent()) {
				return null;
			}
			var lock = maybeLock.get();

			var bike = lock.getBike();
			lock.setBike(null);
			bike.setLock(null);

			db.createOrUpdateBike(bike);
			db.createOrUpdateLock(lock);

			Trip trip = new Trip(rack, account, bike);
			db.createOrUpdateTrip(trip);

			LockManager.unlockLock(lock);

			return new Transaction(lock, trip);
			
		} catch (SQLException e) {
			return null;
		}
	}

	//Take a bike without creating a trip
	public static boolean takeBike(int lockId) {
		try {
			Lock lock = db.getLock(lockId);
			var bike = lock.getBike();
	
			bike.setLock(null);
			lock.setBike(null);

			LockManager.unlockLock(lock);
	
			db.createOrUpdateBike(bike);
			db.createOrUpdateLock(lock);
			return true;
			
		} catch (SQLException e) {
			return false;
		}
	}

	public static boolean returnBike(Lock lock, int bikeId) {
		try {
			Bike bike = db.getBike(bikeId);
	
			var r = bike.getTrips().stream().filter(x -> x.getEnd() == null).findFirst();
	
			if (!r.isPresent()) {
				return false;
			}
	
			var trip = db.getTrip(r.get().getId());
	
			var account = trip.getAccount();//not refreshing, might not work
	
			Date start = trip.getStart();
			Date end = DateUtils.now();
	
			bike.setLock(lock);
	
			lock.setBike(bike);
	
			trip.setEnd(end);
	
			db.createOrUpdateTrip(trip);
			db.createOrUpdateBike(bike);
			db.createOrUpdateLock(lock);
	
			var duration = DateUtils.sub(start, end);
	
			var amount = AccountManager.getPayAmount(account, bike, duration);
	
			var card = account.getCard();
	
			boolean paySuccess = PaymentManager.pay(card, amount);
	
			return paySuccess;

		} catch (SQLException e) {
			return false;
		}
	}

	public static Rack nearestRack(int fromId) {
		Rack rack;
		List<Rack> t;
		try {
			rack = db.getRack(fromId);
			t = db.getRacks();
		}catch (SQLException e){
			return null;
		}
		

		BiFunction<Rack, Rack, Double> distance = (from, to) -> {
			return Math.sqrt(Math.pow(Math.abs(from.getX() - to.getX()), 2)
						+ Math.pow(Math.abs(from.getY() - to.getY()), 2));
		};

		var s = t.stream().filter(x -> x.getId() != fromId).min(new Comparator<Rack>() {
			public int compare(Rack o1, Rack o2) {
				var v1 = distance.apply(o1, rack);
				var v2 = distance.apply(o2, rack);
				return Double.compare(v1, v2);
			}
		});

		return s.get();
	}
	
	public static Rack getRack(int rackId) {
		try {
			return db.getRack(rackId);
		} catch (Exception e) {
			return null;
		}   
	}
	public static List<Rack> getRacks(){
		try{
			var r = db.getRacks();
			return r ;
		}catch (SQLException e){
			return null;
		}
	}

	public static List<Lock> getLocks(Rack rack) {
		var locks = rack.getLocks();

		var adsf =  new ArrayList<> (locks);
		return adsf;
	}

	public static List<Bike> getBikes(int rackId) {
		try {
			var rack = db.getRack(rackId);
			return getBikes(rack);
		}catch (SQLException e){
			return null;
		}

	}

	// @requires rack != null
	public static List<Bike> getBikes(Rack rack) {
		try {
			rack = db.getRack(rack.getId());
	
			Collection<Lock> locks = rack.getLocks();
			List<Bike> b = locks.stream()
						.map(g -> g.getBike())
						.filter(x -> x != null)
						.collect(Collectors.toList());
						
			return b;
		}catch (SQLException e){
			return null;
		}

	}

	public static Bike addBike(Rack rack, Lock lock, BikeType type) {
		try {
			var bike = new Bike(type);
			db.createOrUpdateBike(bike);

			bike.setLock(lock);
			lock.setBike(bike);
			
			// rack.getLocks().stream().
			// 	filter((x) -> x.getId() == lock.getId()).
			// 	findFirst()
			// 	.orElse(null)
			// 	.setBike(bike);;

			db.updateLock(lock);
			db.updateBike(bike);
			// db.createOrUpdateRack(rack);
			return bike;
		} catch (SQLException e){
			return null;
		}
	}

	public static Lock addLock(Rack rack, BikeType type) {
		try {
			if (rack == null || type == null){
				return null;
			}

			var g = rack.getLocks();
			var sorted = g.stream().sorted(new Comparator<Lock>() {
				public int compare(Lock o1, Lock o2) {
					return Integer.compare(o1.getPosition(), o2.getPosition());
				};
			}).collect(Collectors.toList());
			int expectedPos = 0;
			for (int i = 0; i < sorted.size(); i++) {
				if (sorted.get(i).getPosition() != expectedPos)
					break;
				expectedPos++;
			}

			Lock l = new Lock(rack, type, expectedPos);
			db.createOrUpdateLock(l);
			return l;
		} catch (SQLException e) {
			return null;
		}
	}

	public static boolean removeLock(Lock lock) {
		try {
			if (lock.getBike() != null) {
				return false;
			};

			db.removeLock(lock.getId());
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

}