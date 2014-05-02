package sujoo.games.spacegame.datatypes.ai;

import java.util.Random;

import sujoo.games.spacegame.datatypes.CargoEnum;
import sujoo.games.spacegame.datatypes.CargoHold;
import sujoo.games.spacegame.datatypes.Station;

public class StationManagerAI {
	private static final Random random = new Random();
	
	public static void fillStationWithCargo(Station station) {
		CargoHold hold = station.getCargoHold();
		
		for (int i = 0; i < CargoEnum.values().length - 1; i++) {
			hold.addCargo(CargoEnum.values()[i], random.nextInt(hold.remainingCargoSpace() / CargoEnum.values()[i].getSize()));		
		}
	}

}
