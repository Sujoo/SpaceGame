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
		displayLocalSystemMap();
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
				displayLocalSystemMap();
				break;
			case FULL_MAP:
				displayGlobalSystemMap();
				break;
			case DOCK:
				displayStationInformation();
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
		displayLocalSystemMap();
	}
	
	private void displayLocalSystemMap() {
		displaySystemMap(starSystemUtil.createSubGraph(currentStar));
	}
	
	private void displayGlobalSystemMap() {
		displaySystemMap(starSystemUtil.getStarGraph());
	}
	
	private void displaySystemMap(UndirectedSparseGraph<Star, String> graph) {
		gui.loadSystemMap(graph, currentStar);		
	}
	
	private void displayStarSystemInformation() {
		gui.setText(TextUtil.getCurrentStarSystemString(currentStar, starSystemUtil.getNeighborsString(currentStar)));
	}
	
	private void displayStationInformation() {
		gui.setText(TextUtil.getStationString(currentStar.getStation()));
	}
	
	private void printHelp() {
		gui.setText(TextUtil.getHelpString());
	}
}
