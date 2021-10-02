package org.pietroscuttari.managers;

import java.sql.SQLException;
import java.util.Comparator;

import org.pietroscuttari.data.Database;
import org.pietroscuttari.data.Rack;
import org.pietroscuttari.data.Trip;
import org.pietroscuttari.utils.DateUtils;

public class StatisticsManager {
	private static Database db = Database.getInstance();

	private StatisticsManager(){
		
	}

	public static int meanBikesUsed() {
		try {
				var r = db.getTrips();
				var minDate = r.stream().min(new Comparator<Trip>() {
						@Override
						public int compare(Trip o1, Trip o2) {
								return Long.compare(o1.getStart().getTime(), o2.getStart().getTime());
						}
				}).get().getStart();
				long days = DateUtils.sub(minDate, DateUtils.now()).toDays();
				return (int) (r.size() / days);
		} catch (Exception e) {
				return 0;
		}
	}

	//In case of parity it picks an arbitrary one
	public static Rack mostUsedRack() {
		try {
				var t = db.getRacks();
				var o = t.stream().max(new Comparator<Rack>() {
						public int compare(Rack o1, Rack o2) {
								return Integer.compare(o1.getTrips().size(), o2.getTrips().size());
						};
				});
				if (o.isPresent()) {
						return o.get();
				} else {
						return null;
				}
		} catch (SQLException e) {
				return null;
		}
	}
}
