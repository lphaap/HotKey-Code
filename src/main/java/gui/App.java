package gui;

import java.awt.EventQueue;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.Main;
import app.Provider;
import data.Macro;
import data.Profile;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import legacy.Listener;
import javafx.fxml.FXMLLoader;

public class App extends Application {
	private Controller mainController;
	private Provider provider;
    
    @Override
    public void init() {
       Main.debug("Init called.");
    }

    @Override
    public void stop() {
    	Main.debug("Stop called.");
        this.provider.handleDeath();
    }

    @Override
    public void start(Stage stage) throws Exception {
    	Main.debug("Start called.");
        
        this.provider = new Provider(this);
        /*
         // Load the FXML
			URL location = getClass().getResource("Example.fxml");
			FXMLLoader fxmlLoader = new FXMLLoader(location);
			Parent root = fxmlLoader.load();
			 
			Scene scene = new Scene(root, 400, 400);
			stage.setTitle("Example");
			stage.setScene(scene);
			stage.show();
         */
        
        //ResourceLoader<Parent, FXMLController> loader = new ResourceLoader<>("scene.fxml");
        //loader.controller.setLabel("Welcome!");
        // configure the menu to create windows with createWindow
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        this.mainController = (Controller)loader.getController();
        this.mainController.setProvider(this.provider);
        this.mainController.init();
        
        //scene.getStylesheets().add(createStyle());
      
        
        stage.setTitle("JavaFX template");
        stage.setScene(scene);
        stage.show();
    }
    
    public Controller getMainController() {
    	return this.mainController;
    }
}
