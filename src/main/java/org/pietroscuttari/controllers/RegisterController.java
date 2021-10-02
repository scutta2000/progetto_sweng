package org.pietroscuttari.controllers;

import org.pietroscuttari.data.Account;
import org.pietroscuttari.data.AccountType;
import org.pietroscuttari.data.Rack;
import org.pietroscuttari.managers.AccountManager;
import org.pietroscuttari.utils.AlertUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class RegisterController extends Controller{
	private Rack rack;

	@FXML
	public ChoiceBox<AccountType> accountType;
	@FXML
	public TextField password;
	@FXML
	public TextField cardCode;
	@FXML
	public TextField cardCVV;
	@FXML
	public TextField studentEmail;
	@FXML
	public DatePicker expiryDate;
	

	@FXML
	public void register(){

		Account a;		
		if(accountType.getValue() == AccountType.student){
			a = AccountManager.registerStudent(password.getText());
		}else{
			a = AccountManager.register(password.getText(), accountType.getValue(), cardCode.getText(), new Date(expiryDate.getValue().toEpochDay()), cardCVV.getText());
		}


		if (a == null){
			AlertUtils.showError("Account details not valid");
		}else{
			AlertUtils.showInfo("Your new ID is " + a.getId());
			if (accountType.getValue() == AccountType.admin){
				navigate("AdminView", rack);
			}else{
				navigate("LoggedInView", rack, a);
			}
		}
	}

	@Override
	public void onNavigateFrom(Controller sender, Object ... parameter) {
		this.rack = (Rack) parameter[0];

		var types = new ArrayList<>(Arrays.asList(AccountType.values()));
		types.remove(AccountType.admin);

		accountType.getItems().addAll(types);

		changeToStudent(false);

		accountType.getSelectionModel()
			.selectedItemProperty()
			.addListener(
				(ObservableValue<? extends AccountType> observable, AccountType oldValue, AccountType newValue) -> {
					if (newValue == AccountType.student){
						changeToStudent(true);
					}else if (newValue != AccountType.student){
						changeToStudent(false);
					}
				});
	}
	private void changeToStudent(boolean b){
		studentEmail.setVisible(b);

		cardCVV.setVisible(!b);
		cardCode.setVisible(!b);
		expiryDate.setVisible(!b);
	}
	
}
