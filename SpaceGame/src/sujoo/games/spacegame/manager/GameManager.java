package sujoo.games.spacegame.manager;

import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
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
	private final int initCredits = 1000;
	
	private StarSystemManager starSystemManager;
	private MainGui gui;
	private Player humanPlayer;
	
	private Star currentStar;
	private Star previousStar;
	
	public GameManager() {
		starSystemManager = new StarSystemManager(minStarId, totalStarSystems, maximumConnections);
		humanPlayer = new HumanPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits);
		gui = new MainGui(this);
		gui.setVisible(true);
	}
	
	public void play() {
		currentStar = starSystemManager.getRandomStarSystem();
		previousStar = null;
		displayStarSystemInformation();
		displayLocalSystemMap(currentStar, previousStar);
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
				displayLocalSystemMap(currentStar, previousStar);
				break;
			case FULL_MAP:
				displayGlobalSystemMap(currentStar, previousStar);
				break;
			case DOCK:
				displayDockInformation(currentStar, humanPlayer);
				break;
			case BUY:
				buy(commandString, humanPlayer, currentStar.getStation());
				break;
			case SELL:
				sell(commandString, humanPlayer, currentStar.getStation());
				break;
			case STATUS:
				displayStatus(humanPlayer);
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
		Star jumpToStar = starSystemManager.getStarSystem(travelToStarId);
		if (starSystemManager.isNeighbor(currentStar, jumpToStar)) {
			previousStar = currentStar;
			currentStar = jumpToStar;
		}
		displayStarSystemInformation();
		displayLocalSystemMap(currentStar, previousStar);
	}
	
	public void buy(String[] commandString, Player player, Station station) {
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
			displayDockInformation(currentStar, humanPlayer);
			}
		}
	}
	
	public void sell(String[] commandString, Player player, Station station) {
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
			displayDockInformation(currentStar, humanPlayer);
		}
	}
	
	private int getAmount(String command, int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize, int maxStock) {
		int amount = 0;		
		if (StringUtils.isNumeric(command)) {
			amount = Integer.parseInt(command);
		} else if (command.equalsIgnoreCase("max") || command.equalsIgnoreCase("all")) {
			int priceLimit = buyerCredits / cargoPrice;
			int cargoLimit = remainingCargoSpace / cargoSize;
			if (maxStock < priceLimit && maxStock < cargoLimit) {
				amount = maxStock;
			} else if (priceLimit < cargoLimit) {
				amount = priceLimit;
			} else {
				amount = cargoLimit;
			}
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
		gui.setText(TextManager.getCurrentStarSystemString(currentStar, starSystemManager.getNeighborsString(currentStar)));
	}
	
	private void displayDockInformation(Star star, Player player) {
		gui.setText(TextManager.getDockString(star.getStation(), player));
	}
	
	private void printHelp() {
		gui.setText(TextManager.getHelpString());
	}
}
