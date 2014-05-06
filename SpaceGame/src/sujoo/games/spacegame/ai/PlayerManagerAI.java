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
import sujoo.games.spacegame.datatypes.player.AIPlayer;
import sujoo.games.spacegame.datatypes.player.Player;
import sujoo.games.spacegame.datatypes.ship.ShipFactory;
import sujoo.games.spacegame.datatypes.ship.ShipType;
import sujoo.games.spacegame.manager.StarSystemManager;
import sujoo.games.spacegame.manager.TransactionManager;

public class PlayerManagerAI {
	private final int initCredits = 1000;
	
	private List<AIPlayer> aiPlayers;
	private StarSystemManager starSystemManager;
	private Random random;
	
	public PlayerManagerAI(int numberOfAI, StarSystemManager starSystemManager) {
		aiPlayers = Lists.newArrayList();
		this.starSystemManager = starSystemManager;
		random = new Random();
		
		createAIPlayers(numberOfAI);
	}
	
	private void createAIPlayers(int numberOfAI) {
		List<String> traderNames = getNames("resources\\TraderNames.txt");
		for (int i = 0; i < numberOfAI; i++) {
			AIPlayer p = new AIPlayer(ShipFactory.buildShip(ShipType.SMALL_TRANS), initCredits, traderNames.get(i));
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
		for (AIPlayer player : aiPlayers) {
			trade(player);
			jumpToNewSystem(player);
			player.clearRecentlyPurchased();
		}
	}
	
	private void trade(AIPlayer player) {
		sellToStation(player);
		buyFromStation(player);
	}
	
	private void buyFromStation(AIPlayer player) {
		Station station = player.getCurrentStar().getStation();
		int credits = player.getWallet().getCredits();
		CargoHold hold = player.getShip().getCargoHold();
		
		// while ai has money and cargo space
		if (credits > 0 && hold.getRemainingCargoSpace() > 0) {
			int greatestDifference = 0;
			CargoEnum bestCargoEnum = null;
			// find best value item to buy that station has in stock
			for (CargoEnum cargoEnum : CargoEnum.getList()) {
				int difference = cargoEnum.getBaseValue() - station.getPrice(cargoEnum);
				if (difference > greatestDifference && station.getCargoHold().getCargoAmount(cargoEnum) > 0 && !player.isRecentlyPurchased(bestCargoEnum)) {
					greatestDifference = difference;
					bestCargoEnum = cargoEnum;
				}				
			}
			
			// buy max of that item
			if (bestCargoEnum != null) {
				int maxPurchaseAmount = TransactionManager.getMaximumAmount(credits, station.getPrice(bestCargoEnum),
						hold.getRemainingCargoSpace(), bestCargoEnum.getSize(), station.getCargoHold().getCargoAmount(bestCargoEnum));
				if(TransactionManager.validateBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount) == 0) {
					TransactionManager.performBuyFromStationTransaction(player, station, bestCargoEnum, maxPurchaseAmount);
					// remember what items have been bought
					player.addRecentlyPurchased(bestCargoEnum);
				}
			}
		}
	}
	
	private void sellToStation(AIPlayer player) {
		Station station = player.getCurrentStar().getStation();
		CargoHold hold = player.getShip().getCargoHold();
		
		// find best value item to sell that player has in cargo and wasn't just bought
		if (hold.getCargoSpaceUsage() > 0) {
			int greatestDifference = 0;
			CargoEnum bestCargoEnum = null;
			for (CargoEnum cargoEnum : CargoEnum.getList()) {
				if (hold.getCargoAmount(cargoEnum) > 0) {
					int difference = station.getPrice(cargoEnum) - player.getPurchasePrice(cargoEnum);
					if (difference > greatestDifference && !player.isRecentlyPurchased(bestCargoEnum)) {
						greatestDifference = difference;
						bestCargoEnum = cargoEnum;
					}
				}
				
			}
			
			// sell max of that item
			if (bestCargoEnum != null) {
				int maxSaleAmount = TransactionManager.getMaximumAmount(station.getWallet().getCredits(), station.getPrice(bestCargoEnum),
						station.getCargoHold().getRemainingCargoSpace(), bestCargoEnum.getSize(), hold.getCargoAmount(bestCargoEnum));
				if(TransactionManager.validateSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount) == 0) {
					TransactionManager.performSellToStationTransaction(player, station, bestCargoEnum, maxSaleAmount);
					player.addRecentlyPurchased(bestCargoEnum);
				}
			}
		}
	}
	
	private void jumpToNewSystem(AIPlayer player) {
		List<Star> neighbors = starSystemManager.getNeighbors(player.getCurrentStar());
		Star nextJump;
		nextJump = neighbors.get(random.nextInt(neighbors.size()));
		player.setNewCurrentStar(nextJump);
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
		List<Player> result = Lists.newArrayList();
		result.addAll(aiPlayers);
		return result;
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
