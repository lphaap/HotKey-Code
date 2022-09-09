package data;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import io.jsondb.JsonDBTemplate;
import io.jsondb.crypto.Default1Cipher;
import io.jsondb.crypto.ICipher;
import io.jsondb.query.Update;

public class DatabaseHandler {
	private static String location = "G:/Databases/hotkey_test_db";
	
	private JsonDBTemplate db;
	
	public DatabaseHandler() {
		/*if(App.cmdSetup) { //If app is guven database as cmd parameter
			this.location = App.databasePath;
		}*/
		
		String baseScanPackage = "data";

		ICipher cipher = null;
		try {
			cipher = new Default1Cipher("1r8+24pibarAWgS85/Heeg==");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.db = new JsonDBTemplate(location, baseScanPackage, cipher);	
	}
	
	public Profile fetchProfile(String name) {
		return this.db.findById(name, Profile.class);
	}
	
	public List<Profile> fetchAvailableProfiles() {
		return this.db.findAll(Profile.class);
	}		
	
	public JsonDBTemplate getRawDB() {
		return this.db;
	}
	
	
}