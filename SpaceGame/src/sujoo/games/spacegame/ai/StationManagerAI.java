package sujoo.games.spacegame.ai;

import java.util.List;
import java.util.Random;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.planet.Planet;
import sujoo.games.spacegame.datatype.player.Station;

public class StationManagerAI {
	private static final Random random = new Random();
	private static final int priceVariance = 3;
	private static final int cargoVariance = 10;
	private static final int stationCashReload = 50000;
	
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
				station.addCargoGeneratedByPlanet(cargoEnums[i]);
			}
		}
		updateStationPrices(station);
	}
	
	private static void updateStationPrices(Station station) {
		CargoHold hold = station.getCargoHold();
		
		for (CargoEnum cargoEnum : CargoEnum.getList()) {
			int baseValue = cargoEnum.getBaseValue();
			if (hold.getCargoAmount(cargoEnum) > 0) {
				station.setTransactionPrice(baseValue - random.nextInt(baseValue / priceVariance), cargoEnum);
			} else {
				station.setTransactionPrice(baseValue + random.nextInt(baseValue / priceVariance), cargoEnum);
			}
			
		}
	}
	
	public static void refreshStationCargo(Station station, List<Planet> planets) {
		for (CargoEnum cargoEnum : CargoEnum.getList()) {
			if (!station.isCargoGeneratedByPlanet(cargoEnum)) {
				station.getCargoHold().dumpCargo(cargoEnum);
			}
		}
		fillStationWithCargo(station, planets);
		station.getWallet().addCredits(stationCashReload);
	}
}
