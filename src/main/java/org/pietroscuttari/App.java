package org.pietroscuttari;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.pietroscuttari.controllers.Controller;
import org.pietroscuttari.data.Database;

/**
 * JavaFX App
 */
public class App extends Application {

	private static Scene scene;

	private static int rackId = 1;

	public static boolean homeLoaded;

	public static int getrackId() {
		return rackId;
	}

	public static void setrackId(int rackId) {
		App.rackId = rackId;
	}

	@Override
	public void start(Stage stage) throws IOException {
		Database.getInstance().ensureCreated();
		Database.getInstance().createFakeData();


		scene = new Scene(loadView("MainView"), 1280, 720);
		navigate(null, "MainView");
		stage.setScene(scene);
		stage.setTitle("Bike Sharing");
		stage.setMinWidth(900);
		stage.setMinHeight(600);

		stage.show();
		homeLoaded = true;
	}


	public static Parent loadView(String view) throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("javaFX/" + view + ".fxml"));
		return loader.load();
	}

	public static Parent loadView(Controller sender, String view) throws IOException {
		return loadView(sender, view, (Object[]) null);
	}

	public static Parent loadView(Controller sender, String view, Object ... parameter) throws IOException {
		FXMLLoader loader = new FXMLLoader(App.class.getResource("javaFX/" + view + ".fxml"));
		Parent parent = loader.load();
		Controller controller = loader.getController();
		controller.onNavigateFrom(sender, parameter);
		controller.init();
		return parent;
	}

	public static void navigate(Controller sender, String view, Object ... parameter) throws IOException {
		var parent = loadView(sender, view, parameter);
		scene.setRoot(parent);
	}

	public static void main(String[] args) {
		launch();
	}

}