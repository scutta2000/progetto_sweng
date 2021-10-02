package org.pietroscuttari.managers;

import java.sql.SQLException;

import org.pietroscuttari.data.Bike;
import org.pietroscuttari.data.Database;

public class BikeManager {
	private static Database db = Database.getInstance();

	private BikeManager(){
		
	}

	public static boolean reportBroken(int id) {
		try {
				var bike = db.getBike(id);
				if (bike == null)
						return false;
				bike.setBroken(true);
				db.createOrUpdateBike(bike);
				return true;
		} catch (SQLException e) {
				return false;
		}
	}

	public static void removeBike(Bike bike) throws SQLException{
		var lock = bike.getLock();

		RackManager.takeBike(lock.getId()); //Not sure if necessary
		db.removeBike(bike.getId());
	}

	public static boolean makeBikeOperative(Bike bike) {
		try {
				bike.setBroken(false);
				db.createOrUpdateBike(bike);
				return true;
		} catch (SQLException e) {
				return false;
		}
	}
}
