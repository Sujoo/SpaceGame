package sujoo.games.spacegame.datatypes.ship;

public abstract class Ship {
	private ShipType type;
	
	public Ship(ShipType type) {
		this.type = type;
	}

	public ShipType getType() {
		return type;
	}
}
