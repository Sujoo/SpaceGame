package sujoo.games.spacegame.gui;

import java.awt.BorderLayout;
import java.awt.Component;
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

import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.manager.GameManager;

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

public class MainGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672177212106851596L;
	private JPanel contentPane;
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private JTextField textField;
	
	private GameManager controller;

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
		gbl_contentPane.rowHeights = new int[]{100,200,10};
		gbl_contentPane.columnWeights = new double[]{};
		gbl_contentPane.rowWeights = new double[]{};
		contentPane.setLayout(gbl_contentPane);
		
		upperPanel = new JPanel();
		upperPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
		GridBagConstraints gbc_upperPanel = new GridBagConstraints();
		gbc_upperPanel.anchor = GridBagConstraints.NORTH;
		gbc_upperPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_upperPanel.insets = new Insets(0, 0, 5, 0);
		gbc_upperPanel.gridx = 0;
		gbc_upperPanel.gridy = 0;
		contentPane.add(upperPanel, gbc_upperPanel);
		upperPanel.setLayout(new BorderLayout(0, 0));
		
		
		lowerPanel = new JPanel();
		lowerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
		GridBagConstraints gbc_lowerPanel = new GridBagConstraints();
		gbc_lowerPanel.insets = new Insets(0, 0, 5, 0);
		gbc_lowerPanel.fill = GridBagConstraints.BOTH;
		gbc_lowerPanel.gridx = 0;
		gbc_lowerPanel.gridy = 1;
		contentPane.add(lowerPanel, gbc_lowerPanel);
		lowerPanel.setLayout(new BorderLayout(0 ,0));
		
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
	}
	
	public void setLowerPanel(Component panel) {
		lowerPanel.removeAll();
		lowerPanel.add(panel, BorderLayout.CENTER);
		redraw();
	}
	
	public void setUpperPanel(Component panel) {
		upperPanel.removeAll();
		upperPanel.add(panel, BorderLayout.CENTER);
		redraw();
	}
	
	private void redraw() {
		pack();
		textField.requestFocusInWindow();
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
		
		setUpperPanel(vs);
	}
	
	private void enterCommand(String text) {
		textField.setText("");
		controller.enterCommand(text);
	}
}
