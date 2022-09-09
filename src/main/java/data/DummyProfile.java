package data;

import java.io.Serializable;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "dummyprofiles", schemaVersion= "1.0")
public class DummyProfile implements Serializable{

	private static final long serialVersionUID = -5662590921374879332L;
	
	@Id
	private String name;
	
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
}
