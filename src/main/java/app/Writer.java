package app;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Writer {
	private Robot robot;
	private int delay = 50;
	private Clipboard clipboard;
	
	public Writer() throws AWTException {
		robot = new Robot();
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	/**
	 * This method is a wrapper for forcing seperate methods under the same synchronized block
	 * * Output pre formatted text by setting @var String text
	 * * Output raw strings with a list of lines with @var List<String> Lines
	 * * If both are set prioritizes formatted text and returns
	 * 
	 * Special chars for outputs:
	 * \n - new line
	 * \i - insert highlighted text here
	 * 
	 */
	public synchronized void write(String text, List<String> lines) {
		if(text != null) {
			this.output(text);
			return;
		}
		
		if(lines != null) {
			this.output(lines);
			return;
		}
		
		return;
	}
	
	private synchronized void output(String text) {
		String save = this.getClipboard(false); //Save clipboard
		
		if(text.contains("\\i")) { //Add insert if exists
			this.copy();
			String insert = this.getClipboard(false);
			text.replace("\\i", insert);
		}
		
		this.setClipboard(text,false); //Output text
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save,false); //Load saved text back to clipboard
	}
	
	private synchronized void output(List<String> lines) {
		String save = this.getClipboard(false); //Save clipboard
		String text = "";
		
		for(String line : lines) {
			if(line.contains("\\i")) { //Add insert if exists
				this.copy();
				String insert = this.getClipboard(false);
				line.replace("\\i", insert);
			}
			text = text + line + "\n"; //Add lines together to create a formatted string
		}
		
		this.setClipboard(text,false); //Output text
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save,false); //Load saved text back to clipboard
	}
	
	private void refreshClipboard() {
		this.clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}
	
	private void setClipboard(String s, boolean refresh) {
		try {
			StringSelection selection = new StringSelection(s);
			this.clipboard.setContents(selection, selection);
			return;
		}
		catch(Exception e) {
			this.refreshClipboard();
			if(refresh) { //Avoid infinite loops
				Main.debug("Error in setting clipboard");
				return;
			}
			this.setClipboard(s, true);
		}
	}
	
	private String getClipboard(boolean refresh) {
		try {
			String found = (String) this.clipboard.getData(DataFlavor.stringFlavor); 
			Main.debug("CLIPPED: " + found);
			return found;
		}
		catch(Exception e) {
			this.refreshClipboard();
			if(refresh) { //Avoid infinite loops
				Main.log("Error in getting clipboard");
			}
			return this.getClipboard(true);
		}
	}
	
	private void clearClipboard(boolean refresh) {
		try {
			StringSelection selection = new StringSelection("");
			this.clipboard.setContents(selection, selection);
			return;
		}
		catch(Exception e) {
			this.refreshClipboard();
			if(refresh) { //Avoid infinite loops
				Main.log("Error in clearing clipboard");
				return;
			}
			this.clearClipboard(true);
		}
	}
	
	private void copy() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(1);
		robot.keyPress(KeyEvent.VK_C);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_C);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
	private void paste() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(1);
		robot.keyPress(KeyEvent.VK_V);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_V);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
}
