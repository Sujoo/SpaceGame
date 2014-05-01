package sujoo.games.spacegame.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sujoo.games.spacegame.Controller;
import sujoo.games.spacegame.datatypes.Star;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class MainGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672177212106851596L;
	private JPanel contentPane;
	private JPanel graphPanel;
	private JTextArea textArea;
	private JTextField textField;
	
	private Controller controller;

	/**
	 * Create the frame.
	 */
	public MainGui(Controller controller) {
		this.controller = controller;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{424, 0};
		gbl_contentPane.rowHeights = new int[]{10, 222, 20, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		graphPanel = new JPanel();
		GridBagConstraints gbc_graphPanel = new GridBagConstraints();
		gbc_graphPanel.anchor = GridBagConstraints.NORTH;
		gbc_graphPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_graphPanel.insets = new Insets(0, 0, 5, 0);
		gbc_graphPanel.gridx = 0;
		gbc_graphPanel.gridy = 0;
		contentPane.add(graphPanel, gbc_graphPanel);
		graphPanel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
		textArea.setEditable(false);
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 1;
		textArea.setSize(new Dimension(100,100));
		contentPane.add(textArea, gbc_textArea);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterCommand(textField.getText());
                }
            }
        });
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		textField.requestFocusInWindow();
	}
	
	public void addText(String text) {
		textArea.append(text + System.lineSeparator());
	}
	
	public void setGraph(VisualizationViewer<Star, String> vv) {
		graphPanel.add(vv, BorderLayout.CENTER);
		pack();
	}
	
	private void enterCommand(String text) {
		textField.setText("");
		controller.enterCommand(text);
	}
	
	public void clearTextArea() {
		textArea.setText("");		
	}
}
