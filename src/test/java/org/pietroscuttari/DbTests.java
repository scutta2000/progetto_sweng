package org.pietroscuttari;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Database;
import org.pietroscuttari.data.Lock;
import org.pietroscuttari.data.Rack;
import org.pietroscuttari.managers.AccountManager;
import org.pietroscuttari.managers.BikeManager;
import org.pietroscuttari.managers.LockManager;
import org.pietroscuttari.managers.RackManager;
import org.pietroscuttari.utils.DateUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;

public class DbTests {

	private static Database db = Database.getInstance();

	@BeforeAll
	public static void setup() {
		File file = new File("./data.db");
		file.delete();

		db.ensureCreated();
		db.createFakeData();

	}

	@DisplayName("Test Login")
	@Test
	void testLogin() {
		Account a = AccountManager.login(1, "admin"); 

		assertNotNull(a, "Cant login in admin");
	}

	@DisplayName("Test Wrong Login")
	@Test
	void testWrongLogin() {
		Account a = AccountManager.login(1, "expired");

		assertNull(a);
	}

	@DisplayName("Test Register")
	@Test
	void testRegister() {
		assertNotNull(AccountManager.register("psd", AccountType.day, "1111111111111111", DateUtils.years(1), "444"));
	}

	@DisplayName("Test Report")
	@Test
	void testReport() {
		assertTrue(BikeManager.reportBroken(1));
	}

	@DisplayName("Test Make Operative")
	@Test
	void testMakeOperative() {

		assertTrue(BikeManager.makeBikeOperative(RackManager.getBikes(1).get(0)));
	}

	@DisplayName("Test Unlock lock")
	@Test
	void testUnlocklock() {
		var lock = RackManager.getRack(1).getLocks().stream().collect(Collectors.toList()).get(0);
		assertTrue(LockManager.unlockLock(lock));
	}

	@DisplayName("Test Block lock")
	@Test
	void testBlocklock() {
		var lock = RackManager.getRack(1).getLocks().stream().collect(Collectors.toList()).get(0);
		assertTrue(LockManager.blockLock(lock));
	}

	@DisplayName("Test createFakeData racks")
	@Test
	void testFakeRacks(){
		List<String> racks =  Arrays.asList(
			"Via Celora 18",
			"Piazza Duomo",
			"Via Padova 353"
		);
		for (Rack r : RackManager.getRacks()){
			assertTrue(
				racks.stream().anyMatch(
					x -> {return x.equals(r.getAddress());}
				), r.getAddress() + " doesn't match"
			);

			for (Lock l : r.getLocks()){
				var b = l.getBike();
				assertNotNull(b,"Bike: " + b + " is null");

				assertNotNull(b.getLock(),"Bike: " + b + " has a null lock");
				assertNotNull(b.getType(),"Bike: " + b + " has a null type");

				assertNotNull(l.getRack(), l + " has no racks");
			}
		}
	}

	@DisplayName("Test createFakeData accounts")
	@Test
	void testFakeAccounts(){
		var accounts = Map.of(
			1, "admin",				//Id:1
			2, "normal",		    //Id:2
			3, "expired",			//Id:3
			4, "student"			//Id:4
		);

		for (var e: accounts.entrySet()) {
			assertNotNull(
				AccountManager.login(e.getKey(), e.getValue()),
				"Cant login in " + e.getKey() + ": " + e.getValue()
			);
		}

		var expired = AccountManager.login(3, "expired");
		assertTrue(expired.isExpired(), "expired has only " + expired.getNumberOfStrikes() + " strikes");
			

	}
}
