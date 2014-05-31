package sujoo.games.spacegame.datatype.ship;

public class ShipFactory {
    public static Ship buildShip(ShipEnum shipEnum) {
        Ship ship = null;
        switch (shipEnum) {
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
