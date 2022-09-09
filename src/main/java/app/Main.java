package app;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import data.DatabaseHandler;
import data.DummyProfile;
import data.Macro;
import data.Profile;
import gui.App;
import io.jsondb.JsonDBTemplate;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main {
	public static void main( String[] args ) {
		/*
		initDummyProfiles(true);
		initDB(false);
		JsonDBTemplate db = new DatabaseHandler().getRawDB();
		Profile p = db.findById("test_1", Profile.class);
		*/
		
		//Init-Flow: Main -> App -> Provider & Gui + Controller, Provider -> Everything else
		App.launch(App.class, args); 
    }
	
	 public static void log(Object o) {
	    	System.out.println("");
	    	System.out.println(o);
	 }
	    
    public static void debug(Object o) {
    	if(true) {
	    	System.out.println("");
	    	System.out.println(o);
    	}
    }
    
    private static void initDB(boolean init) {
    	JsonDBTemplate db = new DatabaseHandler().getRawDB();
    	if(init) {
    		db.createCollection(data.Profile.class);
    	}
    	Macro m1 = new Macro();
    	m1.setKey(1);
    	m1.setModifier(1);
    	m1.setMacro("m11");
    	Macro m2 = new Macro();
    	m2.setKey(2);
    	m2.setModifier(1);
    	m2.setMacro("m12");
    	Macro m3 = new Macro();
    	m3.setKey(1);
    	m3.setModifier(2);
    	m3.setMacro("m21");
    	Macro m4 = new Macro();
    	m4.setKey(2);
    	m4.setModifier(2);
    	m4.setMacro("m22");
    	Macro m5 = new Macro();
    	m5.setKey(1);
    	m5.setModifier(3);
    	m5.setMacro("m31");
    	Macro m6 = new Macro();
    	m6.setKey(2);
    	m6.setModifier(3);
    	m6.setMacro("m32");
    	Profile p = new Profile();
    	p.insertMacro(m1);
    	p.insertMacro(m2);
    	p.insertMacro(m3);
    	p.insertMacro(m4);
    	p.insertMacro(m5);
    	p.insertMacro(m6);
    	p.setName("test_1");
    	
    	db.upsert(p);
    	
    }
    
    private static void initDummyProfiles(boolean init) {
    	JsonDBTemplate db = new DatabaseHandler().getRawDB();
    	if(init) {
    		db.createCollection(data.DummyProfile.class);
    	}
    	DummyProfile p = new DummyProfile();
    	p.setName("test_1");
    }
}
