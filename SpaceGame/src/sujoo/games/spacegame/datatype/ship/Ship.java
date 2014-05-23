package sujoo.games.spacegame.datatype.ship;

import java.util.Map;

import com.google.common.collect.Maps;

import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.ShipLocationCommand;

public abstract class Ship {
    private ShipType type;
    private CargoHold hold;
    private Map<ShipLocationCommand, ShipComponent> components;

    public Ship(ShipType type) {
        this.type = type;
        hold = new CargoHold(type.getHoldSize());
        components = Maps.newHashMap();
        for (ShipComponentEnumIntf component : type.getComponents()) {
            components.put(component.getLocation(), ShipComponentFactory.buildShipComponent(component.getLocation(), component));
        }
    }

    public boolean hasComponent(ShipLocationCommand location) {
        return components.containsKey(location);
    }

    public ShipComponent getComponent(ShipLocationCommand location) {
        return components.get(location);
    }

    public int getCurrentComponentValue(ShipLocationCommand location) {
        int result = 0;
        if (hasComponent(location)) {
            result = getComponent(location).getCurrentValue();
        }
        return result;
    }

    public int getCurrentMaxComponentValue(ShipLocationCommand location) {
        int result = 0;
        if (hasComponent(location)) {
            result = getComponent(location).getAbsoluteMaxValue();
        }
        return result;
    }
    
    public boolean isShieldUp() {
        if (hasComponent(ShipLocationCommand.SHIELD) && getComponent(ShipLocationCommand.SHIELD).getCurrentValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDestoryed() {
        boolean result = false;
        if (hasComponent(ShipLocationCommand.HULL)) {
            if (getComponent(ShipLocationCommand.HULL).getCurrentValue() == 0) {
                result = true;
            }
        }
        return result;
    }

    public void restoreAllComponents() {
        for (ShipComponent component : components.values()) {
            component.restoreComponent();
        }
    }

    public boolean isEscapePossible(int battleCounter) {
        boolean result = false;
        if (hasComponent(ShipLocationCommand.ENGINE)) {
            if (battleCounter >= 4) {
                result = true;
            }
        }
        return result;
    }

    public ShipType getType() {
        return type;
    }

    public CargoHold getCargoHold() {
        return hold;
    }
}
