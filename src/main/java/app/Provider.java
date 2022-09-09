package app;

import java.awt.AWTException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import data.DatabaseHandler;
import gui.App;
import app.Listener;

public class Provider {
	private App gui;
	private Writer writer;
	private Listener listener;
	private Handler handler;
	private DatabaseHandler db;
	
	public Provider(App gui) {
		this.gui = gui;
		
		try {
			this.writer = new Writer();
		} catch (AWTException e) {e.printStackTrace();}
		
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
    	Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    	logger.setLevel(Level.OFF);
    	logger.setUseParentHandlers(true);
    	this.listener = new Listener(this);
		GlobalScreen.addNativeKeyListener(this.listener);
		
		//We limit the access to listener since Handler will handle interactions with it
		this.handler = new Handler(this, this.listener);  
		
		this.db = new DatabaseHandler();
	}
	
	public App getGUI() {
		return this.gui;
	}
	
	public Writer getWriter() {
		return this.writer;
	}
	
	//We limit the access to listener since Handler will handle interactions with it
	private Listener getListener() {
		return this.listener;
	}
	
	public Handler getHandler() {
		return this.handler;
	}
	
	public DatabaseHandler getDatabase() {
		return this.db;
	}
	
	public void handleDeath() {
		this.handler.killThreads();
	}
}
