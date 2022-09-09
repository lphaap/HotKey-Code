package data;

import java.io.Serializable;

public class Macro implements Serializable{

	private static final long serialVersionUID = 9215846140535330909L;
	
	private int key;
	private int modifier;
	private String macro;
	
	public enum Modifier {
		NO_MODIFIER(-1);
        private final int value;
        Modifier(final int newValue) { value = newValue; }
        public int getValue() { return value; }
	}
	
	public String parseMacro(String rawMacro) {
		return rawMacro;
	}
	
	public int getKey() {
		return this.key;
	}
	
	public void setKey(int key) {
		this.key= key;
	}
	
	public int getModifier() {
		return this.modifier;
	}
	
	public void setModifier(int modifier) {
		this.modifier = modifier;
	}
	
	public String getMacro() {
		return this.macro;
	}
	
	public void setMacro(String macro) {
		this.macro = macro;
	}
}
