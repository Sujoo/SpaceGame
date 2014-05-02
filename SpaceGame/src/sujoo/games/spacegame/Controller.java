package sujoo.games.spacegame;

import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
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
		displayStarSystemInformation();
		displaySystemMap(starSystemUtil.createSubGraph(currentStar));
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
				displayStarSystemInformation();
				break;
			case MAP:
				displaySystemMap(starSystemUtil.createSubGraph(currentStar));
				break;
			case FULL_MAP:
				displaySystemMap(starSystemUtil.getStarGraph());
				break;
			case HELP:
				printHelp();
				break;
			case UNKNOWN:
				break;
			}
		} else {
			printHelp();
		}
	}
	
	private void travel(int travelToStarId) {
		Star jumpToStar = starSystemUtil.getStarSystem(travelToStarId);
		if (starSystemUtil.isNeighbor(currentStar, jumpToStar)) {
			currentStar = jumpToStar;
		}
		displayStarSystemInformation();
	}
	
	private void displaySystemMap(UndirectedSparseGraph<Star, String> graph) {
		gui.loadSystemMap(graph, currentStar);		
	}
	
	private void displayStarSystemInformation() {
		gui.setText(TextUtil.getCurrentStarSystemString(currentStar, starSystemUtil.getNeighborsString(currentStar)));
	}
	
	private void printHelp() {
		gui.setText(TextUtil.getHelpString());
	}
}
