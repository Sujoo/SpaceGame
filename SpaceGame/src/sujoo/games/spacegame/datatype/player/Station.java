package sujoo.games.spacegame.datatype.player;

import java.util.Set;

import sujoo.games.spacegame.datatype.cargo.CargoEnum;
import sujoo.games.spacegame.datatype.ship.Ship;

import com.google.common.collect.Sets;

public class Station extends Player {

	private Set<CargoEnum> cargoGeneratedByPlanet;
	
	public Station(Ship ship, int credits, String name) {
		super(ship, credits, name);
		cargoGeneratedByPlanet = Sets.newHashSet();
	}
	
	public void addCargoGeneratedByPlanet(CargoEnum cargoEnum) {
		cargoGeneratedByPlanet.add(cargoEnum);
	}
	
	public boolean isCargoGeneratedByPlanet(CargoEnum cargoEnum) {
		return cargoGeneratedByPlanet.contains(cargoEnum);
	}
}
