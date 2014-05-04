package sujoo.games.spacegame.manager;

import sujoo.games.spacegame.ai.StationManagerAI;
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
	 * @return validation code : -1(unknown failure), 0(successful validation), 1(not enough cargo to remove), 2(not enough cargo space to add), 3(player not enough money)
	 */
	public static int validateBuyFromStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int result = -1;
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		int stationSellValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		// Does player have enough money?
		if (player.getCredits() >= stationSellValue) {
			// Does station have enough cargo?
			if (stationHold.canRemoveCargo(cargoEnum, amount)) {
				// Does player have enough cargo space?
				if (playerHold.canAddCargo(cargoEnum, amount)) {
					result = 0;
				} else {
					result = 2;
				}
			} else {
				result = 1;
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
	 * @return validation code : -1(unknown failure), 0(successful validation), 1(not enough cargo to remove), 2(not enough cargo space to add)
	 */
	public static int validateSellToStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int result = -1;
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		// Does player have enough cargo?
		if (playerHold.canRemoveCargo(cargoEnum, amount)) {
			// Does station have enough cargo space?
			if (stationHold.canAddCargo(cargoEnum, amount)) {
				result = 0;
			} else {
				result = 2;
			}
		} else {
			result = 1;
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
	public static int performBuyFromStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int stationSellValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		transactCargoTrade(stationHold, playerHold, cargoEnum, amount);
		StationManagerAI.updateStationPrice(station, cargoEnum);
		
		return stationSellValue;
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
	public static int performSellToStationTransaction(Player player, Station station, CargoEnum cargoEnum, int amount) {
		int stationBuyValue = station.getPrices()[CargoEnum.getCargoEnumIndex(cargoEnum)]*amount;
		
		CargoHold playerHold = player.getShip().getCargoHold();
		CargoHold stationHold = station.getCargoHold();
		
		transactCargoTrade(playerHold, stationHold, cargoEnum, amount);
		StationManagerAI.updateStationPrice(station, cargoEnum);
		
		return stationBuyValue;
	}
	
	private static void transactCargoTrade(CargoHold srcHold, CargoHold destHold, CargoEnum cargoEnum, int amount) {
		srcHold.removeCargo(cargoEnum, amount);
		destHold.addCargo(cargoEnum, amount);
	}
}
