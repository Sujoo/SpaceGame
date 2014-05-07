package sujoo.games.spacegame.datatype.ship;

import sujoo.games.spacegame.datatype.command.AttackSubCommand;

public class ShipComponentFactory {
    public static ShipComponent buildShipComponent(AttackSubCommand location, ShipComponentEnumIntf intf) {
        ShipComponent shipComponent = null;
        switch(location) {
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
        }
        return shipComponent;
    }
}