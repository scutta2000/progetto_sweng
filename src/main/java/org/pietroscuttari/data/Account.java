package org.pietroscuttari.data;

import java.time.Duration;
import java.util.Date;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.pietroscuttari.utils.DateUtils;

@DatabaseTable(tableName = "account")
public class Account {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String password;
	@DatabaseField
	private Date startTime;
	@DatabaseField(foreign = true)
	private Card card;
	@DatabaseField
	private AccountType type;
	// @ invariant numberOfStrikes >=0
	@DatabaseField
	private int numberOfStrikes = 0;
	@ForeignCollectionField
	ForeignCollection<Trip> trips;
	

	public Account() {
	}
	public Account(String password, AccountType type) {
		this.type = type;
		this.password = password;
	}


	public int getId() {
		return id;
	}
	public String getPassword() {
		return password;
	}
	public Date getStartTime() {
		return startTime;
	}
	public Card getCard() {
		return card;
	}
	public AccountType getType() {
		return type;
	}
	public int getNumberOfStrikes() {
		return numberOfStrikes;
	}
	public ForeignCollection<Trip> getTrips() {
		return trips;
	}
	public double getCost() {
		if (isStudent())
			return 0;
		switch (type) {
			case day:
				return 4.5;
			case week:
				return 9;
			case year:
				return 36;
			case admin:
				return 0;
			default:
				return 0;
		}
	}
	public Duration getTime() {
		switch (type) {
			case day:
				return Duration.ofDays(1);
			case week:
				return Duration.ofDays(7);
			case year:
				return Duration.ofDays(365);
			case admin:
				return Duration.ofDays(1000);
			default:
				return Duration.ofDays(0);
		}
	}


	public boolean isStudent() {
		return type == AccountType.student;
	}
	public boolean isAdmin() {
		return type == AccountType.admin;
	}
	public boolean isExpired() {
		if (isAdmin())
			return false;
		if (numberOfStrikes >= 3) {
			return true;
		}
		if (startTime == null) {
			return false;
		}
		var actualTime = DateUtils.now();
		var difference = actualTime.getTime() - startTime.getTime() + getTime().toMillis();
		return difference <= 0;
	}


	public void setCard(Card card) {
		this.card = card;
	}
	// @ requires numberOfStrikes >=0
	public void setNumberOfStrikes(int numberOfStrikes) {
		this.numberOfStrikes = numberOfStrikes;
	}



	public void activate() {
		if (this.startTime == null){
			this.startTime = DateUtils.now();
		}
	}


}
