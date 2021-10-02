package org.pietroscuttari.data;

public class Transaction {
	public Transaction(Lock lock, Trip trip) {
		this.lock = lock;
		this.trip = trip;
	}

	private Lock lock;
	private Trip trip;

	public Lock getLock() {
		return lock;
	}

	public Trip getTrip() {
		return trip;
	}
}
