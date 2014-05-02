package sujoo.games.spacegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import sujoo.games.spacegame.datatypes.Command;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.gui.MainGui;

public class Controller {
	private final int totalStarSystems = 10;
	private final int maximumConnections = 4;
	private final int minStarId = 1000;
	
	private StarSystemUtil starSystemUtil;
	private MainGui gui;
	
	private Star currentStar;
	
	public Controller() {
		starSystemUtil = new StarSystemUtil(minStarId, totalStarSystems, maximumConnections);
		gui = new MainGui(this);
		gui.setVisible(true);
	}
	
	public void play() {
		currentStar = starSystemUtil.getRandomStarSystem();
		print();
	}
	
	private void print() {		
		printSubGraph();
		printCurrentStarSystem();
	}
	
	private void printGraph() {
		FRLayout<Star, String> layout = new FRLayout<Star, String>(starSystemUtil.getStarGraph());
		layout.setMaxIterations(10000);
		layout.setRepulsionMultiplier(1.5);
		VisualizationViewer<Star, String> vs = 
				new VisualizationViewer<Star, String>(layout, new Dimension(300,300));
		vs.setPreferredSize(new Dimension(400,400));
		
		Transformer<Star, Paint> vertexPaint = new Transformer<Star, Paint>() {
			public Paint transform(Star star) {
				if (star.equals(currentStar)) {
					return Color.WHITE;
				} else {
					return Color.LIGHT_GRAY;
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
		
		gui.setGraph(vs);
	}
	
	private void printSubGraph() {
		FRLayout<Star, String> layout = new FRLayout<Star, String>(starSystemUtil.createSubGraph(currentStar));
		layout.setMaxIterations(10000);
		layout.setRepulsionMultiplier(1.5);
		VisualizationViewer<Star, String> vs = 
				new VisualizationViewer<Star, String>(layout, new Dimension(300,300));
		vs.setPreferredSize(new Dimension(400,400));
		
		Transformer<Star, Paint> vertexPaint = new Transformer<Star, Paint>() {
			public Paint transform(Star star) {
				if (star.equals(currentStar)) {
					return Color.WHITE;
				} else {
					return Color.LIGHT_GRAY;
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
		
		gui.setGraph(vs);
	}
	
	private void printCurrentStarSystem() {
		gui.addText("Star: " + currentStar.getId());
		gui.addText("Connections: " + starSystemUtil.getNeighborsString(currentStar));
	}
	
	public void enterCommand(String command) {
		if (!command.isEmpty()) {
			String[] commandString = command.split(" ");
			switch (Command.toCommand(commandString[0])) {
			case JUMP:
				if (commandString.length > 1 && StringUtils.isNumeric(commandString[1])) {
					travel(Integer.parseInt(commandString[1]));
				}
				break;
			case SCAN:
				scanCurrentSystem();
				break;
			case FULL_SCAN:
				printGraph();
				break;
			case UNKNOWN:
				break;
			}
		}
	}
	
	private void travel(int travelToStarId) {
		Star jumpToStar = starSystemUtil.getStarSystem(travelToStarId);
		if (starSystemUtil.isNeighbor(currentStar, jumpToStar)) {
			currentStar = jumpToStar;
		}
		gui.clearTextArea();
		printCurrentStarSystem();
	}
	
	private void scanCurrentSystem() {
		printSubGraph();
	}
}
