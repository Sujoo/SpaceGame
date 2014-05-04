package sujoo.games.spacegame.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.manager.GameManager;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import java.awt.Color;

import javax.swing.border.EtchedBorder;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import javax.swing.JScrollPane;

public class MainGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672177212106851596L;
	private JPanel contentPane;
	private JPanel graphPanel;
	private JTextArea textArea;
	private JTextField textField;
	
	private GameManager controller;
	private JScrollPane scrollPane;

	/**
	 * Create the frame.
	 */
	public MainGui(GameManager controller) {
		setResizable(false);
		this.controller = controller;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 350, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{350};
		gbl_contentPane.rowHeights = new int[]{200,200,10};
		gbl_contentPane.columnWeights = new double[]{};
		gbl_contentPane.rowWeights = new double[]{};
		contentPane.setLayout(gbl_contentPane);
		
		graphPanel = new JPanel();
		graphPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
		graphPanel.setBackground(Color.GRAY);
		GridBagConstraints gbc_graphPanel = new GridBagConstraints();
		gbc_graphPanel.anchor = GridBagConstraints.NORTH;
		gbc_graphPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_graphPanel.insets = new Insets(0, 0, 5, 0);
		gbc_graphPanel.gridx = 0;
		gbc_graphPanel.gridy = 0;
		contentPane.add(graphPanel, gbc_graphPanel);
		graphPanel.setLayout(new BorderLayout(0, 0));
		
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setTabSize(4);
		textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		
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
		
		textField.requestFocusInWindow();
	}
	
	public void setText(String text) {
		clearTextArea();
		textArea.append(text);
		pack();
	}
	
	public void setDisplayPanel(JPanel panel) {
		graphPanel.removeAll();
		graphPanel.add(panel, BorderLayout.CENTER);
		pack();
	}
	
	public void loadSystemMap(UndirectedSparseGraph<Star, String> graph, final Star currentStar, final Star previousStar) {
		FRLayout<Star, String> layout = new FRLayout<Star, String>(graph);
		layout.setMaxIterations(10000);
		layout.setRepulsionMultiplier(1.5);
		VisualizationViewer<Star, String> vs = 
				new VisualizationViewer<Star, String>(layout, new Dimension(300,200));
		vs.setPreferredSize(new Dimension(320,250));
		
		Transformer<Star, Paint> vertexPaint = new Transformer<Star, Paint>() {
			public Paint transform(Star star) {
				if (star.equals(currentStar)) {
					return Color.WHITE;
				} else if (star.equals(previousStar)) {
					return Color.LIGHT_GRAY;
				} else {
					return Color.GREEN;
				}
			}
		};

		final Shape shape = new Rectangle(35,20);
		Transformer<Star, Shape> vertexShapeTransformer = new Transformer<Star, Shape>() {
			public Shape transform(Star s) {
				return shape;
			}
		};

		vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Star>());
		vs.getRenderContext().setVertexShapeTransformer(vertexShapeTransformer);
		vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		setDisplayPanel(vs);
	}
	
	private void enterCommand(String text) {
		textField.setText("");
		controller.enterCommand(text);
	}
	
	private void clearTextArea() {
		textArea.setText("");		
	}
}
