package sujoo.games.spacegame.datatype.ship.component;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public class ShipComponentFactory {
    public static ShipComponent buildShipComponent(ShipLocationCommand location, ShipComponentEnumIntf intf) {
        ShipComponent shipComponent = null;
        switch (location) {
        case SHIELD:
            shipComponent = new ShieldComponent((ShieldComponentEnum) intf);
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
        case CARGOHOLD:
            shipComponent = new CargoHoldComponent((CargoHoldComponentEnum) intf);
            break;
        case FUELTANK:
            shipComponent = new TankComponent((TankComponentEnum) intf);
            break;
        case MAGAZINE:
            shipComponent = new TankComponent((TankComponentEnum) intf);
            break;
        }
        return shipComponent;
    }
}
