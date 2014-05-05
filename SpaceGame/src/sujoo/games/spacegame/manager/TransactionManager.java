package sujoo.games.spacegame.manager;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.player.Player;

public class TransactionManager {
	
	/**
	 * 
	 * @param player
	 * @param station
	 * @param cargoEnum
	 * @param amount
	 * @return validation code : -1(unknown failure), 0(successful validation), 1(no room in player), 2(no room in station), 3(no money in player)
	 */
	public static int validateBuyFromStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int result = -1;
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		int stationSellValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		// Does player have enough money?
		if (player.getWallet().getCredits() >= stationSellValue) {
			// Does station have enough cargo?
			if (stationHold.canRemoveCargo(cargoEnum, amount)) {
				// Does player have enough cargo space?
				if (playerHold.canAddCargo(cargoEnum, amount)) {
					result = 0;
				} else {
					result = 1;
				}
			} else {
				result = 2;
			}
		} else {
			result = 3;
		}
		
		return result;
	}
	
	/**
	 * 
	 * @param player
	 * @param station
	 * @param cargoEnum
	 * @param amount
	 * @return validation code : -1(unknown failure), 0(successful validation), 1(no room in station), 2(no room in player), 3(no money in station)
	 */
	public static int validateSellToStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int result = -1;
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		int stationBuyValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		// Does player have enough money?
		if (station.getWallet().getCredits() >= stationBuyValue) {
			// Does player have enough cargo?
			if (playerHold.canRemoveCargo(cargoEnum, amount)) {
				// Does station have enough cargo space?
				if (stationHold.canAddCargo(cargoEnum, amount)) {
					result = 0;
				} else {
					result = 1;
				}
			} else {
				result = 2;
			}
		} else {
			result = 3;
		}
		
		return result;
	}
	
	/**
	 * Player is buying X cargo from station
	 * Station is selling X cargo to player
	 * @param player
	 * @param station
	 * @param cargoEnum
	 * @param amount
	 * @return amount of money player will have to pay to station
	 */
	public static void performBuyFromStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {		
		transactCargoTrade(station.getCargoHold(), player.getShip().getCargoHold(), cargoEnum, amount);
		
		int stationSellValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		station.getWallet().addCredits(stationSellValue);
		player.getWallet().removeCredits(stationSellValue);
	}
	
	/**
	 * Player selling X cargo to station
	 * Station is buying X cargo from player
	 * @param player
	 * @param station
	 * @param cargoEnum
	 * @param amount
	 * @return amount of money player will gain from station
	 */
	public static void performSellToStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		transactCargoTrade(player.getShip().getCargoHold(), station.getCargoHold(), cargoEnum, amount);
		
		int stationBuyValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		player.getWallet().addCredits(stationBuyValue);
		station.getWallet().removeCredits(stationBuyValue);
	}
	
	private static void transactCargoTrade(CargoHold srcHold, CargoHold destHold, CargoEnum cargoEnum, int amount) {
		srcHold.removeCargo(cargoEnum, amount);
		destHold.addCargo(cargoEnum, amount);
	}
	
	public static int getMaximumAmount(int buyerCredits, int cargoPrice, int remainingCargoSpace, int cargoSize, int maxStock) {
		int amount = 0;
		int priceLimit = buyerCredits / cargoPrice;
		int cargoLimit = remainingCargoSpace / cargoSize;
		if (maxStock < priceLimit && maxStock < cargoLimit) {
			amount = maxStock;
		} else if (priceLimit < cargoLimit) {
			amount = priceLimit;
		} else {
			amount = cargoLimit;
		}
		return amount;
	}
}
