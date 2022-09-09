package legacy;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import gui.App;
import app.Main;
import app.Provider;
import gui.Controller;
import legacy.Handler.Hotkey;

public class Listener implements NativeKeyListener {
	private Logger logger;
	private Handler currentHandler;
	private List<Handler> handlers = new ArrayList<Handler>();
	private ParsedKeyQueue keyQueue;
	private boolean pause;
	private Provider provider;
	
	public Listener(Provider provider) {
		this.keyQueue = new ParsedKeyQueue();
		new Thread(this.keyQueue).start();
		initHandlers();
		this.provider = provider;
	}
	
	private void initHandlers() {
		try {
			handlers.add(new PHPHandler());
		} catch (AWTException e) {
			Main.debug("Couldnt create Handlers");
			System.exit(0);
		}
		this.currentHandler = handlers.get(0);
	}
	
	public void nativeKeyPressed(NativeKeyEvent e) {
		Main.debug("PRESS: Code: "+e.getKeyCode()+" Modifiers: "+e.getModifiers()+" ParsedLetter: " + NativeKeyEvent.getModifiersText(e.getModifiers()) +" "+NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE && e.getModifiers() == 1) { //R-Shift + Esc
    		this.handleDeath();
    		return;
        }
		if(e.getKeyCode() == NativeKeyEvent.VC_ESCAPE && e.getModifiers() == 16) { //L-Shift + Esc
			this.pause();
			return;
		}
		if(this.pause) {
			return;
		}
		if(e.getModifiers() == 130) {
			Handler.Hotkey handleKey = null;
			switch(e.getKeyCode()) {
				case 16: //alt gr Q
					Main.debug("Q");
					handleKey = Handler.Hotkey.ALT_GR_Q;
					break;
				case 17: //alt gr W
					Main.debug("W");
					handleKey = Handler.Hotkey.ALT_GR_W;
					break;
				case 18: //alt gr E
					Main.debug("E");
					handleKey = Handler.Hotkey.ALT_GR_E;
					break;
				case 19: //alt gr R
					Main.debug("R");
					handleKey = Handler.Hotkey.ALT_GR_R;
					break;
				case 20: //alt gr T
					Main.debug("T");
					handleKey = Handler.Hotkey.ALT_GR_T;
					break;
				case 30: //alt gr A
					Main.debug("A");
					handleKey = Handler.Hotkey.ALT_GR_A;
					break;
				case 31: //alt gr S
					Main.debug("S");
					handleKey = Handler.Hotkey.ALT_GR_S;
					break;
				case 32: //alt gr D
					Main.debug("D");
					handleKey = Handler.Hotkey.ALT_GR_D;
					break;
				case 33: //alt gr F
					Main.debug("F");
					handleKey = Handler.Hotkey.ALT_GR_F;
					break;
				case 34: //alt gr G
					Main.debug("G");
					handleKey = Handler.Hotkey.ALT_GR_G;
					break;
				case 44: //alt gr Z
					Main.debug("Z");
					handleKey = Handler.Hotkey.ALT_GR_Z;
					break;
				case 45: //alt gr X
					Main.debug("X");
					handleKey = Handler.Hotkey.ALT_GR_X;
					break;
				case 46: //alt gr C
					Main.debug("C");
					handleKey = Handler.Hotkey.ALT_GR_C;
					break;
				case 47: //alt gr V
					Main.debug("V");
					handleKey = Handler.Hotkey.ALT_GR_V;
					break;
				default:
					//Main.debug("PRESS ALT GR: Code: "+e.getKeyCode()+" Modifiers: "+e.getModifiers());
			}
			if(handleKey != null) {
				Main.debug("ADDING: " + handleKey);
				this.keyQueue.addToQueue(handleKey);
			}
		}
	}
	
	public void pause() {
		this.pause = !this.pause;
		Main.log("Paused: " + this.pause);
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		Main.debug("RELEASE: Code: "+e.getKeyCode()+" Modifiers: "+e.getModifiers()+" ParsedLetter: " + NativeKeyEvent.getModifiersText(e.getModifiers()) +" "+NativeKeyEvent.getKeyText(e.getKeyCode()));
		if(e.getModifiers() == 128 && e.getKeyCode() == 29) {
			this.keyQueue.sendToHandler();
		}
		String s = "ParsedLetter: " + 
				NativeKeyEvent.getModifiersText(e.getModifiers()) +" "+
				NativeKeyEvent.getKeyText(e.getKeyCode());
		//new Thread((txt) -> this.controller.setOutputField())->start();
		new Thread(() -> this.provider.getGUI().getMainController().setOutputField(s)).start();
	}

	//Does not work :(
	public void nativeKeyTyped(NativeKeyEvent e) {
	}
	
	private class ParsedKeyQueue implements Runnable{
		ArrayList<Handler.Hotkey> queue = new ArrayList<Handler.Hotkey>();
		int indexToRemove = 0;
		boolean kill = false;
		
		public void run() {
			while(!kill) {
				this.indexToRemove = this.queue.size()-1;
				//Main.debug("QUEUE: "+this.queue.size());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				
				if(
					!this.queue.isEmpty() && 
					!(this.indexToRemove+1 > this.queue.size())
				) {
					//Main.debug("QUEUE: removing "+this.queue.get(0));
					this.popFirst();
				}
				
			}
		}
		
		public synchronized void sendToHandler() {
			if(!queue.isEmpty()) {
				currentHandler.handleInput(queue.get(0));
				this.popFirst();
			}
		}
		
		public synchronized void addToQueue(Handler.Hotkey key) {
			this.queue.add(key);
		}
		
		public synchronized void popFirst() {
			if(!queue.isEmpty()) {
				queue.remove(0);
			}
		}
		
		public void kill() {
			this.kill = true;
		}
		
	}
	
	private void handleDeath() {
		Main.log("HOTKEY CODE EXIT");
		try {
    		GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException nativeHookException) {
    		nativeHookException.printStackTrace();
		}
		this.keyQueue.kill();
	}

}
