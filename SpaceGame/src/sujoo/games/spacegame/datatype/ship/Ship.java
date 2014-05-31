package sujoo.games.spacegame.datatype.ship;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import sujoo.games.spacegame.datatype.command.ShipLocationCommand;
import sujoo.games.spacegame.datatype.ship.component.CargoHoldComponent;
import sujoo.games.spacegame.datatype.ship.component.ShipComponent;
import sujoo.games.spacegame.datatype.ship.component.ShipComponentEnumIntf;
import sujoo.games.spacegame.datatype.ship.component.ShipComponentFactory;
import sujoo.games.spacegame.datatype.ship.component.ShieldComponent;

public abstract class Ship {
    private ShipEnum type;
    private Map<ShipLocationCommand, ShipComponent> components;

    public Ship(ShipEnum type) {
        this.type = type;
        components = Maps.newHashMap();
        for (ShipComponentEnumIntf component : type.getComponents()) {
            components.put(component.getLocation(),
                    ShipComponentFactory.buildShipComponent(component.getLocation(), component));
        }
    }

    public void removeComponent(ShipLocationCommand location) {
        components.remove(location);
    }

    public void replaceComponent(ShipLocationCommand location, ShipComponent component) {
        removeComponent(location);
        components.put(location, component);
    }

    public Optional<ShipComponent> getComponent(ShipLocationCommand location) {
        return Optional.<ShipComponent>fromNullable(components.get(location));
    }
    
    public Optional<CargoHoldComponent> getCargoHold() {
        return Optional.<CargoHoldComponent>fromNullable((CargoHoldComponent) components.get(ShipLocationCommand.CARGOHOLD));
    }
    
    public Optional<ShieldComponent> getShield() {
        return Optional.<ShieldComponent>fromNullable((ShieldComponent) components.get(ShipLocationCommand.SHIELD));
    }

    public int getCurrentComponentValue(ShipLocationCommand location) {
        Optional<ShipComponent> component = getComponent(location);
        if (component.isPresent()) {
            return component.get().getCurrentValue();
        } else {
            return 0;
        }
    }

    public int getCurrentMaxComponentValue(ShipLocationCommand location) {
        Optional<ShipComponent> component = getComponent(location);
        if (component.isPresent()) {
            return component.get().getAbsoluteMaxValue();
        } else {
            return 0;
        }
    }

    public boolean isShieldUp() {
        Optional<ShipComponent> component = getComponent(ShipLocationCommand.SHIELD);
        if (component.isPresent() && component.get().getCurrentValue() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDestoryed() {
        Optional<ShipComponent> component = getComponent(ShipLocationCommand.HULL);
        if (component.isPresent() && component.get().getCurrentValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void restoreAllComponents() {
        for (ShipComponent component : components.values()) {
            component.restoreComponent();
        }
    }

    public boolean isEscapePossible(int battleCounter) {
        Optional<ShipComponent> component = getComponent(ShipLocationCommand.ENGINE);
        int turnsToEscape = getWeight() / ((getCurrentComponentValue(ShipLocationCommand.ENGINE) * 2) + 1);
        if (component.isPresent() && battleCounter > turnsToEscape) {
            return true;
        } else {
            return false;
        }
    }
    
    public int getWeight() {
        int maximumComponentValue = 0;
        for (Entry<ShipLocationCommand, ShipComponent> entry : components.entrySet()) {
            maximumComponentValue += entry.getValue().getAbsoluteMaxValue();
        }
        return maximumComponentValue;
    }

    public ShipEnum getType() {
        return type;
    }
}
