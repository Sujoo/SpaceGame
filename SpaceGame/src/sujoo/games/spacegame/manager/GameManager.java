package sujoo.games.spacegame.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import sujoo.games.spacegame.ai.PlayerManagerAI;
import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.Command;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.player.HumanPlayer;
import sujoo.games.spacegame.datatypes.player.Player;
import sujoo.games.spacegame.datatypes.ship.ShipFactory;
import sujoo.games.spacegame.datatypes.ship.ShipType;
import sujoo.games.spacegame.gui.MainGui;

public class GameManager {
	private final int totalStarSystems = 10;
	private final int maximumConnections = 4;
	private final int minStarId = 1000;
	private final int numberOfAIPlayers = 5;
	private final int initCredits = 1000;
	
	private final int turnsUntilStationRefreshBase = 50;
	private final int turnsUntilModifier = totalStarSystems / numberOfAIPlayers;
	private final int turnsUntilStationRefresh = turnsUntilStationRefreshBase * turnsUntilModifier;
	
	private StarSystemManager starSystemManager;
	private PlayerManagerAI playerManagerAI;
	private MainGui gui;
	private Player humanPlayer;
	
	private int turnCounter;
	
	public GameManager() {
		starSystemManager = new StarSystemManager(minStarId, totalStarSystems, maximumConnections);
		playerManagerAI = new PlayerManagerAI(numberOfAIPlayers, starSystemManager);
		humanPlayer = new HumanPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, "You");
		gui = new MainGui(this);
		gui.setVisible(true);
		turnCounter = 0;
	}
	
	public void play() {
		humanPlayer.setNewCurrentStar(starSystemManager.getRandomStarSystem());
		scanSystem(humanPlayer);
	}
	
	public void enterCommand(String command) {
		if (!command.isEmpty()) {
			String[] commandString = command.split(" ");
			switch (Command.toCommand(commandString[0])) {
			case JUMP:
				travel(commandString, humanPlayer);
				break;
			case SCAN:
				scan(commandString, humanPlayer);
				break;
			case MAP:
				displayLocalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
				break;
			case FULL_MAP:
				displayGlobalSystemMap(humanPlayer.getCurrentStar(), humanPlayer.getPreviousStar());
				break;
			case DOCK:
				dock();
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
				score();
				break;
			case WAIT:
				waitTurn();
				break;
			case UNKNOWN:
				break;
			}
		} else {
			printHelp();
		}
	}
	
	private void score() {
		List<Player> playerList = playerManagerAI.getAIPlayers();
		playerList.add(humanPlayer);
		gui.setLowerPanel(TextManager.getScoreLowerPanel(playerList));
	}
	
	private void travel(String[] commandString, Player player) {
		if (commandString.length > 1 && StringUtils.isNumeric(commandString[1])) {
			Star jumpToStar = starSystemManager.getStarSystem(Integer.parseInt(commandString[1]));
			if (jumpToStar != null && starSystemManager.isNeighbor(player.getCurrentStar(), jumpToStar)) {
				player.setNewCurrentStar(jumpToStar);
				advanceTurn();
				scanSystem(player);
			}
		}
	}
	
	private void advanceTurn() {
		playerManagerAI.performAIPlayerTurns();
		turnCounter++;
		if (turnCounter % turnsUntilStationRefresh == 0) {
			starSystemManager.refreshStationCargo();
		}
	}
	
	private void waitTurn() {
		advanceTurn();
	}
	
	private void buy(String[] commandString, Player player, Station station) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
			int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(), station.getPrice(cargoEnum),
					player.getShip().getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
				switch(validationCode) {
				case 0:
					TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
					break;
				}
			displayDockPanels(humanPlayer);
			}
		}
	}
	
	private void sell(String[] commandString, Player player, Station station) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
				int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(), station.getPrice(cargoEnum),
						station.getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), player.getShip().getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, amountToSell);
				switch(validationCode) {
				case 0:
					TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
					break;
				}
			}
			displayDockPanels(humanPlayer);
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
	
	private void dock() {
		displayDockPanels(humanPlayer);
	}
	
	private void scan(String[] stringCommand, Player player) {
		if (stringCommand.length == 1) {
			scanSystem(player);
		} else if (stringCommand.length > 1) {
			Player otherPlayer = playerManagerAI.getAIPlayer(stringCommand[1]);
			if (otherPlayer != null && player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
				scanPlayer(otherPlayer);
			}
		}
	}
	
	private void scanSystem(Player player) {
		displaySystemScanPanel(player);
		displayLocalSystemMap(player.getCurrentStar(), player.getPreviousStar());
	}
	
	private void displayStatus(Player player) {
		gui.setUpperPanel(TextManager.getStatusUpperPanel(player));
		gui.setLowerPanel(TextManager.getStatusLowerPanel(player));
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
	
	private void displaySystemScanPanel(Player player) {
		gui.setLowerPanel(TextManager.getScanLowerPanel(player.getCurrentStar(), starSystemManager.getNeighborsString(player.getCurrentStar()), playerManagerAI.getAIPlayersInStarSystem(player.getCurrentStar())));
	}
	
	private void displayDockPanels(Player player) {
		gui.setUpperPanel(TextManager.getDockUpperPanel(player.getCurrentStar().getStation()));
		gui.setLowerPanel(TextManager.getDockLowerPanel(player));
	}
	
	private void printHelp() {
		gui.setLowerPanel(TextManager.getHelpLowerPanel());
	}
	
	private void scanPlayer(Player player) {
		gui.setUpperPanel(TextManager.getScanPlayerUpperPanel(player));
		gui.setLowerPanel(TextManager.getScanPlayerLowerPanel(player));
	}
}
