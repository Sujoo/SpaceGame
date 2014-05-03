package sujoo.games.spacegame.ai;

import java.util.Random;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Station;

public class StationManagerAI {
	private static final Random random = new Random();
	private static final double highestPrice = 1000;
	private static final double highestSupply = 1000;
	private static final double m = -1*(highestPrice / highestSupply);
	
	public static void fillStationWithCargo(Station station) {
		CargoHold hold = station.getCargoHold();
		
		for (int i = 0; i < CargoEnum.values().length - 1; i++) {
			hold.addCargo(CargoEnum.values()[i], random.nextInt(hold.remainingCargoSpace() / CargoEnum.values()[i].getSize()));
			updateStationPrice(station, CargoEnum.values()[i]);
		}
	}
	
	public static void updateStationPrice(Station station, CargoEnum cargoEnum) {
		int index = CargoEnum.getCargoEnumIndex(cargoEnum);
		double newPrice = m*station.getCargoHold().getCargo()[index] + highestPrice;
		if (newPrice <= 0) {
			newPrice = 1;
		}
		station.getPrices()[index] = (int) newPrice;
	}
}
