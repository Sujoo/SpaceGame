package sujoo.games.spacegame.datatype.ship;

public class ShipFactory {
    public static Ship buildShip(ShipType type) {
        Ship ship = null;
        switch (type) {
        case SMALL_TRANS:
            ship = new SmallTransportShip();
            break;
        case STATION:
            ship = new StationShip();
            break;
        }
        return ship;
    }
}
