package sujoo.games.spacegame.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

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
	
	private int turnCounter;
	private List<Player> players;
	
	public GameManager() {
		starSystemManager = new StarSystemManager(minStarId, totalStarSystems, maximumConnections);
		playerManagerAI = new PlayerManagerAI(numberOfAIPlayers, starSystemManager);
		
		Player humanPlayer = new HumanPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, "You");
		humanPlayer.setNewCurrentStar(starSystemManager.getRandomStarSystem());
		
		players = Lists.newArrayList();
		players.add(humanPlayer);
		players.addAll(playerManagerAI.getAIPlayers());
		
		gui = new MainGui(this, humanPlayer);
		gui.setVisible(true);
		turnCounter = 0;
	}
	
	//*************************
	//* Start the game, then wait for gui input
	//*************************
	public void play() {
		scanSystem(players.get(0));
	}
	
	//*************************
	//* Entry point for command input
	//*************************
	public void enterCommand(String command, Player player) {
		String[] commandString = command.split(" ");
		Command firstCommand = Command.toCommand(commandString[0]);
		if (firstCommand != null) {
			switch (firstCommand) {
			case JUMP:
				jump(commandString, player);
				break;
			case SCAN:
				scan(commandString, player);
				break;
			case STATUS:
				status(player);
				break;
			case DOCK:
				dock(player);
				break;
			case BUY:
				buy(commandString, player);
				break;
			case SELL:
				sell(commandString, player);
				break;
			case WAIT:
				waitTurn();
				break;
			case SCORE:
				score();
				break;
			case MAP:
				displayLocalSystemMap(player.getCurrentStar(), player.getPreviousStar());
				break;
			case FULL_MAP:
				displayGlobalSystemMap(player.getCurrentStar(), player.getPreviousStar());
				break;
			case HELP:
				help(commandString);
				break;
			}
		} else {
			help(commandString);
		}
	}
	
	//*************************
	//* Jump Command Logic
	//*************************
	private void jump(String[] commandString, Player player) {
		if (commandString.length > 1 && StringUtils.isNumeric(commandString[1])) {
			Star jumpToStar = starSystemManager.getStarSystem(Integer.parseInt(commandString[1]));
			if (jumpToStar != null && starSystemManager.isNeighbor(player.getCurrentStar(), jumpToStar)) {
				player.setNewCurrentStar(jumpToStar);
				advanceTurn();
				scanSystem(player);
			}
		}
	}
	
	//*************************
	//* Scan Command Logic
	//*************************
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
	
	private void displaySystemScanPanel(Player player) {
		gui.setLowerPanel(TextManager.getScanSystemLowerPanel(player.getCurrentStar(), starSystemManager.getNeighborsString(player.getCurrentStar()), playerManagerAI.getAIPlayersInStarSystem(player.getCurrentStar())));
	}
	
	private void scanPlayer(Player player) {
		gui.setUpperPanel(TextManager.getScanPlayerUpperPanel(player));
		gui.setLowerPanel(TextManager.getScanPlayerLowerPanel(player));
	}
	
	
	//*************************
	//* Status Command Logic
	//*************************
	private void status(Player player) {
		gui.setUpperPanel(TextManager.getStatusUpperPanel(player));
		gui.setLowerPanel(TextManager.getStatusLowerPanel(player));
	}
	
	//*************************
	//* Dock Command Logic
	//*************************
	private void dock(Player player) {
		displayDockPanels(player);
	}
	
	private void displayDockPanels(Player player) {
		gui.setUpperPanel(TextManager.getDockUpperPanel(player.getCurrentStar().getStation()));
		gui.setLowerPanel(TextManager.getDockLowerPanel(player));
	}
	
	//*************************
	//* Buy Command Logic
	//*************************
	private void buy(String[] commandString, Player player) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
				Station station = player.getCurrentStar().getStation();
				int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(), station.getPrice(cargoEnum),
					player.getShip().getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
				switch(validationCode) {
				case 0:
					TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
					break;
				}
				displayDockPanels(player);
			}
		}
	}
	
	//*************************
	//* Sell Command Logic
	//*************************
	private void sell(String[] commandString, Player player) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
				Station station = player.getCurrentStar().getStation();
				int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(), station.getPrice(cargoEnum),
						station.getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), player.getShip().getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, amountToSell);
				switch(validationCode) {
				case 0:
					TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
					break;
				}
				displayDockPanels(player);
			}
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
	
	//*************************
	//* Wait Command Logic
	//*************************
	private void waitTurn() {
		advanceTurn();
	}
	
	//*************************
	//* Score Command Logic
	//*************************
	private void score() {
		gui.setLowerPanel(TextManager.getScoreLowerPanel(players));
	}
	
	//*************************
	//* Help Command Logic
	//*************************
	private void help(String[] commandString) {
		gui.setUpperPanel(TextManager.getHelpUpperPanel());
		if (commandString.length < 2) {
			gui.setLowerPanel(TextManager.getHelpLowerPanel(Command.HELP));
		} else {
			Command secondCommand = Command.toCommand(commandString[1]);
			if (secondCommand != null) {
				gui.setLowerPanel(TextManager.getHelpLowerPanel(secondCommand));
			}
		}
	}
	
	//*************************
	//* Display JUNG Graphs
	//*************************
	private void displayLocalSystemMap(Star currStar, Star prevStar) {
		gui.loadSystemMap(starSystemManager.createSubGraph(currStar), currStar, prevStar);
	}
	
	private void displayGlobalSystemMap(Star currStar, Star prevStar) {
		gui.loadSystemMap(starSystemManager.getStarGraph(), currStar, prevStar);
	}
	
	//*************************
	//* End turn timing and logic
	//*************************
	private void advanceTurn() {
		playerManagerAI.performAIPlayerTurns();
		turnCounter++;
		if (turnCounter % turnsUntilStationRefresh == 0) {
			starSystemManager.refreshStationCargo();
		}
	}
}
