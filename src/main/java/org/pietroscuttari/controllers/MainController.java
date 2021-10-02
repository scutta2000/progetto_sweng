package org.pietroscuttari.controllers;


import org.pietroscuttari.data.Rack;
import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Lock;
import org.pietroscuttari.managers.AccountManager;
import org.pietroscuttari.managers.BikeManager;
import org.pietroscuttari.managers.RackManager;
import org.pietroscuttari.managers.StatisticsManager;
import org.pietroscuttari.utils.AlertUtils;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;



public class MainController extends Controller{
	@FXML
	public Text stats;
	@FXML
	public ChoiceBox<Rack> locations;
	@FXML
	public ChoiceBox<Lock> locks;
	@FXML
	public TextField code;
	@FXML
	public TextField password;
	@FXML
	public TextField bikeId;
	@FXML
	public CheckBox broken;

	@Override
	public void onNavigateFrom(Controller sender, Object ... parameter) {
		String s = 
		"Mean bikes used per day: " + StatisticsManager.meanBikesUsed() +
		"\n"+
		"Most used rack: " + StatisticsManager.mostUsedRack();
		stats.setText(s);

		setRacks();
		setLocks();
	}

	private void setRacks(){
		var racks = RackManager.getRacks();
				locations.getItems().addAll(racks);

		locations.getSelectionModel().selectFirst();
	}

	private void setLocks(){
		var allLocks = RackManager.getLocks(locations.getValue());
		locks.getItems().addAll(allLocks);
	}


	@FXML
	public void register(){
		navigate("RegisterView", locations.getValue());
		return;
	}

	@FXML
	public void login(){
		try{
			Account a = AccountManager.login(Integer.parseInt(code.getText()), password.getText());
			if (a==null){
				AlertUtils.showError("Invalid login info");
			}else{
				if (a.getType()==AccountType.admin){
					navigate("AdminView", locations.getValue(), a);
				}else{
					navigate("LoggedInView", locations.getValue(), a);
				}
			}
		} catch (NumberFormatException nfe){
			AlertUtils.showError("Invalid login info");			
		}
		return;
	}

	@FXML
	public void returnBike(){
		if (RackManager.returnBike(locks.getValue(), Integer.parseInt(bikeId.getText()))){
			if (broken.isSelected()){
				BikeManager.reportBroken(Integer.parseInt(bikeId.getText()));
			}
			AlertUtils.showInfo("Bike " + bikeId.getText() + " returned");
		}else{
			AlertUtils.showError("Could not return bike");
		}

		return;
	}
}
