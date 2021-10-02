package org.pietroscuttari.controllers;

import org.pietroscuttari.data.Bike;
import org.pietroscuttari.data.BikeType;
import org.pietroscuttari.data.Lock;
import org.pietroscuttari.data.Rack;
import org.pietroscuttari.managers.RackManager;
import org.pietroscuttari.utils.AlertUtils;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class AdminController extends Controller{
	private Rack rack;
	@FXML
	public ChoiceBox<Bike> bikes;
	@FXML
	public ChoiceBox<BikeType> bikeType;
	@FXML
	public ChoiceBox<Lock> locks;
	@FXML
	public ChoiceBox<BikeType> lockType;

	@Override
	public void onNavigateFrom(Controller sender, Object... parameter) {
		this.rack = (Rack) parameter[0];

		updateChoiceBoxes();
	}
	private void updateChoiceBoxes(){
		var bs = RackManager.getBikes(rack);
		bikes.getItems().setAll(bs);
		bikeType.getItems().setAll(BikeType.values());

		locks.getItems().setAll(RackManager.getLocks(rack));
		lockType.getItems().setAll(BikeType.values());
	}
	@FXML
	public void unlockBike(){
		RackManager.takeBike(bikes.getValue().getLock().getId());
		AlertUtils.showInfo("Bike unlocked");
		updateChoiceBoxes();
	}
	@FXML
	public void addBike(){
		var maybeL = RackManager.getLocks(rack).stream().filter((x) -> x.getType() == bikeType.getValue() && x.getBike() == null ).findFirst();
		if (maybeL.isPresent()){
			var l = maybeL.get();
			RackManager.addBike(rack, l, bikeType.getValue());
			AlertUtils.showInfo("Byke added to lock " + l);
			updateChoiceBoxes();
		}else {
			AlertUtils.showError("No free locks avaiable for selected type");
		}		
	}
	@FXML
	public void removeLock(){
		if (RackManager.removeLock(locks.getValue())){
			AlertUtils.showInfo("Lock " + locks.getValue() + " removed");
			updateChoiceBoxes();
		}else{
			AlertUtils.showError("Lock " + locks.getValue() + " is not empty");
		}
		
	}
	@FXML
	public void addLock(){
		if (RackManager.addLock(rack, lockType.getValue()) != null){
			AlertUtils.showInfo("Lock added");
			updateChoiceBoxes();		
		}else{
			AlertUtils.showError("Please select a lock type");
		}
	}	
}


