package sujoo.games.spacegame.ai;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Star;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.player.Player;
import sujoo.games.spacegame.datatypes.ship.ShipFactory;
import sujoo.games.spacegame.datatypes.ship.ShipType;
import sujoo.games.spacegame.manager.StarSystemManager;
import sujoo.games.spacegame.manager.TransactionManager;

public class PlayerManagerAI {
	private final int initCredits = 1000;
	
	private List<Player> aiPlayers;
	private StarSystemManager starSystemManager;
	private Random random;
	private List<CargoEnum> recentlyPurchasedCargo;
	
	public PlayerManagerAI(int numberOfAI, StarSystemManager starSystemManager) {
		aiPlayers = Lists.newArrayList();
		this.starSystemManager = starSystemManager;
		random = new Random();
		recentlyPurchasedCargo = Lists.newArrayList();
		
		createAIPlayers(numberOfAI);
	}
	
	private void createAIPlayers(int numberOfAI) {
		List<String> traderNames = getNames("resources\\TraderNames.txt");
		for (int i = 0; i < numberOfAI; i++) {
			Player p = new Player(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, traderNames.get(i));
			p.setNewCurrentStar(starSystemManager.getRandomStarSystem());
			aiPlayers.add(p);
		}
	}
	
	private List<String> getNames(String fileName) {
		List<String> names = Lists.newArrayList();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));
			String name;
			while((name = reader.readLine()) != null) {
				names.add(name);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return names;
	}
	
	public void performAIPlayerTurns() {
		for (Player player : aiPlayers) {
			trade(player);
			jumpToNewSystem(player);
		}
	}
	
	private void trade(Player player) {
		// while ai has money and cargo space
			// find best value item to buy that station has in stock
			// buy max of that item
			// remember what items have been bought here
		buyFromStation(player);
		// find best value item to sell that player has in cargo and weren't just bought
			// sell max of that item
		sellToStation(player);
	}
	
	private void buyFromStation(Player player) {
		Station station = player.getCurrentStar().getStation();
		int credits = player.getWallet().getCredits();
		CargoHold hold = player.getShip().getCargoHold();
		
		if (credits > 0 && hold.remainingCargoSpace() > 0) {
			int greatestDifference = 0;
			CargoEnum cargoEnum = null;
			for (int i = 0; i < station.getPrices().length; i++) {
				int difference = CargoEnum.values()[i].getBaseValue() - station.getPrices()[i];
				if (difference > greatestDifference && station.getCargoHold().getCargo()[i] > 0 && !recentlyPurchasedCargo.contains(cargoEnum)) {
					greatestDifference = difference;
					cargoEnum = CargoEnum.values()[i];
				}
			}
			
			if (cargoEnum != null) {
				int maxPurchaseAmount = TransactionManager.getMaximumAmount(credits, station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)],
						hold.remainingCargoSpace(), cargoEnum.getSize(), station.getCargoHold().getCargo()[CargoEnum.getCargoEnumIndex(cargoEnum)]);
				if(TransactionManager.validateBuyFromStationTransaction(player, station, cargoEnum, maxPurchaseAmount) == 0) {
					TransactionManager.performBuyFromStationTransaction(player, station, cargoEnum, maxPurchaseAmount);
					recentlyPurchasedCargo.add(cargoEnum);
				}
			}
		}
	}
	
	private void sellToStation(Player player) {
		Station station = player.getCurrentStar().getStation();
		CargoHold hold = player.getShip().getCargoHold();
		
		if (hold.getCargoSpaceUsage() > 0) {
			int greatestDifference = 0;
			CargoEnum cargoEnum = null;
			for (int i = 0; i < station.getPrices().length; i++) {
				int difference = station.getPrices()[i] - CargoEnum.values()[i].getBaseValue();
				if (difference > greatestDifference && hold.getCargo()[i] > 0 && !recentlyPurchasedCargo.contains(cargoEnum)) {
					greatestDifference = difference;
					cargoEnum = CargoEnum.values()[i];
				}
			}
			
			if (cargoEnum != null) {
				int maxSaleAmount = TransactionManager.getMaximumAmount(station.getWallet().getCredits(), station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)],
						station.getCargoHold().remainingCargoSpace(), cargoEnum.getSize(), hold.getCargo()[CargoEnum.getCargoEnumIndex(cargoEnum)]);
				if(TransactionManager.validateSellToStationTransaction(player, station, cargoEnum, maxSaleAmount) == 0) {
					TransactionManager.performSellToStationTransaction(player, station, cargoEnum, maxSaleAmount);
					recentlyPurchasedCargo.add(cargoEnum);
				}
			}
		}
	}
	
	private void jumpToNewSystem(Player player) {
		List<Star> neighbors = starSystemManager.getNeighbors(player.getCurrentStar());
		player.setNewCurrentStar(neighbors.get(random.nextInt(neighbors.size())));
	}
	
	public List<Player> getAIPlayersInStarSystem(Star star) {
		List<Player> resultList = Lists.newArrayList();
		for (Player player : aiPlayers) {
			if (player.getCurrentStar().equals(star)) {
				resultList.add(player);
			}
		}
		return resultList;
	}
	
	public List<Player> getAIPlayers() {
		return aiPlayers;
	}
	
	public Player getAIPlayer(String name) {
		Player aiPlayer = null;
		for (Player player : aiPlayers) {
			if (player.getName().equalsIgnoreCase(name)) {
				aiPlayer = player;
				break;
			}
		}
		return aiPlayer;
	}
}
