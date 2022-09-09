package gui;

import java.util.ArrayList;
import java.util.List;

import app.Listener;
import app.Main;
import app.Provider;
import data.Macro;
import data.Profile;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	private Provider provider;
	
	@FXML
	private Button btnDelProfile;
	@FXML
	private Button btnDelMacro;
	@FXML
	private Button btnSaveProfile;
	@FXML
	private Button btnNewProfile;
	@FXML
	private Button btnStart;
	@FXML
	private Button btnListen;
	
	@FXML
	private TextField keyField;
	@FXML
	private TextArea outputField;
	
	@FXML
	private ChoiceBox<String> profileBox;
	@FXML
	private ChoiceBox<String> macroBox;
	
	@FXML
	public void handleProfileSaveButton(ActionEvent action) {
		Main.debug("Profile-Save called.");
	}
	@FXML
	public void handleProfileDelButton(ActionEvent action) {
		Main.debug("Profile-Delete called.");
	}
	@FXML
	public void handleMacroDelButton(ActionEvent action) {
		Main.debug("Macro-Delete called.");
	}
	@FXML
	public void handleNewProfileButton(ActionEvent action) {
		Main.debug("New-Profile called.");
	}
	@FXML
	public void handleListenButton(ActionEvent action) {
		Main.debug("Listen-Button called.");
		this.provider.getHandler().modifyToggle();
	}
	@FXML
	public void handleStartButton(ActionEvent action) {
		Main.debug("Start-Button called.");
	}
	
	public void handleMacroBox(String oldValue, String newValue) {
		Main.debug("Macro-box called: " + oldValue + " | " + newValue);
	}

	public void handleProfileBox(String oldValue, String newValue) {
		Main.debug("Profile-box called: " + oldValue + " | " + newValue);
	}
	
	public void setOutputField(String show) {
		this.outputField.setText(show);
	}
	
	public String getOutputField() {
		return this.outputField.getText();
	}
	
	public void setKeyField(String show) {
		this.keyField.setText(show);
	}
	
	public String getKeyField() {
		return this.keyField.getText();
	}
	
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	public void init() {
		List<Profile> profiles = this.provider.getDatabase().fetchAvailableProfiles();
		List<String> profileNames = new ArrayList<String>();
		List<String> macroNames = new ArrayList<String>();
		
		for(Profile p : profiles) {
			profileNames.add(p.getName());
		}
		
		for(Macro m : profiles.get(0).getAllMacros()) {
			String name = "" + Listener.parseModifier(m.getModifier()) + " + ";
			name = Listener.parseKey(m.getKey());
			macroNames.add(name);
		}
		
		this.setMacroBoxValue(macroNames.get(0));
		this.setMacroChoices(macroNames);
		
		this.setProfileBoxValue(profileNames.get(0));
		this.setProfileChoices(profileNames);
		
		this.macroBox.getSelectionModel()
	    .selectedItemProperty()
	    .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {this.handleMacroBox(oldValue, newValue);});
		
		this.profileBox.getSelectionModel()
	    .selectedItemProperty()
	    .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {this.handleProfileBox(oldValue, newValue);});
		
        profiles.clear();
	}
	
	public void setProfileBoxValue(String name) {
		this.profileBox.setValue(name);
	}
	
	public void setMacroBoxValue(String name) {
		this.macroBox.setValue(name);
	}
	
	public void setProfileChoices(List<String> profileNames) {
		this.profileBox.setItems(FXCollections.observableList(profileNames));
	}
	
	public void setMacroChoices(List<String> macroNames) {
		this.macroBox.setItems(FXCollections.observableList(macroNames));
	}
	
	public void importTextField() {
		String s = this.outputField.getText();
		Main.log(s);
	}
}
