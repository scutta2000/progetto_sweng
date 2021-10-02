package org.pietroscuttari.data;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.pietroscuttari.managers.AccountManager;
import org.pietroscuttari.managers.BikeManager;
import org.pietroscuttari.managers.RackManager;
import org.pietroscuttari.utils.DateUtils;

public class Database {
	private static Database _instance;
	
	private static String databaseUrl = "jdbc:sqlite:data.db";
	private ConnectionSource source;
	
	private Dao<Bike, Integer> bikes;
	private Dao<Card, Integer> cards;
	private Dao<Account, Integer> accounts;
	private Dao<Rack, Integer> racks;
	private Dao<Lock, Integer> locks;
	private Dao<Trip, Integer> trips;
	
	private boolean initialized = false;

	private Database() {
	}

	public static Database getInstance() {
		if (_instance == null) {
			_instance = new Database();
			if (!_instance.open()){
				throw new RuntimeException("Invalid database url");
			}
		}
		return _instance;
	}


	
	public void createOrUpdateBike(Bike b) throws SQLException{
		bikes.createOrUpdate(b);
	}
	public void createOrUpdateCard(Card b) throws SQLException{
		cards.createOrUpdate(b);
	}
	public void createOrUpdateAccount(Account b) throws SQLException{
		accounts.createOrUpdate(b);
	}
	public void createOrUpdateRack(Rack b) throws SQLException{
		racks.createOrUpdate(b);
	}
	public void createOrUpdateLock(Lock b) throws SQLException{
		locks.createOrUpdate(b);
	}
	public void createOrUpdateTrip(Trip b) throws SQLException{
		trips.createOrUpdate(b);
	}


	public void updateBike(Bike b) throws SQLException {
		bikes.update(b);
	}
	public void updateLock(Lock b) throws SQLException {
		locks.update(b);
	}

	public Bike getBike(int id) throws SQLException{
		return bikes.queryForId(id);
	}
	public Card getCard(int id) throws SQLException{
		return cards.queryForId(id);
	}
	public Account getAccount(int id) throws SQLException{
		return accounts.queryForId(id);
	}
	public Rack getRack(int id) throws SQLException{
		return racks.queryForId(id);
	}
	public Lock getLock(int id) throws SQLException{
		return locks.queryForId(id);
	}
	public Trip getTrip(int id) throws SQLException{
		return trips.queryForId(id);
	}

	public List<Rack> getRacks() throws SQLException{
		return racks.queryForAll();
	}
	public List<Trip> getTrips() throws SQLException{
		return trips.queryForAll();
	}

	public void removeBike(int id) throws SQLException{
		bikes.delete(getBike(id));
	}
	public void removeCard(int id) throws SQLException{
		cards.delete(getCard(id));
	}
	public void removeAccount(int id) throws SQLException{
		accounts.delete(getAccount(id));
	}
	public void removeRack(int id) throws SQLException{
		racks.delete(getRack(id));
	}
	public void removeLock(int id) throws SQLException{
		locks.delete(getLock(id));
	}
	public void removeTrip(int id) throws SQLException{
		trips.delete(getTrip(id));
	}



	public boolean ensureCreated() {
		if (!open())
			return false;

		try {
			TableUtils.createTableIfNotExists(source, Card.class);
			TableUtils.createTableIfNotExists(source, Account.class);
			TableUtils.createTableIfNotExists(source, Bike.class);
			TableUtils.createTableIfNotExists(source, Lock.class);
			TableUtils.createTableIfNotExists(source, Trip.class);
			TableUtils.createTableIfNotExists(source, Rack.class);

			System.out.println("Tables created");

			cards = DaoManager.createDao(source, Card.class);
			accounts = DaoManager.createDao(source, Account.class);
			bikes = DaoManager.createDao(source, Bike.class);
			locks = DaoManager.createDao(source, Lock.class);
			trips = DaoManager.createDao(source, Trip.class);
			racks = DaoManager.createDao(source, Rack.class);

			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private boolean open() {
		try {
			source = new JdbcConnectionSource(databaseUrl);
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	private boolean close() {
		try {
			source.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public void createFakeData(){
		if (this.initialized){
			return;
		}
		this.initialized = true;

		try {
			Rack[] racks = {
				new Rack("Via Celora 18", 0, 0),
				new Rack("Piazza Duomo", 10, 10),
				new Rack("Via Padova 353", 20, 20)
			};
			for (Rack r : racks){
				createOrUpdateRack(r);
				this.racks.refresh(r); //necessary to initialize foreignCollections

				for (BikeType t : BikeType.values()){
					var l = RackManager.addLock(r, t);
					if (l  == null){
						throw new NullPointerException();
					}
					var b = RackManager.addBike(r, l, t);
					if (b == null){
						throw new IllegalArgumentException("Cant add bike " + l + " " + "t");
					}
					System.out.println();
				}
				createOrUpdateRack(r);
			}
			
			var asdf = locks.queryForAll();
			LinkedHashMap<String, AccountType> accounts = new LinkedHashMap <String, AccountType>();
			accounts.put("admin", AccountType.admin);				//Id:1
			accounts.put("normal", AccountType.week);				//Id:2
			accounts.put("expired", AccountType.day);				//Id:3
			
			for (var e: accounts.entrySet()) {
				var a = AccountManager.register(e.getKey(), e.getValue(), "378734493671000",DateUtils.years(1), "123");
				if (a==null){
					throw new NullPointerException("Cant register " + e.getKey());
				}
			}

			AccountManager.registerStudent("student");				//Id:4

			
			var expired = AccountManager.login(3, "expired");
			if (expired == null){
				throw new NullPointerException("Cant log into expired");
			}
			for (int i=0; i<3; i++) {
				AccountManager.addStrike(expired);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}
	}

	//When the class gets destroyed by the garbage collector it runs this method
	@Override
	public void finalize(){
		_instance.close();
	}
}
