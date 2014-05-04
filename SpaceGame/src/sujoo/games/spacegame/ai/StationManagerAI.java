package sujoo.games.spacegame.ai;

import java.util.List;
import java.util.Random;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Station;
import sujoo.games.spacegame.datatypes.planet.Planet;

public class StationManagerAI {
	private static final Random random = new Random();
	private static final int priceVariance = 3;
	private static final int cargoVariance = 10;
	
	public static void fillStationWithCargo(Station station, List<Planet> planets) {
		CargoHold hold = station.getCargoHold();
		
		for (Planet planet : planets) {
			CargoEnum[] cargoEnums = planet.getType().getStationCargoEnums();
			for (int i = 0; i < cargoEnums.length; i++) {
				int baseProduction = planet.getType().getBaseStationCargoProduction()[i];
				int randomVariance = random.nextInt(baseProduction / cargoVariance);
				if (random.nextBoolean()) {
					randomVariance *= -1;
				}
				hold.addCargo(cargoEnums[i], baseProduction + randomVariance);
			}
		}
		updateStationPrices(station);
	}
	
	public static void updateStationPrices(Station station) {
		CargoHold hold = station.getCargoHold();
		
		for (int i = 0; i < CargoEnum.values().length; i++) {
			int baseValue = CargoEnum.values()[i].getBaseValue();
			if (hold.getCargo()[i] > 0) {
				station.getPrices()[i] = baseValue - random.nextInt(baseValue / priceVariance);
			} else {
				station.getPrices()[i] = baseValue + random.nextInt(baseValue / priceVariance);
			}
		}
	}
}
