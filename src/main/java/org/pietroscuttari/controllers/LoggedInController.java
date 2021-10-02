package org.pietroscuttari.controllers;

import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.BikeType;
import org.pietroscuttari.data.Rack;
import org.pietroscuttari.data.Transaction;
import org.pietroscuttari.data.Trip;
import org.pietroscuttari.managers.AccountManager;
import org.pietroscuttari.managers.RackManager;
import org.pietroscuttari.utils.AlertUtils;
import org.pietroscuttari.utils.DateUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class LoggedInController extends Controller{
	private Account account;
	private Rack rack;

	@FXML
	public ChoiceBox<BikeType> bikeType;
	@FXML
	public Text bikeReturned;

	@FXML
	public void unlock(){
		if (account.isExpired()){
			AlertUtils.showError("Your account is expired");
		}

		Transaction t = RackManager.unlockBike(rack.getId(), account, bikeType.getValue());
		
		if (t==null){
			AlertUtils.showError("No bikes available for selected type");
		}else{
			AlertUtils.showInfo("Unlocked Bike " + t.getTrip().getBike().getId()  +" at lock "+ t.getLock());
			navigate("MainView");
		}

	}
	@FXML
	public void exit(){
		navigate("MainView");
	}

	@Override
	public void onNavigateFrom(Controller sender, Object ...parameter) {
		this.rack = (Rack) parameter[0];
		this.account = (Account) parameter[1];

		Trip t = AccountManager.getLastTrip(account);

		// If a bike has been returned in the last 2 min show confirmation, otherwise hide it
		if (t != null && (t.getEnd() == null || DateUtils.sub(t.getEnd(), DateUtils.now()).getSeconds() >= 2 * 60)) {
			bikeReturned.setVisible(false);
		}

		bikeType.getItems().addAll(BikeType.values());
	}

	
}
