package sujoo.games.spacegame.datatype.ship;

import java.util.Map;

import com.google.common.collect.Maps;

import sujoo.games.spacegame.datatype.cargo.CargoHold;
import sujoo.games.spacegame.datatype.command.AttackSubCommand;
import sujoo.games.spacegame.gui.BattleFeedbackEnum;

public abstract class Ship {
    private ShipType type;
    private CargoHold hold;

    private Map<AttackSubCommand, ShipComponent> components;

    public Ship(ShipType type) {
        this.type = type;
        hold = new CargoHold(type.getHoldSize());
        components = Maps.newHashMap();
        for (ShipComponentEnumIntf component : type.getComponents()) {
            components.put(component.getLocation(), ShipComponentFactory.buildShipComponent(component.getLocation(), component));
        }
    }

    public boolean hasComponent(AttackSubCommand location) {
        return components.containsKey(location);
    }

    public ShipComponent getComponent(AttackSubCommand location) {
        return components.get(location);
    }

    public int getCurrentComponentValue(AttackSubCommand location) {
        int result = 0;
        if (components.containsKey(location)) {
            result = components.get(location).getCurrentValue();
        }
        return result;
    }

    public int getCurrentMaxComponentValue(AttackSubCommand location) {
        int result = 0;
        if (components.containsKey(location)) {
            result = components.get(location).getAbsoluteMaxValue();
        }
        return result;
    }

    public BattleFeedbackEnum damageComponent(AttackSubCommand location, int damage) {
        BattleFeedbackEnum result = null;
        // if the component isn't on the ship, than the player input the wrong
        // command so leave feedback null
        if (components.containsKey(location)) {
            // If ship has shields, damage those first
            if (components.containsKey(AttackSubCommand.SHIELD) && location != AttackSubCommand.SHIELD) {
                ShipComponent shield = components.get(AttackSubCommand.SHIELD);
                // Can shield absorb all the damage?
                if (shield.getCurrentValue() >= damage) {
                    shield.takeDamage(damage);
                    damage = 0;
                    result = BattleFeedbackEnum.SHIELD_HIT;
                } else { // No
                    damage = damage - shield.getCurrentValue();
                    shield.takeDamage(shield.getCurrentValue());
                }
            }

            components.get(location).takeDamage(damage);
            result = BattleFeedbackEnum.COMPONENT_DAMAGE;

            if (isDestoryed()) {
                result = BattleFeedbackEnum.SHIP_DESTROYED;
            }
        }
        return result;
    }

    public BattleFeedbackEnum repairComponent(AttackSubCommand location) {
        BattleFeedbackEnum result = null;
        if (components.containsKey(location)) {
            components.get(location).repair();
            result = BattleFeedbackEnum.COMPONENT_REPAIR;
        }
        return result;
    }

    public BattleFeedbackEnum performShieldRecharge(int battleCounter) {
        BattleFeedbackEnum feedback = null;
        if (components.containsKey(AttackSubCommand.SHIELD)) {
            ShipShieldComponent shield = (ShipShieldComponent) components.get(AttackSubCommand.SHIELD);
            if (battleCounter % shield.getRechargeTime() == 0) {
                shield.restoreShield();
                feedback = BattleFeedbackEnum.SHIELD_RECHARGE;
            }
        }
        return feedback;
    }

    private boolean isDestoryed() {
        boolean result = false;
        if (components.containsKey(AttackSubCommand.HULL)) {
            if (components.get(AttackSubCommand.HULL).getCurrentValue() == 0) {
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
        if (components.containsKey(AttackSubCommand.ENGINE)) {
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
