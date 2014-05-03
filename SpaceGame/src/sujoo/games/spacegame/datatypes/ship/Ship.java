package sujoo.games.spacegame.datatypes.ship;

import sujoo.games.spacegame.datatypes.CargoHold;

public abstract class Ship {
	private ShipType type;
	private CargoHold hold;
	
	public Ship(ShipType type) {
		this.type = type;
		hold = new CargoHold(type.getHoldSize());
	}

	public ShipType getType() {
		return type;
	}
	
	public CargoHold getCargoHold() {
		return hold;
	}
}
