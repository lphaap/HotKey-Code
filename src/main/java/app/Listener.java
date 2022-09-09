package app;

import java.util.ArrayList;
import java.util.List;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import app.Main;
import app.Listener.KeyboardInput;
import javafx.application.Platform;

public class Listener implements NativeKeyListener {
	private boolean pause;
	private boolean kill;
	private boolean modifierOverride;
	private Provider provider;
	private List<Integer> activeModifiers;
	private List<String> pressedChecksums = new ArrayList<String>();
	
	public Listener(Provider provider) {
		this.provider = provider;
		this.activeModifiers = new ArrayList<Integer>();
	}

	
	public void nativeKeyPressed(NativeKeyEvent e) {
		Main.debug("PRESS: Code: "+e.getKeyCode()+" Modifiers: "+e.getModifiers()+" ParsedLetter: " + NativeKeyEvent.getModifiersText(e.getModifiers()) +" "+NativeKeyEvent.getKeyText(e.getKeyCode()));
	}
	
	public static String parseKey(int key) {
		return NativeKeyEvent.getKeyText(key);
	}
	
	public static String parseModifier(int modifier) {
		return NativeKeyEvent.getModifiersText(modifier);
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		Main.debug("RELEASE: Code: "+e.getKeyCode()+" Modifiers: "+e.getModifiers()+" ParsedLetter: " + NativeKeyEvent.getModifiersText(e.getModifiers()) +" "+NativeKeyEvent.getKeyText(e.getKeyCode()));
		
		if(pause) {
			return;
		}
		
		if(this.modifierOverride || this.activeModifiers.contains(e.getModifiers())) {
			this.provider.getHandler().handleKeyboardInput(
				new KeyboardInput(
						e.getModifiers(), 
						e.getKeyCode(), 
						Listener.parseModifier(e.getModifiers()), 
						Listener.parseKey(e.getKeyCode())
				)
			);
		}
	}
	
	public void killThreads() {
		this.kill = true;
	}
	
	private class InputCleaner implements Runnable {
		
		@Override
		public void run() {
			while(!kill) {
				
				try {Thread.sleep(10);} 
				catch (InterruptedException e) {e.printStackTrace();}
				
				if(!inputQueue.isEmpty()) {
					KeyboardInput toProcess = inputQueue.get(0);
					if(!guiInject) {
						/*Macro macro = activeProfile.getMacro(toProcess.getKey(), toProcess.getModifier());
						if(macro != null) {
							provider.getWriter().write(macro.getMacro(), null);
						}*/
					}
					else {
						Main.debug("Trying to process:" + toProcess.getKey());
						Platform.runLater(() -> {
							provider.getGUI().getMainController().setKeyField(
										toProcess.getParsedModifier()+" + "+toProcess.getParsedKey()
									);
							}
						);
					}
					inputQueue.remove(0);
				}
			}
		}
		
	}
	
	public class KeyboardInput {
		private int modifier;
		private int key;
		private String parsedModifier;
		private String parsedKey;
		
		public KeyboardInput(int modifier, int key, String parsedModifier,String parsedKey) { 
			this.modifier = modifier;
			this.key = key;
			this.parsedKey = parsedKey;
			this.parsedModifier = parsedModifier;
		}
	
		public int getKey() { return this.key; }
		public int getModifier() { return this.modifier; }
		public String getParsedKey() { return this.parsedKey; }
		public String getParsedModifier() { return this.parsedModifier; }
	}

	//Does not work :(
	public void nativeKeyTyped(NativeKeyEvent e) {}
	
	public void setPause() {
		this.pause = !this.pause;
		Main.log("Paused: " + this.pause);
	}
	
	public boolean isPaused() {
		return this.pause;
	}
	
	public void setActiveModifiers(List<Integer> modifiers) {
		this.activeModifiers = modifiers;
	}
	
	public List<Integer> getActiveModifiers() {
		return this.activeModifiers;
	}
	
	public void setModifierOverride(boolean value) {
		this.modifierOverride = value;
	}
	
	public boolean isModifierOverriden() {
		return this.modifierOverride;
	}
	
	
}
