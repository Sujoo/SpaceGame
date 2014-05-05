package sujoo.games.spacegame.gui;

import java.awt.Color;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class STextArea extends DefaultStyledDocument {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3288664979018625961L;
	
	private SimpleAttributeSet attr;
	private final Color defaultColor = Color.WHITE;

	public STextArea() {
		super();
		attr = new SimpleAttributeSet();
		setFont("Consolas");
		setColor(defaultColor);
		//setBackground(Color.BLACK);
	}
	
	public void setFont(String font) {
		attr.addAttribute(StyleConstants.FontFamily, font);
	}
	
	public void setColor(Color color) {
		attr.addAttribute(StyleConstants.Foreground, color);
	}
	
	public void append(String text) {
		try {
	    	insertString(getLength(), text, attr);
	    } catch (BadLocationException badLocationException) {
	        System.err.println("Bad insert");
	    }
	}
	
	public void appendLine(String text) {
		append(text + System.lineSeparator());
	}
	
	public void append(String text, Color color) {
		setColor(color);
		append(text);
		setColor(defaultColor);
	}
	
	public void appendLine(String text, Color color) {
		append(text + System.lineSeparator(), color);
	}
}
