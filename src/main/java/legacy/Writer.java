package legacy;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;

import app.Main;

public class Writer {
	Robot robot;
	int delay = 50;
	
	public Writer() throws AWTException {
		robot = new Robot();
	}
	
	public synchronized void writeLine(String line) {
		String save = this.getClipboard();
		
		this.setClipboard(line);
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save);
	}
	
	public synchronized void writeLineInclude(String line, int indexForInsert) {
		String save = this.getClipboard();
		
		this.clearClipboard();
		this.copy();
		String insert = this.getClipboard();
		
		String lineWithInsert = 
			"" + 
			line.substring(0, indexForInsert) + 
			((insert != null) ? insert : "") + 
			line.substring(indexForInsert, line.length());
		
		this.setClipboard(lineWithInsert);
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save);
	}
	
	public synchronized void writeRows(List<String> rows) {
		String save = this.getClipboard();
		
		String lineFormat = "";
		for(String row : rows) {
			String formattedRow = "";
			formattedRow = formattedRow + row;
			
			if(row.contains("|TAB|")) {
				formattedRow = formattedRow.replace("|TAB|", "\t");
			}
			
			if(rows.indexOf(row) < rows.size()-1) {
				formattedRow = formattedRow + "\n"; 
			}
			
			lineFormat = lineFormat + formattedRow;
		}
		
		this.setClipboard(lineFormat);
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save);
	}
	
	public synchronized void writeRowsInclude(List<String> rows, int indexForRow) {
		String save = this.getClipboard();
		
		this.clearClipboard();
		this.copy();
		String insert = this.getClipboard();
		
		String lineFormat = "";
		for(String row : rows) {
			
			String formattedRow = "";
			
			formattedRow = formattedRow + row;
			
			if(rows.indexOf(row) == indexForRow) {
				formattedRow = formattedRow + ((insert != null) ? insert : "");
			}
			
			if(row.contains("|TAB|")) {
				formattedRow = formattedRow.replace("|TAB|", "\t");
				formattedRow = formattedRow.replace("\n","\n\t");
				formattedRow = formattedRow.replace("\r","\r\t");
			}
			
			if(rows.indexOf(row) < rows.size()-1) {
				formattedRow = formattedRow + "\n"; 
			}
			
			lineFormat = lineFormat + formattedRow;
		}
		
		this.setClipboard(lineFormat);
		this.paste();
		
		robot.delay(150);
		this.setClipboard(save);
	}
	
	public void setClipboard(String s) {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection(s);
			clipboard.setContents(selection, selection);
		}
		catch(Exception e) {
			Main.debug("Error in setting clipboard");
		}
	}
	
	public String getClipboard() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			String found = (String) clipboard.getData(DataFlavor.stringFlavor); 
			Main.debug("CLIPPED: " + found);
			return found;
		}
		catch(Exception e) {
			Main.log("Error in getting clipboard");
			return null;
		}
	}
	
	public void clearClipboard() {
		try {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection selection = new StringSelection("");
			clipboard.setContents(selection, selection);
		}
		catch(Exception e) {
			Main.log("Error in clearing clipboard");
		}
	}
	
	public void copy() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(1);
		robot.keyPress(KeyEvent.VK_C);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_C);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
	
	public void paste() {
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.delay(1);
		robot.keyPress(KeyEvent.VK_V);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_V);
		robot.delay(1);
		robot.keyRelease(KeyEvent.VK_CONTROL);
	}
}
