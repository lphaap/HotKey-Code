package app;

import java.util.ArrayList;
import java.util.List;

import app.Listener.KeyboardInput;
import data.Macro;
import data.Profile;
import gui.App;
import javafx.application.Platform;

public class Handler {
	private Provider provider;
	private Profile activeProfile;
	private boolean kill;
	private boolean guiInject;
	
	private List<KeyboardInput> inputQueue = new ArrayList<KeyboardInput>();
	private Listener listener;
	
	public Handler(Provider provider, Listener listener) {
		this.provider = provider;
		this.listener = listener;
		new Thread(new InputProcessor()).start();
	}
	
	public void handleKeyboardInput( KeyboardInput input) {
		this.inputQueue.add(input);
	}
	
	public void loadProfile(String name) {
		Profile fetched = this.provider.getDatabase().fetchProfile(name);
		if(fetched != null) {
			this.activeProfile = fetched;
		}
	}
	
	public void modifyToggle() {
		if(!this.listener.isModifierOverriden()) {
			this.listener.setModifierOverride(true);
			this.guiInject = true;
		}
		else {
			this.listener.setModifierOverride(false);
			this.guiInject = false;
		}
		
	}
	
	public void killThreads() {
		this.kill = true;
		this.listener.killThreads();
	}
	
	private class InputProcessor implements Runnable {
		
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
	
}
