package sujoo.games.spacegame.gui;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class ScrollLog extends JScrollPane {
    private static final long serialVersionUID = 7744006204661250432L;
    private STextArea infoTextArea;
    private JTextPane infoPane;

    public ScrollLog(Dimension dimension) {
        super();
        setPreferredSize(dimension);
        infoTextArea = new STextArea();
        infoPane = getStandardTextPane(infoTextArea);
        setViewportView(infoPane);
    }

    private JTextPane getStandardTextPane(STextArea textArea) {
        JTextPane textPane = new JTextPane(textArea);
        textPane.setEditable(false);
        textPane.setBackground(Colors.defaultBackgroundColor);
        return textPane;
    }

    public void addText(String text) {
        infoTextArea.preAppendLine(text);
        infoPane.setCaretPosition(0);
    }
}
