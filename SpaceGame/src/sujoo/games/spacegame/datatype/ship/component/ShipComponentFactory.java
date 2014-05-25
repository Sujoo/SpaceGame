package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public class ShipComponentFactory {
    public static ShipComponent buildShipComponent(ShipLocationCommand location, ShipComponentEnumIntf intf) {
        ShipComponent shipComponent = null;
        switch (location) {
        case SHIELD:
            shipComponent = new ShipShieldComponent((ShipShieldComponentEnum) intf);
            break;
        case HULL:
            shipComponent = new ShipComponent(intf);
            break;
        case ENGINE:
            shipComponent = new ShipComponent(intf);
            break;
        case WEAPON:
            shipComponent = new ShipComponent(intf);
            break;
        case CARGO:
            break;
        }
        return shipComponent;
    }
}
