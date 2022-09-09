package legacy;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.List;

import legacy.Writer;
import app.Main;
public class PHPHandler implements Handler{
	Writer writer;
	
	public PHPHandler() throws AWTException {
		this.writer = new Writer();
		String clip = this.writer.getClipboard();
		this.writer.setClipboard("INIT");
		try {	
			Thread.sleep(150);
		} catch (InterruptedException e) {}
		this.writer.setClipboard(clip);
	}
	
	public void handleInput(Hotkey key) {
		Main.debug("Found: "+key);
		switch(key) {
			case ALT_GR_Q:
				Main.log("Writing: ->");
				this.writer.writeLine("->");
				break;
			case ALT_GR_W:
				Main.log("Writing: ??");
				this.writer.writeLine("??");
				break;
			case ALT_GR_E:
				Main.log("Writing: ( )");
				this.writer.writeLineInclude("()",1);
				break;
			case ALT_GR_R:
				Main.log("Writing: { }");
				this.writer.writeLineInclude("{}",1);
				break;
			case ALT_GR_T:
				Main.log("Writing: [ ]");
				this.writer.writeLineInclude("[]",1);
				break;
			case ALT_GR_A:
				Main.log("Writing: If-clause");
				this.writeIf();
				break;
			case ALT_GR_S:
				Main.log("Writing: Else-clause");
				this.writeElse();
				break;
			case ALT_GR_D:
				Main.log("Writing: Switch-case");
				this.writeSwitch();
				break;
			case ALT_GR_F:
				Main.log("Writing: Foreach-loop");
				this.writeForeach();
				break;
			case ALT_GR_G:
				Main.log("Writing: For-loop");
				this.writeFor();
				break;
			case ALT_GR_Z:
				Main.log("Writing: While-loop");
				this.writeWhile();
				break;
			case ALT_GR_X:
				Main.log("Writing: !==");
				this.writer.writeLine("!==");
				break;
			case ALT_GR_C:
				Main.log("Writing: ===");
				this.writer.writeLine("===");
				break;
			case ALT_GR_V:
				Main.log("Writing: =>");
				this.writer.writeLine("=>");
				break;
			default:
				break;
		}
		
	}
	
	private void writeForeach() {
		List<String> send = new ArrayList<String>();
		send.add("foreach($ as $) {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	private void writeFor() {
		List<String> send = new ArrayList<String>();
		send.add("for ($i = 1; $i <= ; $i++) {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	private void writeIf() {
		List<String> send = new ArrayList<String>();
		send.add("if($ ) {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	private void writeWhile() {
		List<String> send = new ArrayList<String>();
		send.add("while($ ) {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	
	private void writeElseIf() {
		List<String> send = new ArrayList<String>();
		send.add("elseif($ ) {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	private void writeElse() {
		List<String> send = new ArrayList<String>();
		send.add("else {");
		send.add("|TAB|");
		send.add("}");
		this.writer.writeRowsInclude(send,1);
	}
	
	private void writeSwitch() {
		List<String> send = new ArrayList<String>();
		send.add("switch($ ) {");
		send.add("|TAB|case 1:");
		send.add("|TAB||TAB|");
		send.add("|TAB||TAB|break;");
		send.add("|TAB|case 2:");
		send.add("|TAB||TAB|");
		send.add("|TAB||TAB|break;");
		send.add("|TAB|case 3:");
		send.add("|TAB||TAB|");
		send.add("|TAB||TAB|break;");
		send.add("|TAB|default:");
		send.add("|TAB||TAB|");
		send.add("}");
		this.writer.writeRows(send);
	}

}
