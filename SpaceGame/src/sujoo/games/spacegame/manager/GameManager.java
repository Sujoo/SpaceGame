package sujoo.games.spacegame.manager;

import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.Command;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.player.Player;
import sujoo.games.spacegame.datatypes.ship.Ship;
import sujoo.games.spacegame.datatypes.ship.ShipFactory;
import sujoo.games.spacegame.datatypes.ship.ShipType;
import sujoo.games.spacegame.gui.MainGui;

public class GameManager {
	private final int totalStarSystems = 10;
	private final int maximumConnections = 4;
	private final int minStarId = 1000;
	private final int numberOfAIPlayers = 5;
	private final int initCredits = 1000;
	private final Ship playerStartingShip = ShipFactory.buildShip(ShipType.SMALL_TRANS);
	
	private StarSystemManager starSystemManager;
	private AIPlayerManager aiPlayerManager;
	private MainGui gui;
	private Player humanPlayer;
	
	public GameManager() {
		starSystemManager = new StarSystemManager(minStarId, totalStarSystems, maximumConnections);
		aiPlayerManager = new AIPlayerManager(numberOfAIPlayers, starSystemManager);
		humanPlayer = new Player(playerStartingShip, initCredits);
		gui = new MainGui(this);
		gui.setVisible(true);
	}
	
	public void play() {
		humanPlayer.setNewCurrentStar(starSystemManager.getRandomStarSystem());
		displayStarSystemInformation();
		displayLocalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
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
				displayLocalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
				break;
			case FULL_MAP:
				displayGlobalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
				break;
			case DOCK:
				displayDockInformation(humanPlayer.getCurrentStar(), humanPlayer);
				break;
			case BUY:
				buy(commandString, humanPlayer, humanPlayer.getCurrentStar().getStation());
				break;
			case SELL:
				sell(commandString, humanPlayer, humanPlayer.getCurrentStar().getStation());
				break;
			case STATUS:
				displayStatus(humanPlayer);
				break;
			case HELP:
				printHelp();
				break;
			case SCORE:
				printScore();
				break;
			case UNKNOWN:
				break;
			}
		} else {
			printHelp();
		}
	}
	
	private void printScore() {
		gui.setText(TextManager.getScoreString(humanPlayer, aiPlayerManager.getAIPlayers()));
	}
	
	private void travel(int travelToStarId) {
		Star jumpToStar = starSystemManager.getStarSystem(travelToStarId);
		if (starSystemManager.isNeighbor(humanPlayer.getCurrentStar(), jumpToStar)) {
			humanPlayer.setNewCurrentStar(jumpToStar);
		}
		aiPlayerManager.performAIPlayerTurns();
		displayStarSystemInformation();
		displayLocalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
	}
	
	private void buy(String[] commandString, Player player, Station station) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
			int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(), station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)],
					player.getShip().getCargoHold().remainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargo()[CargoEnum.getCargoEnumIndex(cargoEnum)]);
				int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
				switch(validationCode) {
				case 0:
					TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
					break;
				}
			displayDockInformation(humanPlayer.getCurrentStar(), humanPlayer);
			}
		}
	}
	
	private void sell(String[] commandString, Player player, Station station) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
				int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(), station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)],
						station.getCargoHold().remainingCargoSpace(), cargoEnum.getSize(), player.getShip().getCargoHold().getCargo()[CargoEnum.getCargoEnumIndex(cargoEnum)]);
				int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, amountToSell);
				switch(validationCode) {
				case 0:
					TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
					break;
				}
			}
			displayDockInformation(humanPlayer.getCurrentStar(), humanPlayer);
		}
	}
	
	private int getAmount(String command, int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize, int maxStock) {
		int amount = 0;		
		if (StringUtils.isNumeric(command)) {
			amount = Integer.parseInt(command);
		} else if (command.equalsIgnoreCase("max") || command.equalsIgnoreCase("all")) {
			amount = TransactionManager.getMaximumAmount(buyerCredits, cargoPrice, remainingCargoSpace, cargoSize, maxStock);
		}
		return amount;
	}
	
	private void displayStatus(Player player) {
		gui.setText(TextManager.getPlayerStatusString(player));
	}
	
	private void displayLocalSystemMap(Star currStar, Star prevStar) {
		displaySystemMap(starSystemManager.createSubGraph(currStar), currStar, prevStar);
	}
	
	private void displayGlobalSystemMap(Star currStar, Star prevStar) {
		displaySystemMap(starSystemManager.getStarGraph(), currStar, prevStar);
	}
	
	private void displaySystemMap(UndirectedSparseGraph<Star, String> graph, Star currStar, Star prevStar) {
		gui.loadSystemMap(graph, currStar, prevStar);		
	}
	
	private void displayStarSystemInformation() {
		gui.setText(TextManager.getCurrentStarSystemString(humanPlayer.getCurrentStar(), starSystemManager.getNeighborsString(humanPlayer.getCurrentStar()), aiPlayerManager.getAIPlayersInStarSystem(humanPlayer.getCurrentStar())));
	}
	
	private void displayDockInformation(Star star, Player player) {
		gui.setText(TextManager.getDockString(star.getStation(), player));
	}
	
	private void printHelp() {
		gui.setText(TextManager.getHelpString());
	}
}
