package sujoo.games.spacegame.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.ai.PlayerManagerAI;
import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.command.PrimaryCommand;
import sujoo.games.spacegame.datatype.command.TransactionSubCommand;
import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.HumanPlayer;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.datatype.player.Station;
import sujoo.games.spacegame.datatype.ship.ShipFactory;
import sujoo.games.spacegame.datatype.ship.ShipType;
import sujoo.games.spacegame.gui.ErrorEnum;
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
		PrimaryCommand firstCommand = PrimaryCommand.toCommand(commandString[0]);
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
			if (otherPlayer != null) {
				if (player.getCurrentStar().equals(otherPlayer.getCurrentStar())) {
					scanPlayer(otherPlayer);
				} else {
					gui.displayError(ErrorEnum.PLAYER_NOT_IN_SYSTEM);
				}
			} else {
				gui.displayError(ErrorEnum.INVALID_PLAYER_NAME);
			}
		}
	}
	
	private void scanSystem(Player player) {
		displayScanSystem(player);
		displayLocalSystemMap(player.getCurrentStar(), player.getPreviousStar());
	}
	
	private void displayScanSystem(Player player) {
		gui.displayScanSystem(player, starSystemManager.getNeighborsString(player.getCurrentStar()), playerManagerAI.getAIPlayersInStarSystem(player.getCurrentStar()));
	}
	
	private void scanPlayer(Player player) {
		gui.displayScanPlayer(player);
	}
	
	
	//*************************
	//* Status Command Logic
	//*************************
	private void status(Player player) {
		gui.displayStatus(player);
	}
	
	//*************************
	//* Dock Command Logic
	//*************************
	private void dock(Player player) {
		displayDockCargo(player);
	}
	
	private void displayDockCargo(Player player) {
		gui.displayDockCargo(player);
	}
	
	//*************************
	//* Buy Command Logic
	//*************************
	private void buy(String[] commandString, Player player) {
		if (commandString.length >= 3) {
			CargoEnum cargoEnum = CargoEnum.toCargoEnum(commandString[2]);
			if (cargoEnum != null) {
				Station station = player.getCurrentStar().getStation();
				int amountToBuy = getAmount(commandString[1], player.getWallet().getCredits(), station.getTransactionPrice(cargoEnum),
					player.getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
				switch(validationCode) {
				case 0:
					TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, amountToBuy);
					break;
				case 1:
					gui.displayError(ErrorEnum.PLAYER_NO_CARGO_SPACE);
					break;
				case 2:
					gui.displayError(ErrorEnum.STATION_NO_CARGO_SPACE);
					break;
				case 3:
					gui.displayError(ErrorEnum.PLAYER_NO_MONEY);
					break;
				}
				displayDockCargo(player);
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
				int amountToSell = getAmount(commandString[1], station.getWallet().getCredits(), station.getTransactionPrice(cargoEnum),
						station.getCargoHold().getRemainingCargoSpace(), cargoEnum.getSize(), player.getCargoHold().getCargoAmount(cargoEnum));
				int validationCode = TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, amountToSell);
				switch(validationCode) {
				case 0:
					TransactionManager.performSellToStationTransaction(player, station, cargoEnum, amountToSell);
					break;
				case 1:
					gui.displayError(ErrorEnum.STATION_NO_CARGO_SPACE);
					break;
				case 2:
					gui.displayError(ErrorEnum.PLAYER_NO_CARGO_SPACE);
					break;
				case 3:
					gui.displayError(ErrorEnum.STATION_NO_MONEY);
					break;
				}
				displayDockCargo(player);
			}
		}
	}
	
	private int getAmount(String command, int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize, int maxStock) {
		int amount = 0;		
		if (StringUtils.isNumeric(command)) {
			amount = Integer.parseInt(command);
		} else if (TransactionSubCommand.isMaxAllCommand(command)) {
			amount = TransactionManager.getMaximumAmount(buyerCredits, cargoPrice, remainingCargoSpace, cargoSize, maxStock);
		} else {
			gui.displayError(ErrorEnum.INVALID_TRANSACTION_AMOUNT);
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
		gui.displayScore(players);
	}
	
	//*************************
	//* Help Command Logic
	//*************************
	private void help(String[] commandString) {
		if (PrimaryCommand.toCommand(commandString[0]) == null) {
			gui.displayError(ErrorEnum.INVALID_PRIMARY_COMMAND);
		}
		PrimaryCommand secondCommand = PrimaryCommand.HELP;
		if (commandString.length >= 2) {
			if (PrimaryCommand.toCommand(commandString[1]) != null) {
				secondCommand = PrimaryCommand.toCommand(commandString[1]);
			} else {
				gui.displayError(ErrorEnum.INVALID_SECONDARY_COMMAND);
			}
		}
		
		gui.displayHelp(secondCommand);
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
