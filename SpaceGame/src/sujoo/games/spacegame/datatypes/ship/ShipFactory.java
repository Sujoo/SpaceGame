package sujoo.games.spacegame.datatypes.ship;

public class ShipFactory {
	public static Ship buildShip(ShipType type) {
		Ship ship = null;
		switch(type) {
		case SMALL_TRANS:
			ship = new SmallTransportShip();
			break;
		}
		return ship;
	}
}
