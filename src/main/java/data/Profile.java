package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import app.Provider;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "profiles", schemaVersion= "1.0")
public class Profile implements Serializable{

	private static final long serialVersionUID = 6864874777701344024L;

	@Id
	private String name;
	
	private Map<Integer, Map<Integer, Macro>> modifierToMap = new HashMap<Integer, Map<Integer,Macro>>();
	
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	public Map<Integer, Map<Integer, Macro>> getmodifierToMapMap() { return this.modifierToMap; }
	public void modifierToMapMap(Map<Integer, Map<Integer, Macro>> modifierToMapMap) { 
		this.modifierToMap = modifierToMapMap;
	}
	
	public Macro getMacro(int key, int modifier) {
		Map<Integer,Macro> keyToMacro = this.modifierToMap.get(modifier);
		if(keyToMacro != null) {
			return keyToMacro.get(key);
		}
		return null;
	}
	
	public void insertMacro(Macro macro) {	
		Map<Integer, Macro> keyToMacro = this.modifierToMap.get(macro.getModifier());
		if(keyToMacro != null) {
			keyToMacro.put(macro.getKey(), macro);
		}
		else {
			Map<Integer, Macro> mapToAdd = new HashMap<Integer, Macro>();
			mapToAdd.put(macro.getKey(), macro);
			this.modifierToMap.put(macro.getModifier(), mapToAdd);
		}
	}
	
	public List<Macro> getAllMacros() {
		List<Macro> re = new ArrayList<Macro>();
		for(Map<Integer,Macro> keyToMacro : this.modifierToMap.values()) {
			re = Stream.concat(re.stream(), keyToMacro.values().stream()).collect(Collectors.toList());
		}
		return re;
	}
}
